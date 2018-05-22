package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import java.io.IOException;
import java.util.ArrayList;

public interface IQuestionnaireImportDAO {

    /**
     * Get the contents of a File
     *
     * @param filePath the path to the File to be read
     * @return a list of Strings representing the rows of the File
     * @throws IOException if the File at filePath cannot be read
     * */
     ArrayList<String> getContents(String filePath) throws IOException;

}
