package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

public interface CourseDAO {
    void create(Course course) throws PersistenceException;

    void update(Course course) throws PersistenceException;

    void search(Course course) throws PersistenceException;

    void delete(Course course) throws PersistenceException;
}
