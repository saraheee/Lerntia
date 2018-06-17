package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamWriter;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamResultsWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleExamResultsWriterService implements IExamResultsWriterService {


    private final IExamResultsWriterDAO iExamResultsWriterDAO;

    @Autowired
    public SimpleExamResultsWriterService(IExamResultsWriterDAO iExamResultsWriterDAO) {
        this.iExamResultsWriterDAO = iExamResultsWriterDAO;
    }

    @Override
    public void writeExamResults(ExamWriter examwriter) throws ServiceException {
        try {
            iExamResultsWriterDAO.writeExamResults(examwriter);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }
}
