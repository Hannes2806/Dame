package alleDateien.mvc;

import alleDateien.*;
import alleDateien.mvc.data.SpielSpeichern;
import alleDateien.mvc.gui.DameZeichnen;
import alleDateien.mvc.gui.DebugModus;
import alleDateien.mvc.gui.SpFigZeichnen;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.lang.System.exit;

public class SpielGUI extends Main {

    private String modus;
    private int[][] pgn = new int[8][8];
    private Spielstein[][] steinpgn = new Spielstein[8][8];

    private JButton spielfeldButtonListener;
    private static JFrame fenster;

    private static JPanel[][] feld;
    private int option;
    protected int[][] afterDebugPGN;
    private JPanel checkerboard;
    private String zug_grid = "Weiß";
    private JMenuBar menueLeiste;

    private SpielLogik logik;
    private DebugModus debug;
    private SpielSpeichern speichern;

    public SpielGUI() {
        feld = new JPanel[8][8];
    }

    public void setModus(String modus) {
        this.modus = modus;
    }

    public void starteGUI() {
        fenster = Startbildschirm.fenster;

        logik = new SpielLogik();
        debug = new DebugModus();
        speichern = new SpielSpeichern();

        this.afterDebugPGN = new int[8][8];
        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        setMenueBar();
        setzeSpielfeldGridLayout();

        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal
                setSpielfeldInhalte(i, j);
            }
        }
        setzeDebugMenuepunkt();
    }

    public void setSpielfeldInhalte(int i, int j) {
        JPanel panel = new JPanel();

        spielfeldButtonListener = new JButton("");
        setSpielfeldButtonLogik(i,j);

        panel.add(spielfeldButtonListener);

        feld[i][j] = panel;
        feld[i][j].setLayout(new GridLayout());

        feld[i][j].setOpaque(true);
        faerbeHintergrund(i, j);

        checkerboard.add(feld[i][j]); // Spielfeld in die Mitte des Bildschirms setzen

        // Button unsichtbar machen
        spielfeldButtonListener.setBorderPainted(false);
        spielfeldButtonListener.setContentAreaFilled(false);
        spielfeldButtonListener.setFocusPainted(false);
        spielfeldButtonListener.setOpaque(false);

        setSpielfigur(i, j);
    }

    private static void faerbeHintergrund(int y, int x) {
        feld[y][x].setBackground(Color.GRAY);
        if (hintergrundIstDunkel(y, x)) { // jedes zweite Feld färben
            feld[y][x].setBackground(Color.DARK_GRAY);
        }
    }

    private static boolean hintergrundIstDunkel(int y, int x) {
        return ((y+x) % 2 == 1);
    }

    public void setzeSpielfeldGridLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 0);

        JPanel zugPanel = new JPanel();
        JLabel zugLabel = new JLabel("Zug von: " + zug_grid);
        zugLabel.setForeground(Color.WHITE);
        zugLabel.setFont(new Font("Arial", Font.BOLD, 20));
        zugPanel.add(zugLabel);
        zugPanel.setBackground(Color.DARK_GRAY);

        // Panel von aktuellem Spieler
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.01;
        fenster.add(zugPanel, gbc);

        // Panel fuer labels (A-H)
        JPanel buchstabenPanel = new JPanel(new GridLayout(1, 8));
        buchstabenPanel.setBackground(Color.DARK_GRAY);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 0.05;
        fenster.add(buchstabenPanel, gbc);

        // Panel fuer labels (1-8)
        JPanel nummernPanel = new JPanel(new GridLayout(8, 1));
        nummernPanel.setBackground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(nummernPanel, gbc);

        setzeSpielfeldMarkierungen(buchstabenPanel, nummernPanel);
        erzeugeSpielfeldGUI(gbc);
    }

    public void setzeSpielfeldMarkierungen(JPanel buchstabenPanel, JPanel nummernPanel) {
        for (int iterator = 0; iterator < 8; iterator++) {
            JLabel letterLabel = new JLabel(String.valueOf((char) ('a' + iterator)), SwingConstants.CENTER);
            letterLabel.setForeground(Color.WHITE);
            letterLabel.setFont(new Font("Arial", Font.BOLD, 20));
            buchstabenPanel.add(letterLabel);
        }
        for (int num=8; num>0; num--) {
            JLabel nummernLabel = new JLabel(String.valueOf(num), SwingConstants.CENTER);
            nummernLabel.setForeground(Color.WHITE);
            nummernLabel.setFont(new Font("Arial", Font.BOLD, 20));
            nummernLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Abstand Rand
            nummernPanel.add(nummernLabel);
        }
    }

    public void erzeugeSpielfeldGUI(GridBagConstraints gbc) {
        // Spielfeld
        checkerboard = new JPanel();
        checkerboard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        checkerboard.setLayout(new GridLayout(8, 8));
        checkerboard.setBackground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        fenster.add(checkerboard, gbc);

        // Raender erstellen
        JPanel linksRand = new JPanel();
        linksRand.setBackground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(linksRand, gbc);

        JPanel rechtsRand = new JPanel();
        rechtsRand.setBackground(Color.DARK_GRAY);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(rechtsRand, gbc);
    }

    public void setSpielfeldButtonLogik(int i, int j) {
        if (modus.equals("spiel")) {
            figurBewegenOderSchlagen(i,j);
        } else {
            spielfeldButtonListener.addActionListener(e -> aktualisierePGN_debug(i, j));
        }
    }

    public void figurBewegenOderSchlagen(int i, int j) {
        if (pgn[i][j]==0) {
            spielfeldButtonListener.addActionListener(e -> schlageFigur(i, j));
        } else {
            spielfeldButtonListener.addActionListener(e -> logik.aktiviereSpielstein(i, j, steinpgn, pgn));
        }
    }

    public void setSpielfigur(int i, int j) {
        switch (pgn[i][j]) {
            case -1: // Bauer schwarz
                SpFigZeichnen figur1 = new SpFigZeichnen("black");
                spielfeldButtonListener.add(figur1);
                checkerboard.add(feld[i][j]);
                break;

            case -2: // Dame schwarz
                DameZeichnen figur3 = new DameZeichnen("black");
                spielfeldButtonListener.add(figur3);
                checkerboard.add(feld[i][j]);
                break;

            case 1: // Bauer weiß
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                spielfeldButtonListener.add(figur2);
                checkerboard.add(feld[i][j]);
                break;

            case 2: // Dame weiß
                DameZeichnen figur4 = new DameZeichnen("white");
                spielfeldButtonListener.add(figur4);
                checkerboard.add(feld[i][j]);
                break;

            default:
        }
    }

    public void setzeDebugMenuepunkt() {
        // wenn Benutzer Debug-Modus angeklickt hat, neuen Menüpunkt erstellen
        if (modus.equals("debug")) {
            JMenu debug = new JMenu("Debug");
            JMenuItem wBauer = new JMenuItem("Spielfigur - Weiß");
            JMenuItem bBauer = new JMenuItem("Spielfigur - Schwarz");
            JMenuItem wDame = new JMenuItem("Dame - Weiß");
            JMenuItem bDame = new JMenuItem("Dame - Schwarz");
            JMenuItem loeschen = new JMenuItem("Figur löschen");
            JMenuItem spielStarten = new JMenuItem("Spiel starten");

            wDame.addActionListener(e -> debugSetzeSpielfigur(2));
            wBauer.addActionListener(e -> debugSetzeSpielfigur(1));
            bDame.addActionListener(e -> debugSetzeSpielfigur(-2));
            bBauer.addActionListener(e -> debugSetzeSpielfigur(-1));
            loeschen.addActionListener(e -> debugSetzeSpielfigur(0));
            spielStarten.addActionListener(e -> debugStarten());

            wBauer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            bBauer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            wDame.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            bDame.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            loeschen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            wBauer.setBackground(Color.decode("#d4d4d4"));
            wDame.setBackground(Color.decode("#d4d4d4"));
            bBauer.setBackground(Color.decode("#949494"));
            bDame.setBackground(Color.decode("#949494"));
            loeschen.setBackground(Color.decode("#FF7074"));
            spielStarten.setBackground(Color.decode("9498256"));

            debug.add(wDame);
            debug.add(wBauer);
            debug.add(bDame);
            debug.add(bBauer);
            debug.add(loeschen);
            debug.add(spielStarten);
            menueLeiste.add(debug);
        }
    }

    public void setMenueBar() {
        // Menüleiste
        menueLeiste = new JMenuBar();
        JMenu dateiMenue = new JMenu("Datei");
        JMenuItem speichern_menuItem = new JMenuItem("Speichern");
        speichern_menuItem.addActionListener(e -> spielSpeichern());
        JMenuItem startseite_menuItem = new JMenuItem("Startseite");
        startseite_menuItem.addActionListener(e -> setzeStartseite());
        JMenuItem beenden_menuItem = new JMenuItem("Beenden");
        beenden_menuItem.addActionListener(e -> spielBeenden());

        speichern_menuItem.setBackground(Color.decode("#92C7CF"));
        startseite_menuItem.setBackground(Color.decode("#AAD7D9"));
        beenden_menuItem.setBackground(Color.decode("#91C8E4"));

        dateiMenue.add(speichern_menuItem);
        dateiMenue.add(startseite_menuItem);
        dateiMenue.add(beenden_menuItem);
        menueLeiste.add(dateiMenue);
        fenster.setJMenuBar(menueLeiste);
    }

    public void debugStarten() {
        // alles neu zeichnen und spiellogik starten
        this.modus = "spiel";
        fenster.setJMenuBar(null);
        setMenueBar();
        fenster.getContentPane().removeAll();
        setPGN(afterDebugPGN);
        setSpielfeld();
        fenster.repaint();
    }

    public void schlageFigur(int i, int j) {
        int[][] setzePGN = logik.schlageOderBewege(i, j);
        if (setzePGN != null) {
            this.zug_grid = logik.getWerIstDran();
            pgn = setzePGN;
            aktualisiereSteinPGN(pgn);
            fenster.getContentPane().removeAll();
            fenster.setJMenuBar(null);
            setMenueBar();
            setSpielfeld();
            logik.spielEnde();
            fenster.repaint();
        }
    }

    private void aktualisiereSteinPGN(int[][] pgn) {
        SpielData.erstelleSteinpgn(pgn);
        this.steinpgn = SpielData.getSteinpgn();
    }

    public void aktualisierePGN_debug(int y, int x) {
        if (hintergrundIstDunkel(y, x)) {
            // live anzeigen der neuen Steine
            pruefeBauerZuDame(y, x);
            fenster.getContentPane().removeAll();
            fenster.setJMenuBar(null);
            setMenueBar();
            setSpielfeld();
            fenster.repaint();
        }
    }

    public void pruefeBauerZuDame(int y, int x) {
        if (y==0 && debug.getDebugFigur()==1) {
            afterDebugPGN[y][x] = 2;
            pgn[y][x] = 2;
        } else if (y==7 && debug.getDebugFigur()==-1) {
            afterDebugPGN[y][x] = -2;
            pgn[y][x] = -2;
        } else {
            afterDebugPGN[y][x] = debug.getDebugFigur();
            pgn[y][x] = debug.getDebugFigur();
        }
    }

    public void setStandardPGN() {
        this.pgn = SpielData.getStandardpgn();
        this.steinpgn = SpielData.getSteinpgn();
    }

    public void setGespeichertePGN() {
        this.pgn = speichern.getGeladenePGN();
        this.steinpgn = SpielData.steinpgn;
        SpielData.setAktuellepgn(this.pgn);
        SpielData.erstelleSteinpgn(this.pgn);
    }

    // für Debug
    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
        this.steinpgn = SpielData.steinpgn;
        SpielData.setAktuellepgn(this.pgn);
        SpielData.erstelleSteinpgn(this.pgn);
    }

    public void infoBox() {
        Object[] optionen = {"Fortfahren", "Abbrechen"};

        option = JOptionPane.showOptionDialog(null, "Das Spiel wird nicht gespeichert. Fortfahren?", "Spielspeicherung",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);
    }

    public void setzeStartseite() {
        if (spielIstGespeichert()) { // wenn PGN's ungleich
            infoBox();
            if (option == JOptionPane.YES_OPTION) { // abbrechen
                GUIzuStartseite();
            }
        } else {
            GUIzuStartseite();
        }
    }

    public boolean spielIstGespeichert() {
        return !Arrays.deepEquals( // wenn gleich: true; else: false
            speichern.getGeladenePGN(),
            this.pgn
        );
    }

    public void spielBeenden() {
        if (spielIstGespeichert()) { // wenn PGN's ungleich
            infoBox(); // Warnung wenn spiel nicht gespeichert wurde
            if (option == JOptionPane.YES_OPTION) {
                exit(0);
            }
        } else {
            exit(0);
        }
    }

    public static void GUIzuStartseite() {
        fenster.getContentPane().removeAll();
        fenster.setJMenuBar(null);
        fenster.repaint();
        Startbildschirm sb = new Startbildschirm();
        sb.setStartbildschirm();
    }

    public void spielSpeichern() {
        speichern.setPGN(pgn);
        speichern.speichereSpiel();
    }

    public void debugSetzeSpielfigur(int figur) {
        debug.setFigur(figur);
    }

    public static void markiereZieleFarbig(java.util.List<int[]> bewegungsziele) {
        for ( int i=0; i<8; i++ ) { // Felder standard färben (schwarz-weiss)
            for ( int j=0; j<8; j++ ) {
                faerbeHintergrund(i, j);
            }
        }

        for (int[] bewegungsziel : bewegungsziele) { // Mögliche Züge färben
            feld[bewegungsziel[0]][bewegungsziel[1]].setBackground(Color.ORANGE);
        }
    }

    public static void spielGewonnenEnde(String farbe) {
        Object[] optionen = {"Zur Startseite", "Zum Spiel zurückkehren"};

        // Anzeigen des Dialogs
        int option = JOptionPane.showOptionDialog(
            null,
            farbe + " hat das Spiel gewonnen!",
            "Spiel Gewonnen",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            optionen,
            optionen[0]
        );

        // Überprüfen, ob der Button geklickt wurde
        if (option == 0) {
            GUIzuStartseite();
        }
    }
}
