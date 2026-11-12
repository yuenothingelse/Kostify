package kostify.view.components;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/** Tombol bulat: avatar profil (putih) dan tombol tambah (hijau gradasi). */
public class CircleButton extends JButton {
    private final Icon icon;
    private final Color solid;

    private CircleButton(Icon icon, Color solid, int size, String accessibleName) {
        this.icon = icon;
        this.solid = solid;
        setPreferredSize(new Dimension(size, size));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        getAccessibleContext().setAccessibleName(accessibleName);
    }

    public static CircleButton solid(Icons.Name name, int iconSize, Color iconColor, Color bg, int size, String label) {
        return new CircleButton(Icons.of(name, iconSize, iconColor), bg, size, label);
    }

    public static CircleButton gradient(Icons.Name name, int iconSize, Color iconColor, int size, String label) {
        return new CircleButton(Icons.of(name, iconSize, iconColor), null, size, label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        if (solid != null) {
            g2.setColor(solid);
        } else {
            g2.setPaint(new GradientPaint(0, 0, Theme.GREEN_DARK, getWidth(), getHeight(), Theme.GREEN_MID));
        }
        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        if (getModel().isPressed()) {
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        }
        icon.paintIcon(this, g2, (getWidth() - icon.getIconWidth()) / 2, (getHeight() - icon.getIconHeight()) / 2);
        g2.dispose();
    }
}
