package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

import java.util.List;

public interface ICourseDAO {

    /**
     * Create a new Course
     *
     * @param course the new Course that is going to be created and sent to the Database
     * @throws PersistenceException if Course can't be created.
     */
    void create(Course course) throws PersistenceException;

    /**
     * Update an existing Course with new parameters and save those in the Database
     *
     * @param course the Course in Question with updated Values
     * @throws PersistenceException if the method can't update the Course
     */
    void update(Course course) throws PersistenceException;

    /**
     * Delete an existing course from the Database
     *
     * @param course the course that needs to be deleted
     * @throws PersistenceException if the method can't delete the course in question
     */
    void delete(Course course) throws PersistenceException;

    /**
     * Reads all Courses from the Database
     *
     * @return List with all courses
     * @throws PersistenceException if it's not possible to get the List
     */
    List<Course> readAll() throws PersistenceException;
}
