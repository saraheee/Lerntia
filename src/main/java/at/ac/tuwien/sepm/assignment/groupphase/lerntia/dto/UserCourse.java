package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class UserCourse {
    private String matriculationNumber;
    private String cmark;
    private String semester;

    public UserCourse() {}

    public UserCourse(String matriculationNumber, String cmark, String semester) {
        this.matriculationNumber = matriculationNumber;
        this.cmark = cmark;
        this.semester = semester;
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

    @Override
    public String toString() {
        return "UserCourse{" +
            "matriculationNumber='" + matriculationNumber + '\'' +
            ", cmark='" + cmark + '\'' +
            ", semester='" + semester + '\'' +
            '}';
    }
}
