package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleQuestionnaireImportService implements IQuestionnaireImportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final QuestionnaireImportDAO questionnaireImportDAO;
    private final SimpleQuestionService simpleQuestionService;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final SimpleExamQuestionnaireService simpleExamQuestionnaireService;
    private final SimpleQuestionnaireQuestionService simpleQuestionnaireQuestionService;

    public SimpleQuestionnaireImportService(
        QuestionnaireImportDAO questionnaireImportDAO,
        SimpleQuestionService simpleQuestionService,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        SimpleExamQuestionnaireService simpleExamQuestionnaireService,
        SimpleQuestionnaireQuestionService simpleQuestionnaireQuestionService
    ) {
        this.questionnaireImportDAO = questionnaireImportDAO;
        this.simpleQuestionService = simpleQuestionService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.simpleExamQuestionnaireService = simpleExamQuestionnaireService;
        this.simpleQuestionnaireQuestionService = simpleQuestionnaireQuestionService;
    }

    public void importQuestionnaire(File file, Course course, String name, boolean isExam) throws ServiceException {

        String pathStr = file.getAbsolutePath();

        // TODO - fix duplicate code

        if (isExam) {
            List<ExamQuestionnaire> questionnaires = null;
            questionnaires = simpleExamQuestionnaireService.readAll();

            for (ExamQuestionnaire questionnaire : questionnaires) {
                if (name.equals(questionnaire.getName())) {
                    throw new ServiceException("Dieser Name existiert schon!");
                }
            }

        } else {
            List<LearningQuestionnaire> questionnaires = null;
            questionnaires = simpleLearningQuestionnaireService.readAll();

            for (LearningQuestionnaire questionnaire : questionnaires) {
                if (name.equals(questionnaire.getName())) {
                    throw new ServiceException("Dieser Name existiert schon!");
                }
            }
        }

        // get questionnaire file content

        ArrayList<String> fileContent = new ArrayList<>();

        try {
            fileContent = questionnaireImportDAO.getContents(pathStr);
        } catch (IOException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }

        ArrayList<Long> questionIDs = new ArrayList<>();

        for (String aFileContent : fileContent) {

            // split the rows, the seperator is ";"
            String[] lineParts = aFileContent.split(";");

            // check if there are too many columns
            if (lineParts.length > 9) {
                throw new ServiceException("Zu viele Spalten");
            }

            // index 6 has the right answers. this is an integer
            try {
                int rightAnswers = Integer.parseInt(lineParts[6]);
            } catch (NumberFormatException e) {
                throw new ServiceException("Richtige Antwort fehlt");
            }

            // index 7 is the image (optional)
            String picture = "";
            try {
                if (!lineParts[7].equals("")) {
                    picture = lineParts[7];
                    String path = System.getProperty("user.dir") + File.separator + "img" + File.separator + name + File.separator + picture;
                    File f = new File(path);
                    if (!f.exists()) {
                        throw new ServiceException("Mindestens ein Bild aus csv-Datei wurde nicht gefunden");
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // there is no image
                picture = "";
            }

            // index 8 is the optional feedback
            String feedback = "";
            try {
                if (!lineParts[8].equals("")) {
                    feedback = lineParts[8];
                }
            } catch (IndexOutOfBoundsException e) {
                // there is no feedback
                feedback = "";
            }

            Question q = new Question();
            q.setId((long) 0);
            q.setQuestionText(lineParts[0]);
            q.setPicture(picture);
            q.setAnswer1(lineParts[1]);
            q.setAnswer2(lineParts[2]);
            q.setAnswer3(lineParts[3]);
            q.setAnswer4(lineParts[4]);
            q.setAnswer5(lineParts[5]);
            q.setCorrectAnswers(lineParts[6]);
            q.setOptionalFeedback(feedback);
            q.setDeleted(false);
            simpleQuestionService.create(q);

            questionIDs.add(q.getId());
        }

        Long questionnaireID;

        if (isExam) {
            ExamQuestionnaire examQuestionnaire = new ExamQuestionnaire(course.getId(), (long) 0, false, name, false, LocalDate.now());
            simpleExamQuestionnaireService.create(examQuestionnaire);
            questionnaireID = examQuestionnaire.getId();
        } else {
            LearningQuestionnaire learningQuestionnaire = new LearningQuestionnaire(course.getId(), (long) 0, false, name, false);
            simpleLearningQuestionnaireService.create(learningQuestionnaire);
            questionnaireID = learningQuestionnaire.getId();
        }

        for (Long questionID : questionIDs) {

            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();

            questionnaireQuestion.setQid(questionnaireID);
            questionnaireQuestion.setQuestionid(questionID);
            questionnaireQuestion.setDeleted(false);

            simpleQuestionnaireQuestionService.create(questionnaireQuestion);
        }
    }

    @Override
    public void importPictures(File file, String name) throws IOException {
        questionnaireImportDAO.importPictures(file, name);
    }

    public void deletePictures(File file) {
        questionnaireImportDAO.deletePictures(file);
    }
}