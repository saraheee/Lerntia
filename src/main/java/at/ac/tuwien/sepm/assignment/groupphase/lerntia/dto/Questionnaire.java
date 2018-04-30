package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

import java.util.Objects;

public class Questionnaire {
    private String cid;
    private String semester;
    private Long id;

    public Questionnaire() {}

    public Questionnaire(String cid, String semester, Long id) {
        this.cid = cid;
        this.semester = semester;
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
            "cid='" + cid + '\'' +
            ", semester='" + semester + '\'' +
            ", id=" + id +
            '}';
    }
}
