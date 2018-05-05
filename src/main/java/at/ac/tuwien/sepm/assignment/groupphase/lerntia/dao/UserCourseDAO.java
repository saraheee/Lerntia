package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserCourseDAO implements IUserCourseDAO {

    private Connection connection;

    private static final String SQL_USERCOURSE_CREATE_STATEMENT ="INSERT INTO PUserCourse(matriculationNumber,cmark,semestr,isDeleted) VALUES (?,?,?,?)";

    public UserCourseDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(UserCourse userCourse) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_USERCOURSE_CREATE_STATEMENT);
            pscreate.setString(1, userCourse.getMatriculationNumber());
            pscreate.setString(2, userCourse.getCmark());
            pscreate.setString(3, userCourse.getSemester());
            pscreate.setBoolean(4, userCourse.getDeleted());
            pscreate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException(e.getMessage());
        }
    }
}
