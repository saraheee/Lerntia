package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;

public interface UserDAO {
    void create(User user) throws PersistenceException;

    void update(User user) throws PersistenceException;

    void read(User user) throws PersistenceException;

    void delete(User user) throws PersistenceException;
}
