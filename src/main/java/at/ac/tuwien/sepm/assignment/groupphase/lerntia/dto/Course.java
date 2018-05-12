package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Course {
    private String mark;
    private String semester;
    private Boolean isDeleted;

    public Course() {}

    public Course(String mark, String semester, Boolean isDeleted) {
        this.mark = mark;
        this.semester = semester;
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
            ", isDeleted=" + isDeleted +
            '}';
    }
}
