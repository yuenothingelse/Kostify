package kostify.view.components;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/** Ikon vektor (digambar dengan Java2D) agar tajam di ukuran berapa pun. Koordinat dasar 24 x 24. */
public final class Icons {

    public enum Name { HOME, USERS, DOOR, BED, WALLET, PERSON, CHEVRON, PLUS }

    private Icons() {
    }

    public static Icon of(Name name, int size, Color color) {
        return new VectorIcon(name, size, color);
    }

    private record VectorIcon(Name name, int size, Color color) implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);
            g2.translate(x, y);
            g2.scale(size / 24.0, size / 24.0);
            g2.setColor(color);
            switch (name) {
                case HOME -> home(g2);
                case USERS -> users(g2);
                case DOOR -> door(g2);
                case BED -> bed(g2);
                case WALLET -> wallet(g2);
                case PERSON -> person(g2);
                case CHEVRON -> chevron(g2);
                case PLUS -> plus(g2);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static final BasicStroke ROUND = new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static void home(Graphics2D g) {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(12, 2.8);
        p.lineTo(1.8, 12);
        p.lineTo(5, 12);
        p.lineTo(5, 20.2);
        p.lineTo(10, 20.2);
        p.lineTo(10, 14.2);
        p.lineTo(14, 14.2);
        p.lineTo(14, 20.2);
        p.lineTo(19, 20.2);
        p.lineTo(19, 12);
        p.lineTo(22.2, 12);
        p.closePath();
        g.fill(p);
        g.setStroke(ROUND);
        g.draw(p);
    }

    private static void users(Graphics2D g) {
        g.fill(new Ellipse2D.Double(5.6, 4.6, 6.8, 6.8));
        Path2D.Double b1 = new Path2D.Double();
        b1.moveTo(2.5, 19.5);
        b1.curveTo(2.5, 15.8, 5.4, 13.3, 9, 13.3);
        b1.curveTo(12.6, 13.3, 15.5, 15.8, 15.5, 19.5);
        b1.closePath();
        g.fill(b1);
        g.fill(new Ellipse2D.Double(14.7, 6.4, 5.2, 5.2));
        Path2D.Double b2 = new Path2D.Double();
        b2.moveTo(16.2, 13.4);
        b2.curveTo(19.4, 13.1, 21.5, 15.1, 21.5, 18.5);
        b2.lineTo(16.9, 18.5);
        b2.curveTo(16.9, 16.6, 16.7, 15, 16.2, 13.4);
        b2.closePath();
        g.fill(b2);
    }

    private static void door(Graphics2D g) {
        g.fill(new RoundRectangle2D.Double(5, 2.5, 14, 19, 5.2, 5.2));
        g.setColor(new Color(255, 255, 255, 150));
        g.setStroke(new BasicStroke(1f));
        g.draw(new RoundRectangle2D.Double(7.6, 5, 8.8, 14, 2.4, 2.4));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(13.8, 10.8, 2.4, 2.4));
    }

    private static void bed(Graphics2D g) {
        g.fill(new RoundRectangle2D.Double(2.5, 4, 2.2, 16, 2, 2));
        Path2D.Double base = new Path2D.Double();
        base.moveTo(4.7, 14.5);
        base.lineTo(21.5, 14.5);
        base.lineTo(21.5, 19);
        base.lineTo(19.3, 19);
        base.lineTo(19.3, 16.7);
        base.lineTo(4.7, 16.7);
        base.closePath();
        g.fill(base);
        Path2D.Double mattress = new Path2D.Double();
        mattress.moveTo(10, 9.2);
        mattress.lineTo(17, 9.2);
        mattress.curveTo(19.5, 9.2, 21.5, 11.2, 21.5, 13.7);
        mattress.lineTo(21.5, 14.5);
        mattress.lineTo(10, 14.5);
        mattress.closePath();
        g.fill(mattress);
        g.fill(new Ellipse2D.Double(5.4, 8.8, 4.4, 4.4));
    }

    private static void wallet(Graphics2D g) {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(4, 6.5);
        p.curveTo(4, 4.57, 5.57, 3, 7.5, 3);
        p.lineTo(17, 3);
        p.lineTo(17, 6.2);
        p.lineTo(7.6, 6.2);
        p.curveTo(7.05, 6.2, 6.6, 6.65, 6.6, 7.2);
        p.curveTo(6.6, 7.75, 7.05, 8.2, 7.6, 8.2);
        p.lineTo(19, 8.2);
        p.curveTo(20.1, 8.2, 21, 9.1, 21, 10.2);
        p.lineTo(21, 17.5);
        p.curveTo(21, 19.43, 19.43, 21, 17.5, 21);
        p.lineTo(7.5, 21);
        p.curveTo(5.57, 21, 4, 19.43, 4, 17.5);
        p.closePath();
        g.fill(p);
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(15.4, 12.6, 2.8, 2.8));
    }

    private static void person(Graphics2D g) {
        g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Ellipse2D.Double(8.7, 4.5, 6.6, 6.6));
        Path2D.Double body = new Path2D.Double();
        body.moveTo(5, 20.5);
        body.curveTo(5.6, 16.5, 8.4, 14.3, 12, 14.3);
        body.curveTo(15.6, 14.3, 18.4, 16.5, 19, 20.5);
        g.draw(body);
    }

    private static void chevron(Graphics2D g) {
        g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D.Double p = new Path2D.Double();
        p.moveTo(9, 5);
        p.lineTo(16, 12);
        p.lineTo(9, 19);
        g.draw(p);
    }

    private static void plus(Graphics2D g) {
        g.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(12, 4.5, 12, 19.5));
        g.draw(new Line2D.Double(4.5, 12, 19.5, 12));
    }
}
