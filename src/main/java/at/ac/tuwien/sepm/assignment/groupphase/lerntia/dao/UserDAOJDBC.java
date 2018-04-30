package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAOJDBC implements UserDAO {

    Connection connection = null;
    
    public UserDAOJDBC() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(Course course) throws PersistenceException {

    }

    @Override
    public void update(Course course) throws PersistenceException {

    }

    @Override
    public void read(Course course) throws PersistenceException {

    }

    @Override
    public void delete(Course course) throws PersistenceException {

    }
}
