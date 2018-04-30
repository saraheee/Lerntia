package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;


public class Questionnaire {
    private String cmark;
    private String semester;
    private Long id;

    public Questionnaire() {}

    public Questionnaire(String cmark, String semester, Long id) {
        this.cmark = cmark;
        this.semester = semester;
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
            "cmark='" + cmark + '\'' +
            ", semester='" + semester + '\'' +
            ", id=" + id +
            '}';
    }
}
