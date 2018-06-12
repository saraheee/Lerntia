package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleUserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.maven.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ExamResultsWriterDAO implements IExamResultsWriterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleUserService simpleUserService;
    private final SimpleQuestionService questionService;

    public ExamResultsWriterDAO(
        SimpleQuestionService questionService,
        SimpleUserService simpleUserService
    ) {
        this.questionService = questionService;
        this.simpleUserService = simpleUserService;
    }

    @Override
    public void writeExamResults(List<Question> questions, String name, String path) throws PersistenceException {

        // create the document

        LOG.info("Create new Document for new report");
        Document document = new Document();
        try {
            //PdfWriter.getInstance(document, new FileOutputStream("exam_report.pdf"));
            PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden.");
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden.");
        }
        document.open();

        // images are used to show if an answer has been selected or not.
        // these two images are loaded here and placed in a cell.
        // later these cells are added to the table of answers.

        Image img_checked = null;
        try {
            img_checked = Image.getInstance(Resource.class.getResource("/icons/exam_report_box_checked.png"));
            img_checked.scaleAbsolute((float) 12.0, (float) 12.0);
        } catch (BadElementException e) {
            throw new PersistenceException("Die Recourcen für die PDF Datei konnten nicht geladen werden.");
        } catch (IOException e) {
            throw new PersistenceException("Die Recourcen für die PDF Datei konnten nicht geladen werden.");
        }

        PdfPCell cell_checked = new PdfPCell(img_checked, false);
        cell_checked.setFixedHeight(12);
        cell_checked.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell_checked.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Image img_box = null;
        try {
            img_box = Image.getInstance(Resource.class.getResource("/icons/exam_report_box.png"));
            img_box.scaleAbsolute((float) 12.0, (float) 12.0);
        } catch (BadElementException e) {
            throw new PersistenceException("Die Recourcen für die PDF Datei konnten nicht geladen werden.");
        } catch (IOException e) {
            throw new PersistenceException("Die Recourcen für die PDF Datei konnten nicht geladen werden.");
        }

        PdfPCell cell_box = new PdfPCell(img_box, false);
        cell_box.setFixedHeight(12);
        cell_box.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell_box.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // prepare the fonts used in the document

        Font fontTitle       = FontFactory.getFont(FontFactory.HELVETICA, 26, BaseColor.BLACK);
        Font fontExamName    = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font fontExamDate    = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font fontStudentInfo = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

        LOG.info("Prepare report");

        // at first we create the header with exam name, student info and so on

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

        User student = null;
        try {
            student = simpleUserService.read();
        } catch (ServiceException e) {
            throw new PersistenceException("Die Daten des Studenten konnten für die PDF Datei nicht geladen werden.");
        }

        Paragraph studentInfoParagraph = new Paragraph("Student:\nName: "+student.getName()+"\nMatrikelnummer: "+student.getMatriculationNumber(), fontStudentInfo);
        studentInfoParagraph.setSpacingAfter(10);

        headerContainerParagraph.add(studentInfoParagraph);

        try {
            document.add(headerContainerParagraph);
        } catch (DocumentException e) {
            throw new PersistenceException("Der Header konnte nicht in das PDF integriert werden.");
        }

        // Each question is added to a table as well as an indicator if the answer was correct or not

        for (var i = 0; i < questions.size(); i++){

            // at first the question text is added to the document.
            // afterwards a table is created where each row holds an answer and the
            // expected state as well as the actual state of the checkbox.

            // the container is used to hold the question as well as the table with the answers.
            // this is done to ensure, that the question is on a different page than the answers.

            Paragraph container = new Paragraph();

            Paragraph paragraphQuestionNumber = new Paragraph("Frage " + (i+1) + ":");
            paragraphQuestionNumber.setSpacingBefore(15);

            Paragraph paragraphQuestionText = new Paragraph(questions.get(i).getQuestionText());
            paragraphQuestionText.setSpacingAfter(5);

            container.add(paragraphQuestionNumber);
            container.add(paragraphQuestionText);

            // add an image if the question has one

            if (questions.get(i).getPicture() != "") {

                Image imgQuestion = null;

                String imagePath =
                    System.getProperty("user.dir") + File.separator + "img" + File.separator +
                        name + File.separator +
                        questions.get(i).getPicture();

                try {
                    imgQuestion = Image.getInstance(imagePath);
                } catch (BadElementException e) {
                    throw new PersistenceException("Ein Bild konnte nicht für das PDF geladen werden.");
                } catch (IOException e) {
                    throw new PersistenceException("Ein Bild konnte nicht für das PDF geladen werden.");
                }

                // there have been problems with adding the image directly.
                // as a workaround a table is created with a single cell that holds the image.

                PdfPTable imgTable = new PdfPTable(1);

                imgTable.setWidthPercentage(100);

                PdfPCell cellImgQuestion = new PdfPCell(imgQuestion, false);

                // very wide images get a smaller height. if this is not done, the image would
                // be too big and reach out of the bounds of the pdf file.

                if (imgQuestion.getWidth() > ( imgQuestion.getHeight()*2 )) {
                    cellImgQuestion.setFixedHeight(80);
                } else {
                    cellImgQuestion.setFixedHeight(180);
                }

                cellImgQuestion.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellImgQuestion.setBorder(PdfPCell.NO_BORDER);

                imgTable.addCell(cellImgQuestion);
                imgTable.setSpacingAfter(5);

                container.add(imgTable);
            }

            container.setSpacingAfter(15);
            container.setKeepTogether(true);

            // a nesting table is needed to ensure that a table is not split over two pages.

            PdfPTable nesting = new PdfPTable(1);
            nesting.setWidthPercentage(100);

            // create a table with 4 columns and stretch it to 100% of the page width

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            try {
                table.setWidths(new float[] { 8, (float) 1.25, (float) 1.5, (float) 1});
            } catch (DocumentException e) {
                throw new PersistenceException("Eine Tabelle konnte nicht für das PDF formatiert werden.");
            }

            table.addCell("Antworten");
            table.addCell("Erwartet");
            table.addCell("Ausgewählt");
            table.addCell("Richtig");

            ArrayList<String> allAnswers = questionService.getAllAnswers(questions.get(i));

            for (int j = 0; j < allAnswers.size(); j++){

                table.addCell(allAnswers.get(j));

                String correctAnswers = questions.get(i).getCorrectAnswers();
                String checkedAnswers = questions.get(i).getCheckedAnswers();

                // we have to start with 1 because the answers also start with 1
                String indexStr = Integer.toString(j+1);

                boolean answerWasCorrect = correctAnswers.contains(indexStr);

                boolean answerWasCecked;

                // skipped questions are treated as false

                try{
                    answerWasCecked = checkedAnswers.contains(indexStr);
                } catch (NullPointerException e){
                    answerWasCecked = false;
                }

                table.addCell( (answerWasCorrect) ? cell_checked : cell_box );
                table.addCell( (answerWasCecked) ? cell_checked : cell_box );
                table.addCell( (answerWasCorrect == answerWasCecked) ? cell_checked : cell_box );
            }

            PdfPCell cell = new PdfPCell(table);
            cell.setBorder(PdfPCell.NO_BORDER);
            nesting.addCell(cell);

            container.add(nesting);

            try {
                document.add(container);
            } catch (DocumentException e) {
                throw new PersistenceException("Die Ergebnisse konnte nicht in das Dokument eingefügt werden.");
            }
        }

        document.close();
    }
}
