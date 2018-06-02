package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExamResultsWriterDAO implements IExamResultsWriterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleQuestionService questionService;

    public ExamResultsWriterDAO(SimpleQuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public void writeExamResults(List<Question> questions, String path) throws PersistenceException {

        LOG.info("Create new Document for new report");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("exam_report.pdf"));
        } catch (DocumentException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden");
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden");
        }
        document.open();

        Image img_checked = null;
        try {
            img_checked = Image.getInstance("src/main/resources/icons/exam_report_box_checked.png");
            img_checked.scaleAbsolute((float) 12.0, (float) 12.0);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfPCell cell_checked = new PdfPCell(img_checked, false);
        cell_checked.setFixedHeight(12);
        cell_checked.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell_checked.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Image img_box = null;
        try {
            img_box = Image.getInstance("src/main/resources/icons/exam_report_box.png");
            img_box.scaleAbsolute((float) 12.0, (float) 12.0);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfPCell cell_box = new PdfPCell(img_box, false);
        cell_box.setFixedHeight(12);
        cell_box.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell_box.setVerticalAlignment(Element.ALIGN_MIDDLE);

        LOG.info("Prepare report");

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
            paragraphQuestionText.setSpacingAfter(10);

            container.add(paragraphQuestionNumber);
            container.add(paragraphQuestionText);

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
                e.printStackTrace();
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
                throw new PersistenceException("Die Ergebnisse konnte nicht in das Dokument eingefügt werden");
            }
        }

        document.close();
    }
}
