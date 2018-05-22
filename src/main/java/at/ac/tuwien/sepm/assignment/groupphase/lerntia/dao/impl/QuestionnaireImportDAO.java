package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireImportDAO;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@Component
public class QuestionnaireImportDAO implements IQuestionnaireImportDAO {

    @Override
    public ArrayList<String> getContents(String filePath) throws IOException {

        // A BufferedReader Object is created using path that is the argument of this method
        FileReader fr = new FileReader(filePath);
        ArrayList<String> fileContent;
        try (BufferedReader br = new BufferedReader(fr)) {

            fileContent = new ArrayList<>();

            String line;

            // Every line is read from the file and added to the ArrayList

            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
        }

        return fileContent;
    }
}
