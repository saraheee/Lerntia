package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class PersistenceException extends Exception {

    private String customMessage;

    public PersistenceException(String message) {
        super(message);
        this.customMessage = message;
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}
