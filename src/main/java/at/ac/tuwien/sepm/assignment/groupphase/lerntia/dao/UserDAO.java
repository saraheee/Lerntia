package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO implements IUserDAO {

    Connection connection = null;
    private static final String SQL_USER_CREATE_STATEMENT = "INSERT INTO PUser(name, matriculationNumber, studyProgramme) VALUES (?, ? ,?)";
    private static final String  SQL_USER_UPDATE_STATEMENT = "UPDATE PUser SET name = ?, studyProgramme = ? WHERE matriculationNumber = ?";
    private static final String SQL_USER_READ_STATEMENT = "SELECT * FROM PUser WHERE matriculationNumber = ?";

    public UserDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(User user) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_USER_CREATE_STATEMENT);
            pscreate.setString(1, user.getName());
            pscreate.setString(2, user.getMatriculationNumber());
            pscreate.setString(3, user.getStudyProgramme());
            pscreate.executeUpdate();
        }catch (Exception e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(User user) throws PersistenceException {
        try {
            PreparedStatement psupdate = connection.prepareStatement(SQL_USER_UPDATE_STATEMENT);
            psupdate.setString(1, user.getName());
            psupdate.setString(2, user.getStudyProgramme());
            psupdate.setString(3, user.getMatriculationNumber());
            psupdate.executeUpdate();
        }catch (Exception e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void read(User user) throws PersistenceException {
        try {
            PreparedStatement psread = connection.prepareStatement(SQL_USER_READ_STATEMENT);
            psread.setString(1, user.getMatriculationNumber());
            User u = new User();
            ResultSet rs = psread.executeQuery();
            if(rs.next()) {
                u.setName(rs.getString(1));
                u.setMatriculationNumber(rs.getString(2));
                u.setStudyProgramme(rs.getString(3));
            }
        }catch (Exception e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void delete(User user) throws PersistenceException {
        // todo check if soft delete is necessery
    }
}
