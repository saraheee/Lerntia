package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class CourseServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ICourseService courseService;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp(){
        try {
            this.ICourseService(new SimpleCourseService(new CourseDAO(jdbcConnectionManager)));
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private void ICourseService(SimpleCourseService simpleCourseService) {
        this.courseService = simpleCourseService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // validation
    // -----------------------------------------------------------------------------------------------------------------

    // correct course

    @Test
    public void validateCorrectCourse() throws ServiceException {
        Course course = new Course("asdf", "2018S", "asdf", false);
        courseService.validate(course);
    }

    // mark missing

    @Test(expected = ServiceException.class)
    public void validateNoMark() throws ServiceException {
        Course course = new Course("", "2018S", "asdf", false);
        courseService.validate(course);
    }

    // name missing

    @Test(expected = ServiceException.class)
    public void validateNoName() throws ServiceException {
        Course course = new Course("asdf", "2018S", "", false);
        courseService.validate(course);
    }

    // semester does not end with "W" or "S"

    @Test(expected = ServiceException.class)
    public void validateSemesterDoesntEndWithWOrS() throws ServiceException {
        Course course = new Course("asdf", "2018F", "asdf", false);
        courseService.validate(course);
    }

    // year is not an integer

    @Test(expected = ServiceException.class)
    public void validateYearIsNotAnInteger() throws ServiceException {
        Course course = new Course("asdf", "asdfS", "asdf", false);
        courseService.validate(course);
    }

    // year is not 4 digits

    @Test(expected = ServiceException.class)
    public void validateYearIsNotFourDigits() throws ServiceException {
        Course course = new Course("asdf", "18S", "asdf", false);
        courseService.validate(course);
    }

    @After
    public void wrapUp(){

    }
}
