package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Course {
    private String id;
    private String semester;

    public Course() {}

    public Course(String id, String semester) {
        this.id = id;
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
            "id='" + id + '\'' +
            ", semester='" + semester + '\'' +
            '}';
    }
}
