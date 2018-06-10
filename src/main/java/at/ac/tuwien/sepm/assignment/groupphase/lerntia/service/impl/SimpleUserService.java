package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleUserService implements IUserService {

    private final ConfigReader configReader;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public SimpleUserService(UserDAO userDAO) {
        this.configReader = new ConfigReader("student");
    }

    @Override
    public void update(User user) throws ServiceException {
        // todo change config file
    }

    @Override
    public User read() throws ServiceException {
        try {
            return new User(configReader.getValue("name"), configReader.getValue("matriculationNumber"), configReader.getValue("studyProgramme"), false);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
