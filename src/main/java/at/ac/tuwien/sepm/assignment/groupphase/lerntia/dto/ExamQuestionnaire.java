package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date;
    private String name;

    public ExamQuestionnaire() {}

    public ExamQuestionnaire(Long courseId, Long id, Boolean isDeleted, String name, LocalDate date) {
        super(courseId, id, isDeleted);
        this.date = date;
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseId='" + super.getCourseID() + '\'' +
            ", id=" + super.getId()+
            ", date=" + date +
            ", name=" + name +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
