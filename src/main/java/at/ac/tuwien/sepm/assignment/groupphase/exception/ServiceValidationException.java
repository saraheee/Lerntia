package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class ServiceValidationException extends Exception {

    private String custommessage;

    public ServiceValidationException(String message) {
        super(message);
        this.custommessage = message;
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidationException(Throwable cause) {
        super(cause);
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage = custommessage;
    }

}
