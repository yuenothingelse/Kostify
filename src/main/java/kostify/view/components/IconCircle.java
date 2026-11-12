package kostify.view.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/** Lingkaran mint berisi ikon, dipakai di kartu statistik dan judul kartu. */
public class IconCircle extends JComponent {
    private final Icons.Name name;
    private final int iconSize;
    private final Color fg;

    public IconCircle(Icons.Name name, int iconSize, Color fg) {
        this.name = name;
        this.iconSize = iconSize;
        this.fg = fg;
        setOpaque(false);
        setPreferredSize(new Dimension(34, 34));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        g2.setColor(Theme.MINT);
        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        Icons.of(name, iconSize, fg).paintIcon(this, g2, (getWidth() - iconSize) / 2, (getHeight() - iconSize) / 2);
        g2.dispose();
    }
}
