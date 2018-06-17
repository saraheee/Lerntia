package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamWriter;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.net.URL;
import java.util.List;

public interface IExamResultsWriterDAO {

    /**
     * Write the exam results to a file for future reference
     *
     * @param examwriter includes questions, name and path of exam
     * @throws PersistenceException if the exam results cannot be written
     * */
    void writeExamResults(ExamWriter examwriter) throws PersistenceException;

    /**
     * Get the header of the exam
     *
     * @param name the name of the exam
     * @return the header for the exam
     * @throws PersistenceException if the header cannot be obtained
     * */
    Paragraph getHeader(String name) throws PersistenceException;

    /**
     * Get the paragraph of one question. Containing the image and the table with answers
     *
     * @param question the question for which the paragraph will be created
     * @param name the name of the exam
     * @param i the index of the current question
     * @return the question paragraph
     * @throws PersistenceException if the question paragraph cannot be obtained
     * */
    Paragraph getQuestionParagraph(Question question, String name, int i) throws PersistenceException;

    /**
     * Get the table containing all the answers and their state
     *
     * @param question the question for which the answer table will be created
     * @return the answer table
     * @throws PersistenceException if the answer table cannot be obtained
     * */
    PdfPTable getAnswerTable(Question question) throws PersistenceException;

    /**
     * Get cells containing an image used to display the state of an answer
     *
     * @param path the path where the image can be found
     * @return the cell with the image
     * @throws PersistenceException if the cell cannot be created
     * */
    PdfPCell getImageCellBox( URL path ) throws PersistenceException;

    /**
     * Get the image to a question
     *
     * @param name the name of the exam
     * @param picture the name of the picture
     * @return the cell with the image
     * @throws PersistenceException if the cell cannot be created
     * */
    PdfPCell getImageCellQuestions(String name, String picture) throws PersistenceException;

}
