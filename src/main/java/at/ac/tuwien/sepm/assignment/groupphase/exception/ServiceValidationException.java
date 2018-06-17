package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class ServiceValidationException extends Exception {

    private String customMessage;

    public ServiceValidationException(String message) {
        super(message);
        this.customMessage = message;
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidationException(Throwable cause) {
        super(cause);
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}
