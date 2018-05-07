package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import java.io.File;

public interface IQuestionnaireImportService {

    public void importQuestionnaire( File filePath ) throws ServiceException;

}
