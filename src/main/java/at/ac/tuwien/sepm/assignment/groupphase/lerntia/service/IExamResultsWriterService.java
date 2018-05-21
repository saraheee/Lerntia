package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

public interface IExamResultsWriterService {

    public void writeExamResults(List<Question> questions, String path);

}
