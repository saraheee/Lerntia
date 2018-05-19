package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CourseDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private ICourseDAO courseDAO;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            connection = jdbcConnectionManager.getTestConnection();
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }
    @After
    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }


    @Test
    public void createNewCourse() throws PersistenceException {
        try {
            Course course = new Course();
            course.setSemester("2018W");
            course.setName("ECG");
            course.setMark("124.119");
            courseDAO.create(course);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewCourseError() throws PersistenceException {
        try {
            Course tgi = new Course();
            tgi.setSemester("2018W");
            tgi.setName("TGI");
            tgi.setMark(null);
            courseDAO.create(tgi);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void updateExistingUserandReadUser()throws PersistenceException{
        try {
            Course course = new Course();
            course.setSemester("2017S");
            course.setMark("151.999");
            course.setName("Akustik 2");
            courseDAO.create(course);

            Course courseUpdated = new Course();
            courseUpdated.setId(course.getId());
            courseUpdated.setSemester("2018W");
            courseUpdated.setMark("151.999");
            courseUpdated.setName("Akustik 2");

            courseDAO.update(courseUpdated);


        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void deleteCourse() throws PersistenceException {
        try {
            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("123.349");
            tgi.setName("TGI");
            courseDAO.create(tgi);
            Course tgidelete =new Course();
            tgidelete.setMark(tgi.getMark());
            tgidelete.setId(tgi.getId());
            courseDAO.delete(tgidelete);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void deleteCourseError() throws PersistenceException{
        try {
            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("111.222");
            tgi.setName("Informatik 1");
            courseDAO.create(tgi);
            tgi.setId(null);
            courseDAO.delete(tgi);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void countSizeofReadAll() throws PersistenceException{

        int currentNumber = courseDAO.readAll().size();

        Course ECV = new Course();
        ECV.setSemester("2015S");
        ECV.setMark("123.111");
        ECV.setName("ECV");
        courseDAO.create(ECV);

        List list = courseDAO.readAll();
        assertEquals(currentNumber+1,list.size());
        courseDAO.delete(ECV);

        List list2 = courseDAO.readAll();
        assertEquals(currentNumber,list2.size());
    }

    @Test
    public void keyTest() throws PersistenceException{

        Course PK1 = new Course();
        PK1.setSemester("2016W");
        PK1.setMark("112.659asdf");
        PK1.setName("Programmieren 1");
        courseDAO.create(PK1);

        Course tgi = new Course();
        tgi.setSemester("2016W");
        tgi.setMark("126.349asdf");
        tgi.setName("TGI");
        courseDAO.create(tgi);
    }
}
