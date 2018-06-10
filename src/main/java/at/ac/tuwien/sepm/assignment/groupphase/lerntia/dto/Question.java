package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.CheckBox;

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
    private CheckBox containPicture;

    private String checkedAnswers;

    public Question() {
    }

    public Question(Long id, String questionText, String picture, String answer1, String answer2, String answer3,
                    String answer4, String answer5, String correctAnswers, String optionalFeedback, Boolean isDeleted) {
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
        JFXPanel fxPanel = new JFXPanel();
        containPicture = new CheckBox();
        if (picture != null && picture.equals("")) {
            this.setContainPicture(false);
            this.containPicture.setText(checked.Nein.toString());
        }
        if (picture != null && picture.trim().length() > 0) {
            this.setContainPicture(true);
            this.containPicture.setText(checked.Ja.toString());
        } else {
            this.setContainPicture(false);
            this.containPicture.setText(checked.Nein.toString());
        }
        this.containPicture.setDisable(true);
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
            ", questionText='" + questionText + '\'' +"\n"+
            ", picture='" + picture + '\'' +"\n"+
            ", answer1='" + answer1 + '\'' +"\n"+
            ", answer2='" + answer2 + '\'' +"\n"+
            ", answer3='" + answer3 + '\'' +"\n"+
            ", answer4='" + answer4 + '\'' +"\n"+
            ", answer5='" + answer5 + '\'' +"\n"+
            ", correctAnswers='" + correctAnswers + '\'' +"\n"+
            ", optionalFeedback='" + optionalFeedback + '\'' +"\n"+
            ", isDeleted=" + isDeleted +
            '}';
    }

    public String toStringGUI(){
        return "Question Text: '" + questionText + '\'' +"\n"+
            "Picture: '" + picture + '\'' +"\n"+
            "Answer 1: '" + answer1 + '\'' +"\n"+
            "Answer 2: '" + answer2 + '\'' +"\n"+
            "Answer 3: '" + answer3 + '\'' +"\n"+
            "Answer 4: '" + answer4 + '\'' +"\n"+
            "Answer 5: '" + answer5 + '\'' +"\n"+
            "Correct Answers: '" + correctAnswers + '\'' +"\n"+
            "Optional Feedback: '" + optionalFeedback + '\'';
    }

    public String getCheckedAnswers() {
        return checkedAnswers;
    }

    public void setCheckedAnswers(String checkedAnswers) {
        this.checkedAnswers = checkedAnswers;
    }

    public CheckBox getContainPicture() {
        return containPicture;
    }

    private void setContainPicture(boolean set) {
        this.containPicture.setSelected(set);
    }

    public enum checked {
        Ja, Nein
    }
}
