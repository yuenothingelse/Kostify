package kostify.view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/** Ilustrasi rumah kost untuk splash screen (area 340 x 200). */
public final class HouseIllustration {
    private HouseIllustration() {
    }

    private static void fill(Graphics2D g, int rgb, Shape s) {
        g.setColor(new Color(rgb));
        g.fill(s);
    }

    private static void window(Graphics2D g, double x, double y, double w, double h) {
        Rectangle2D.Double r = new Rectangle2D.Double(x, y, w, h);
        fill(g, 0xA9D2BA, r);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2f));
        g.draw(r);
    }

    public static void draw(Graphics2D g2, int ox, int oy) {
        Graphics2D g = (Graphics2D) g2.create();
        Theme.quality(g);
        g.translate(ox, oy);

        fill(g, 0xD9EEE0, new Ellipse2D.Double(10, 164, 320, 24));
        fill(g, 0xE3F2E9, new Ellipse2D.Double(23, 26, 64, 24));
        fill(g, 0xE3F2E9, new Ellipse2D.Double(56, 18, 36, 24));
        fill(g, 0xE3F2E9, new Ellipse2D.Double(255, 23, 60, 22));

        fill(g, 0x4C7F62, new Rectangle2D.Double(310, 142, 4, 34));
        fill(g, 0x4BA971, new Ellipse2D.Double(290, 68, 44, 80));
        fill(g, 0x4C7F62, new Rectangle2D.Double(27, 152, 3, 24));
        fill(g, 0x5FB57F, new Ellipse2D.Double(12, 108, 32, 52));
        fill(g, 0x3F9D63, new Ellipse2D.Double(40, 128, 28, 28));

        fill(g, 0xFFFFFF, new Rectangle2D.Double(82, 108, 190, 66));
        fill(g, 0xEAF2ED, new Rectangle2D.Double(232, 108, 40, 66));
        fill(g, 0xFFFFFF, new Rectangle2D.Double(98, 68, 152, 42));
        fill(g, 0x2A8A52, new Polygon(new int[]{86, 150, 262, 252}, new int[]{72, 22, 22, 72}, 4));
        fill(g, 0x1E7A45, new Polygon(new int[]{262, 300, 252}, new int[]{22, 66, 72}, 3));
        fill(g, 0x1E7A45, new Rectangle2D.Double(84, 70, 178, 5));

        window(g, 110, 82, 24, 22);
        window(g, 146, 80, 34, 26);
        window(g, 196, 82, 24, 22);
        window(g, 96, 124, 26, 28);
        window(g, 198, 132, 22, 24);
        fill(g, 0x2A8A52, new Rectangle2D.Double(142, 104, 44, 2.5));
        fill(g, 0x1E7A45, new Rectangle2D.Double(146, 126, 22, 48));
        fill(g, 0x2A8A52, new Polygon(new int[]{190, 252, 258, 184}, new int[]{118, 118, 127, 127}, 4));

        fill(g, 0x3F9D63, new Ellipse2D.Double(90, 163, 28, 16));
        fill(g, 0x5FB57F, new Ellipse2D.Double(136, 165, 32, 16));
        fill(g, 0x3F9D63, new Ellipse2D.Double(191, 164, 30, 16));
        fill(g, 0x5FB57F, new Ellipse2D.Double(230, 162, 32, 18));

        fill(g, 0x14582F, new Rectangle2D.Double(282, 164, 4, 12));
        fill(g, 0x14582F, new RoundRectangle2D.Double(268, 140, 32, 24, 6, 6));
        Path2D.Double glyph = new Path2D.Double();
        glyph.moveTo(284, 146);
        glyph.lineTo(292, 153);
        glyph.lineTo(289.5, 153);
        glyph.lineTo(289.5, 159);
        glyph.lineTo(286, 159);
        glyph.lineTo(286, 155);
        glyph.lineTo(282, 155);
        glyph.lineTo(282, 159);
        glyph.lineTo(278.5, 159);
        glyph.lineTo(278.5, 153);
        glyph.lineTo(276, 153);
        glyph.closePath();
        fill(g, 0xFFFFFF, glyph);
        g.dispose();
    }
}
