package alleDateien.mvc.gui;

import alleDateien.mvc.SpielGUI;

public class DebugModus extends SpielGUI {

    private int figur;

    public void setFigur(int figur) {
        this.figur = figur;
    }

    // Debug-Modus: gibt die ausgewählte Figur zurück
    public int getDebugFigur() {
        switch(figur) {
            case -1:
                return -1;

            case -2:
                return -2;

            case 1:
                return 1;

            case 2:
                return 2;

            default:
                return 0;
        }
    }
}