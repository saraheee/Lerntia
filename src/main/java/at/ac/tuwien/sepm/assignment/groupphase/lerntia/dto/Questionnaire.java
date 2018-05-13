package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;


public abstract class Questionnaire {
    private Long courseId;
    private Long id;
    private Boolean isDeleted;

    public Questionnaire() {}

    public Questionnaire(Long courseId, Long id, Boolean isDeleted) {
        this.courseId = courseId;
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public Long getCourseID() {
        return courseId;
    }

    public void setCourseID(Long courseID) {
        this.courseId = courseId;
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
