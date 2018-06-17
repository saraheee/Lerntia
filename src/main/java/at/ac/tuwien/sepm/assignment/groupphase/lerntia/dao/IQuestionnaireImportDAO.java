package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface IQuestionnaireImportDAO {

    /**
     * Get the contents of a File
     *
     * @param filePath the path to the File to be read
     * @return a list of Strings representing the rows of the File
     * @throws IOException if the File at filePath cannot be read
     */
    ArrayList<String> getContents(String filePath) throws IOException, PersistenceException;

    /**
     * copy given files in new directory
     *
     * @param file a directory with all images
     * @param name the name of the file
     * @throws IOException if the File cannot be read
     * @throws PersistenceException if the directory can't be created
     */
    void importPictures(File file, String name) throws IOException, PersistenceException;

}
