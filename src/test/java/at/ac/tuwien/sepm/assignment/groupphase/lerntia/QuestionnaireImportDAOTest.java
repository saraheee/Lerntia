package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class QuestionnaireImportDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionnaireImportDAO importDAO;
    private Connection connection;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();

            this.IQuestionnaireImportDAO(new QuestionnaireImportDAO());

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    private void IQuestionnaireImportDAO(QuestionnaireImportDAO importDAO) {
        this.importDAO = importDAO;
    }

    @Test(expected = PersistenceException.class)
    public void getContentsOfMissingPath() throws IOException, PersistenceException {
        importDAO.getContents("");
    }

    @Test
    public void getContentsOfCorrectFile() throws IOException, PersistenceException {
        ArrayList<String> results = importDAO.getContents(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile.csv");
        int count = 0;
        for (String result : results) {
            count++;
            LOG.debug("test content: " + result);
            assertTrue(result.equals("Frage?;Antwort eins;Antwort zwei;Antwort drei;Antwort vier;Antwort fuenf;34"));
        }
        assertTrue(count == 2);
    }

    @Test
    public void testImportPictures() throws IOException, PersistenceException {
        importDAO.importPictures(new File(System.getProperty("user.dir") + File.separator + "img_original" + File.separator + "test_image.png"), "test");
    }
}

