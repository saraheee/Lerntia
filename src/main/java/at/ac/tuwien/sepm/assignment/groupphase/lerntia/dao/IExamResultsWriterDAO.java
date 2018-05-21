package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

public interface IExamResultsWriterDAO {

    public void writeExamResults(List<Question> questions, String path);

}
