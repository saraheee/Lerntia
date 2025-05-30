package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.ImportQuestionnaire;

import java.io.File;
import java.io.IOException;

public interface IQuestionnaireImportService {

    /**
     * import file
     *
     * @param importQuestionnaire object with needed info for importing
     * @throws ServiceException if an error occurred
     */
    void importQuestionnaire(ImportQuestionnaire importQuestionnaire) throws ServiceException;

    /**
     * copy given files in new directory
     *
     * @param file a directory with all images
     * @param name the name of the file
     * @throws ServiceException if an error occurred
     * @throws IOException      if the File cannot be read
     */
    void importPictures(File file, String name) throws ServiceException, IOException;

    /**
     * delete files from a directory
     *
     * @param file a directory with all images to delete
     */
    void deletePictures(File file);
}
