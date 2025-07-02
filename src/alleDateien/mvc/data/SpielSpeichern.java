package alleDateien.mvc.data;

import java.io.*;

public class SpielSpeichern {
    private int[][] pgn;
    final private String spiel_ort = "gespeichertesSpiel.txt";

    public SpielSpeichern() {
        this.pgn = new int[8][8];
    }

    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
    }

    public void speichereSpiel() {
        try {
            FileWriter file = new FileWriter(spiel_ort);

            StringBuilder builder = new StringBuilder();

            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    builder.append(pgn[i][j]);
                    builder.append(",");
                }
                builder.append("\n");
            }

            BufferedWriter writer = new BufferedWriter(file);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException ignored) {}
    }

    public int[][] getGeladenePGN() {
        BufferedReader reader;
        String zeileLesen = "";

        try {
            reader = new BufferedReader(new FileReader(spiel_ort));

            int zeile = 0;

            while((zeileLesen = reader.readLine()) != null)
            {
                String[] zeichen = zeileLesen.split(",");
                int spalte = 0;
                for(String c : zeichen)
                {
                    pgn[zeile][spalte] = Integer.parseInt(c);
                    spalte++;
                }
                zeile++;
            }

            reader.close();
        } catch (IOException ignored) {}

        return this.pgn;
    }
}
