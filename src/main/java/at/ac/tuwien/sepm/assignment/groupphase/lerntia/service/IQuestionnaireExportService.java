package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IQuestionnaireExportService {

    /**
     * Exports the Selected Questionnaire as a CSV Data. After that, its select the StudyMode again as a Default.
     * @param fileName, contains the FileName which is used for saving the File.
     *
     */
    void exportSelectedQuestionnaire(String fileName);

    /**
     * This Fucntions gets every Question for the SelectedQuestionnaire
     * @return all the Questions of the selectedQuestionnaire in form of a list
     */
    List<Question> getAllData(LearningQuestionnaire learningQuestionnaire);
}
