package alleDateien.mvc.gui;

import javax.swing.*;
import java.awt.*;

public class DameZeichnen extends JPanel {

    String color;

    public DameZeichnen(String farbe) {
        this.color = farbe;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int diameter = Math.min(getWidth(), getHeight());
        diameter *= 0.8;

        // Durchmesser äußerer Kreis
        int xAussen = (getWidth() - diameter) / 2;
        int yInnen = (getHeight() - diameter) / 2;

        // Äußerstes Kreis färben
        g2d.setColor(getColor());
        g2d.fillOval(xAussen, yInnen, diameter, diameter);

        // Durchmesser des mittleren Kreises
        int mitteDiameter = (int) (diameter * 0.6);
        int xMitte = xAussen + (diameter - mitteDiameter) / 2;
        int yMitte = yInnen + (diameter - mitteDiameter) / 2;

        // Mittleren Kreis färben
        g2d.setColor(Color.GRAY);
        g2d.fillOval(xMitte, yMitte, mitteDiameter, mitteDiameter);

        // Durchmesser des inneren Kreises
        int innerDiameter = (int) (diameter * 0.3);
        int xInner = xAussen + (diameter - innerDiameter) / 2;
        int yInner = yInnen + (diameter - innerDiameter) / 2;

        // Innersten Kreis färben
        g2d.setColor(getColor());
        g2d.fillOval(xInner, yInner, innerDiameter, innerDiameter);
    }

    public Color getColor() {
        if (color.equals("black")) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
