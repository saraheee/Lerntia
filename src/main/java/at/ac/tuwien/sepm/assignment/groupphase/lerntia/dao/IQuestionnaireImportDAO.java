package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import java.io.IOException;
import java.util.ArrayList;

public interface IQuestionnaireImportDAO {

    public ArrayList<String> getContents(String filePath ) throws IOException;

}
