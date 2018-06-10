package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.apache.commons.io.FileExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IQuestionnaireExportService {

    /**
     * Exports the selected questionnaire as a CSV file. After that, it selects the StudyMode again as a default.
     *
     * @param questionnaire the questionnaire to export, which name gets used for the filename
     * @throws ServiceException    if exporting the questionnaire fails
     * @throws FileExistsException if the file already exists
     */
    void exportSelectedQuestionnaire(LearningQuestionnaire questionnaire) throws ServiceException, FileExistsException;

    /**
     * Gets every question for the selected questionnaire
     *
     * @return all questions of the selected questionnaire in form of a list
     * @throws ServiceException when getting all questions fails
     */
    List<Question> getAllData(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Overwrites an existing CSV file with the current version of the questionnaire
     *
     * @param questionnaire the questionnaire to export
     * @throws ServiceException    if exporting the questionnaire fails
     * @throws FileExistsException if the file already exists
     */
    void overwriteFile(LearningQuestionnaire questionnaire) throws ServiceException, FileExistsException;
}
