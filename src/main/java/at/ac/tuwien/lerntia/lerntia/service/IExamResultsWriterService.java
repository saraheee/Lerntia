package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.ExamWriter;

public interface IExamResultsWriterService {

    /**
     * Write the exam results to a file for future reference
     *
     * @param examWriter includes questions, name and path of exam
     * @throws ServiceException if the exam results cannot be written
     */
    void writeExamResults(ExamWriter examWriter) throws ServiceException;

    /**
     * Send the exam results per email to the user specified in the config file
     *
     * @param filePath the path of the exam results
     * @throws ServiceException if the email cannot be send
     */
    void sendExamResultsPerEmail(String filePath) throws ServiceException;

}
