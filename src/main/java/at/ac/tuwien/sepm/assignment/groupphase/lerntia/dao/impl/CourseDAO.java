package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;
import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseDAO implements ICourseDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_COURSE_CREATE_STATEMENT="INSERT INTO Course(id,name,mark,semester,isDeleted) VALUES (default ,?,?,?,false)";
    private static final String SQL_COURSE_UPDATE_STATEMENT="UPDATE Course set mark = ?, semester=? , name=? WHERE id =?" ;
    private static final String SQL_COURSE_DELETE_STATEMENT="UPDATE Course set isDeleted=true  WHERE id = ?";
    private static final String SQL_COURSE_READALL_STATEMENT="SELECT * from Course where isDeleted = false ";

    private Connection connection;

    @Autowired
    public CourseDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException{
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Database connection for CourseDAO obtained.");
        } catch (PersistenceException e) {
            LOG.error("Course Constructor failed while trying to get connection!");
            throw e;
        }
    }

    @Override
    public void create(Course course) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for new Course Creation.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_COURSE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            try {
                pscreate.setString(1,course.getName());
                pscreate.setString(2,course.getMark());
                pscreate.setString(3,course.getSemester());
                pscreate.executeUpdate();
                ResultSet generatedKeys = pscreate.getGeneratedKeys();
                try {
                    if (generatedKeys.next()){
                        course.setId(generatedKeys.getLong(1));
                    }
                }finally {
                    generatedKeys.close();
                }
                LOG.info("Course succesfully added to Database.");
            }finally {
                pscreate.close();
            }
        }catch (SQLException e){
            throw new PersistenceException("CourseDAO CREATE error: New Course couldn't be created, check if all mandatory values have been inserted or if connection to the Database is valid.");
        }
    }

    @Override
    public void update(Course course) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for Course Update.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_COURSE_UPDATE_STATEMENT);
            try {
                psupdate.setString(1, course.getMark());
                psupdate.setString(2, course.getSemester());
                psupdate.setString(3, course.getName());
                psupdate.setLong(4, course.getId());
                psupdate.executeUpdate();
                LOG.info("Course succefsully updated in Database.");
            }finally {
                psupdate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO UPDATE error: Selected Course couldn't be updated, check if all mandatory values have been inserted or if connection to the Database is valid.");
        }
    }

    @Override
    public void search(Course course) throws PersistenceException {
        //this method is currently empty because its not yet determined if this method is even necessary for this programm.
        //If so the method will be deleted
    }

    @Override
    public void delete(Course course) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for Course Deletion");
            PreparedStatement psdelete = connection.prepareStatement(SQL_COURSE_DELETE_STATEMENT);
            try {
                psdelete.setLong(1, course.getId());
                psdelete.executeUpdate();
                LOG.info("Course in question soft-deleted in Database.");
            }finally {
                psdelete.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO DELETE error: Selected Course couldn't be deleted, check if the connection to the Database is valid.");
        }
    }

    @Override
    public List readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all available Courses from the Database.");
            ArrayList<Course> list = new ArrayList<>();
            try (ResultSet rsreadall = connection.prepareStatement(SQL_COURSE_READALL_STATEMENT).executeQuery()) {
                    Course course;
                    while (rsreadall.next()) {
                        course = new Course();
                        course.setId(rsreadall.getLong(1));
                        course.setMark(rsreadall.getString(2));
                        course.setSemester(rsreadall.getString(3));
                        course.setName(rsreadall.getString(4));
                        list.add(course);
                    }
                    LOG.info("All Courses found.");
                    return list;
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO READALL error: not all courses could have been found, check if connection to the Database is valid.");
        }
    }
}