package at.ac.tuwien.sepm.assignment.groupphase.util;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JDBCConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CONNECTION_URL = "jdbc:h2:file:~/Lerntia/db/lerntia";
    private static final String INITIAL_RESOURCE = "classpath:sql/createAndInsert.sql";
    private static final String TEST_CONNECTION_URL = "jdbc:h2:~/lerntiaTestDB";
    private static final String TEST_RESOURCE = "classpath:sql/testCreate.sql";

    private static Connection connection;
    private static boolean isTestConnection;

    public static void setIsTestConnection(boolean isTestConnection) {
        JDBCConnectionManager.isTestConnection = isTestConnection;
    }

    public Connection getConnection() throws PersistenceException {
        if (connection == null) {
            if (isTestConnection()) {
                LOG.info("Trying to initialize the test database");
            } else {
                LOG.info("Trying to initialize the database");
            }
            initDatabase();
        }
        return connection;
    }

    public Connection getTestConnection() throws PersistenceException {
        isTestConnection = true;
        return getConnection();
    }

    private void initDatabase() throws PersistenceException {
        try {
            Class.forName("org.h2.Driver");
            if (isTestConnection) {
                connection = DriverManager.getConnection(TEST_CONNECTION_URL + ";INIT=RUNSCRIPT FROM '" + TEST_RESOURCE + "'", "sa", "");
            } else {
                connection = DriverManager.getConnection(CONNECTION_URL + ";INIT=RUNSCRIPT FROM '" + INITIAL_RESOURCE + "'", "sa", "");
            }
        } catch (SQLException e) {
            closeConnection();
            throw new PersistenceException("Could not initialize the database: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            throw new PersistenceException("Could not initialize the database. Class not found! " + e.getLocalizedMessage());
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Failed to close connection");
            }
            connection = null;
        }
    }

    public boolean isTestConnection() {
        return isTestConnection;
    }
}
