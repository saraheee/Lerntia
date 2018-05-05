package at.ac.tuwien.sepm.assignment.groupphase.util;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JDBCConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CONNECTION_URL =  "jdbc:h2:tcp://localhost/~/lerntia";
    private static final String INITIAL_RESOURCE = "classpath:sql/create.sql";

    private static Connection connection;

    public static Connection getConnection() throws PersistenceException {
        if (connection == null) {
            LOG.info("Trying to initialize the database");
            initDatabase();
        }
        return connection;
    }

    private static void initDatabase() throws PersistenceException {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(CONNECTION_URL, "sa", "");
            InputStream inputStream = JDBCConnectionManager.class.getResourceAsStream(INITIAL_RESOURCE);
            if(inputStream == null) {
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

    public void closeConnection() {
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
