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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseDAOJDBC implements CourseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_COURSE_CREATE_STATEMENT="INSERT INTO Course(mark,semester) VALUES (?,?)";
    private static final String SQL_COURSE_UPDATE_STATEMENT="UPDATE Course set semester=? WHERE mark =?" ;
    //private static final String SQL_COURSE_SEARCH_STATEMENT="";
    //private static final String SQL_COURSE_DELETE_STATEMENT="";
    private static final String SQL_COURSE_READALL_STATEMENT="SELECT * from Course";
    private Connection connection=null;
    public CourseDAOJDBC() throws PersistenceException{
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(Course course) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_COURSE_CREATE_STATEMENT);
            pscreate.setString(1,course.getMark());
            pscreate.setString(2,course.getSemester());
            pscreate.execute();
        }catch (Exception e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Course course) throws PersistenceException {
        try {
            PreparedStatement psupdate = connection.prepareStatement(SQL_COURSE_UPDATE_STATEMENT);
            psupdate.setString(1,course.getSemester());
            psupdate.setString(2,course.getMark());
            psupdate.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void search(Course course) throws PersistenceException {
        //TODO check if search method is necessary

    }

    @Override
    public void delete(Course course) throws PersistenceException {
        //TODO check if isDeleted flag is necessary
    }

    @Override
    public List readAll() throws PersistenceException {
        try {
            ArrayList<Course> list = new ArrayList<>();
            ResultSet rsreadall = connection.prepareStatement(SQL_COURSE_READALL_STATEMENT).executeQuery();
            Course course;
            while (rsreadall.next()){
                course = new Course();
                course.setMark(rsreadall.getString(1));
                course.setSemester(rsreadall.getString(2));
                list.add(course);
            }
            return list;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }
}
