package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionLearnAlgorithm {
    private long ID; // distinct key
    private Integer successValue; // how often a question is answered correctly
    private Integer failureValue; // how often a question is answered wrong
    private double points; // calculated with values

    public QuestionLearnAlgorithm() {
        this.setSuccessValue(0);
        this.setFailureValue(0);
        this.setPoints(100.0);

    }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Integer getSuccessValue() {
        return successValue;
    }

    public void setSuccessValue(Integer successValue) {
        this.successValue = successValue;
    }

    public Integer getFailureValue() {
        return failureValue;
    }

    public void setFailureValue(Integer failureValue) {
        this.failureValue = failureValue;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "QuestionLearnAlgorithm{" +
            "ID=" + ID +
            ", successValue=" + successValue +
            ", failureValue=" + failureValue +
            ", points=" + points +
            '}';
    }
}