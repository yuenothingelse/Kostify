package kostify.view.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel putih membulat dengan bayangan lembut. Jika bayangan aktif, bentuk kartu digeser
 * ke dalam sejauh SHADOW piksel supaya bayangan tetap ada di dalam area komponen.
 */
public class RoundedPanel extends JPanel {
    public static final int SHADOW = 6;

    private final int arc;
    private final Color fill;
    private final boolean shadow;

    public RoundedPanel(int arc, Color fill, boolean shadow) {
        this.arc = arc;
        this.fill = fill;
        this.shadow = shadow;
        setOpaque(false);
        setLayout(null);
    }

    public int inset() {
        return shadow ? SHADOW : 0;
    }

    /** Menaruh komponen anak dengan koordinat relatif terhadap bentuk kartu. */
    public void place(Component child, int x, int y, int w, int h) {
        add(child);
        child.setBounds(x + inset(), y + inset(), w, h);
    }

    /** Menaruh kartu ini di parent; koordinat merujuk pada bentuk kartu, bukan bayangannya. */
    public void placeIn(Container parent, int x, int y, int w, int h) {
        parent.add(this);
        setBounds(x - inset(), y - inset(), w + 2 * inset(), h + 2 * inset());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        int i = inset();
        int w = getWidth() - 2 * i;
        int h = getHeight() - 2 * i;
        if (shadow) {
            g2.setColor(new Color(30, 100, 60, 7));
            for (int k = SHADOW; k >= 1; k--) {
                g2.fill(new RoundRectangle2D.Float(i - k, i - k + 2, w + 2 * k, h + 2 * k, arc + 2 * k, arc + 2 * k));
            }
        }
        g2.setColor(fill);
        g2.fill(new RoundRectangle2D.Float(i, i, w, h, arc, arc));
        g2.dispose();
    }
}
