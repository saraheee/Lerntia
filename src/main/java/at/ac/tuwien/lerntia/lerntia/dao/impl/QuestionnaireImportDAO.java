package at.ac.tuwien.lerntia.lerntia.dao.impl;

import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireImportDAO;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class QuestionnaireImportDAO implements IQuestionnaireImportDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public ArrayList<String> getContents(String filePath) throws IOException, PersistenceException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new PersistenceException("Der Pfad für das Importieren der Fragen ist null!");
        }
        // A BufferedReader Object is created using path that is the argument of this method
        FileReader fr = new FileReader(filePath);
        ArrayList<String> fileContent;
        try (BufferedReader br = new BufferedReader(fr)) {

            fileContent = new ArrayList<>();

            String line;

            // Every line is read from the file and added to the ArrayList

            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
        }

        return fileContent;
    }

    @Override
    public void importPictures(File file, String name) throws IOException, PersistenceException {
        if (file == null || name == null) {
            throw new PersistenceException("Die Datei oder der Name für das Importieren der Bilder ist null!");
        }
        Path imgPath = Paths.get(System.getProperty("user.dir") + File.separator + "img");
        File imgDir = new File(String.valueOf(imgPath));

        if (!Files.exists(imgPath)) {
            LOG.info("Image directory not found - will be created");
            if (!imgDir.mkdir()) {
                throw new PersistenceException("Das Bilder Verzeichnis 'img' konnte nicht angelegt werden!");
            }
        }

        Path dirPath = Paths.get(System.getProperty("user.dir") + File.separator + "img" + File.separator + name);
        File dir = new File(String.valueOf(dirPath));
        if (!Files.exists(dirPath)) {
            if (!dir.mkdir()) {
                throw new PersistenceException("Das Bilder Verzeichnis für den Fragebogen '" + name + "' konnte nicht angelegt werden!");
            }
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                String p = dir.getAbsolutePath() + File.separator + child.getName();
                try {
                    Path path = Paths.get(p);
                    Files.copy(child.toPath(), path);
                } catch (IOException e) {
                    deletePictures(dir);
                    throw new IOException("Bild kann nicht gelesen werden: " + p);
                }
            }
        }
    }

    public void deletePictures(File file) {
        FileSystemUtils.deleteRecursively(file);
    }
}
