package kostify.view;

import kostify.exception.DataAccessException;
import kostify.model.Kamar;
import kostify.model.Pengelola;
import kostify.service.KamarService;
import kostify.service.LaporanService;
import kostify.util.Session;
import kostify.view.components.CircleButton;
import kostify.view.components.HeaderPanel;
import kostify.view.components.IconCircle;
import kostify.view.components.Icons;
import kostify.view.components.Logo;
import kostify.view.components.RoomListCard;
import kostify.view.components.RoundedPanel;
import kostify.view.components.ScreenPanel;
import kostify.view.components.Theme;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

/**
 * Layar Beranda: header, tiga kartu statistik, dan ringkasan daftar kamar.
 * TODO: tampilkan nama penghuni pada daftar setelah modul Penghuni selesai,
 * serta kartu tunggakan dan pendapatan.
 */
public class BerandaPanel extends ScreenPanel {
    private final LaporanService laporanService;
    private final KamarService kamarService;
    private final StatCard totalCard = new StatCard(Icons.Name.HOME, "Total Kamar", false, false);
    private final StatCard terisiCard = new StatCard(Icons.Name.USERS, "Kamar Terisi", true, true);
    private final StatCard kosongCard = new StatCard(Icons.Name.DOOR, "Kamar Kosong", false, true);
    private final RoomListCard list = new RoomListCard("Daftar Kamar", true, k -> k.getTipe().toString());
    private Runnable onLogout;

    public BerandaPanel(LaporanService laporanService, KamarService kamarService) {
        this.laporanService = laporanService;
        this.kamarService = kamarService;
        setLayout(null);

        RoundedPanel stats = new RoundedPanel(56, Color.WHITE, true);
        stats.placeIn(this, 9, 198, 375, 139);
        stats.place(totalCard, 12, 13, 110, 111);
        stats.place(terisiCard, 132, 13, 110, 111);
        stats.place(kosongCard, 252, 13, 110, 111);

        add(list);
        list.setBounds(14 - list.inset(), 351 - list.inset(), 368 + 2 * list.inset(), 200);

        add(buildHeader()); // ditambahkan terakhir agar berada di bawah kartu (z-order)
        refresh();
    }

    private HeaderPanel buildHeader() {
        HeaderPanel header = new HeaderPanel(232);
        header.setBounds(0, 0, Theme.WIDTH, 232);

        Logo logo = new Logo(40, true);
        logo.setBounds(22, 52, 40, 40);
        header.add(logo);
        JLabel brand = Theme.label("Kostify", 18, Font.BOLD, Color.WHITE);
        brand.setBounds(74, 50, 250, 24);
        header.add(brand);
        JLabel tagline = Theme.label("Boarding House Management", 11, Font.PLAIN, Color.WHITE);
        tagline.setBounds(74, 74, 250, 16);
        header.add(tagline);

        CircleButton avatar = CircleButton.solid(Icons.Name.PERSON, 26, Theme.TITLE, Color.WHITE, 44, "Profil");
        avatar.setBounds(327, 76, 44, 44);
        avatar.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(a -> {
                if (onLogout != null) {
                    onLogout.run();
                }
            });
            menu.add(logout);
            menu.show(avatar, avatar.getWidth() - menu.getPreferredSize().width, avatar.getHeight() + 4);
        });
        header.add(avatar);

        Pengelola user = Session.getCurrent();
        JLabel halo = Theme.label("Halo,", 14, Font.PLAIN, Color.WHITE);
        halo.setBounds(22, 118, 200, 18);
        header.add(halo);
        JLabel name = Theme.label(user != null ? user.getNama() : "Pengelola", 22, Font.BOLD, Color.WHITE);
        name.setBounds(22, 136, 290, 28);
        header.add(name);
        JLabel greeting = Theme.label("Semoga harimu menyenangkan!", 12, Font.PLAIN, Color.WHITE);
        greeting.setBounds(22, 166, 290, 16);
        header.add(greeting);
        return header;
    }

    public void onLihatSemua(Runnable action) {
        list.setOnLink(action);
    }

    public void onLogout(Runnable action) {
        this.onLogout = action;
    }

    public void refresh() {
        try {
            totalCard.setValue(laporanService.totalKamar());
            terisiCard.setValue(laporanService.kamarTerisi());
            kosongCard.setValue(laporanService.kamarKosong());
            List<Kamar> all = kamarService.semuaKamar();
            list.setRows(all.subList(0, Math.min(4, all.size())));
        } catch (DataAccessException e) {
            list.setRows(List.of());
        }
        list.setBounds(14 - list.inset(), 351 - list.inset(),
                368 + 2 * list.inset(), list.getPreferredSize().height);
        revalidate();
        repaint();
    }

    /** Kartu statistik kecil berwarna mint. */
    private static final class StatCard extends RoundedPanel {
        private final JLabel value;

        StatCard(Icons.Name icon, String label, boolean boldLabel, boolean boldValue) {
            super(12, Theme.TINT, false);
            place(new IconCircle(icon, 20, Theme.TITLE), 14, 14, 34, 34);
            JLabel caption = Theme.label(label, 11, boldLabel ? Font.BOLD : Font.PLAIN, Theme.MUTED);
            place(caption, 14, 58, 92, 16);
            value = Theme.label("-", 22, boldValue ? Font.BOLD : Font.PLAIN, Theme.VALUE);
            place(value, 14, 76, 92, 28);
        }

        void setValue(int number) {
            value.setText(String.valueOf(number));
        }
    }
}
