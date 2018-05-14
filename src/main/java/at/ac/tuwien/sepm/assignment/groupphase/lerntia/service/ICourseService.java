package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

import java.util.List;

public interface ICourseService {

    /**
     * Create a new Course
     *
     * @param course the new Course that is going to be created and sent to the Database
     * @throws ServiceException if Course can't be created.
     * */
    void create(Course course) throws ServiceException;

    /**
     * Update an existing Course with new parameters and save those in the Database
     *
     * @param course the Course in Question with updated Values
     * @throws ServiceException if the method can't update the Course
     * */
    void update(Course course) throws ServiceException;

    /**
     * Search for a course in the Database with specific searchparameters
     *
     * @param course the Parameters used to find the Courses in question
     * @throws ServiceException if the method can't search for the course
     * */
    void search(Course course) throws ServiceException;

    /**
     * Delete an existing course from the Database
     *
     * @param course the course that needs to be deleted
     * @throws ServiceException if the method can't delete the course in question
     * */
    void delete(Course course) throws ServiceException;

    /**
     * Reads all Courses from the Database
     *
     * @return List with all courses
     * @throws ServiceException if it's not possible to get the List
     * */
    List<Course> readAll() throws ServiceException;

    void validate(Course course) throws ServiceException;

}
