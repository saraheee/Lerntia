package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;
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

public class UserCourseDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IUserCourseDAO userCourseDAO;
    private IUserDAO userDAO;
    private ICourseDAO courseDAO;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            connection = jdbcConnectionManager.getTestConnection();
            this.IUserCourseDAO(new UserCourseDAO(jdbcConnectionManager));
            this.IUserDAO(new UserDAO(jdbcConnectionManager));
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void IUserCourseDAO(UserCourseDAO userCourseDAO) {
        this.userCourseDAO = userCourseDAO;
    }

    @After
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createNewUserCourse() throws PersistenceException {
        String mark = "123.123";
        User Fabio = new User();
        Fabio.setMatriculationNumber("01526912");
        userDAO.create(Fabio);
        Course tgi = new Course();
        tgi.setSemester(Semester.WS + "19");
        tgi.setMark(mark);
        courseDAO.create(tgi);
        UserCourse FabioTGI = new UserCourse();
        FabioTGI.setCmark(mark);
        FabioTGI.setMatriculationNumber("01526912");
        FabioTGI.setSemester(Semester.WS + "19");
        FabioTGI.setCourseID(tgi.getId());

        userCourseDAO.create(FabioTGI);

    }

    @Test(expected = PersistenceException.class)
    public void createNewUserCourseReferentialError() throws PersistenceException {
        UserCourse FabioTGI = new UserCourse();
        FabioTGI.setCmark("123.123");
        FabioTGI.setMatriculationNumber("01526912");
        FabioTGI.setSemester(Semester.WS + "19");

        userCourseDAO.create(FabioTGI);

    }

}
