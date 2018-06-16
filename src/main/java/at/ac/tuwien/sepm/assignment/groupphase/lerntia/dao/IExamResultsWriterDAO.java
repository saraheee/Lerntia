package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.net.URL;
import java.util.List;

public interface IExamResultsWriterDAO {

    /**
     * Write the exam results to a file for future reference
     *
     * @param questions a list of all the questions used in the exam
     * @param name the name of the exam
     * @param path the path where the exam results should be saved
     * @throws PersistenceException if the exam results cannot be written
     * */
    void writeExamResults(List<Question> questions, String name, String path) throws PersistenceException;

    Paragraph getHeader(String name) throws PersistenceException;

    Paragraph getQuestionParagraph(Question question, String name, int i) throws PersistenceException;

    PdfPTable getAnswerTable(Question question) throws PersistenceException;

    PdfPCell getImageCellBox( URL path ) throws PersistenceException;

    PdfPCell getImageCellQuestions(String name, String picture) throws PersistenceException;

}
