package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.User;

public interface IUserService {

    /**
     * Read all information of the user saved in the config file
     *
     * @return User containing all attributes that are provided in the config file or a warning that an attribute is missing
     * @throws ServiceException if the user data ('student.properties') file is not provided in config folder
     */
    User read() throws ServiceException;

}
