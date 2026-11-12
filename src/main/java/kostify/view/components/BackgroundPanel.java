package kostify.view.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Path2D;

/** Latar putih ke mint dengan gelombang hijau di pojok. Dipakai splash dan login. */
public class BackgroundPanel extends JPanel {

    public BackgroundPanel() {
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        int w = getWidth();
        int h = getHeight();
        g2.setPaint(new LinearGradientPaint(0, 0, 0, h, new float[]{0f, 0.6f, 1f},
                new Color[]{Color.WHITE, new Color(0xF2FAF5), new Color(0xE9F6EE)}));
        g2.fillRect(0, 0, w, h);

        Path2D.Double topLeft = new Path2D.Double();
        topLeft.moveTo(0, 0);
        topLeft.lineTo(196, 0);
        topLeft.curveTo(192, 38, 160, 56, 118, 68);
        topLeft.curveTo(66, 82, 26, 98, 0, 122);
        topLeft.closePath();
        g2.setColor(new Color(0xD5, 0xEE, 0xDD, 217));
        g2.fill(topLeft);

        Path2D.Double bottomRight = new Path2D.Double();
        bottomRight.moveTo(w, h - 136);
        bottomRight.curveTo(w - 47, h - 130, w - 87, h - 88, w - 125, h - 52);
        bottomRight.curveTo(w - 157, h - 24, w - 197, h - 6, w - 233, h);
        bottomRight.lineTo(w, h);
        bottomRight.closePath();
        g2.setPaint(new GradientPaint(w - 233, h - 136, new Color(0xCB, 0xE9, 0xD6, 204),
                w, h, new Color(0x94, 0xD0, 0xAB, 204)));
        g2.fill(bottomRight);
        g2.dispose();
    }
}
