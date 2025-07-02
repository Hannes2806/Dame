package alleDateien;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static alleDateien.mvc.SpielData.aktuellepgn;

public class Bauer extends Spielstein {
    private final int pos_x;
    private final int pos_y;

    private final int farbe;
    private int laufrichtung;

    private Boolean zugzwang;
    private Boolean bewegungsfaehig = true;

    private final List<int[]> bewegungsziele = new ArrayList<>();

    private final HashMap<List<Integer>, List<int[]>> bewegungszieleMitPfad = new HashMap<>();
    private final List<List<int[]>> bewegungspfadeGehen = new ArrayList<>();
    private final List<List<int[]>> bewegungspfadeSchlagen = new ArrayList<>();

    public Bauer(int pos_x, int pos_y, int farbe) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.farbe = farbe;
        switch (this.farbe) {
            case 1:
                this.laufrichtung = -1; //weiß
                break;
            case -1:
                this.laufrichtung = 1; //schwarz
        }
        setBewegungsziele();
    }

    public void setBewegungsziele() {
        this.zugzwang = false;
        setBewegungspfade();

        // Falls Figur schlagen kann werden nur ziele mit Schlag angezeigt
        if (this.bewegungspfadeSchlagen.isEmpty()) {
            for (List<int[]> pfad : this.bewegungspfadeGehen) {
                findeZielposition(pfad);
            }
        } else {
            for (List<int[]> pfad : this.bewegungspfadeSchlagen) {
                findeZielposition(pfad);
            }
        }

        this.bewegungsfaehig = !this.bewegungsziele.isEmpty();
    }

    private void findeZielposition(List<int[]> pfad) {
        int[] ziel = {this.pos_x, this.pos_y};
        for (int[] schritt: pfad) {
            ziel[0] += schritt[0];
            ziel[1] += schritt[1];
        }
        this.bewegungsziele.add(ziel);
        List<Integer> keyZiel = konvertiereArrayZuListe(ziel); // da HashMap mit int[] als key nicht umgehen kann
        this.bewegungszieleMitPfad.put(keyZiel, pfad);
    }

    private static List<Integer> konvertiereArrayZuListe(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        return list;
    }

    private void setBewegungspfade() {
        pruefeEinenSchritt(pos_x, pos_y); //Methode ruft sich selbst neu auf für jeden Schritt und speichert alle Pfade ab
    }

    // Methoden um Liste "bewegungspfade" zu bestimmen. Methoden rufen sich selbst neu auf und durchlaufen jeden Pfad
    private void pruefeEinenSchritt(int x, int y) {
        int[] sprung = {-1, 1}; // y-Richtung, y-Richtung
        pruefeBewegungsfaehigkeiten(sprung, x, y);
        verfolgeBewegungsfaehigkeiten(sprung, x, y);
    }

    private void pruefeEinenSchritt(int x, int y, List<int[]> pfadAlt) {
        int[] sprung = {-1, 1}; // y-Richtung, y-Richtung
        pruefeBewegungsfaehigkeiten(sprung, x, y);
        verfolgeBewegungsfaehigkeiten(sprung, x, y, pfadAlt);
    }

    //Step 1
    private void pruefeBewegungsfaehigkeiten(int[] sprungweiten,int x, int y) {
        for (int i = 0; i < 2; i++) {
            pruefeBewegungsfaehigkeit(sprungweiten, i, x, y);
        }
    }

    // Step 2
    private void verfolgeBewegungsfaehigkeiten(int[] sprungweiten, int x, int y) {
        for (int i = 0; i < 2; i++) {
            if (abs(sprungweiten[i]) == 1) {
                List<int[]> pfad = new ArrayList<>();
                int[] sprungPosition = {abs(sprungweiten[i]) * this.laufrichtung, sprungweiten[i]};
                pfad.add(sprungPosition);
                this.bewegungspfadeGehen.add(pfad);
            }
            else if (abs(sprungweiten[i]) == 2) {
                List<int[]> pfad = new ArrayList<>();
                int[] sprungPosition = {abs(sprungweiten[i]) * this.laufrichtung, sprungweiten[i]};
                pfad.add(sprungPosition);
                int neu_x = x + 2 * this.laufrichtung;
                int neu_y = y + sprungweiten[i];
                pruefeEinenSchritt(neu_x, neu_y, pfad);
            }
        }
    }

    private void verfolgeBewegungsfaehigkeiten(int[] sprungweiten, int x, int y, List<int[]> pfadAlt) {
        if (abs(sprungweiten[0]) == 2 || abs(sprungweiten[1]) == 2) {
            for (int i = 0; i < 2; i++) {
                List<int[]> pfadAltVortsetzung = new ArrayList<>(pfadAlt);
                testeVerzweigung(sprungweiten, x, y, pfadAltVortsetzung, i);
            }
        }
        else {
            this.bewegungspfadeSchlagen.add(pfadAlt);
        }
    }

    private void pruefeBewegungsfaehigkeit(int[] sprungweiten, int i, int x, int y) {
        try {
            switch (aktuellepgn[x + this.laufrichtung][y + sprungweiten[i]] * this.laufrichtung) {
                case 2:
                case 1:
                    sprungweiten[i] *= 2;
                    schlagenOderNichtSchlagen(sprungweiten, i, x, y);
                    break;
                case 0:
                    break;
                case -1:
                case -2:
                    sprungweiten[i] = 0; // Weg ist von eigenem Spielstein versperrt
                    break;
                default:
                    System.out.println("Unbekannter Spielstein");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            sprungweiten[i] = 0;
        }
    }

    private void testeVerzweigung(int[] sprungweiten, int x, int y, List<int[]> pfadAlt, int i) {
        if (abs(sprungweiten[i]) == 2) {
            int[] sprungPosition = {abs(sprungweiten[i]) * this.laufrichtung, sprungweiten[i]};
            pfadAlt.add(sprungPosition);
            int neu_x = x + 2 * this.laufrichtung;
            int neu_y = y + sprungweiten[i];
            pruefeEinenSchritt(neu_x, neu_y, pfadAlt);
        }
    }

    private void schlagenOderNichtSchlagen(int[] sprungweiten, int i, int x, int y) {
        if (aktuellepgn[x + 2 * this.laufrichtung][y + sprungweiten[i]] != 0) {
            sprungweiten[i] = 0;
        }
        else {
            this.zugzwang = true;
        }
    }

    @Override
    public Boolean getZugzwang() {
        return zugzwang;
    }

    @Override
    public List<int[]> getBewegungsziele() {
        return bewegungsziele;
    }

    @Override
    public int getFarbe() {
        return farbe;
    }

    @Override
    public HashMap<List<Integer>, List<int[]>> getBewegungszieleMitPfad() {
        return bewegungszieleMitPfad;
    }

    @Override
    public List<List<int[]>> getBewegungspfadeSchlagen() {
        return bewegungspfadeSchlagen;
    }

    @Override
    public Boolean getBewegungsfaehig() {
        return bewegungsfaehig;
    }
}
