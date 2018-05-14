package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;


public class UserDAOTest {


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IUserDAO userDAO;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            connection = jdbcConnectionManager.getTestConnection();
            this.IUserDAO(new UserDAO(jdbcConnectionManager));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    public void IUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Test
    public void createNewUser()throws PersistenceException{
        try {
            User newUser = new User();
            newUser.setMatriculationNumber("0231231");
            newUser.setName("Jovan");
            newUser.setStudyProgramme("033 532");
            userDAO.create(newUser);
            User other = newUser;
            other.setDeleted(false);

            assertEquals(newUser, other);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewUserError()throws PersistenceException{
        try {
            User failedUser = new User();
            failedUser.setMatriculationNumber(null);
            failedUser.setName(null);
            failedUser.setStudyProgramme(null);
            userDAO.create(failedUser);

        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void updateExistingUserandReadUser()throws PersistenceException{
        try {
            User fabio = new User();
            fabio.setMatriculationNumber("123123123");
            fabio.setName("Fabio");
            fabio.setStudyProgramme("033 532");
            userDAO.create(fabio);

            User fabio2 = new User();
            fabio2.setMatriculationNumber("123123123");
            fabio2.setStudyProgramme("033 555");
            fabio2.setName("NotFabio");
            userDAO.update(fabio2);

            User other = new User();
            other.setMatriculationNumber("123123123");
            other = userDAO.read(other);

            assertEquals(fabio2.getMatriculationNumber(),other.getMatriculationNumber());
            assertEquals(fabio2.getName(),other.getName());
            assertEquals(fabio2.getStudyProgramme(),other.getStudyProgramme());
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void deleteUser() throws PersistenceException {
        try {
            User deletedUser = new User();
            deletedUser.setMatriculationNumber("0123456");
            deletedUser.setName("Fabio");
            deletedUser.setStudyProgramme("033 532");
            userDAO.create(deletedUser);
            userDAO.delete(deletedUser);
            User other = userDAO.read(deletedUser);
            assertEquals(true,other.getDeleted());
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void deleteUserError() throws PersistenceException{
        try {
            User fabio = new User();
            fabio.setMatriculationNumber("01526912");
            fabio.setName("Fabio");
            fabio.setStudyProgramme("033 532");
            userDAO.create(fabio);
            fabio.setMatriculationNumber(null);
            userDAO.delete(fabio);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @After
    public void rollback() throws SQLException {
        connection.rollback();
    }

}
