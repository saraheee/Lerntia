package at.ac.tuwien.sepm.assignment.groupphase.util;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private InputStream inputStream;
    private Properties prop;

    public ConfigReader(String config) throws ConfigReaderException {

        String propsPath = System.getProperty("user.home");
        propsPath += File.separator + "Lerntia" + File.separator + "config" + File.separator + config + ".properties";

        this.prop = new Properties();
        File propsFile = new File(propsPath);

        LOG.debug("Reading config properties: propsPath: *{}* propsFile: *{}*", propsPath, propsFile);

        try {
            this.inputStream = new FileInputStream(propsFile);
            LOG.debug("Opened input stream: {}", inputStream);
            try {
                this.prop.load(this.inputStream);
            } catch (IOException e) {
                try {
                    this.inputStream.close();
                } catch (IOException e1) {
                    throw new ConfigReaderException("Das Inputstream für das Lesen der Konfigurationsdaten konnte weder geöffnet noch geschlossen werden. Bitte überprüfen Sie, ob die notwendigen PROPERTIES Dateien vorhanden sind.");
                }
                throw new ConfigReaderException("Das Inputstream für das Lesen der Konfigurationsdaten konnte nicht geöffnet werden. Bitte überprüfen Sie, ob die notwendigen PROPERTIES Dateien vorhanden sind.");
            }
        } catch (FileNotFoundException e) {
            throw new ConfigReaderException("Die Config Datei \""+config+".properties\" konnte nicht gefunden werden. Bitte überprüfen Sie, ob die notwendigen PROPERTIES Dateien vorhanden sind.");
        }
    }

    public String getValue(String key) {
        return this.prop.getProperty(key);
    }

    public int getValueInt(String key) {
        return Integer.parseInt(this.prop.getProperty(key));
    }

    public boolean getValueBoolean(String key) {
        return Boolean.valueOf(this.prop.getProperty(key));
    }

    public void close() throws ConfigReaderException {
        try {
            this.inputStream.close();
        } catch (IOException e) {
            throw new ConfigReaderException("Das Inputstream für das Lesen der Konfigurationsdaten konnte nicht geschlossen werden. Bitte überprüfen Sie, ob die notwendigen PROPERTIES Dateien vorhanden sind.");
        }
    }

    public void checkIfAllPropertiesFilesAreProvided() throws ConfigReaderException {
        LOG.debug("Checking if all properties files are provided.");
        String propertiesFileNamesArray[] = new String[]{"about", "course", "questions", "speech", "student"};
      //  String propertiesFileNamesArray[] = new String[]{"about", "course", "questions"};
        InputStream inputStream = null;
        String propsPath, basePath = System.getProperty("user.home") + File.separator + "Lerntia" + File.separator + "config";
        try {
            for (String propertiesFileName : propertiesFileNamesArray) {
                propsPath = basePath + File.separator + propertiesFileName + ".properties";
                File propsFile = new File(propsPath);
                inputStream = new FileInputStream(propsFile);
                Properties properties = new Properties();
                properties.load(inputStream);
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException e) {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                // ignore, as there is one exception to be send in case anything is wrong
            }
            throw new ConfigReaderException("Die Initialisierung könnte nicht erfolgreich durchgehen, da manche " +
                "PROPERTIES Dateien nicht vorhanden sind. Bitte überprüfen, dass es in dem Verzeichnis \"" + basePath +
                "\" die Folgenden PROPERTIES Dateien befinden: {\"about\", \"course\", \"questions\", \"speech\", \"student\"}");
        } // end of the main try-catch block
    }
}