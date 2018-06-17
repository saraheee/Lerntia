package at.ac.tuwien.sepm.assignment.groupphase.exception;


public class TextToSpeechServiceException extends ServiceException {

    private String customMessage;

    public TextToSpeechServiceException(String message) {
        super(message);
        this.customMessage = message;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}


