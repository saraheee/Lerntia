package at.ac.tuwien.sepm.assignment.groupphase.exception;

public class PersistenceException extends Exception {

    private String custommessage;

    public PersistenceException(String message) {
        super(message);
        this.custommessage = message;
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage = custommessage;
    }

}
