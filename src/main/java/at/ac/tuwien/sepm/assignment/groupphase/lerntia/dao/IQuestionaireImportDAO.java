package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IQuestionaireImportDAO {

    public ArrayList<String> getContents(String filePath ) throws IOException;

}
