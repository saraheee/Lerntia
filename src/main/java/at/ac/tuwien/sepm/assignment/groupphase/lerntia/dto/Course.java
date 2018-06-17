package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class Course {
    private Long id; // distinct key
    private String mark; // course number (eg. 123.456)
    private String semester; // WS or SS and year (eg. WS2018 or SS2018)
    private String name; // name of the course
    private Boolean isDeleted; // if course is deleted

    public Course() {
        this.isDeleted =false;
    }

    public Course(String mark, String semester, String name, Boolean isDeleted) {
        this.mark = mark;
        this.semester = semester;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
