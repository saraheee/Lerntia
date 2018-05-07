package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Classes;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Interfaces.IUserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UserDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
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
            pscreate.setString(1, user.getName());
            pscreate.setString(2, user.getMatriculationNumber());
            pscreate.setString(3, user.getStudyProgramme());
            pscreate.executeUpdate();
            LOG.info("User succesfully added to Database.");
        }catch (Exception e){
            LOG.error("User CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement for updating User values.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_USER_UPDATE_STATEMENT);
            psupdate.setString(1, user.getName());
            psupdate.setString(2, user.getStudyProgramme());
            psupdate.setString(3, user.getMatriculationNumber());
            psupdate.executeUpdate();
            LOG.info("User succesfully updated.");
        }catch (Exception e){
            LOG.error("User UPDATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public User read(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement to get User from Database");
            PreparedStatement psread = connection.prepareStatement(SQL_USER_READ_STATEMENT);
            psread.setString(1, user.getMatriculationNumber());
            User u = new User();
            ResultSet rs = psread.executeQuery();
            if(rs.next()) {
                u.setName(rs.getString(1));
                u.setMatriculationNumber(rs.getString(2));
                u.setStudyProgramme(rs.getString(3));
            }
            LOG.info("User found.");
            return u;
        }catch (Exception e){
            LOG.error("User READ DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void delete(User user) throws PersistenceException {
        try {
            LOG.info("Prepare statement for User deletion.");
            PreparedStatement psdelete = connection.prepareStatement(SQL_USER_DELETE_STATEMENT);
            psdelete.setString(1,user.getMatriculationNumber());
            psdelete.executeUpdate();
            LOG.info("User soft-deleted from Database.");
        } catch (SQLException e) {
            LOG.error("User DELETE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}
