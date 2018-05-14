package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto;

public class LearningQuestionnaire extends Questionnaire {
    private String name;

    public LearningQuestionnaire() {}

    public LearningQuestionnaire(Long courseId, Long id, Boolean isDeleted, String name) {
        super(courseId, id, isDeleted);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExamQuestionnaire{" +
            "courseId='" + super.getCourseID() + '\'' +
            ", id=" + super.getId()+
            ", name=" + name +
            "isDeleted=" + super.getDeleted() +
            '}';
    }
}
