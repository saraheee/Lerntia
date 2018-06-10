package at.ac.tuwien.sepm.assignment.groupphase.exception;


public class TextToSpeechServiceException extends ServiceException {

    private String custommessage;

    public TextToSpeechServiceException(String message) {
        super(message);
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage = custommessage;
    }

}


