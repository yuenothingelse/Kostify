package kostify.view.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/** Logo Kostify: rumah dengan orang di pintu. Versi putih dipakai di atas header hijau. */
public class Logo extends JComponent {
    private final boolean white;

    public Logo(int size, boolean white) {
        this.white = white;
        setOpaque(false);
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        draw(g2, 0, 0, Math.min(getWidth(), getHeight()), white);
        g2.dispose();
    }

    public static void draw(Graphics2D g2, int x, int y, int size, boolean white) {
        Graphics2D g = (Graphics2D) g2.create();
        g.translate(x, y);
        g.scale(size / 80.0, size / 80.0);

        Path2D.Double house = new Path2D.Double();
        house.moveTo(40, 4);
        house.lineTo(70, 26);
        house.quadTo(74, 29, 74, 34);
        house.lineTo(74, 64);
        house.curveTo(74, 70.6, 68.6, 76, 62, 76);
        house.lineTo(18, 76);
        house.curveTo(11.4, 76, 6, 70.6, 6, 64);
        house.lineTo(6, 34);
        house.quadTo(6, 29, 10, 26);
        house.closePath();
        g.setPaint(white ? Color.WHITE
                : new GradientPaint(0, 0, new Color(0x43AB6B), 80, 80, new Color(0x1A6F3E)));
        g.fill(house);

        g.setColor(white ? Theme.GREEN_MID : Color.WHITE);
        g.fill(new Ellipse2D.Double(33, 23, 14, 14));
        Path2D.Double body = new Path2D.Double();
        body.moveTo(26, 66);
        body.lineTo(26, 52);
        body.curveTo(26, 44.3, 32.3, 38, 40, 38);
        body.curveTo(47.7, 38, 54, 44.3, 54, 52);
        body.lineTo(54, 66);
        body.lineTo(46, 66);
        body.lineTo(46, 54);
        body.curveTo(46, 50.7, 43.3, 48, 40, 48);
        body.curveTo(36.7, 48, 34, 50.7, 34, 54);
        body.lineTo(34, 66);
        body.closePath();
        g.fill(body);
        g.dispose();
    }
}
