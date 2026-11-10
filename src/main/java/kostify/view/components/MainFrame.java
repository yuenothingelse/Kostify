package kostify.view;

import kostify.Main;
import kostify.controller.KamarController;
import kostify.controller.PembayaranController;
import kostify.controller.PenghuniController;
import kostify.dao.KamarDao;
import kostify.dao.PembayaranDao;
import kostify.dao.PenghuniDao;
import kostify.dao.impl.KamarDaoImpl;
import kostify.dao.impl.PembayaranDaoImpl;
import kostify.dao.impl.PenghuniDaoImpl;
import kostify.service.KamarService;
import kostify.service.LaporanService;
import kostify.service.PembayaranService;
import kostify.service.PenghuniService;
import kostify.util.Session;
import kostify.view.components.BottomNav;
import kostify.view.components.Theme;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/** Jendela utama berukuran layar iPhone 16 (393 x 852): konten di atas, menu bawah di bawah. */
public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final BerandaPanel berandaPanel;
    private final KamarController kamarController;
    private final BottomNav nav;

    public MainFrame() {
        super("Kostify - Boarding House Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Perakitan objek (dependency injection manual)
        KamarDao kamarDao = new KamarDaoImpl();
        PenghuniDao penghuniDao = new PenghuniDaoImpl();
        PembayaranDao pembayaranDao = new PembayaranDaoImpl();

        LaporanService laporanService = new LaporanService(kamarDao);
        KamarService kamarService = new KamarService(kamarDao);
        PenghuniService penghuniService = new PenghuniService(penghuniDao, kamarDao);
        PembayaranService pembayaranService = new PembayaranService(pembayaranDao, penghuniDao);

        berandaPanel = new BerandaPanel(laporanService, kamarService);
        KamarPanel kamarPanel = new KamarPanel();
        PenghuniPanel penghuniPanel = new PenghuniPanel();
        PembayaranPanel pembayaranPanel = new PembayaranPanel();

        kamarController = new KamarController(kamarPanel, kamarService);
        new PenghuniController(penghuniPanel, penghuniService);
        new PembayaranController(pembayaranPanel, pembayaranService);

        berandaPanel.onLihatSemua(() -> showCard(BottomNav.KAMAR));
        berandaPanel.onLogout(() -> {
            Session.logout();
            dispose();
            Main.showLogin();
        });

        content.add(berandaPanel, BottomNav.BERANDA);
        content.add(kamarPanel, BottomNav.KAMAR);
        content.add(penghuniPanel, BottomNav.PENGHUNI);
        content.add(pembayaranPanel, BottomNav.PEMBAYARAN);

        nav = new BottomNav(this::showCard);

        JPanel root = new JPanel(new BorderLayout());
        root.add(content, BorderLayout.CENTER);
        root.add(nav, BorderLayout.SOUTH);
        root.setPreferredSize(new Dimension(Theme.WIDTH, Theme.HEIGHT));
        setContentPane(root);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        showCard(BottomNav.BERANDA);
    }

    private void showCard(String key) {
        cards.show(content, key);
        nav.setActive(key);
        if (BottomNav.BERANDA.equals(key)) {
            berandaPanel.refresh();
        } else if (BottomNav.KAMAR.equals(key)) {
            kamarController.refresh();
        }
    }
}
