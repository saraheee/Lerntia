package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO implements IUserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_USER_CREATE_STATEMENT = "INSERT INTO PUser(name, matriculationNumber, studyProgramme) VALUES (?, ? ,?)";
    private static final String SQL_USER_UPDATE_STATEMENT = "UPDATE PUser SET name = ?, studyProgramme = ? WHERE matriculationNumber = ?";
    private static final String SQL_USER_READ_STATEMENT = "SELECT * FROM PUser WHERE matriculationNumber = ?";
    private static final String SQL_USER_DELETE_STATEMENT = "UPDATE PUser SET isDeleted=TRUE WHERE matriculationNumber = ?";
    private Connection connection;

    @Autowired
    public UserDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Database connection for UserDAO obtained.");
        } catch (PersistenceException e) {
            LOG.error("User Constructor failed while trying to get connection!");
            throw e;
        }
    }

    @Override
    public void create(User user) throws PersistenceException {
        LOG.info("Prepare Statement for User creation.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_USER_CREATE_STATEMENT)) {
            psCreate.setString(1, user.getName());
            psCreate.setString(2, user.getMatriculationNumber());
            psCreate.setString(3, user.getStudyProgramme());
            psCreate.executeUpdate();
            LOG.info("User successfully added to Database.");
        } catch (Exception e) {
            throw new PersistenceException("UserDAO CREATE error: item couldn't be created, check if all mandatory values have been added, or if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(User user) throws PersistenceException {
        LOG.info("Prepare statement for updating User values.");
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_USER_UPDATE_STATEMENT)) {
            psUpdate.setString(1, user.getName());
            psUpdate.setString(2, user.getStudyProgramme());
            psUpdate.setString(3, user.getMatriculationNumber());
            psUpdate.executeUpdate();
            LOG.info("User successfully updated.");
        } catch (Exception e) {
            throw new PersistenceException("UserDAO UPDATE error: item couldn't be updated, check if all mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public User read(User user) throws PersistenceException {
        LOG.info("Prepare statement to get User from Database");
        User u;
        try (PreparedStatement psRead = connection.prepareStatement(SQL_USER_READ_STATEMENT)) {
            psRead.setString(1, user.getMatriculationNumber());
            u = new User();
            try (ResultSet rs = psRead.executeQuery()) {
                if (rs.next()) {
                    u.setName(rs.getString(1));
                    u.setMatriculationNumber(rs.getString(2));
                    u.setStudyProgramme(rs.getString(3));
                    u.setDeleted(rs.getBoolean(4));
                }
            }
            LOG.info("User found.");
            return u;
        } catch (Exception e) {
            throw new PersistenceException("UserDAO READ error: item coulnd't be found, check if there is a user of ir the connection to the Database is valid.");
        }
    }

    @Override
    public void delete(User user) throws PersistenceException {
        LOG.info("Prepare statement for User deletion.");
        try (PreparedStatement psDelete = connection.prepareStatement(SQL_USER_DELETE_STATEMENT)) {
            if (user.getMatriculationNumber() != null) {
                psDelete.setString(1, user.getMatriculationNumber());
            }
            psDelete.executeUpdate();
            LOG.info("User soft-deleted from Database.");
        }
         catch (SQLException e) {
            throw new PersistenceException("UserDAO DELETE error: item couldn't be deleted, check if the connection to the Database is valid.");
        }
    }
}
