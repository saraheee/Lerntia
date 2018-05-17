package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date;

    public ExamQuestionnaire() {}

    public ExamQuestionnaire(Long courseID, Long id, Boolean isDeleted, String name, LocalDate date) {
        super(courseID, id, name, isDeleted);
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
            ", id=" + super.getId()+
            ", date=" + date +
            ", name=" + super.getName() +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
