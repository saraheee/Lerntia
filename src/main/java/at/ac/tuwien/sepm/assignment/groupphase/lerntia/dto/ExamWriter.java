package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.util.List;

public class ExamWriter {

    private List<Question> questions;
    private String name;
    private String path;

    public ExamWriter(List<Question> questions, String name, String path) {
        this.questions = questions;
        this.name = name;
        this.path = path;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
