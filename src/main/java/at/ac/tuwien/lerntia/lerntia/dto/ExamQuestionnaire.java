package at.ac.tuwien.lerntia.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date; // date of creation

    public ExamQuestionnaire() {
    }

    public ExamQuestionnaire(Long courseID, Long id, Boolean isDeleted, String name, boolean selected, LocalDate date) {
        super(courseID, id, name, selected, isDeleted);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseID='" + super.getCourseID() + '\'' +
            ", id=" + super.getId() +
            ", date=" + date +
            ", name=" + super.getName() +
            ", selected=" + super.getSelected() +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
