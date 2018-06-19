package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamWriter;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.maven.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class ExamResultsWriterDAO implements IExamResultsWriterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PdfPCell cell_checked;
    private PdfPCell cell_box;

    private Font fontTitle;
    private Font fontExamName;
    private Font fontExamDate;
    private Font fontStudentInfo;

    private User student;

    @Autowired
    public ExamResultsWriterDAO() throws PersistenceException {

        // images are used to show if an answer has been selected or not.
        // these two images are loaded here and placed in a cell.
        // later these cells are added to the table of answers.

        this.cell_checked = getImageCellBox(Resource.class.getResource("/icons/exam_report_box_checked.png"));
        this.cell_box = getImageCellBox(Resource.class.getResource("/icons/exam_report_box.png"));

        this.fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 26, BaseColor.BLACK);
        this.fontExamName = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        this.fontExamDate = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        this.fontStudentInfo = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
    }

    @Override
    public void writeExamResults(ExamWriter examwriter) throws PersistenceException {
        if (examwriter == null || examwriter.getPath() == null || examwriter.getName() == null ||
            examwriter.getQuestions() == null || examwriter.getUser() == null) {
            throw new PersistenceException("Mindestens ein Wert des Ergebnisschreibers oder der Ergebnisschreiber selbst ist null!");
        }
        this.student = examwriter.getUser();

        // create the document

        LOG.info("Create new Document for new report");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(examwriter.getPath()));
        } catch (DocumentException | FileNotFoundException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden.");
        }
        document.open();

        // prepare the fonts used in the document

        LOG.info("Prepare report");

        // at first we create the header with exam name, student info and so on

        try {
            Paragraph headerContainerParagraph = getHeader(examwriter.getName());
            document.add(headerContainerParagraph);
        } catch (DocumentException e) {
            throw new PersistenceException("Der Header konnte nicht in das PDF integriert werden.");
        }

        // Each question is added to a table as well as an indicator if the answer was correct or not

        for (var i = 0; i < examwriter.getQuestions().size(); i++) {

            try {
                Paragraph container = getQuestionParagraph(examwriter.getQuestions().get(i), examwriter.getName(), i);
                document.add(container);
            } catch (DocumentException e) {
                throw new PersistenceException("Die Ergebnisse konnten nicht in das Dokument eingefügt werden.");
            }
        }

        document.close();
    }

    public Paragraph getHeader(String name) throws PersistenceException {
        if (name == null) {
            throw new PersistenceException("PDF-Name ist null!");
        }

        // the container is used to ensure that the text is not split over two pages
        // highly unlikely, but just to be sure.

        Paragraph headerContainerParagraph = new Paragraph();

        Paragraph titleParagraph = new Paragraph("Prüfung", fontTitle);
        titleParagraph.setSpacingAfter(0);

        headerContainerParagraph.add(titleParagraph);

        Paragraph examNameParagraph = new Paragraph(name, fontExamName);
        examNameParagraph.setSpacingAfter(0);

        headerContainerParagraph.add(examNameParagraph);

        Paragraph dateParagraph = new Paragraph("am: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()), fontExamDate);
        dateParagraph.setSpacingAfter(10);

        headerContainerParagraph.add(dateParagraph);

        Paragraph studentInfoParagraph = new Paragraph("Student:\nName: " + this.student.getName() + "\nMatrikelnummer: " + this.student.getMatriculationNumber(), fontStudentInfo);
        studentInfoParagraph.setSpacingAfter(10);

        headerContainerParagraph.add(studentInfoParagraph);

        return headerContainerParagraph;
    }

    public Paragraph getQuestionParagraph(Question question, String name, int i) throws PersistenceException {
        if (question == null || name == null) {
            throw new PersistenceException("PDF Frage oder Name ist null!");
        }
        // at first the question text is added to the document.
        // afterwards a table is created where each row holds an answer and the
        // expected state as well as the actual state of the checkbox.

        // the container is used to hold the question as well as the table with the answers.
        // this is done to ensure, that the question is on a different page than the answers.

        Paragraph container = new Paragraph();

        Paragraph paragraphQuestionNumber = new Paragraph("Frage " + (i + 1) + ":");
        paragraphQuestionNumber.setSpacingBefore(15);

        Paragraph paragraphQuestionText = new Paragraph(question.getQuestionText());
        paragraphQuestionText.setSpacingAfter(5);

        container.add(paragraphQuestionNumber);
        container.add(paragraphQuestionText);

        // add an image if the question has one

        if (!question.getPicture().equals("")) {

            PdfPCell cellImgQuestion = getImageCellQuestions(name, question.getPicture());

            // there have been problems with adding the image directly.
            // as a workaround a table is created with a single cell that holds the image.

            PdfPTable imgTable = new PdfPTable(1);

            imgTable.setWidthPercentage(100);

            imgTable.addCell(cellImgQuestion);
            imgTable.setSpacingAfter(5);

            container.add(imgTable);
        }

        container.setSpacingAfter(15);
        container.setKeepTogether(true);

        // a nesting table is needed to ensure that a table is not split over two pages.

        PdfPTable nesting = new PdfPTable(1);
        nesting.setWidthPercentage(100);

        PdfPTable answerTable = getAnswerTable(question);

        PdfPCell cell = new PdfPCell(answerTable);
        cell.setBorder(PdfPCell.NO_BORDER);
        nesting.addCell(cell);

        container.add(nesting);

        return container;
    }

    public PdfPTable getAnswerTable(Question question) throws PersistenceException {
        if (question == null) {
            throw new PersistenceException("PDF-Frage ist null!");
        }
        // create a table with 4 columns and stretch it to 100% of the page width

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        try {
            table.setWidths(new float[]{8, (float) 1.25, (float) 1.5, (float) 1});
        } catch (DocumentException e) {
            throw new PersistenceException("Eine Tabelle konnte nicht für das PDF formatiert werden.");
        }

        table.addCell("Antworten");
        table.addCell("Erwartet");
        table.addCell("Ausgewählt");
        table.addCell("Richtig");

        ArrayList<String> allAnswers = new ArrayList<>();

        try {
            if (!question.getAnswer1().equals("")) {
                allAnswers.add(question.getAnswer1());
            }
        } catch (NullPointerException e) {
            // no answer present.
        }
        try {
            if (!question.getAnswer2().equals("")) {
                allAnswers.add(question.getAnswer2());
            }
        } catch (NullPointerException e) {
            // no answer present.
        }
        try {
            if (!question.getAnswer3().equals("")) {
                allAnswers.add(question.getAnswer3());
            }
        } catch (NullPointerException e) {
            // no answer present.
        }
        try {
            if (!question.getAnswer4().equals("")) {
                allAnswers.add(question.getAnswer4());
            }
        } catch (NullPointerException e) {
            // no answer present.
        }
        try {
            if (!question.getAnswer5().equals("")) {
                allAnswers.add(question.getAnswer5());
            }
        } catch (NullPointerException e) {
            // no answer present.
        }

        for (int j = 0; j < allAnswers.size(); j++) {

            table.addCell(allAnswers.get(j));

            String correctAnswers = question.getCorrectAnswers();
            String checkedAnswers = question.getCheckedAnswers();

            // we have to start with 1 because the answers also start with 1
            String indexStr = Integer.toString(j + 1);

            boolean answerWasCorrect = correctAnswers.contains(indexStr);

            boolean answerWasChecked;

            // skipped questions are treated as false

            try {
                answerWasChecked = checkedAnswers.contains(indexStr);
            } catch (NullPointerException e) {
                answerWasChecked = false;
            }

            table.addCell((answerWasCorrect) ? cell_checked : cell_box);
            table.addCell((answerWasChecked) ? cell_checked : cell_box);
            table.addCell((answerWasCorrect == answerWasChecked) ? cell_checked : cell_box);
        }

        return table;
    }

    public PdfPCell getImageCellBox(URL path) throws PersistenceException {
        if (path == null) {
            throw new PersistenceException("PDF-Pfad ist null!");
        }
        Image img;
        try {
            img = Image.getInstance(path);
            img.scaleAbsolute((float) 12.0, (float) 12.0);
        } catch (BadElementException | IOException e) {
            throw new PersistenceException("Die Ressourcen für die PDF Datei konnten nicht geladen werden.");
        }

        PdfPCell cell = new PdfPCell(img, false);

        cell.setFixedHeight(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    public PdfPCell getImageCellQuestions(String name, String picture) throws PersistenceException {
        if (name == null || picture == null) {
            throw new PersistenceException("PDF-Name oder das Bild ist null!");
        }
        Image img;

        String imagePath =
            System.getProperty("user.dir") + File.separator + "img" + File.separator + name + File.separator + picture;

        try {
            img = Image.getInstance(imagePath);
        } catch (BadElementException | IOException e) {
            throw new PersistenceException("Ein Bild konnte nicht für das PDF geladen werden: " + imagePath);
        }

        PdfPCell cell = new PdfPCell(img, false);

        // very wide images get a smaller height. if this is not done, the image would
        // be too big and reach out of the bounds of the pdf file.

        if (img.getWidth() > (img.getHeight() * 2)) {
            cell.setFixedHeight(80);
        } else {
            cell.setFixedHeight(180);
        }

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);

        return cell;
    }
}

