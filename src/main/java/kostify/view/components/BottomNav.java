package kostify.view.components;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/** Menu bawah berbentuk pil dengan empat tujuan. */
public class BottomNav extends JPanel {
    public static final String BERANDA = "beranda";
    public static final String KAMAR = "kamar";
    public static final String PENGHUNI = "penghuni";
    public static final String PEMBAYARAN = "pembayaran";

    private final Map<String, NavButton> buttons = new LinkedHashMap<>();

    public BottomNav(Consumer<String> onSelect) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_BOTTOM);
        setBorder(new EmptyBorder(0, 22, 13, 25));

        RoundedPanel pill = new RoundedPanel(68, Color.WHITE, true);
        pill.setLayout(new GridLayout(1, 4));
        int s = pill.inset();
        pill.setBorder(new EmptyBorder(s, s + 10, s, s + 10));
        pill.setPreferredSize(new Dimension(334 + 2 * s, 68 + 2 * s));

        add(pill, BERANDA, "Beranda", Icons.Name.HOME, onSelect);
        add(pill, KAMAR, "Kamar", Icons.Name.BED, onSelect);
        add(pill, PENGHUNI, "Penghuni", Icons.Name.USERS, onSelect);
        add(pill, PEMBAYARAN, "Pembayaran", Icons.Name.WALLET, onSelect);
        add(pill, BorderLayout.CENTER);
    }

    private void add(JPanel pill, String key, String label, Icons.Name icon, Consumer<String> onSelect) {
        NavButton button = new NavButton(label, icon);
        button.addActionListener(e -> onSelect.accept(key));
        buttons.put(key, button);
        pill.add(button);
    }

    public void setActive(String key) {
        buttons.forEach((k, b) -> b.setActive(k.equals(key)));
    }

    private static final class NavButton extends JButton {
        private final String label;
        private final Icons.Name icon;
        private boolean active;

        NavButton(String label, Icons.Name icon) {
            this.label = label;
            this.icon = icon;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            getAccessibleContext().setAccessibleName(label);
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);
            Color color = active ? Theme.NAV_ACTIVE : Theme.NAV_INACTIVE;
            int top = (getHeight() - 42) / 2;
            Icons.of(icon, 24, color).paintIcon(this, g2, (getWidth() - 24) / 2, top);
            g2.setFont(Theme.font(active ? Font.BOLD : Font.PLAIN, 10));
            g2.setColor(color);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, top + 24 + 4 + fm.getAscent());
            g2.dispose();
        }
    }
}
