package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionLearnAlgorithm {
    private long ID;
    private Integer successvalue;
    private Integer failurevalue;
    private double points;

    public QuestionLearnAlgorithm() {
        this.setSuccessvalue(0);
        this.setFailurevalue(0);
        this.setPoints(100.0);

    }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Integer getSuccessvalue() {
        return successvalue;
    }

    public void setSuccessvalue(Integer successvalue) {
        this.successvalue = successvalue;
    }

    public Integer getFailurevalue() {
        return failurevalue;
    }

    public void setFailurevalue(Integer failurevalue) {
        this.failurevalue = failurevalue;
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
            ", successvalue=" + successvalue +
            ", failurevalue=" + failurevalue +
            ", points=" + points +
            '}';
    }
}