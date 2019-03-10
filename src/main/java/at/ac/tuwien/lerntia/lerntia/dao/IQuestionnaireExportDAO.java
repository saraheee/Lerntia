package at.ac.tuwien.lerntia.lerntia.dao;

import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import org.apache.commons.io.FileExistsException;

import java.util.List;

public interface IQuestionnaireExportDAO {

    /**
     * Exports the selected questionnaire as a CSV file.
     *
     * @param questionnaire the questionnaire to export, which name gets used for the filename
     * @param allData       all data that should be written to the export file
     * @throws PersistenceException if exporting the questionnaire fails
     * @throws FileExistsException  if the file already exists
     */
    void exportQuestionnaire(LearningQuestionnaire questionnaire, List<Question> allData) throws PersistenceException, FileExistsException;


    /**
     * Overwrites an existing CSV file with the current version of the questionnaire
     *
     * @param questionnaire the questionnaire to export
     * @param allData       all data that should be written to the export file
     * @throws PersistenceException if exporting the questionnaire fails
     * @throws FileExistsException  if the file already exists
     */
    void overwriteFile(LearningQuestionnaire questionnaire, List<Question> allData) throws PersistenceException, FileExistsException;
}
