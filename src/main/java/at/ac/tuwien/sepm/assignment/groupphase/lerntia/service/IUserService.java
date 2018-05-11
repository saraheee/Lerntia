package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;

public interface IUserService {

    /**
     * Create a new User and save it to the Database
     *
     * @param user the user that needs to be saved to the Database
     * @throws ServiceException if the method can't save the User to the Database
     * */
    void create(User user) throws ServiceException;

    /**
     * Update an existing ueser to the Database
     *
     * @param user User that needs to be updated in the Database
     * @throws ServiceException if the method can't update the user
     * */
    void update(User user) throws ServiceException;

    /**
     * Read all information of an User
     *
     * @param user user in question
     * @throws ServiceException if the user can't be accessed
     * */
    User read(User user) throws ServiceException;

    /**
     * Deletes an user from the Database
     *
     * @param user User that needs to be Deleted from the Database
     * @throws ServiceException if the method can't delete the user
     * */
    void delete(User user) throws ServiceException;
}
