package at.ac.tuwien.sepm.assignment.groupphase.util;


public enum ButtonText {
    YES("Ja"),
    NO("Nein"),
    CONTINUE("Weiter"),
    CANCEL("Abbrechen"),
    SHOW("Anzeigen"),
    ALLQUESTIONS("Alle Fragen"),
    WRONGQUESTIONS("Falsche Fragen"),
    ALGORITHMOFF("Algorithmus AUS"),
    ALGORITHMON("Algorithmus EIN");

    private String name;

    ButtonText(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}