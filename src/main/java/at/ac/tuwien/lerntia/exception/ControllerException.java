package at.ac.tuwien.lerntia.exception;

public class ControllerException extends Exception {

    private String customMessage;

    public ControllerException(String message) {
        super(message);
        this.customMessage = message;
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}

