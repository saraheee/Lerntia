package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LerntiaServiceTest {

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
