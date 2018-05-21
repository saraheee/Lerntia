package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamResultsWriterDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamResultsWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleExamResultsWriterService implements IExamResultsWriterService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IExamResultsWriterDAO iExamResultsWriterDAO;

    public SimpleExamResultsWriterService(IExamResultsWriterDAO iExamResultsWriterDAO) {
        this.iExamResultsWriterDAO = iExamResultsWriterDAO;
    }

    @Override
    public void writeExamResults(List<Question> questions, String path) {
        iExamResultsWriterDAO.writeExamResults(questions, path);
    }
}
