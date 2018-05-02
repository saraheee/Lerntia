package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class UserQuestionnaire {
    private String matriculationNumber;
    private Long qid;

    public UserQuestionnaire() {}

    public UserQuestionnaire(String matriculationNumber, Long qid) {
        this.matriculationNumber = matriculationNumber;
        this.qid = qid;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    @Override
    public String toString() {
        return "UserQuestionnaire{" +
            "matriculationNumber='" + matriculationNumber + '\'' +
            ", qid=" + qid +
            '}';
    }
}
