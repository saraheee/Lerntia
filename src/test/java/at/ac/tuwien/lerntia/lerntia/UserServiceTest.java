package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.lerntia.dto.User;
import at.ac.tuwien.lerntia.lerntia.service.IUserService;
import at.ac.tuwien.lerntia.util.ConfigReader;
import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.service.impl.SimpleUserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

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
    public void checkIfTheReturnedValuesMatchThoseFromTheConfigFileIffTheyAreProvided() {
        User modelUser, retunedUser;

        modelUser = new User(configReaderStudent.getValue("name"), configReaderStudent.getValue("matriculationNumber"),
            configReaderStudent.getValue("studyProgramme"), false);
        try {
            retunedUser = userService.read();
            if(modelUser.getName() != null && !modelUser.getName().trim().isEmpty()) {
                Assert.assertEquals(modelUser.getName(), retunedUser.getName());
            } else {
                Assert.assertNotEquals(modelUser.getName(), retunedUser.getName());
            }
            if(modelUser.getMatriculationNumber() != null && !modelUser.getMatriculationNumber().trim().isEmpty()) {
                Assert.assertEquals(modelUser.getMatriculationNumber(), retunedUser.getMatriculationNumber());
            } else {
                Assert.assertNotEquals(modelUser.getMatriculationNumber(), retunedUser.getMatriculationNumber());
            }
            if(modelUser.getStudyProgramme() != null && !modelUser.getStudyProgramme().trim().isEmpty()) {
                Assert.assertEquals(modelUser.getStudyProgramme(), retunedUser.getStudyProgramme());
            } else {
                Assert.assertNotEquals(modelUser.getStudyProgramme(), retunedUser.getStudyProgramme());
            }
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
