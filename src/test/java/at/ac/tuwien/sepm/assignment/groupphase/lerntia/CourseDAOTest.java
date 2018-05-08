package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Classes.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Interfaces.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.util.List;

public class CourseDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private ICourseDAO courseDAO;

    @Before
    public void setUp() {
        try {
            connection = JDBCConnectionManager.getTestConnection();
            this.ICourseDAO(new CourseDAO());
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    public void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Test
    public void createNewCourse() throws PersistenceException {
        try {
            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);
            Course other = tgi;
            other.setDeleted(false);
            assertEquals(other,tgi);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewCourseError() throws PersistenceException {
        try {
            Course tgi = new Course();
            tgi.setSemester("2019W");
            tgi.setMark(null);
            courseDAO.create(tgi);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void updateExistingUserandReadUser()throws PersistenceException{
        try {
            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);

            Course tgiUpdated = new Course();
            tgiUpdated.setSemester("2018W");
            tgiUpdated.setMark("123.349");

            courseDAO.update(tgiUpdated);


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
            courseDAO.create(tgi);
            Course tgidelete =new Course();
            tgidelete.setMark(tgi.getMark());
            courseDAO.delete(tgidelete);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void deleteCourseError() throws PersistenceException{
        try {
            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);
            tgi.setMark(null);
            courseDAO.delete(tgi);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void countSizeofReadAll() throws PersistenceException{
        Course PK1 = new Course();
        PK1.setSemester("2018S");
        PK1.setMark("112.659");
        courseDAO.create(PK1);

        Course tgi = new Course();
        tgi.setSemester("2017S");
        tgi.setMark("123.349");
        courseDAO.create(tgi);

        Course ECV = new Course();
        ECV.setSemester("2015S");
        ECV.setMark("123.111");
        courseDAO.create(ECV);

        List list = courseDAO.readAll();
        assertEquals(3,list.size());
        courseDAO.delete(ECV);

        List list2 = courseDAO.readAll();
        assertEquals(2,list2.size());
    }
}
