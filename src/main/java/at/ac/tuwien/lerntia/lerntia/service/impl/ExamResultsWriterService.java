package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.lerntia.lerntia.dto.ExamWriter;
import at.ac.tuwien.lerntia.lerntia.service.IExamResultsWriterService;
import at.ac.tuwien.lerntia.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamResultsWriterService implements IExamResultsWriterService {


    private final IExamResultsWriterDAO iExamResultsWriterDAO;

    @Autowired
    public ExamResultsWriterService(IExamResultsWriterDAO iExamResultsWriterDAO) {
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
