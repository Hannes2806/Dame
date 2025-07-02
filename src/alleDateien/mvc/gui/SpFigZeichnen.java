package alleDateien.mvc.gui;

import javax.swing.*;
import java.awt.*;

public class SpFigZeichnen extends JPanel {

    String color;

    public SpFigZeichnen(String farbe) {
        this.color = farbe;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(getColor());

        int diameter = Math.min(getWidth(), getHeight());
        diameter *= 0.8;

        int xAussen = (getWidth() - diameter) / 2;
        int yAussen = (getHeight() - diameter) / 2;

        g2d.setColor(getColor());
        g2d.fillOval(xAussen, yAussen, diameter, diameter);
    }

    public Color getColor() {
        if (color.equals("black")) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}