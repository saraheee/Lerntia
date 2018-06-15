package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CourseServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ICourseService courseService;
    private Connection connection;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.ICourseService(new SimpleCourseService(new CourseDAO(jdbcConnectionManager)));
        } catch (PersistenceException e) {
            // TODO - show alert or throw new exception
        }
    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    private void ICourseService(SimpleCourseService simpleCourseService) {
        this.courseService = simpleCourseService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // creation
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void createCorrectCourse() throws ServiceException {
        int before = courseService.readAll().size();
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        int after = courseService.readAll().size();
        String mark = courseService.readAll().get(after - 1).getMark();
        assertTrue(before < after);
        assertTrue(mark.equals("asdf"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // update
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void updateCorrectCourse() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        String old = course.getSemester();
        String after = Semester.WS + "2018";
        course.setSemester(after);
        courseService.update(course);
        assertTrue(!old.equals(after));
        assertTrue(course.getSemester().equals(Semester.WS + "2018"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // search
    // -----------------------------------------------------------------------------------------------------------------
    // TODO: search not yet implemented

    // -----------------------------------------------------------------------------------------------------------------
    // delete
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void deleteCorrectCourse() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        int before = courseService.readAll().size();
        courseService.delete(course);
        int after = courseService.readAll().size();
        assertTrue(before > after);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // readAll
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void readAllCourses() throws ServiceException {
        int before = courseService.readAll().size();
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        List<Course> list = courseService.readAll();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count++;
        }
        assertEquals(before + 1, count);
        assertTrue(list.get(count - 1).getMark().equals("asdf"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // validation
    // -----------------------------------------------------------------------------------------------------------------

    // correct course

    @Test
    public void validateCorrectCourse() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.validate(course);
    }

    // mark missing

    @Test(expected = ServiceException.class)
    public void validateNoMark() throws ServiceException {
        Course course = new Course("", Semester.SS + "18", "asdf", false);
        courseService.validate(course);
    }

    // name missing

    @Test(expected = ServiceException.class)
    public void validateNoName() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "18", "", false);
        courseService.validate(course);
    }

    // semester format not correct

    @Test(expected = ServiceException.class)
    public void validateSemesterDoesntEndWithWOrS() throws ServiceException {
        Course course = new Course("asdf", "FF18", "asdf", false);
        courseService.validate(course);
    }

    // year is not an integer

    @Test(expected = ServiceException.class)
    public void validateYearIsNotAnInteger() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "asdf", "asdf", false);
        courseService.validate(course);
    }

    // year is not 4 digits

    @Test(expected = ServiceException.class)
    public void validateYearIsNotFourDigits() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "12345", "asdf", false);
        courseService.validate(course);
    }

    // year is negative

    @Test(expected = ServiceException.class)
    public void validateYearIsNegative() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "-123", "asdf", false);
        courseService.validate(course);
    }

    @After
    public void wrapUp() {

    }
}
