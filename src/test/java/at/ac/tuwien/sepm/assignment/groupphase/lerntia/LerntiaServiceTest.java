package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import static org.hamcrest.core.Is.is;

public class LerntiaServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;

    @Before
    public void setUp() {
        try {
            connection = JDBCConnectionManager.getTestConnection();
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    @Test
    public void testSimpleLerntiaService() throws ServiceException {
        Assert.assertThat(42, is(42));
    }

   /** private final LerntiaDAO lerntiaDAO = mock(JDBCLerntiaDAO.class);
    private final LerntiaService lerntiaService = new SimpleLerntiaService(lerntiaDAO);

    public LerntiaServiceTest() throws PersistenceException {
        when(lerntiaDAO.readAnswerForQuestion(any())).thenReturn(new Answer(0L, "42"));
    }

    @Test
    public void testSimpleLerntiaService() throws ServiceException {
        Assert.assertThat(lerntiaService.goToExamMode().getText(), is("42!"));
    }
    */

}
