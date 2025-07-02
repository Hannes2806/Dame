Alle wichtigen Dateien: /src

Einfach runterladen und als neues Projekt erstellen

Git-push:
Falls keine 2FA eingestellt ist, könnt ihr euch per HTTP & Terminal verbinden und alles hochladen. Sonst müsstet ihr eine SSH Verbindung einrichten
(-> https://docs.github.com/en/authentication/connecting-to-github-with-ssh)

Falls erfolgt, funktioniert es bei mir mit den Befehlen:
1. im Terminal in das Verzeichnis der Dateien gehen
2. git init -> erstellt eine leere Repository für git push
3. git add . -> alle Dateien auswählen
4. git commit -m "dies ist ein Text" -> kurzen Text über updates schreiben
5. git push -u origin main ODER git push -> die Dateien hochladen

   Per 'git status' aktuellen Status einsehen, 'git branch' aktuelle Repository (?)

To-Dos:
- [ ] einfach ALLES nochmal überarbeiten -> sauberer und verständlicher gestalten
- [ ] auf Bugs untersuchen (und bestenfalls hier aufschreiben)
- [x] Spielfeld beschriftung hinzufügen (1-8 und a-h)
- [X] Spieler nur abwechselnd bewegen lassen (weiß, schwarz, weiß,...)
- [X] Prüfen ob Spiel zu ende ist
- [X] "Endscreen" - wenn jemand gewonnen hat
- [X] Spielspeicherung
- [X] Bauer können nach unten und oben platziert werden (in 'checkBauerBewegen()')
- [X] Damen können die eigene Farbe schlagen
- [X] Debug-Modus: wenn man einen Bauern auf ein Feld setzt in dem es zur Dame werden sollte und man das Spiel startet, bleibt es ein Bauer
- [X] Bug: falls ein Bauer übersprungen wird um zum Spielfeldrand zu kommen, wird man keine Dame sondern bleibt ein Bauer -> behoben durch anderen Aufbau der Spiellogik
- [X] Felder auf die man springen farbig hervorheben
- [X] Auf Windows Spielfeld schön machen
- [X] Mehrfachsprünge implementieren
- [ ] Spielspeicherung bugfix: wenn man spiel lädt und ändert wird nicht nochmal gefragt ob man speichern will
      - bei der Abfrage wird die SpielGUI pgn mit der SpielSpeichern pgn verglichen. Die SpielGUI pgn aktualisiert sich nicht...
- [X] (noch nicht überprüft, aber) was wenn noch kein spiel gespeichert wurde? -> leeres Feld wird angezeigt
- [X] neues Spiel, figuren bewegen, speichern und zur Startseite gehen. Wenn man auf neues spiel klickt, wird das gespeicherte Spiel geladen
      -> aber nicht, wenn man das Programm neu startet
      LÖSUNG: 'standardpgn' wird nicht als Attribut der Klasse SpielData initialisiert, sondern per Konstruktor
- [X] Mögliche Züge wird unabhängig des aktuellen Spielers für beide Steine angezeigt
...
