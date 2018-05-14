package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

import java.io.File;

public interface IQuestionnaireImportService {

    public void importQuestionnaire(File file, Course course, String name) throws ServiceException;

    public void importPictures(File file, String name) throws ServiceException;

}
