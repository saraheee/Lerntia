package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Course {
    private String mark;
    private String semester;
    private String name;
    private Boolean isDeleted;

    public Course() {}

    public Course(String mark, String semester, String name, Boolean isDeleted) {
        this.mark = mark;
        this.semester = semester;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Course{" +
            "mark='" + mark + '\'' +
            ", semester='" + semester + '\'' +
            ", name='" + name + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
