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

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_USERCOURSE_CREATE_STATEMENT = "INSERT INTO PUserCourse(matriculationNumber,courseid,isDeleted) VALUES (?,?,FALSE )";
    private Connection connection;

    public UserCourseDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for UserCourseDAO successfully found.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for UserCourseDAO!");
            throw e;
        }
    }

    @Override
    public void create(UserCourse userCourse) throws PersistenceException {
        LOG.info("Prepare Statement for new UserCourse entry.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_USERCOURSE_CREATE_STATEMENT)) {
            psCreate.setString(1, userCourse.getMatriculationNumber());
            psCreate.setLong(2, userCourse.getCourseID());
            psCreate.execute();
            LOG.info("Statement for new UserCourse entry successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("UserCourseDAO CREATE error: item couldn't have been created, check if all mandatory values have been added or if the connection to the Database is valid.");
        }
    }
}
