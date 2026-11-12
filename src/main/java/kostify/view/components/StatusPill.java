package kostify.view.components;

import kostify.model.enums.StatusKamar;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/** Label status kamar: hijau untuk Terisi, oranye untuk Kosong. */
public class StatusPill extends JComponent {
    private final String text;
    private final boolean terisi;

    public StatusPill(StatusKamar status) {
        this.terisi = status == StatusKamar.TERISI;
        this.text = status.toString();
        setFont(Theme.font(terisi ? Font.PLAIN : Font.BOLD, 11));
        setOpaque(false);
        FontMetrics fm = getFontMetrics(getFont());
        setPreferredSize(new Dimension(fm.stringWidth(text) + 28, 22));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        g2.setColor(terisi ? new Color(0xC9E7D3) : new Color(0xFFE2A8));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        g2.setFont(getFont());
        g2.setColor(terisi ? new Color(0x2F7D50) : new Color(0xB8700A));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2,
                (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }
}
