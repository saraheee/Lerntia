package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class ControllerException extends Exception {

    private String custommessage;

    public ControllerException(String message) {
        super(message);
        this.custommessage = message;
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage = custommessage;
    }

}

