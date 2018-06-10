package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IQuestionnaireExportService {

    /**
     * Exports the selected questionnaire as a CSV file. After that, it selects the StudyMode again as a default.
     *
     * @param fileName, contains the FileName which is used for saving the File.
     * @throws ServiceException when exporting the file fails
     */
    void exportSelectedQuestionnaire(String fileName) throws ServiceException;

    /**
     * Gets every question for the selected questionnaire
     *
     * @return all questions of the selected questionnaire in form of a list
     * @throws ServiceException when getting all questions fails
     */
    List<Question> getAllData(LearningQuestionnaire learningQuestionnaire) throws ServiceException;
}
