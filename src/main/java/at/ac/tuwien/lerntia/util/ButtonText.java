package at.ac.tuwien.lerntia.util;


public enum ButtonText {
    YES("Ja"),
    NO("Nein"),
    CONTINUE("Weiter"),
    CANCEL("Abbrechen"),
    SHOW("Anzeigen"),
    ALLQUESTIONS("Alle Fragen"),
    WRONGQUESTIONS("Falsche Fragen"),
    ALGORITHMOFF("Alg. aus"),
    ALGORITHMON("Alg. ein");

    private final String name;

    ButtonText(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}