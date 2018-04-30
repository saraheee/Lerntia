package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;

public interface UserDAO {
    void create(Course course) throws PersistenceException;

    void update(Course course) throws PersistenceException;

    void read(Course course) throws PersistenceException;

    void delete(Course course) throws PersistenceException;
}
