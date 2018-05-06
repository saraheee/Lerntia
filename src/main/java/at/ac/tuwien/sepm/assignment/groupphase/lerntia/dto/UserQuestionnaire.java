package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class UserQuestionnaire {
    private String matriculationNumber;
    private Long qid;
    private Boolean isDeleted;

    public UserQuestionnaire() {}

    public UserQuestionnaire(String matriculationNumber, Long qid, Boolean isDeleted) {
        this.matriculationNumber = matriculationNumber;
        this.qid = qid;
        this.isDeleted = isDeleted;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "UserQuestionnaire{" +
            "matriculationNumber='" + matriculationNumber + '\'' +
            ", qid=" + qid +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
