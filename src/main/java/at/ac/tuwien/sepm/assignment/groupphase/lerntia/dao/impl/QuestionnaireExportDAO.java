package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireExportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class QuestionnaireExportDAO implements IQuestionnaireExportDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private boolean overwriteFile = false;


    @Override
    public void exportQuestionnaire(LearningQuestionnaire questionnaire, List<Question> allQuestions) throws PersistenceException, FileExistsException {
        if (questionnaire == null || allQuestions == null) {
            throw new PersistenceException("At least one value of the questionnaire export values is null!");
        }
        var csvPath = Paths.get(System.getProperty("user.dir") + File.separator + "csv_export");
        var csvDir = new File(String.valueOf(csvPath));

        //Create export directory
        if (!Files.exists(csvPath)) {
            if (!csvDir.mkdir()) {
                throw new PersistenceException("Failed to create export directory!");
            }
            LOG.info("Created a new export directory for the CSV.");
        }

        if (!overwriteFile) {
            //Check if file exists
            FileFilter fileFilter = new WildcardFileFilter("*.CSV", IOCase.INSENSITIVE);
            var fileList = csvDir.listFiles(fileFilter);
            if (fileList != null && fileList.length > 0) {
                Arrays.sort(fileList, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                for (var f : fileList) {
                    LOG.debug("Existing file names: " + f.getName());
                    if (f.getName().equals(questionnaire.getName() + ".csv")) {
                        LOG.debug("File already exists for this questionnaire.");
                        throw new FileExistsException("Dieser Name existiert bereits!");
                    }
                }
            }
        }
        overwriteFile = false;

        //Create the export file
        FileWriter writer;
        try {
            writer = new FileWriter(csvPath + File.separator + questionnaire.getName() + ".csv");
        } catch (IOException e) {
            LOG.error("Failed to create the export file.");
            throw new PersistenceException("Failed to create the CSV export file.");
        }
        //Get all questions
        var csvOutput = new StringBuilder();
        for (var q : allQuestions) {
            csvOutput.append(q.getQuestionText()).append(";")
                .append(q.getAnswer1()).append(";")
                .append(q.getAnswer2()).append(";")
                .append(q.getAnswer3()).append(";")
                .append(q.getAnswer4()).append(";")
                .append(q.getAnswer5()).append(";")
                .append(q.getCorrectAnswers()).append(";")
                .append(q.getPicture()).append(";")
                .append(q.getOptionalFeedback()).append(";\n");
        }
        try {
            writer.append(csvOutput.toString());
        } catch (IOException e) {
            LOG.error("Failed to write data to the export file.");
            throw new PersistenceException("Failed to write all questions for the CSV export.");
        }
        //Flush & Close
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOG.error("Failed to flush and close the writer for the export file.");
            throw new PersistenceException("Failed to close the writer for the CSV export.");
        }
    }


    @Override
    public void overwriteFile(LearningQuestionnaire questionnaire, List<Question> allQuestions) throws PersistenceException, FileExistsException {
        if (questionnaire == null || allQuestions == null) {
            throw new PersistenceException("At least one value of the questionnaire export values is null!");
        }
        overwriteFile = true;
        exportQuestionnaire(questionnaire, allQuestions);
    }

}