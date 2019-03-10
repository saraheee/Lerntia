package at.ac.tuwien.lerntia.lerntia.dto;

public class LearningQuestionnaire extends Questionnaire {

    public LearningQuestionnaire() {
    }

    public LearningQuestionnaire(Long courseID, Long id, Boolean isDeleted, String name, boolean selected) {
        super(courseID, id, name, selected, isDeleted);
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
            ", id=" + super.getId() +
            ", name=" + super.getName() +
            ", selected=" + super.getSelected() +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
