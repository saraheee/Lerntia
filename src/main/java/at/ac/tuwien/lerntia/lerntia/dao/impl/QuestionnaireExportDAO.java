package at.ac.tuwien.lerntia.lerntia.dao.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireExportDAO;
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
import java.nio.file.Path;
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
            throw new PersistenceException("Mindestens ein Wert für das Exportieren ist null");
        }
        Path csvPath = Paths.get(System.getProperty("user.dir") + File.separator + "csv_export");
        File csvDir = new File(String.valueOf(csvPath));

        //Create export directory
        if (!Files.exists(csvPath)) {
            if (!csvDir.mkdir()) {
                throw new PersistenceException("Der Versuch ein Verzeichnis zu erzeugen ist fehlgeschlagen.");
            }
            LOG.info("Created a new export directory for the CSV.");
        }

        if (!overwriteFile) {
            //Check if file exists
            FileFilter fileFilter = new WildcardFileFilter("*.CSV", IOCase.INSENSITIVE);
            File[] fileList = csvDir.listFiles(fileFilter);
            if (fileList != null && fileList.length > 0) {
                Arrays.sort(fileList, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                for (File f : fileList) {
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
            throw new PersistenceException("Der Versuch eine CSV Exportdatei zu erzeugen ist fehlgeschlagen.");
        }
        //Get all questions
        StringBuilder csvOutput = new StringBuilder();
        for (Question q : allQuestions) {
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
            throw new PersistenceException("Der Versuch alle notwendigen Fragen zu exportieren ist fehlgeschlagen.");
        }
        //Flush & Close
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new PersistenceException("Der Versuch den CSV-Schreiber zu schließen ist fehlgeschlagen..");
        }
    }


    @Override
    public void overwriteFile(LearningQuestionnaire questionnaire, List<Question> allQuestions) throws PersistenceException, FileExistsException {
        if (questionnaire == null || allQuestions == null) {
            throw new PersistenceException("Mindestens ein Wert für das Exportieren der Fragen ist null!");
        }
        overwriteFile = true;
        exportQuestionnaire(questionnaire, allQuestions);
    }

}