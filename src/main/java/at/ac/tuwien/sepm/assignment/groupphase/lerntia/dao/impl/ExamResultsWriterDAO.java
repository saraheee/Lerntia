package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class ExamResultsWriterDAO implements IExamResultsWriterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void writeExamResults(List<Question> questions, String path) throws PersistenceException {

        // A document object is created in which the report will be saved

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("exam_report.pdf"));
        } catch (DocumentException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden");
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Das PDF-Dokument konnte nicht erstellt werden");
        }
        document.open();

        // Prepare the report

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(2);

        // Each question is added to a table as well as an indicator if the answer was correct or not

        for (var i = 0; i < questions.size(); i++){

            boolean answersCorrect = false;

            try {
                var checkedAnswers = questions.get(i).getCheckedAnswers();
                answersCorrect = checkedAnswers.equals(questions.get(i).getCorrectAnswers());
            } catch (NullPointerException e){
                // TODO - at least one question has not been answered. this should also be checked in the controller.
            }

            // It is sufficient to add the cells without thinking about rows or columns as the number of
            // columns is saved in the table object.

            table.addCell(questions.get(i).getQuestionText());
            table.addCell((answersCorrect) ? "Richtig" : "Falsch");
        }

        // The table is added to the document which is closed afterwards

        try {
            document.add(table);
        } catch (DocumentException e) {
            throw new PersistenceException("Die Ergebnisse konnte nicht in das Dokument eingefügt werden");
        }

        document.close();
    }
}
