package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class ConfigReaderException extends Exception {

    private String customMessage;

    public ConfigReaderException(String message) {
        super(message);
        this.customMessage = message;
    }

    public ConfigReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigReaderException(Throwable cause) {
        super(cause);
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
