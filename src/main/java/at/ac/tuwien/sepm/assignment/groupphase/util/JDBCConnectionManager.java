package at.ac.tuwien.sepm.assignment.groupphase.util;

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
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            // used for accessing create.sql file
            String script = JDBCConnectionManager.class.getClassLoader().getResource("sql/create.sql").getPath();
            if(script == null) {
                LOG.warn("Script returned a null pointer!");
            }
            if(System.getProperty("os.name").startsWith("Windows")) {
                script = script.substring(1); // on Windows, the string would start with an '/' causing an InvocationException
            }
            // for details, see: http://www.h2database.com/html/features.html
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/lerntia;INIT=RUNSCRIPT FROM '"+ script +"'", "sa", "");
            //connection = DriverManager.getConnection("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:sql/create.sql'", "sa", "");
        }
        return connection;
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
