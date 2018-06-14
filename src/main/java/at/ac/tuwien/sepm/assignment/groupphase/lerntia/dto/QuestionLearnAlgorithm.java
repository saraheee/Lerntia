package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class QuestionLearnAlgorithm {
    private long ID; // distinc key
    private Integer successvalue; // how often a question is answered correctly
    private Integer failurevalue; // how often a questions is answered wrong
    private double points; // calculated with values

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