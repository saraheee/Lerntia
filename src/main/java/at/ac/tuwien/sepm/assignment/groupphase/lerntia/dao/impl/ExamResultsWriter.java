package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

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
public class ExamResultsWriter implements IExamResultsWriterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void writeExamResults(List<Question> questions, String path) {

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("exam_report.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        LOG.info("Prepare report");

        PdfPTable table = new PdfPTable(2);

        for (var i = 0; i < questions.size(); i++){

            boolean answersCorrect = false;

            try {
                var checkedAnswers = questions.get(i).getCheckedAnswers();
                answersCorrect = checkedAnswers.equals(questions.get(i).getCorrectAnswers());
            } catch (NullPointerException e){
                // TODO - at least one question has not been answered.
            }

            table.addCell(questions.get(i).getQuestionText());
            table.addCell((answersCorrect) ? "Richtig" : "Falsch");
        }

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
    }
}
