package alleDateien;

import alleDateien.mvc.SpielData;
import alleDateien.mvc.SpielGUI;
import alleDateien.mvc.gui.Fenster;

import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class Startbildschirm {
    static public JFrame fenster = new Fenster();
    private SpielGUI gui = new SpielGUI();
    private SpielData data;

    public Startbildschirm() {
    }

    public void setStartbildschirm() {
        // Design für Hauptbildschirm
        fenster.setLayout(new GridBagLayout());

        JPanel elementPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;

        JLabel dame = new JLabel("dame", SwingConstants.CENTER);
        dame.setFont(new Font("Arial", Font.BOLD, 160));
        dame.setForeground(Color.LIGHT_GRAY);

        JButton debug = new JButton("Debug-Modus");
        JButton spiel = new JButton("Neues Spiel");
        JButton spiel_laden = new JButton("Spiel laden");
        JButton schliessen = new JButton("Beenden");

        gbc.gridx = 0;
        gbc.gridy = 0;
        elementPanel.add(dame, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        elementPanel.add(debug, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        elementPanel.add(spiel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        elementPanel.add(schliessen, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        elementPanel.add(spiel_laden, gbc);

        spiel.addActionListener(e -> starteSpiel());
        spiel_laden.addActionListener( e -> starteGeladenesSpiel());
        debug.addActionListener(e -> starteDebug());
        schliessen.addActionListener(e -> exit(0));

        elementPanel.setBackground(Color.BLACK);
        fenster.add(elementPanel);
        fenster.setVisible(true);
    }

    public void starteDebug() {
        fenster.getContentPane().removeAll();
        gui.setModus("debug");
        gui.starteGUI();
        gui.setSpielfeld();
        fenster.repaint();
    }

    public void starteSpiel() {
        fenster.getContentPane().removeAll();

        erstelleSpielData();

        gui.setModus("spiel");
        gui.starteGUI();
        gui.setStandardPGN();
        gui.setSpielfeld();
        fenster.repaint();
    }

    public void starteGeladenesSpiel() {
        fenster.getContentPane().removeAll();
        gui.setModus("spiel");
        gui.starteGUI();
        gui.setGespeichertePGN();
        gui.setSpielfeld();
        fenster.repaint();
    }

    public void erstelleSpielData() {
        if (this.data == null) {
            System.out.println("erstelle SpielData");
            new SpielData();
        } else {
            System.out.println("Keine SpielData vorhanden");
        }
    }
}
