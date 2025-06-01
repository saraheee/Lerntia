# Lerntia

## Projektbeschreibung
Lerntia (abgeleitet von Lernziel) ist ein Lern- und Prüfungstool, das einem Studenten mit Beeinträchtigung einerseits die eigenständige
Vorbereitung auf seine Prüfungen ermöglichen soll, und andererseits auch bei der Abwicklung seiner Prüfungen eingesetzt
werden kann. Es richtet sich in erster Linie an den Studenten, der damit zu Übungszwecken Fragen beantworten kann.
Weiters können Tutor_innen ihm damit seine Fragen verwalten und das LVA-Team kann damit Prüfungen abwickeln.
Das Tool unterstützt Multiple-Choice Fragen mit bis zu fünf Antwortmöglichkeiten mit optional einem Bild und Feedbacktext pro Frage.

## Projektteam

Sarah El-Sherbiny, Jovan Jeromela, Stefan Huemer, Fabio Francescato, Andrea Kracker, Magdy El Sadany

Das Projekt wurde im Rahmen der LVA Software Engineering und Projektmanagement (QSE) im SS18 an der TU Wien unter der Betreuung
von Barbara Schuhmacher und Lukas Kathrein umgesetzt.

## Kurzanleitung

*   Das Programm kann mit dem Befehl\
    ```mvnw clean compile exec:java -X```\
    bereinigt, gebaut, und ausgeführt werden.


*   Das Programm kann mit dem Befehl\
    ```mvnw test```\
    getestet werden.


*   Das Programm kann mit dem Befehl\
    ```mvnw package```\
    als ausführbares jar-File packetiert werden.

Zuletzt getestet im **Juni 2025** mit folgenden Versionen:
* ```Maven 3.9.9```
* ```Java 10.0.2```

Benötigte Libraries mit diesem Setup befinden sich unter:
```Lerntia/libraries/.m2.zip```

## Tastenkürzel
* ```a``` ...... Frage und Antwortmöglichkeiten vorlesen
* ```1-5``` ... Antwort ```X``` vorlesen und auswählen, wobei ```X``` für ```1-5``` steht
* ```z``` ...... Bild vergrößern/verkleinern
* ```n``` ...... nächste Frage anzeigen
* ```v``` ...... vorherige Frage anzeigen
* ```c``` ...... im Lernmodus: gewählte Antworten überprüfen; im Prüfungsmodus: Prüfung abgeben
* ```g``` ...... Lernalgorithmus ein/aus (nur im Lernmodus)
* ```Strg + K```  Fragebogen auswählen (nur im Lernmodus)

Inhalte in runden Klammern ```()``` werden angezeigt, aber nicht vorgelesen und dienen dazu Fachbegriffe
oder Übersetzungen zu hinterlegen. Das Abgeben der Prüfung ist unwiderruflich und erzeugt ein Ergebnis-PDF, das per
E-Mail übermittelt wird.

