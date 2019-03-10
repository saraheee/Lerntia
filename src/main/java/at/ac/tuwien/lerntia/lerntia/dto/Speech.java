package at.ac.tuwien.lerntia.lerntia.dto;

public class Speech {
    private String question; // question
    private String answer1; // first answer
    private String answer2; // second answer
    private String answer3; // third answer
    private String answer4; // fourth answer
    private String answer5; // fifth answer
    private String voice; // used voice
    private String singleAnswer; // text of a single answer to read
    private String feedbackText; // feedback after answered question

    public Speech() {
    }

    public Speech(String question, String answer1, String answer2, String answer3, String answer4, String answer5, String voice) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.voice = voice;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getSingleAnswer() {
        return singleAnswer;
    }

    public void setSingleAnswer(String singleAnswer) {
        this.singleAnswer = singleAnswer;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    @Override
    public String toString() {
        return "TextToSpeech{" +
            "question='" + question + '\'' +
            ", answer1='" + answer1 + '\'' +
            ", answer2='" + answer2 + '\'' +
            ", answer3='" + answer3 + '\'' +
            ", answer4='" + answer4 + '\'' +
            ", answer5='" + answer5 + '\'' +
            ", voice='" + voice + '\'' +
            '}';
    }

}