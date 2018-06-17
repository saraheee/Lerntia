package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
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

    /**
     * Select an ExamQuestionnaire to be used for an exam
     *
     * @param examQuestionnaire the ExamQuestionnaire to be selected
     * @throws ServiceException if the ExamQuestionnaire cannot be selected
     */
    void select(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    /**
     * Deselect an ExamQuestionnaire that was previously used
     *
     * @param examQuestionnaire the ExamQuestionnaire to be deselected
     * @throws ServiceException if the ExamQuestionnaire cannot be deselected
     */
    void deselect(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    /**
     * Get the selected ExamQuestionnaire
     *
     * @return the selected ExamQuestionnaire
     * @throws ServiceException if the selected ExamQuestionnaire cannot be retrieved
     */
    ExamQuestionnaire getSelected() throws ServiceException;
}
