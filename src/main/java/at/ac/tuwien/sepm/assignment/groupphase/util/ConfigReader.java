package at.ac.tuwien.sepm.assignment.groupphase.util;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Properties prop;
    private InputStream inputStream;

    public ConfigReader(String config) throws ConfigReaderException {

        String propsPath = System.getProperty("user.dir");
        propsPath += File.separator + "config" + File.separator + config + ".properties";

        this.prop = new Properties();
        File propsFile = new File(propsPath);

        LOG.debug("Reading config properties: propsPath: *{}* propsFile: *{}*", propsPath, propsFile);

        if (Files.exists(Paths.get(propsPath))) {
            try {
                this.inputStream = new FileInputStream(propsFile);
                this.prop.load(this.inputStream);
            } catch (IOException e) {
                try {
                    this.inputStream.close();
                } catch (IOException e1) {
                    throw new ConfigReaderException("Der Inputstream für das Lesen der Konfigurationsdateien konnte nicht geschlossen werden!");
                }
                throw new ConfigReaderException("Das Lesen der Konfigurationsdateien ist fehlgeschlagen!");
            }
        }
    }

    public static String checkIfAllPropertiesFilesAreProvided() {
        LOG.debug("Checking if all properties files are provided.");
        String propertiesFileNamesArray[] = new String[]{"about", "course", "questions", "speech", "student"};
        String basePath = System.getProperty("user.dir") + File.separator + "config";
        StringBuilder message = new StringBuilder();

        for (String propertiesFileName : propertiesFileNamesArray) {
            String path = basePath + File.separator + propertiesFileName + ".properties";
            Path propsPath = Paths.get(path);

            if (!Files.exists(propsPath)) {
                message.append(propsPath).append("\n");
            }
        }
        return String.valueOf(message);
    }

    public static Map<String, String> readTextFile(String name) throws ConfigReaderException {
        LOG.debug("Trying to read text file with name '" + name + "'.");
        String propsPath = System.getProperty("user.dir");
        propsPath += File.separator + name + ".txt";

        Map<String, String> wordMap = new HashMap<>();

        Properties prop = new Properties();
        File propsFile = new File(propsPath);

        if (Files.exists(Paths.get(propsPath))) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(propsFile);

                Scanner sc = new Scanner(inputStream);
                while (sc.hasNext()) {
                    String firstWord = sc.next();
                    String secondWordToEnd = "";
                    if (sc.hasNextLine()) {
                        secondWordToEnd = sc.nextLine();
                    }
                    wordMap.put(firstWord, secondWordToEnd);
                }
                sc.close();
                inputStream.close();
            } catch (IOException e) {
                throw new ConfigReaderException("Das Lesen der Textdatei ist fehlgeschlagen!");
            }
        }
        return wordMap;
    }

    public String getValue(String key) {
        return this.prop.getProperty(key);
    }

    public int getValueInt(String key) {
        try {
            return Integer.parseInt(this.prop.getProperty(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Boolean getValueBoolean(String key) {
        return Boolean.valueOf(this.prop.getProperty(key));
    }

    public void close() throws ConfigReaderException {
        if (inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
                throw new ConfigReaderException("Der Inputstream für das Lesen der Konfigurationsdateien konnte nicht geschlossen werden. Bitte überprüfen, ob die notwendigen PROPERTIES Dateien vorhanden sind.");
            }
        }
    }
}