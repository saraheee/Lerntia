package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;

public interface IUserCourseDAO {

    /**
     * Create a new User and save it to the Database
     *
     * @param userCourse the user-course connection that needs to be saved to the Database
     * @throws PersistenceException if the method can't save the UserCourse to the Database
     * */
    void create(UserCourse userCourse) throws PersistenceException;
}
