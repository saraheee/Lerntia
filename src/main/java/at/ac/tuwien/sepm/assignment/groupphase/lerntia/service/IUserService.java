package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;

public interface IUserService {

    /**
     * Read all information of the user saved in the config file
     *
     * @throws ServiceException if the user data (i.e. a student.properties) file is not provided in config folder
     * */
    User read() throws ServiceException;

}
