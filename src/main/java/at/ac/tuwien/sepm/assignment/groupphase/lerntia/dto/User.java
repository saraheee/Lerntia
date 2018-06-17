package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class User {
    private String name; // name of user
    private String matriculationNumber; // student id (e.g. 01234567)
    private String studyProgramme; // study number (e.g. E 033 123)
    private Boolean isDeleted; // if deleted

    public User() {
        this.isDeleted = false;
    }

    public User(String name, String matriculationNumber, String studyProgramme, Boolean isDeleted) {
        this.name = name;
        this.matriculationNumber = matriculationNumber;
        this.studyProgramme = studyProgramme;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public String getStudyProgramme() {
        return studyProgramme;
    }

    public void setStudyProgramme(String studyProgramme) {
        this.studyProgramme = studyProgramme;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", matriculationNumber='" + matriculationNumber + '\'' +
            ", studyProgramme='" + studyProgramme + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
