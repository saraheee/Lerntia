package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

public interface IExamResultsWriterService {

    /**
     * Write the exam results to a file for future reference
     *
     * @param questions a list of all the questions used in the exam
     * @param path the path where the exam results should be saved
     * @throws ServiceException if the exam results cannot be written
     * */
    public void writeExamResults(List<Question> questions, String path) throws ServiceException, PersistenceException;

}
