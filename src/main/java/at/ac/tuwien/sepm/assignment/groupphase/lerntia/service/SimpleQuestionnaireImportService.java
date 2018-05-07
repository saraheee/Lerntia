package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionnaireImportDAO;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
public class SimpleQuestionnaireImportService implements IQuestionnaireImportService {

    public void importQuestionnaire( File file ) throws ServiceException {

        String pathStr = file.getAbsolutePath();
        Path path = Paths.get(file.getAbsolutePath());

        // define questionaire name

        String fileName = path.getFileName().toString();

        int pos = fileName.lastIndexOf(".");
        String questionaireName = fileName.substring(0, pos);

        if (questionaireName.startsWith("fragen_")){
            questionaireName = questionaireName.replace("fragen_", "");
        }

        // TODO - check if questionaire already exists

        // get questionaire file content

        QuestionnaireImportDAO questionnaireImportDAO = new QuestionnaireImportDAO();

        ArrayList<String> fileContent = new ArrayList<>();

        try {
            fileContent = questionnaireImportDAO.getContents(pathStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < fileContent.size(); i++) {

            // split the rows, the seperator is ";"
            String[] lineParts = fileContent.get(i).split(";");

            // check if there are to many columns
            if(lineParts.length > 9){
                // TODO - error
            }

            // index 6 has the right answers. this is an integer
            try {
                int rightAnswers = Integer.parseInt(lineParts[6]);
            } catch(NumberFormatException e) {
                // TODO - error
            }

            // index 7 is the image (optional)
            try {
                if ( ! lineParts[7].equals("")) {
                    // TODO - validate image
                }
            } catch (IndexOutOfBoundsException e) {
                // there is no image
            }

            System.out.println(lineParts[0]);
        }
    }
}
