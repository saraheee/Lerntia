package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class UserCourse {
    private String matriculationNumber;
    private String cmark;
    private String semester;
    private Boolean isDeleted;
    private long courseID;

    public UserCourse() {}

    public UserCourse(String matriculationNumber, String cmark, String semester, Boolean isDeleted) {
        this.matriculationNumber = matriculationNumber;
        this.cmark = cmark;
        this.semester = semester;
        this.isDeleted = isDeleted;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public String getCmark() {
        return cmark;
    }

    public void setCmark(String cmark) {
        this.cmark = cmark;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "UserCourse{" +
            "matriculationNumber='" + matriculationNumber + '\'' +
            ", cmark='" + cmark + '\'' +
            ", semester='" + semester + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
