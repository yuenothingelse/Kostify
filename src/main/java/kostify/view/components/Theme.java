package kostify.view.components;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Ukuran, warna, dan font bersama. Ukuran layar mengikuti iPhone 16 (393 x 852). */
public final class Theme {
    public static final int WIDTH = 393;
    public static final int HEIGHT = 767;

    public static final Color GREEN_DARK = new Color(0x1C7340);
    public static final Color GREEN_MID = new Color(0x2F8F57);
    public static final Color GREEN_LIGHT = new Color(0x44A56C);
    public static final Color TITLE = new Color(0x1F6B3E);
    public static final Color SUBTITLE = new Color(0x4A8E68);
    public static final Color MINT = new Color(0xCDE8D6);
    public static final Color TINT = new Color(0xF1F7F3);
    public static final Color TEXT = new Color(0x1B2B22);
    public static final Color MUTED = new Color(0x6B8579);
    public static final Color VALUE = new Color(0x4E9B6C);
    public static final Color LINK = new Color(0x2E9B55);
    public static final Color CARD_TITLE = new Color(0x2D6E49);
    public static final Color BG_TOP = new Color(0xF5FAF7);
    public static final Color BG_BOTTOM = new Color(0xE4F1E9);
    public static final Color SEPARATOR = new Color(0xE8EFEA);
    public static final Color NAV_INACTIVE = new Color(0x5F7488);
    public static final Color NAV_ACTIVE = new Color(0x1E9B4F);
    public static final Color CHEVRON = new Color(0x5A7287);
    public static final Color DANGER = new Color(0xC62828);

    private Theme() {
    }

    public static Font font(int style, float size) {
        return new Font(Font.SANS_SERIF, style, 12).deriveFont(style, size);
    }

    public static void quality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static JLabel label(String text, float size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font(style, size));
        label.setForeground(color);
        return label;
    }

    /** Menggambar teks rata tengah pada garis dasar (baseline) tertentu. */
    public static void drawCentered(Graphics2D g, String text, Font font, Color color, int centerX, int baseline) {
        g.setFont(font);
        g.setColor(color);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, centerX - fm.stringWidth(text) / 2, baseline);
    }
}
