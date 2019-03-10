package at.ac.tuwien.lerntia.exception;

public class ServiceException extends Exception {

    private String customMessage;

    public ServiceException(String message) {
        super(message);
        this.customMessage = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}
