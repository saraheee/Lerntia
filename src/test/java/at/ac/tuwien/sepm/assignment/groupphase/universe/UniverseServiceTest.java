package at.ac.tuwien.sepm.assignment.groupphase.universe;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dao.JDBCUniverseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dao.UniverseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.universe.service.SimpleUniverseService;
import at.ac.tuwien.sepm.assignment.groupphase.universe.service.UniverseService;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UniverseServiceTest {

    private final UniverseDAO universeDAO = mock(JDBCUniverseDAO.class);
    private final UniverseService universeService = new SimpleUniverseService(universeDAO);

    public UniverseServiceTest() throws PersistenceException {
        when(universeDAO.readAnswerForQuestion(any())).thenReturn(new Answer(0L, "42"));
    }

    @Test
    public void testSimpleUniverseService() throws ServiceException {
        Assert.assertThat(universeService.calculateAnswer().getText(), is("42!"));
    }

}
