package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.lerntia.lerntia.dto.ExamWriter;
import at.ac.tuwien.lerntia.lerntia.service.IExamResultsWriterService;
import at.ac.tuwien.lerntia.util.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Properties;

@Service
public class ExamResultsWriterService implements IExamResultsWriterService {


    private final IExamResultsWriterDAO iExamResultsWriterDAO;
    private ConfigReader configReaderServer = null;
    private ConfigReader configReaderMail = null;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public ExamResultsWriterService(IExamResultsWriterDAO iExamResultsWriterDAO) {
        this.iExamResultsWriterDAO = iExamResultsWriterDAO;
    }

    private void createConfigReaderServer() throws ServiceException {
        if (configReaderServer == null) {
            LOG.debug("configReaderServer is null, trying to add a new one.");
            try {
                configReaderServer = new ConfigReader("server");
            } catch (ConfigReaderException e) {
                throw new ServiceException(e.getCustomMessage());
            }
        }
    }

    private void createConfigReaderMail() throws ServiceException {
        if (configReaderMail == null) {
            LOG.debug("configReaderMail is null, trying to add a new one.");
            try {
                configReaderMail = new ConfigReader("mail");
            } catch (ConfigReaderException e) {
                throw new ServiceException(e.getCustomMessage());
            }
        }
    }

    private void sendEmailWithAttachments(String host,
                                          String port,
                                          final String userName,
                                          final String password,
                                          String toAddress,
                                          String ccAddress_1, String ccAddress_2,
                                          String subject,
                                          String message,
                                          String[] attachFiles) throws MessagingException, UnsupportedEncodingException {
        // Set SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // Create a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // Create a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        InternetAddress[] ccAddresses = {new InternetAddress(ccAddress_1), new InternetAddress(ccAddress_2)};
        InternetAddress[] replyToAddresses = {new InternetAddress(ccAddress_1)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setRecipients(Message.RecipientType.CC, ccAddresses);
        msg.setReplyTo(replyToAddresses);
        msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
        msg.setSentDate(new Date());

        // Create message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/plain; charset=UTF-8");

        // Create multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Add attachment(s)
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }
        // Set the multi-part as e-mail content
        msg.setContent(multipart);

        // Send the e-mail
        Transport.send(msg);
    }

    @Override
    public void writeExamResults(ExamWriter examWriter) throws ServiceException {
        try {
            iExamResultsWriterDAO.writeExamResults(examWriter);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void sendExamResultsPerEmail(String filePath) throws ServiceException {
        // SMTP info
        createConfigReaderServer();
        String host = configReaderServer.getValue("host");
        String port = configReaderServer.getValue("port");
        String mailFrom = configReaderServer.getValue("username");
        String password = configReaderServer.getValue("password");
        String mailCC_1 = configReaderServer.getValue("mailCC_1");
        String mailCC_2 = configReaderServer.getValue("mailCC_2");

        // Message info
        createConfigReaderMail();
        String mailTo = configReaderMail.getValue("mailTo");
        String subject = configReaderMail.getValue("subject");
        String message =  configReaderMail.getValue("message");

        // Attachments
        int numberOfAttachments = 1;
        String[] attachFiles = new String[numberOfAttachments];
        attachFiles[0] = filePath;

        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                mailCC_1, mailCC_2, subject, message, attachFiles);
            LOG.info("Email sent.");
        } catch (Exception e) {
            LOG.info("Could not send email.");
            throw new ServiceException("Failed to send email: " + e.getLocalizedMessage());
        }

    }
}
