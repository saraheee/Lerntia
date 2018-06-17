package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.CheckBox;

import java.text.BreakIterator;
import java.util.Locale;

public class Question {
    private Long id; // distinct key
    private String questionText; // the question
    private String picture; // name of the file of the picture
    private String answer1; // first answer
    private String answer2; // second answer
    private String answer3; // third answer
    private String answer4; // fourth answer
    private String answer5; // fifth answer
    private String correctAnswers; // which answers are correct
    private String optionalFeedback; // feedback that is shown after answered question
    private Boolean isDeleted; // if question is deleted
    private CheckBox containPicture; // if question contains picture

    private String checkedAnswers; // selected answers

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
        JFXPanel fxPanel = new JFXPanel(); //needed for tests
        containPicture = new CheckBox();
        if (picture != null && picture.trim().equals("")) {
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
            ", questionText='" + questionText + '\'' + "\n" +
            ", picture='" + picture + '\'' + "\n" +
            ", answer1='" + answer1 + '\'' + "\n" +
            ", answer2='" + answer2 + '\'' + "\n" +
            ", answer3='" + answer3 + '\'' + "\n" +
            ", answer4='" + answer4 + '\'' + "\n" +
            ", answer5='" + answer5 + '\'' + "\n" +
            ", correctAnswers='" + correctAnswers + '\'' + "\n" +
            ", optionalFeedback='" + optionalFeedback + '\'' + "\n" +
            ", isDeleted=" + isDeleted +
            '}';
    }

    public String toStringGUI() {
        //the first three fields are mandatory
        return "Fragestellung: " + formatLines(questionText, new Locale ("de","AT")) +
            "Antwort 1: " + answer1 + "\n" +
            "Antwort 2: " + answer2 + "\n" +
            ((answer3 != null && answer3.trim().length() > 0) ? "Antwort 3: " + answer3 + "\n" : "") +
            ((answer4 != null && answer4.trim().length() > 0) ? "Antwort 4: " + answer4 + "\n" : "") +
            ((answer5 != null && answer5.trim().length() > 0) ? "Antwort 5: " + answer5 + "\n" : "") +
            ((picture != null && picture.trim().length() > 0) ? "Bildname: " + picture : "");
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

    private static String formatLines(String target, Locale currentLocale) {
        var boundary = BreakIterator.getSentenceInstance(currentLocale);
        boundary.setText(target);
        var start = boundary.first();
        var end = boundary.next();
        var lineLength = 0;
        var word = new StringBuilder();
        while (end != BreakIterator.DONE) {
            word.append(target.substring(start, end));
            lineLength = lineLength + word.length();
            if (lineLength >= 100) {
                word.append('\n');
                lineLength = word.length();
            }
            start = end;
            end = boundary.next();
        }
        word = (word.substring(word.length() - 1).equals("\n") ? word : (word.append("\n")));
        return word.toString();
    }

    public enum checked {
        Ja, Nein
    }
}
