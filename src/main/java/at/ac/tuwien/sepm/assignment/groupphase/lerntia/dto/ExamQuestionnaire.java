package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date;

    public ExamQuestionnaire() {}

    public ExamQuestionnaire(String cmark, String semester, Long qid, Boolean isDeleted, LocalDate date) {
        super(cmark, semester, qid, isDeleted);
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
            "cmark='" + super.getCmark() + '\'' +
            ", semester='" + super.getSemester() + '\'' +
            ", qid=" + super.getId()+
            "date=" + date +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
