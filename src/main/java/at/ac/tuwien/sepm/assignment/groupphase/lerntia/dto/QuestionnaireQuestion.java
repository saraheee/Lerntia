package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionnaireQuestion {
    private String cmark;
    private String semester;
    private Long qid;
    private Long questionid;

    public QuestionnaireQuestion() {}

    public QuestionnaireQuestion(String cmark, String semester, Long qid, Long questionid) {
        this.cmark = cmark;
        this.semester = semester;
        this.qid = qid;
        this.questionid = questionid;
    }

    public String getCmark() {
        return cmark;
    }

    public void setCmark(String cmark) {
        this.cmark = cmark;
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
            "cmark='" + cmark + '\'' +
            ", semester='" + semester + '\'' +
            ", qid=" + qid +
            ", questionid=" + questionid +
            '}';
    }
}
