package at.ac.tuwien.sepm.assignment.groupphase.util;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JDBCConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CONNECTION_URL = "jdbc:h2:tcp://192.168.0.102/~/lerntia";
    //private static final String TEST_CONNECTION_URL = "jdbc:h2:file:database./lerntiaTestDB";
    private static final String TEST_CONNECTION_URL = "jdbc:h2:~/lerntiaTestDB";
    private static final String INITIAL_RESOURCE = "classpath:sql/create.sql";
    private static final String DROP_RESOURCE = "classpath:sql/drop.sql";

    private static Connection connection;
    private static boolean isTestConnection;

    public static Connection getConnection() throws PersistenceException {
        if (connection == null) {
            LOG.info("Trying to initialize the database");
            initDatabase();
        }
        return connection;
    }

    public static Connection getTestConnection() throws PersistenceException {
        isTestConnection = true;
        initDatabase();
        return getConnection();
    }

    private static void initDatabase() throws PersistenceException {
        try {
            Class.forName("org.h2.Driver");
            if (isTestConnection) {
                connection = DriverManager.getConnection(TEST_CONNECTION_URL + ";INIT=RUNSCRIPT FROM '" + DROP_RESOURCE + "'", "sa", "");
                connection = DriverManager.getConnection(TEST_CONNECTION_URL + ";INIT=RUNSCRIPT FROM '" + INITIAL_RESOURCE + "'", "sa", "");
            } else {
                connection = DriverManager.getConnection(CONNECTION_URL, "sa", "");
            }
            var inputStream = JDBCConnectionManager.class.getResourceAsStream(INITIAL_RESOURCE);
            if (inputStream == null) {
                LOG.error("Input stream for create statements is null!");
            } else {
                RunScript.execute(connection, new InputStreamReader(inputStream));
                LOG.info("Reading initial commands from input stream.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOG.debug("Could not initialize the database:" + e.getMessage());
            throw new PersistenceException(e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Failed to close connection '{}'", e.getMessage(), e);
            }
            connection = null;
        }
    }

}
