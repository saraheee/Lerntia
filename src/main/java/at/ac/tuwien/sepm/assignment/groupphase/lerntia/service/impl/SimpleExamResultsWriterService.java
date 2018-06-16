package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamResultsWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleExamResultsWriterService implements IExamResultsWriterService {


    private final IExamResultsWriterDAO iExamResultsWriterDAO;

    @Autowired
    public SimpleExamResultsWriterService(IExamResultsWriterDAO iExamResultsWriterDAO) {
        this.iExamResultsWriterDAO = iExamResultsWriterDAO;
    }

    @Override
    public void writeExamResults(List<Question> questions, String name, String path) throws ServiceException {
        try {
            iExamResultsWriterDAO.writeExamResults(questions, name, path);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }
}
