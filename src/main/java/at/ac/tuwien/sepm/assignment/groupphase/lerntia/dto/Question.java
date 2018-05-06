package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Question {
    private Long id;
    private String questionText;
    private String picture;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String correctAnswers;
    private String optionalFeedback;
    private Boolean isDeleted;

    public Question() {}

    public Question(Long id, String questionText, String picture, String answer1, String answer2, String answer3, String answer4, String answer5, String correctAnswers, String optionalFeedback, Boolean isDeleted) {
        this.id = id;
        this.questionText = questionText;
        this.picture = picture;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.correctAnswers = correctAnswers;
        this.optionalFeedback = optionalFeedback;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getOptionalFeedback() {
        return optionalFeedback;
    }

    public void setOptionalFeedback(String optionalFeedback) {
        this.optionalFeedback = optionalFeedback;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", questionText='" + questionText + '\'' +
            ", picture='" + picture + '\'' +
            ", answer1='" + answer1 + '\'' +
            ", answer2='" + answer2 + '\'' +
            ", answer3='" + answer3 + '\'' +
            ", answer4='" + answer4 + '\'' +
            ", answer5='" + answer5 + '\'' +
            ", correctAnswers='" + correctAnswers + '\'' +
            ", optionalFeedback='" + optionalFeedback + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
