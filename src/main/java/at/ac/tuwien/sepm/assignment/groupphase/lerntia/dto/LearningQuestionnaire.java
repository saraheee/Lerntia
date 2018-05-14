package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class LearningQuestionnaire extends Questionnaire {
    private String name;
    private boolean selected;

    public LearningQuestionnaire() {}

    public LearningQuestionnaire(Long courseId, Long id, Boolean isDeleted, String name, boolean selected) {
        super(courseId, id, isDeleted);
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseId='" + super.getCourseID() + '\'' +
            ", id=" + super.getId()+
            ", name=" + name +
            ", selected=" + selected +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
