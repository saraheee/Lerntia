package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionnaireQuestion {
    private String cid;
    private String semester;
    private Long qid;
    private Long questionid;

    public QuestionnaireQuestion() {}

    public QuestionnaireQuestion(String cid, String semester, Long qid, Long questionid) {
        this.cid = cid;
        this.semester = semester;
        this.qid = qid;
        this.questionid = questionid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Long getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Long questionid) {
        this.questionid = questionid;
    }

    @Override
    public String toString() {
        return "QuestionnaireQuestion{" +
            "cid='" + cid + '\'' +
            ", semester='" + semester + '\'' +
            ", qid=" + qid +
            ", questionid=" + questionid +
            '}';
    }
}
