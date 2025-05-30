package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireImportDAO;
import at.ac.tuwien.lerntia.lerntia.dto.*;
import at.ac.tuwien.lerntia.lerntia.service.*;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionnaireImportService implements IQuestionnaireImportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IQuestionnaireImportDAO questionnaireImportDAO;
    private final IQuestionService simpleQuestionService;
    private final ILearningQuestionnaireService simpleLearningQuestionnaireService;
    private final IExamQuestionnaireService simpleExamQuestionnaireService;
    private final IQuestionnaireQuestionService simpleQuestionnaireQuestionService;

    @Autowired
    public QuestionnaireImportService(
        IQuestionnaireImportDAO questionnaireImportDAO,
        IQuestionService simpleQuestionService,
        ILearningQuestionnaireService simpleLearningQuestionnaireService,
        IExamQuestionnaireService simpleExamQuestionnaireService,
        IQuestionnaireQuestionService simpleQuestionnaireQuestionService
    ) {
        this.questionnaireImportDAO = questionnaireImportDAO;
        this.simpleQuestionService = simpleQuestionService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.simpleExamQuestionnaireService = simpleExamQuestionnaireService;
        this.simpleQuestionnaireQuestionService = simpleQuestionnaireQuestionService;
    }

    public void importQuestionnaire(ImportQuestionnaire importQuestionnaire) throws ServiceException {

        String pathStr = importQuestionnaire.getFile().getAbsolutePath();

        if (importQuestionnaire.getIsExam()) {
            List<ExamQuestionnaire> questionnaires = simpleExamQuestionnaireService.readAll();

            for (ExamQuestionnaire questionnaire : questionnaires) {
                if (importQuestionnaire.getName().equals(questionnaire.getName())) {
                    throw new ServiceException("Dieser Name existiert bereits!");
                }
            }

        } else {
            List<LearningQuestionnaire> questionnaires = simpleLearningQuestionnaireService.readAll();

            for (LearningQuestionnaire questionnaire : questionnaires) {
                if (importQuestionnaire.getName().equals(questionnaire.getName())) {
                    throw new ServiceException("Dieser Name existiert bereits!");
                }
            }
        }

        // get questionnaire file content

        ArrayList<String> fileContent;

        try {
            fileContent = questionnaireImportDAO.getContents(pathStr);
        } catch (IOException | PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException("Persistence exception caught");
        }

        ArrayList<Long> questionIDs = new ArrayList<>();
        int questionIndex = 1;
        for (String aFileContent : fileContent) {

            // split the rows, the separator is ";"
            String[] lineParts = aFileContent.split(";");

            // check if there are too many columns
            if (lineParts.length > 9) {
                throw new ServiceException("Zu viele Spalten!");
            }

            // index 6 has the right answers. this is an integer
            try {
                int rightAnswers = Integer.parseInt(lineParts[6]);
            } catch (NumberFormatException e) {
                throw new ServiceException("Richtige Antwort fehlt!");
            }

            // index 7 is the image (optional)
            String picture = "";
            try {
                if (!lineParts[7].equals("")) {
                    picture = lineParts[7];
                    String path = System.getProperty("user.dir") + File.separator + "img" + File.separator + importQuestionnaire.getName() + File.separator + picture;
                    File f = new File(path);
                    if (!f.exists()) {
                        throw new ServiceException("Mindestens ein Bild aus csv-Datei wurde nicht gefunden!");
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
            q.setIndex(questionIndex);
            q.setDeleted(false);
            simpleQuestionService.create(q);

            questionIDs.add(q.getId());
            questionIndex ++;
        }

        Long questionnaireID;

        if (importQuestionnaire.getIsExam()) {
            ExamQuestionnaire examQuestionnaire = new ExamQuestionnaire(importQuestionnaire.getCourse().getId(), (long) 0, false, importQuestionnaire.getName(), false, LocalDate.now());
            simpleExamQuestionnaireService.create(examQuestionnaire);
            questionnaireID = examQuestionnaire.getId();
        } else {
            LearningQuestionnaire learningQuestionnaire = new LearningQuestionnaire(importQuestionnaire.getCourse().getId(), (long) 0, false, importQuestionnaire.getName(), false);
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
    public void importPictures(File file, String name) throws IOException, ServiceException {
        try {
            questionnaireImportDAO.importPictures(file, name);
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to import pictures.");
        }
    }

    public void deletePictures(File file) {
        questionnaireImportDAO.deletePictures(file);
    }
}