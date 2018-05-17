package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class LearningQuestionnaire extends Questionnaire {

    private boolean selected;

    public LearningQuestionnaire() {}

    public LearningQuestionnaire(Long courseID, Long id, Boolean isDeleted, String name, boolean selected) {
        super(courseID, id, name, isDeleted);
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseID='" + super.getCourseID() + '\'' +
            ", id=" + super.getId()+
            ", name=" + super.getName() +
            ", selected=" + selected +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
