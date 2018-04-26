package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.util.Objects;

public class Question {

    public Question(String questionText) {
        this.text = questionText;
    }

    private final String text;

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Question{" +
            "text='" + text + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Question question = (Question) o;
        return Objects.equals(text, question.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

}
