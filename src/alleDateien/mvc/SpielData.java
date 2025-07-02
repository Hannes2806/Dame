package alleDateien.mvc;

import alleDateien.Bauer;
import alleDateien.Dame;
import alleDateien.Leerfeld;
import alleDateien.Spielstein;

public class SpielData {
    public static int[][] standardpgn;

    public static Spielstein[][] steinpgn = new Spielstein[8][8];
    public static int[][] aktuellepgn = new int[8][8];

    public SpielData() {
        setStandardpgn();
        aktuellepgn = standardpgn;
        erstelleSteinpgn(aktuellepgn);
    }

    public static void erstelleSteinpgn(int[][] zahlenpgn) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                waehleSpielstein(i, j, zahlenpgn);
            }
        }
    }

    private static void waehleSpielstein(int i, int j, int[][] zahlenpgn) {
        switch (zahlenpgn[i][j]) {
            case 0:
                steinpgn[i][j] = new Leerfeld(i, j);
                break;
            case 1:
                steinpgn[i][j] = new Bauer(i, j, 1); //weiß
                break;
            case -1:
                steinpgn[i][j] = new Bauer(i, j, -1); //schwarz
                break;
            case 2:
                steinpgn[i][j] = new Dame(i, j, 1);
                break;
            case -2:
                steinpgn[i][j] = new Dame(i, j, -1);
                break;
            default:
                System.out.println("Unbekannter Spielstein fehler");
        }
    }

    public static Spielstein[][] getSteinpgn() {
        return steinpgn;
    }

    public static int[][] getStandardpgn() {
        return standardpgn;
    }

    public static void setAktuellepgn(int[][] aktuellepgn) {
        SpielData.aktuellepgn = aktuellepgn;
    }

    private static void setStandardpgn() {
        standardpgn = new int[][] {
            // a, b, c, d, e, f, g, h
            // -1 = Bauer schwarz
            // -2 = Dame schwarz
            // 1 = Bauer weiß
            // 2 = Dame weiß
            // 0 = leer
            {0, -1, 0, -1, 0, -1, 0, -1},
            {-1, 0, -1, 0, -1, 0, -1, 0},
            {0, -1, 0, -1, 0, -1, 0, -1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0}
        };
    }
}