package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserCourseDAO implements IUserCourseDAO {

    private Connection connection=null;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_USERCOURSE_CREATE_STATEMENT ="INSERT INTO PUserCourse(matriculationNumber,courseid,isDeleted) VALUES (?,?,false )";

    public UserCourseDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Connection for UserCourseDAO succesfully found.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for UserCourseDAO!");
            throw e;
        }
    }

    @Override
    public void create(UserCourse userCourse) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for new UserCourse entry.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_USERCOURSE_CREATE_STATEMENT);
            pscreate.setString(1, userCourse.getMatriculationNumber());
            pscreate.setLong(2,userCourse.getCourseID());
            pscreate.execute();
            LOG.info("Statement for new UserCourse entry succesfully sent.");
        } catch (SQLException e) {
            LOG.error("UserCourse CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}
