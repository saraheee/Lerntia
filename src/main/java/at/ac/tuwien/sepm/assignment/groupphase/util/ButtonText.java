package at.ac.tuwien.sepm.assignment.groupphase.util;


public enum ButtonText {
    YES("Ja"),
    NO("Nein"),
    CONTINUE("Weiter"),
    ALLQUESTIONS("Alle Fragen"),
    WRONGQUESTIONS("Falsche Fragen");

    private String name;

    ButtonText(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}