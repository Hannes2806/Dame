package alleDateien;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static alleDateien.mvc.SpielData.aktuellepgn;

public class Dame extends Spielstein {
    private final int pos_x;
    private final int pos_y;

    private final int farbe;

    private Boolean zugzwang;
    private Boolean bewegungsfaehig;

    private final List<int[]> bewegungsziele = new ArrayList<>();
    private final HashMap<List<Integer>, List<int[]>> bewegungszieleMitPfad = new HashMap<>();
    private final List<List<int[]>> bewegungspfadeGehen = new ArrayList<>();
    private final List<List<int[]>> bewegungspfadeSchlagen = new ArrayList<>();
    private final List<List<int[]>> bewegungspfadeMerkeGehen = new ArrayList<>();
    private List<List<int[]>> bewegungspfadeMerkeSchlagen = new ArrayList<>();
    private final List<int[]> bereitsGeschlageneFiguren = new ArrayList<>();


    public Dame(int pos_x, int pos_y, int farbe) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.farbe = farbe;
        setBewegungsziele(); //ermittle mögliche Zielkoordinaten "bewegungsziele", die Bewegungspfade durch eine Liste aller schritte für jeden Pfad, unf prüfe ob Zugzwang besteht oder bewegungsfähigkeit gegeben ist
    }

    private void setBewegungsziele() {
        this.zugzwang = false;
        setBewegungspfade();

        // Falls Figur schlagen kann, werden nur ziele mit Schlag angezeigt
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
        for (int[] schritt : pfad) {
            ziel[0] += schritt[0];
            ziel[1] += schritt[1];
        }
        this.bewegungsziele.add(ziel);
        List<Integer> keyZiel = convertiereArrayZuListe(ziel); //da HashMap mit int[] als key nicht umgehen kann
        this.bewegungszieleMitPfad.put(keyZiel, pfad);
    }

    private static List<Integer> convertiereArrayZuListe(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        return list;
    }

    private void setBewegungspfade() {
        pruefeEinenSchritt(pos_x, pos_y); //Methode ruft sich selbst neu auf für jeden Schritt und speichert alle Pfade ab
    }

    // Methoden um Liste "bewegungspfade" zu bestimmen. Methoden rufen sich selbst neu auf und durchlaufen jeden Pfad.
    private void pruefeEinenSchritt(int x, int y) {
        int[] sprungweiten = {-1, 1}; //y-Richtung (1), y-Richtung (2)
        List<int[]> bewegungspfadeEineRichtung = new ArrayList<>();
        pruefeBewegungsfaehigkeiten(bewegungspfadeEineRichtung, sprungweiten, x, y);
        verfolgeBewegungsfaehigkeiten(x, y);
    }

    private void pruefeEinenSchritt(int x, int y, List<int[]> bewegungspfadeEineRichtungAlt) {
        int[] sprungweiten = {-1, 1}; //y-Richtung (1), y-Richtung (2)
        List<int[]> bewegungspfadeEineRichtung = new ArrayList<>();
        pruefeBewegungsfaehigkeiten(bewegungspfadeEineRichtung, sprungweiten, x, y);
        verfolgeBewegungsfaehigkeiten(x, y, bewegungspfadeEineRichtungAlt);
    }

    //Step 2:
    private void verfolgeBewegungsfaehigkeiten(int x, int y) {
        for (List<int[]> bewegungspfad : bewegungspfadeMerkeGehen) {
            this.bewegungspfadeGehen.add(bewegungspfad);
        }
        List<List<int[]>> bewegungspfadeSchlagen = this.bewegungspfadeMerkeSchlagen;
        this.bewegungspfadeMerkeSchlagen = new ArrayList<>(); //Leeren für nächsten Schritt auf dem Pfad
        for (List<int[]> bewegungspfad : bewegungspfadeSchlagen) {
            int neu_x = x + bewegungspfad.get(bewegungspfad.size() - 1)[0];
            int neu_y = y + bewegungspfad.get(bewegungspfad.size() - 1)[1];
            pruefeEinenSchritt(neu_x, neu_y, bewegungspfad);
        }
    }

    private void verfolgeBewegungsfaehigkeiten(int x, int y, List<int[]> bewegungspfadeEineRichtungAlt) {
        if (!this.bewegungspfadeMerkeSchlagen.isEmpty()) {
            List<List<int[]>> bewegungspfadeSchlagen = this.bewegungspfadeMerkeSchlagen;
            this.bewegungspfadeMerkeSchlagen = new ArrayList<>(); //Leeren für nächsten Schritt auf dem Pfad
            for (List<int[]> bewegungspfad : bewegungspfadeSchlagen) {
                List<int[]> bewegungspfadSum = new ArrayList<>(bewegungspfadeEineRichtungAlt);
                bewegungspfadSum.add(bewegungspfad.get(0));
                int neu_x = x + bewegungspfad.get(bewegungspfad.size() - 1)[0];
                int neu_y = y + bewegungspfad.get(bewegungspfad.size() - 1)[1];
                pruefeEinenSchritt(neu_x, neu_y, bewegungspfadSum);
            }
        } else {
            this.bewegungspfadeSchlagen.add(bewegungspfadeEineRichtungAlt);
        }
    }

    //Step 1
    private void pruefeBewegungsfaehigkeiten(List<int[]> bewegungspfadeEineRichtung, int[] sprungweiten, int x, int y) {
        for (int i : sprungweiten) {
            for (int j : sprungweiten) {
                bewegungspfadeEineRichtung = pruefeEineRichtung(bewegungspfadeEineRichtung, i, j, x, y);
            }
        }
    }

    private List<int[]> pruefeEineRichtung(List<int[]> bewegungspfadeEineRichtung, int i, int j, int x, int y) {
        int count = 0;
        for (int k = 1; k < 9; k++) {
            bewegungspfadeEineRichtung = pruefeEinFeld(bewegungspfadeEineRichtung, i, j, k, x, y);
            if (bewegungspfadeEineRichtung == null) {
                return new ArrayList<>();
            } else if (bewegungspfadeEineRichtung.isEmpty()) {
                count++;
            } else {
                entferneEinzelschritte(count); //da wenn man springen kann nicht gehen kann
                this.bewegungspfadeMerkeSchlagen.add(bewegungspfadeEineRichtung);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private void entferneEinzelschritte(int count) {
        for (int i = 0; i < count; i++) {
            this.bewegungspfadeMerkeGehen.remove(this.bewegungspfadeMerkeGehen.size() - 1);
        }
    }

    private List<int[]> pruefeEinFeld(List<int[]> bewegungspfadeEineRichtung, int i, int j, int k, int x, int y) {
        try {
            int figur = aktuellepgn[x + i * k][y + j * k] * this.farbe;
            int[] figurPosition = {x + i * k, y + j * k};
            if (istPositionEnthalten(this.bereitsGeschlageneFiguren, figurPosition)) {
                figur = 0;
            }
            switch (figur) {
                case 2:
                case 1: // Weg ist von eigenem Spielstein versperrt
                    return null;
                case 0:
                    int[] einSchritt = {i * k, j * k};
                    bewegungspfadeEineRichtung.add(einSchritt);
                    this.bewegungspfadeMerkeGehen.add(bewegungspfadeEineRichtung);
                    return new ArrayList<>();
                case -1:
                case -2:
                    bewegungspfadeEineRichtung = schlagenOderNichtSchlagen(bewegungspfadeEineRichtung, i, j, k, x, y);
                    return bewegungspfadeEineRichtung;
                default:
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    private List<int[]> schlagenOderNichtSchlagen(List<int[]> bewegungspfadeEineRichtung, int i, int j, int k, int x, int y) {
        if (aktuellepgn[x + i * k + i / abs(i)][y + j * k + j / abs(j)] != 0) {
            return null;
        } else {
            this.zugzwang = true;
            int[] geschlageneFigur = {x + i * k, y + j * k};
            this.bereitsGeschlageneFiguren.add(geschlageneFigur);
            int[] einSchritt = {i * k + i / abs(i), j * k + j / abs(j)};
            bewegungspfadeEineRichtung.add(einSchritt);
            return bewegungspfadeEineRichtung;
        }
    }

    private boolean istPositionEnthalten(List<int[]> bereitsGeschlageneFiguren, int[] figurPosition) {
        boolean enthalten = false;
        for (int[] position : bereitsGeschlageneFiguren) {
            if (Arrays.equals(position, figurPosition)) {
                enthalten = true;
                break;
            }
        }
        return enthalten;
    }


    @Override
    public Boolean getZugzwang() {
        return zugzwang;
    }

    @Override
    public List<int[]> getBewegungsziele() {
        return bewegungsziele;
    }

    public Boolean getBewegungsfaehig() {
        return bewegungsfaehig;
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
}
