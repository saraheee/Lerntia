package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.util.Objects;

public class Answer {

    private final Long id;
    private final String text;

    public Answer(Long id, String answerText) {
        this.id = id;
        this.text = answerText;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", text='" + text + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) &&
            Objects.equals(text, answer.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

}
