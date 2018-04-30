package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class User {
    private String name;
    private String matriculationNumber;
    private String studyProgramme;

    public User() {}

    public User(String name, String matriculationNumber, String studyProgramme) {
        this.name = name;
        this.matriculationNumber = matriculationNumber;
        this.studyProgramme = studyProgramme;
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

    @Override
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", matriculationNumber='" + matriculationNumber + '\'' +
            ", studyProgramme='" + studyProgramme + '\'' +
            '}';
    }
}
