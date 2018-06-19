package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements IUserService {

    private ConfigReader configReader = null;

    @Override
    public User read() throws ServiceException {
        if(configReader == null) {
            try {
                this.configReader = new ConfigReader("student");
            } catch (ConfigReaderException e) {
                throw new ServiceException(e.getCustomMessage());
            }
        }
        User user = new User(configReader.getValue("name"), configReader.getValue("matriculationNumber"),
            configReader.getValue("studyProgramme"), false);
        if (user.getName() == null || user.getName().trim().isEmpty() || user.getMatriculationNumber() == null ||
            user.getMatriculationNumber().trim().isEmpty() || user.getStudyProgramme() == null ||
            user.getStudyProgramme().trim().isEmpty()) {
            throw new ServiceException("Some student data could not be read. Is student.properties file provided?");
        }

        return user;
    }
}