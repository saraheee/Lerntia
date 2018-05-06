package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import java.io.*;
import java.util.ArrayList;

public class QuestionnaireImportDAO implements IQuestionnaireImportDAO {

    @Override
    public ArrayList<String> getContents(String filePath) throws IOException {

        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);

        ArrayList<String> fileContent = new ArrayList<>();

        String line;

        while((line = br.readLine()) != null) {
            fileContent.add(line);
        }

        return fileContent;
    }

}
