package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.time.LocalDate;

public class ExamQuestionnaire extends Questionnaire {
    private LocalDate date;

    public ExamQuestionnaire() {}

    public ExamQuestionnaire(String cid, String semester, Long qid, LocalDate date) {
        super(cid, semester, qid);
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
            "cid='" + super.getCid() + '\'' +
            ", semester='" + super.getSemester() + '\'' +
            ", qid=" + super.getId()+
            "date=" + date +
            '}';
    }
}
