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

    private static final String SQL_COURSE_CREATE_STATEMENT = "INSERT INTO Course(id,name,mark,semester,isDeleted) VALUES (default ,?,?,?,FALSE)";
    private static final String SQL_COURSE_UPDATE_STATEMENT = "UPDATE Course SET mark = ?, semester=? , name=? WHERE id =?";
    private static final String SQL_COURSE_DELETE_STATEMENT = "UPDATE Course SET isDeleted=TRUE  WHERE id = ?";
    private static final String SQL_COURSE_READALL_STATEMENT = "SELECT * FROM Course WHERE isDeleted = FALSE ";

    private Connection connection;

    @Autowired
    public CourseDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for CourseDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for CourseDAO retrieved.");
        }
    }

    @Override
    public void create(Course course) throws PersistenceException {
        if (course == null || course.getName() == null || course.getMark() == null || course.getSemester() == null) {
            throw new PersistenceException("At least one value of the course is null!");
        }
        try {
            LOG.info("Prepare Statement for new Course Creation.");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_COURSE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
                psCreate.setString(1, course.getName());
                psCreate.setString(2, course.getMark());
                psCreate.setString(3, course.getSemester());
                psCreate.executeUpdate();
                try (ResultSet generatedKeys = psCreate.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        course.setId(generatedKeys.getLong(1));
                    }
                }
                LOG.info("Course successfully added to the database.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO CREATE error: New Course couldn't be created, check if all mandatory values have been inserted or if connection to the Database is valid.");
        }
    }

    @Override
    public void update(Course course) throws PersistenceException {
        if (course == null || course.getName() == null || course.getMark() == null || course.getSemester() == null || course.getId() == null) {
            throw new PersistenceException("At least one value of the course is null!");
        }
        try {
            LOG.info("Prepare Statement for Course Update.");
            try (PreparedStatement psUpdate = connection.prepareStatement(SQL_COURSE_UPDATE_STATEMENT)) {
                psUpdate.setString(1, course.getMark());
                psUpdate.setString(2, course.getSemester());
                psUpdate.setString(3, course.getName());
                psUpdate.setLong(4, course.getId());
                psUpdate.executeUpdate();
                LOG.info("Course successfully updated in Database.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO UPDATE error: Selected Course couldn't be updated, check if all mandatory values have been inserted or if connection to the Database is valid.");
        }
    }


    @Override
    public void delete(Course course) throws PersistenceException {
        if (course == null || course.getId() == null) {
            throw new PersistenceException("At least one value of the course is null!");
        }
        try {
            LOG.info("Prepare Statement for Course Deletion");
            try (PreparedStatement psDelete = connection.prepareStatement(SQL_COURSE_DELETE_STATEMENT)) {
                psDelete.setLong(1, course.getId());
                psDelete.executeUpdate();
                LOG.info("Course in question soft-deleted in Database.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("CourseDAO DELETE error: Selected Course couldn't be deleted, check if the connection to the Database is valid.");
        }
    }

    @Override
    public List<Course> readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all available Courses from the Database.");
            ArrayList<Course> list = new ArrayList<>();
            try (ResultSet rsReadAll = connection.prepareStatement(SQL_COURSE_READALL_STATEMENT).executeQuery()) {
                Course course;
                while (rsReadAll.next()) {
                    course = new Course();
                    course.setId(rsReadAll.getLong(1));
                    course.setMark(rsReadAll.getString(2));
                    course.setSemester(rsReadAll.getString(3));
                    course.setName(rsReadAll.getString(4));
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