package alleDateien.mvc;

import alleDateien.Spielstein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class SpielLogik {

    private int[][] pgn;
    private int y_arr;
    private int x_arr;
    private Spielstein[][] steinpgn;

    private Spielstein aktuellerStein;

    private int newY;
    private int newX;
    private int[] zielpunkt;
    private List<Integer> zielpunktListe;

    private int zugFarbe = 1; // 1 -> weiß ; -1 -> schwarz

    public SpielLogik() {
    }

    public void aktiviereSpielstein(int i, int j, Spielstein[][] steinpgn, int[][] pgn) {
        setAttributes(i, j, pgn, steinpgn);
        List<int[]> bewegungsziele = steinpgn[i][j].getBewegungsziele();
        markierePotentielleFelder(bewegungsziele);
    }

    public void setAttributes(int y, int x, int[][] pgn, Spielstein[][] steinpgn) {
        this.pgn = pgn;
        this.steinpgn = steinpgn;
        this.y_arr = y;
        this.x_arr = x;
        this.aktuellerStein = steinpgn[y][x];
    }

    public int[][] schlageOderBewege(int newY, int newX) { // wenn leeres Feld angeklickt wird
        this.newY = newY;
        this.newX = newX;
        this.zielpunkt = new int[]{newY, newX}; //Hier schauen ob ersetzbar durch zielpunktListe
        this.zielpunktListe = new ArrayList<>(Arrays.asList(newY, newX)); // so gesehen das selbe wie 'zielpunkt'

        if (aktuellerStein!=null && zugIstMoeglich() && richtigeFarbe() && testeZugzwang()) {
            zieheFigur();
            return pgn;
        }
        return null;
    }

    private boolean zugIstMoeglich() {
        boolean moeglich = false;
        for (int[] moeglichesZiel : this.aktuellerStein.getBewegungsziele()) {
            if (Arrays.equals(moeglichesZiel, this.zielpunkt)) {
                moeglich = true;
                break;
            }
        }
        return moeglich;
    }

    private boolean richtigeFarbe() {
        return this.zugFarbe==this.aktuellerStein.getFarbe();
    }

    private boolean testeZugzwang() {
        List<Spielstein> steineUnterZugzwang = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                steineUnterZugzwang = fuelleListeZugzwang(i, j, steineUnterZugzwang);
            }
        }
        if (!steineUnterZugzwang.isEmpty()) {
            return steineUnterZugzwang.contains(this.aktuellerStein);
        }
        return true;
    }

    private List<Spielstein> fuelleListeZugzwang(int i, int j, List<Spielstein> steineUnterZugzwang) {
        if (steinpgn[i][j].getFarbe()==this.zugFarbe && steinpgn[i][j].getZugzwang()) {
            steineUnterZugzwang.add(steinpgn[i][j]);
        }
        return steineUnterZugzwang;
    }

    private void zieheFigur() {
        //Pfad abgehen und alle Figuren auf dem Weg werden entfernt
        List<int[]> bewegungspfad = this.aktuellerStein.getBewegungszieleMitPfad().get(this.zielpunktListe);
        List<int[]> zuEntfernendeSteine = new ArrayList<>();
        int posX = this.x_arr;
        int posY = this.y_arr;

        if (bewegungspfadIstEnthalten(bewegungspfad, this.aktuellerStein.getBewegungspfadeSchlagen())) { //nur fürs Schlagen Figuren entfernen //contains() reicht nicht da ungleiche Objektreferenzen
            for (int[] schritt: bewegungspfad) {
                posX += schritt[1] - schritt[1] / abs(schritt[1]);
                posY += schritt[0] - schritt[0] / abs(schritt[0]);
                int[] entfernendeFigur = {posY, posX};
                zuEntfernendeSteine.add(entfernendeFigur);
                posX += schritt[1] / abs(schritt[1]);
                posY += schritt[0] / abs(schritt[0]);
            }
        }
        aktualisierePGN(zuEntfernendeSteine);
    }

    private void aktualisierePGN(List<int[]> zuEntfernendeSteine) {
        for (int[] stein: zuEntfernendeSteine) {
            this.pgn[stein[0]][stein[1]] = 0;
        }
        switch (newY) {
            case 0:
            case 7:
                this.pgn[newY][newX] = 2 * pgn[y_arr][x_arr] / abs(pgn[y_arr][x_arr]);
                break;
            default:
                this.pgn[newY][newX] = pgn[y_arr][x_arr];
                break;
        }
        this.pgn[y_arr][x_arr] = 0;

        this.zugFarbe = aendereAktuellenSpieler();
    }

    private boolean bewegungspfadIstEnthalten(List<int[]> bewegungspfad, List<List<int[]>> bewegungspfadeSchlagen) {
        boolean enthalten = false;
        for (List<int[]> pfad: bewegungspfadeSchlagen) {
            if (listenSindGleich(pfad, bewegungspfad)) {
                enthalten = true;
                break;
            }
        }
        return enthalten;
    }

    public static boolean listenSindGleich(List<int[]> list1, List<int[]> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!Arrays.equals(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int aendereAktuellenSpieler() {
        if (this.zugFarbe == 1) { // weiß hat gespielt, jetzt schwarz
            return -1;
        }
        return 1;
    }

    public String getWerIstDran() {
        if (this.zugFarbe == 1) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }

    public void markierePotentielleFelder(List<int[]> bewegungsziele) {
        if (weissUndRichtigeFigur() || schwarzUndRichtigeFigur()) {
            SpielGUI.markiereZieleFarbig(bewegungsziele);
        }
    }

    public boolean weissUndRichtigeFigur() {
        return getWerIstDran().equals("Weiß") && pgn[y_arr][x_arr]>0;
    }

    public boolean schwarzUndRichtigeFigur() {
        return getWerIstDran().equals("Schwarz") && pgn[y_arr][x_arr]<0;
    }

    public void spielEnde() {
        String farbeOhneFigur = keineFigurUebrig();
        String farbeBewegungsunfaehig = figurenNichtBewegungsfaehig();

        if (farbeOhneFigur.equals("weiß") || farbeBewegungsunfaehig.equals("weiß")) {
            SpielGUI.spielGewonnenEnde("schwarz");
        }
        else if (farbeOhneFigur.equals("schwarz") || farbeBewegungsunfaehig.equals("schwarz")) {
            SpielGUI.spielGewonnenEnde("weiß");
        }
    }

    private String figurenNichtBewegungsfaehig() {
        int[] figurenBewegungsfaehig = {0, 0}; //[0] = weiß, [1] = schwarz
        String farbe = "keine";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                zaehleBewegungsfaehig(i, j, figurenBewegungsfaehig);
                steinpgn[i][j].getBewegungsfaehig();
            }
        }
        if (figurenBewegungsfaehig[0] == 0) {
            farbe = "weiß";
        }
        else if (figurenBewegungsfaehig[1] == 0) {
            farbe = "schwarz";
        }
        return farbe;
    }

    private void zaehleBewegungsfaehig(int i, int j, int[] figurenBewegungsfaehig) {
        int farbe = this.steinpgn[i][j].getFarbe();
        boolean bewegungsfaehig = this.steinpgn[i][j].getBewegungsfaehig();
        if (farbe == 1 && bewegungsfaehig) {
            figurenBewegungsfaehig[0]++;
        }
        else if (farbe == -1 && bewegungsfaehig) {
            figurenBewegungsfaehig[1]++;
        }
    }

    private String keineFigurUebrig() {
        int[] figurenAnzahl = {0, 0}; //[0] = weiß, [1] = schwarz
        String farbe = "keine";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                zaehleFiguren(i, j, figurenAnzahl);
            }
        }

        if (figurenAnzahl[0] == 0) {
            farbe = "weiß";
        }
        else if (figurenAnzahl[1] == 0) {
            farbe = "schwarz";
        }
        return farbe;
    }

    private void zaehleFiguren(int i, int j, int[] figurenAnzahl) {
        switch (this.pgn[i][j]) {
            case -2:
            case -1:
                figurenAnzahl[1]++;
                break;
            case 1:
            case 2:
                figurenAnzahl[0]++;
                break;
            default:
                break;
        }
    }

    public int[][] getPgn() {
        return this.pgn;
    }
}
