package at.ac.tuwien.lerntia.lerntia.dto;

import java.io.File;

public class ImportQuestionnaire {

    private File file;
    private Course course;
    private String name;
    private boolean isExam;

    public ImportQuestionnaire(File file, Course course, String name, boolean isExam) {
        this.file = file;
        this.course = course;
        this.name = name;
        this.isExam = isExam;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsExam() {
        return isExam;
    }

    public void setIsExam(boolean exam) {
        isExam = exam;
    }
}
