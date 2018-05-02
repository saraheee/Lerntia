package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;


public abstract class Questionnaire {
    private String cmark;
    private String semester;
    private Long id;
    private Boolean isDeleted;

    public Questionnaire() {}

    public Questionnaire(String cmark, String semester, Long id, Boolean isDeleted) {
        this.cmark = cmark;
        this.semester = semester;
        this.id = id;
        this.isDeleted = isDeleted;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
