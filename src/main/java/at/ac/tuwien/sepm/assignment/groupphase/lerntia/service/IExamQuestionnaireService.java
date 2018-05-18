package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import javafx.collections.ObservableList;

import java.util.List;

public interface IExamQuestionnaireService {

    /**
     * Create a new ExamQuestionnaire and save it in the Database
     *
     * @param examQuestionnaire ExamQuestionnaire that needs to be saved to the Database
     * @throws ServiceException if the ExamQuestionnaire can't be created
     * */
    void create(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    /**
     * Update an existing ExamQuestionnaire with new Parameters and save those to the Database
     *
     * @param examQuestionnaire the ExamQuestionnaire in question with the new or same parameters.
     * @throws ServiceException if the method can't update the ExamQuestionnaire
     * */
    void update(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    /**
     * Search for ExamQuestionnaire with given searchparameters in the Database
     *
     * @param searchparameters all or some searchparameters of the ExamQuestionnaire
     * @throws ServiceException if the method can't render
     * */
    void search(ExamQuestionnaire searchparameters) throws ServiceException;

    /**
     * Delete ExamQuestionnaire from the Database
     *
     * @param examQuestionnaire ExamQuestionnaire in question that has to be deleted
     * @throws ServiceException if the method can't delete the ExamQuestionnaire from the Database
     * */
    void delete(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    List<ExamQuestionnaire> readAll() throws ServiceException;

    void select(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    void deselect(ExamQuestionnaire examQuestionnaire) throws ServiceException;

    ExamQuestionnaire getSelected() throws ServiceException;
}
