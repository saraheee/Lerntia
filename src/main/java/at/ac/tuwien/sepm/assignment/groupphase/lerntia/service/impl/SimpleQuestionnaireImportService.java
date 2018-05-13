package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleQuestionnaireImportService implements IQuestionnaireImportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final QuestionnaireImportDAO questionnaireImportDAO;
    private final SimpleQuestionService simpleQuestionService;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final SimpleQuestionnaireQuestionService simpleQuestionnaireQuestionService;

    public SimpleQuestionnaireImportService(
        QuestionnaireImportDAO questionnaireImportDAO,
        SimpleQuestionService simpleQuestionService,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        SimpleQuestionnaireQuestionService simpleQuestionnaireQuestionService
    ) {
        this.questionnaireImportDAO = questionnaireImportDAO;
        this.simpleQuestionService = simpleQuestionService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.simpleQuestionnaireQuestionService = simpleQuestionnaireQuestionService;
    }

    public void importQuestionnaire(File file, String course, String name) throws ServiceException {

        String pathStr = file.getAbsolutePath();

        List<LearningQuestionnaire> questionnaires = simpleLearningQuestionnaireService.readAll();
        for (int i = 0; i < questionnaires.size(); i++) {
            if (name.equals(questionnaires.get(i).getName())) {
                throw new ServiceException("Dieser Name existiert schon!");
            }
        }

        // get questionaire file content

        ArrayList<String> fileContent = new ArrayList<>();

        try {
            fileContent = questionnaireImportDAO.getContents(pathStr);
        } catch (IOException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }

        ArrayList<Long> questionIDs = new ArrayList<>();

        for (int i = 0; i < fileContent.size(); i++) {

            // split the rows, the seperator is ";"
            String[] lineParts = fileContent.get(i).split(";");

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
            try {
                if (!lineParts[7].equals("")) {
                    // TODO - validate image
                }
            } catch (IndexOutOfBoundsException e) {
                // there is no image
            }

            Question q = new Question((long) 0, lineParts[0], "", lineParts[1], lineParts[2], lineParts[3], lineParts[4], lineParts[5], lineParts[6], "", false);
            simpleQuestionService.create(q);

            questionIDs.add(q.getId());
        }

        LearningQuestionnaire learningQuestionnaire = new LearningQuestionnaire("1", "4", (long) 0, false, name);

        simpleLearningQuestionnaireService.create(learningQuestionnaire);

        Long learningQuestionnaireID = learningQuestionnaire.getId();

        for (int i = 0; i < questionIDs.size(); i++) {

            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();

            questionnaireQuestion.setQid(learningQuestionnaireID);
            questionnaireQuestion.setQuestionid(questionIDs.get(i));
            questionnaireQuestion.setDeleted(false);

            simpleQuestionnaireQuestionService.create(questionnaireQuestion);
        }
    }

    @Override
    public void importPictures (File file, String name) throws ServiceException {
        File dir = new File(name); // in current directory
        dir.mkdir();
        File[] files = file.listFiles();
        for (File child : files) {
            try {
                String p = dir.getName()+"/"+child.getName();
                Path path = Paths.get(p);
                Files.copy(child.toPath(), path);
            } catch (IOException e) {
                throw new ServiceException("Bild kann nicht gelesen werden");
            }
        }
    }
}