package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

import java.io.File;
import java.io.IOException;

public interface IQuestionnaireImportService {

    // TODO - file should not be used here. only in the DAO layer.
    void importQuestionnaire(File file, Course course, String name, boolean isExam) throws ServiceException;

    // TODO - file should not be used here. only in the DAO layer.
    void importPictures(File file, String name) throws ServiceException, IOException;

}
