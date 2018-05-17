package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;


public abstract class Questionnaire {
    private Long courseID;
    private Long id;
    private String name;
    private Boolean isDeleted;

    public Questionnaire() {}

    public Questionnaire(Long courseID, Long id, String name, Boolean isDeleted) {
        this.courseID = courseID;
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
