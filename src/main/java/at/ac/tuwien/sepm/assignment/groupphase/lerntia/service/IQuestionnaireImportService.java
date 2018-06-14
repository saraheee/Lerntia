package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

import java.io.File;
import java.io.IOException;

public interface IQuestionnaireImportService {

    /**
     * import file
     *
     * @param file a csv-file
     * @param course the course where the questions should be added
     * @param name the name of the questionnaire
     * @throws ServiceException if an error occured
     * @throws IOException if the File cannot be read
     */
    void importQuestionnaire(File file, Course course, String name, boolean isExam) throws ServiceException;

    /**
     * copy given files in new directory
     *
     * @param file a directory with all images
     * @param name the name of the file
     * @throws ServiceException if an error occured
     * @throws IOException if the File cannot be read
     */
    void importPictures(File file, String name) throws ServiceException, IOException;

}
