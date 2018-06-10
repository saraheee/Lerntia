package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;

public interface IUserService {

    /**
     * Update an existing ueser to the Database
     *
     * @param user User that needs to be updated in the Database
     * @throws ServiceException if the method can't update the user
     * */
    void update(User user) throws ServiceException;

    /**
     * Read all information of the user saved in the config file
     *
     * @throws ServiceException if the user can't be accessed
     * */
    User read() throws ServiceException;

}
