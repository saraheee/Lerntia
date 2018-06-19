package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;

public interface IUserService {

    /**
     * Read all information of the user saved in the config filem
     *
     * @return User containing all attributes that are provided in the config file or a warning that an attribute is missing
     *
     * @throws ServiceException if the user data ('student.properties') file is not provided in config folder
     * */
    User read() throws ServiceException;

}
