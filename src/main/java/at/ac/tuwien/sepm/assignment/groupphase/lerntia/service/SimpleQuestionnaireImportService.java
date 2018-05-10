package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleQuestionnaireImportService implements IQuestionnaireImportService {

    private final QuestionnaireImportDAO questionnaireImportDAO;
    private final SimpleQuestionService simpleQuestionService;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;

    public SimpleQuestionnaireImportService(
        QuestionnaireImportDAO questionnaireImportDAO,
        SimpleQuestionService simpleQuestionService,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService
    ){
        this.questionnaireImportDAO = questionnaireImportDAO;
        this.simpleQuestionService = simpleQuestionService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
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
            e.printStackTrace();
        }

        for(int i = 0; i < fileContent.size(); i++) {

            // split the rows, the seperator is ";"
            String[] lineParts = fileContent.get(i).split(";");

            // check if there are too many columns
            if(lineParts.length > 9){
                throw new ServiceException("Zu viele Spalten");
            }

            // index 6 has the right answers. this is an integer
            try {
                int rightAnswers = Integer.parseInt(lineParts[6]);
            } catch(NumberFormatException e) {
                throw new ServiceException("Richtige Antwort fehlt");
            }

            // index 7 is the image (optional)
            try {
                if ( ! lineParts[7].equals("")) {
                    // TODO - validate image
                }
            } catch (IndexOutOfBoundsException e) {
                // there is no image
            }

            Question q = new Question((long) 0, lineParts[0], "", lineParts[1], lineParts[2], lineParts[3], lineParts[4], lineParts[5], lineParts[6], "", false);
            simpleQuestionService.create(q);
        }
        simpleLearningQuestionnaireService.create(new LearningQuestionnaire("1", "4", (long)0, false, name));
    }
}