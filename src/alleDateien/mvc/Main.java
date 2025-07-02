package alleDateien.mvc;

import alleDateien.Startbildschirm;

import javax.swing.*;

public class Main {
    public static void main (String[] args) {
        SwingUtilities.invokeLater(() -> {
            Startbildschirm sb = new Startbildschirm();
            sb.setStartbildschirm();
        });
    }
}
