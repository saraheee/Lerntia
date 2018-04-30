package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Course {
    private String mark;
    private String semester;

    public Course() {}

    public Course(String mark, String semester) {
        this.mark = mark;
        this.semester = semester;
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

    @Override
    public String toString() {
        return "Course{" +
            "mark='" + mark + '\'' +
            ", semester='" + semester + '\'' +
            '}';
    }
}
