package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date;

    public ExamQuestionnaire() {}

    public ExamQuestionnaire(Long courseId, Long id, Boolean isDeleted, LocalDate date) {
        super(courseId, id, isDeleted);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseId='" + super.getCourseID() + '\'' +
            ", id=" + super.getId()+
            ", date=" + date +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
