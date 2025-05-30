package at.ac.tuwien.lerntia.lerntia.dto;


public abstract class Questionnaire {
    private Long courseID; // foreign key of course
    private Long id; // distinct key
    private String name; // name of questionnaire
    private Boolean selected; // if currently selected
    private Boolean isDeleted; // if it is deleted

    Questionnaire() {}

    Questionnaire(Long courseID, Long id, String name, Boolean selected, Boolean isDeleted) {
        this.courseID = courseID;
        this.id = id;
        this.name = name;
        this.selected = selected;
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

    Boolean getDeleted() {
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

    Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
