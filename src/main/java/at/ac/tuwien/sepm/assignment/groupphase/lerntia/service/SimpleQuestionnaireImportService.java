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

        String imgDirPath = "/home/stefan/java/sepm-gruppe/ss18_sepm_qse_08/img/";

        // define questionaire name

        Path path = Paths.get(file.getAbsolutePath());

        String fileName = path.getFileName().toString();

        int pos = fileName.lastIndexOf(".");
        String questionaireName = fileName.substring(0, pos);

        if (questionaireName.startsWith("fragen_")){
            questionaireName = questionaireName.replace("fragen_", "");
        }

        // TODO - check if questionaire already exists

        // get questionaire file content

        QuestionnaireImportDAO test = new QuestionnaireImportDAO();

        ArrayList<String> fileContent = new ArrayList<>();

        try {
            fileContent = test.getContents(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < fileContent.size(); i++) {

            String[] lineParts = fileContent.get(i).split(";");

            if(lineParts.length > 9){
                // error
            }

            // index 6 has the right answers. this is an integer
            try {
                int rightAnswers = Integer.parseInt(lineParts[6]);
            } catch(NumberFormatException e) {
                // error
            }



            // 7 image

            try {
                if ( ! lineParts[7].equals("")) {



                    System.out.println(lineParts[7]);


                    String imgPath = imgDirPath + questionaireName + "/" + lineParts[7];



                    System.out.println(imgPath);

                    File filenew = new File(imgPath);

                    FileInputStream imgFileInputStream = null;
                    try {
                        imgFileInputStream = new FileInputStream(imgPath);
                    } catch (FileNotFoundException e) {
                        // TODO - error
                        System.out.println("cint");
                        continue;
                    }

                    Image image = new Image(imgFileInputStream);

                    if (500 > image.getHeight()) {

                    }

                    if (500 > image.getWidth()) {

                    }

                    // 5 mb
                    if (5242880 < filenew.length()) {

                    }










                }
            } catch (IndexOutOfBoundsException e) {

                // es gibt einfach kein bild

            }



        }

        //System.out.println(fileContent);
    }

}
