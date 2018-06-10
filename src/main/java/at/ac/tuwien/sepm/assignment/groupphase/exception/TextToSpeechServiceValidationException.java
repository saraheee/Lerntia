package at.ac.tuwien.sepm.assignment.groupphase.exception;


public class TextToSpeechServiceValidationException extends ServiceException {

    private String custommessage;

    public TextToSpeechServiceValidationException(String message) {
        super(message);
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage = custommessage;
    }

}


