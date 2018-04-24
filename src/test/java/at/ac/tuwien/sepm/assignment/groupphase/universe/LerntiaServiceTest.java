package at.ac.tuwien.sepm.assignment.groupphase.universe;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dao.JDBCLerntiaDAO;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dao.LerntiaDAO;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.universe.service.LerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.universe.service.SimpleLerntiaService;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LerntiaServiceTest {

    private final LerntiaDAO lerntiaDAO = mock(JDBCLerntiaDAO.class);
    private final LerntiaService lerntiaService = new SimpleLerntiaService(lerntiaDAO);

    public LerntiaServiceTest() throws PersistenceException {
        when(lerntiaDAO.readAnswerForQuestion(any())).thenReturn(new Answer(0L, "42"));
    }

    @Test
    public void testSimpleLerntiaService() throws ServiceException {
        Assert.assertThat(lerntiaService.goToExamMode().getText(), is("42!"));
    }

}
