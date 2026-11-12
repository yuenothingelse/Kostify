package kostify.view.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Font;
import java.awt.geom.RoundRectangle2D;

/** Header hijau bergradasi dengan sudut bawah membulat. Anak ditaruh dengan koordinat absolut. */
public class HeaderPanel extends JPanel {
    private JLabel subtitle;

    public HeaderPanel(int height) {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(Theme.WIDTH, height));
    }

    /** Header sederhana berisi judul dan subjudul (dipakai layar selain Beranda). */
    public static HeaderPanel titled(String title, String subtitleText, int height) {
        HeaderPanel header = new HeaderPanel(height);
        JLabel titleLabel = Theme.label(title, 22, Font.BOLD, Color.WHITE);
        titleLabel.setBounds(22, 54, 340, 28);
        header.add(titleLabel);
        header.subtitle = Theme.label(subtitleText, 12, Font.PLAIN, Color.WHITE);
        header.subtitle.setBounds(22, 86, 340, 16);
        header.add(header.subtitle);
        return header;
    }

    public JLabel getSubtitleLabel() {
        return subtitle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        g2.setPaint(new LinearGradientPaint(0, 0, getWidth(), getHeight(), new float[]{0f, 0.55f, 1f},
                new Color[]{Theme.GREEN_DARK, Theme.GREEN_MID, Theme.GREEN_LIGHT}));
        g2.fill(new RoundRectangle2D.Float(0, -40, getWidth(), getHeight() + 40, 60, 60));
        g2.dispose();
    }
}
