package kostify.view.components;

import kostify.model.Kamar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** Kartu putih berisi daftar kamar (nomor, keterangan, status). Dipakai di Beranda dan Data Kamar. */
public class RoomListCard extends RoundedPanel {
    private static final int ROW_HEIGHT = 48;

    private final JPanel rows = new JPanel();
    private final Function<Kamar, String> detail;
    private BiConsumer<Kamar, MouseEvent> onRowClick;
    private Runnable onLink;

    public RoomListCard(String title, boolean showLink, Function<Kamar, String> detail) {
        super(44, Color.WHITE, true);
        this.detail = detail;
        setLayout(new BorderLayout());
        int s = inset();
        setBorder(new EmptyBorder(s + 10, s + 14, s + 6, s + 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
        header.add(new IconCircle(Icons.Name.DOOR, 20, Theme.TITLE), BorderLayout.WEST);
        JLabel titleLabel = Theme.label(title, 14, Font.BOLD, Theme.CARD_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        header.add(titleLabel, BorderLayout.CENTER);
        if (showLink) {
            JPanel link = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 11));
            link.setOpaque(false);
            JLabel text = Theme.label("Lihat Semua", 11, Font.BOLD, Theme.LINK);
            JLabel chevron = new JLabel(Icons.of(Icons.Name.CHEVRON, 12, Theme.LINK));
            link.add(text);
            link.add(chevron);
            link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onLink != null) {
                        onLink.run();
                    }
                }
            });
            header.add(link, BorderLayout.EAST);
        }
        add(header, BorderLayout.NORTH);

        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setOpaque(false);
        add(rows, BorderLayout.CENTER);
    }

    public void setOnRowClick(BiConsumer<Kamar, MouseEvent> onRowClick) {
        this.onRowClick = onRowClick;
    }

    public void setOnLink(Runnable onLink) {
        this.onLink = onLink;
    }

    public void setRows(List<Kamar> list) {
        rows.removeAll();
        for (Kamar kamar : list) {
            rows.add(new Row(kamar));
        }
        rows.revalidate();
        rows.repaint();
    }

    private final class Row extends JPanel {
        Row(Kamar kamar) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(0, 8, 0, 4));

            JLabel number = Theme.label(kamar.getNomor(), 12, Font.BOLD, new Color(0x3D6B52));
            number.setPreferredSize(new Dimension(54, ROW_HEIGHT));
            add(number, BorderLayout.WEST);
            add(Theme.label(detail.apply(kamar), 11, Font.PLAIN, Theme.MUTED), BorderLayout.CENTER);

            JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 13));
            east.setOpaque(false);
            east.add(new StatusPill(kamar.getStatus()));
            JLabel chevron = new JLabel(Icons.of(Icons.Name.CHEVRON, 14, Theme.CHEVRON));
            chevron.setPreferredSize(new Dimension(14, 22));
            east.add(chevron);
            add(east, BorderLayout.EAST);

            if (onRowClick != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onRowClick.accept(kamar, e);
                    }
                });
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, ROW_HEIGHT);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, ROW_HEIGHT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Theme.SEPARATOR);
            g.drawLine(0, 0, getWidth(), 0);
        }
    }
}
