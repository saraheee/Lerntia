package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.ExamQuestionnaire;

import java.util.List;

public interface IExamQuestionnaireService {

    /**
     * Create a new ExamQuestionnaire and save it
     *
     * @param examQuestionnaire ExamQuestionnaire that needs to be saved
     * @throws ServiceException if the ExamQuestionnaire can't be created
     */
    void create(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    /**
     * Read all ExamQuestionnaires
     *
     * @return list of all ExamQuestionnaires
     * @throws ServiceException if the list cannot be retrieved
     */
    List<ExamQuestionnaire> readAll() throws ServiceException;


}
