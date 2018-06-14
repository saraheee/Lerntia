package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionnaireQuestion {
    private String cmark; // mark of course
    private String semester; // ws or ss plus year
    private Long qid; // id of questionnaire
    private Long questionid; // id of question
    private Boolean isDeleted; // if deleted

    public QuestionnaireQuestion() {}

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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "QuestionnaireQuestion{" +
            "cmark='" + cmark + '\'' +
            ", semester='" + semester + '\'' +
            ", qid=" + qid +
            ", questionid=" + questionid +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
