package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

public interface IExamResultsWriterDAO {

    /**
     * Write the exam results to a file for future reference
     *
     * @param questions a list of all the questions used in the exam
     * @param path the path where the exam results should be saved
     * @throws PersistenceException if the exam results cannot be written
     * */
    void writeExamResults(List<Question> questions, String path) throws PersistenceException;

}
