package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleUserService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;

public class UserServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ConfigReader configReaderStudent;
    private IUserService userService;

    @Before
    public void setUp() {
        try {
                this.configReaderStudent = new ConfigReader("student");
        } catch (ConfigReaderException e) {
                LOG.error("Failed to get student config reader: {}", e.getLocalizedMessage());
        }
        this.userService = new SimpleUserService();
    }


    @Test
    public void checkIfTheReturnedValuesMatchThoseFromTheConfigFile() {
        User modelUser, retunedUser;

        modelUser = new User(configReaderStudent.getValue("name"), configReaderStudent.getValue("matriculationNumber"),
            configReaderStudent.getValue("studyProgramme"), false);
        try {
            retunedUser = userService.read();
            Assert.assertEquals(modelUser.getName(), retunedUser.getName());
            Assert.assertEquals(modelUser.getMatriculationNumber(), retunedUser.getMatriculationNumber());
            Assert.assertEquals(modelUser.getStudyProgramme(), retunedUser.getStudyProgramme());
            Assert.assertEquals(modelUser.getDeleted(), retunedUser.getDeleted());
        } catch (ServiceException e) {
            Assert.assertTrue(((modelUser.getName() == null || modelUser.getName().trim().isEmpty()) && (modelUser.getMatriculationNumber() == null ||
                modelUser.getMatriculationNumber().trim().isEmpty()) && (modelUser.getStudyProgramme() == null ||
                modelUser.getStudyProgramme().trim().isEmpty())));
        }
    }


    @After
    public void closeConfigReader() {
        try {
            configReaderStudent.close();
        } catch (ConfigReaderException e) {
            LOG.error("Could not close student config reader: {}", e.getLocalizedMessage());
        }
    }
}
