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
    Connection connection = null;
    private static final String SQL_USER_CREATE_STATEMENT = "INSERT INTO PUser(name, matriculationNumber, studyProgramme) VALUES (?, ? ,?)";
    private static final String  SQL_USER_UPDATE_STATEMENT = "UPDATE PUser SET name = ?, studyProgramme = ? WHERE matriculationNumber = ?";
    private static final String SQL_USER_READ_STATEMENT = "SELECT * FROM PUser WHERE matriculationNumber = ?";
    private static final String SQL_USER_DELETE_STATEMENT ="UPDATE PUser SET isDeleted=TRUE WHERE matriculationNumber = ?";

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
        try {
            LOG.info("Prepare Statement for User creation.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_USER_CREATE_STATEMENT);
            try {
                pscreate.setString(1, user.getName());
                pscreate.setString(2, user.getMatriculationNumber());
                pscreate.setString(3, user.getStudyProgramme());
                pscreate.executeUpdate();
                LOG.info("User succesfully added to Database.");
            }finally {
                pscreate.close();
            }
        }catch (Exception e){
            throw new PersistenceException("UserDAO CREATE error: item couldn't be created, check if all mandatory values have been added, or if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement for updating User values.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_USER_UPDATE_STATEMENT);
            try {
                psupdate.setString(1, user.getName());
                psupdate.setString(2, user.getStudyProgramme());
                psupdate.setString(3, user.getMatriculationNumber());
                psupdate.executeUpdate();
                LOG.info("User succesfully updated.");
            }finally {
                psupdate.close();
            }
        }catch (Exception e){
            throw new PersistenceException("UserDAO UPDATE error: item couldn't be updated, check if all mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public User read(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement to get User from Database");
            PreparedStatement psread = connection.prepareStatement(SQL_USER_READ_STATEMENT);
            try {
                psread.setString(1, user.getMatriculationNumber());
                User u = new User();
                try (ResultSet rs = psread.executeQuery()) {
                    if (rs.next()) {
                        u.setName(rs.getString(1));
                        u.setMatriculationNumber(rs.getString(2));
                        u.setStudyProgramme(rs.getString(3));
                        u.setDeleted(rs.getBoolean(4));
                    }
                }
                LOG.info("User found.");
                return u;
            }finally {
                psread.close();
            }
        }catch (Exception e){
            throw new PersistenceException("UserDAO READ error: item coulnd't be found, check if there is a user of ir the connection to the Database is valid.");
        }
    }

    @Override
    public void delete(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement for User deletion.");
            PreparedStatement psdelete = connection.prepareStatement(SQL_USER_DELETE_STATEMENT);
            try {
                if (user.getMatriculationNumber() != null) {
                    psdelete.setString(1, user.getMatriculationNumber());
                }
                psdelete.executeUpdate();
                LOG.info("User soft-deleted from Database.");
            }finally {
                psdelete.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("UserDAO DELETE error: item couldn't be deleted, check if the connection to the Database is valid.");
        }
    }
}
