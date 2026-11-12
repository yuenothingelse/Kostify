package kostify.view.components;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/** Tombol utama berbentuk pil dengan gradasi hijau. */
public class PillButton extends JButton {

    public PillButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(Theme.font(Font.BOLD, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        g2.setPaint(new GradientPaint(0, 0, Theme.GREEN_DARK, getWidth(), 0, Theme.GREEN_MID));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        if (getModel().isPressed()) {
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
