package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamWriter;

public interface IExamResultsWriterService {

    /**
     * Write the exam results to a file for future reference
     *
     * @param examwriter includes questions, name and path of exam
     * @throws ServiceException if the exam results cannot be written
     */
    void writeExamResults(ExamWriter examwriter) throws ServiceException;

}
