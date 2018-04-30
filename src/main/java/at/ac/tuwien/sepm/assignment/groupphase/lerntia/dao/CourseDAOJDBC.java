package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CourseDAOJDBC implements CourseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_COURSE_CREATE_STATEMENT="INSERT INTO Course(id,semester) VALUES (DEFAULT ,?)";
    private static final String SQL_COURSE_UPDATE_STATEMENT="";
    private static final String SQL_COURSE_SEARCH_STATEMENT="";
    private static final String SQL_COURSE_DELETE_STATEMENT="";
    private static final String SQL_COURSE_READALL_STATEMENT="";
    private Connection connection=null;
    public CourseDAOJDBC() throws PersistenceException{
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Course course) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_COURSE_CREATE_STATEMENT);
        }catch (Exception e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Course course) throws PersistenceException {

    }

    @Override
    public void search(Course course) throws PersistenceException {

    }

    @Override
    public void delete(Course course) throws PersistenceException {

    }
}
