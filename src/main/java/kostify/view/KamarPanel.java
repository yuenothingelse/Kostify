package kostify.view;

import kostify.exception.ValidationException;
import kostify.model.Kamar;
import kostify.model.enums.StatusKamar;
import kostify.model.enums.TipeKamar;
import kostify.util.FormatUtil;
import kostify.view.components.CircleButton;
import kostify.view.components.HeaderPanel;
import kostify.view.components.Icons;
import kostify.view.components.RoomListCard;
import kostify.view.components.ScreenPanel;
import kostify.view.components.Theme;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Layar Data Kamar. Ketuk satu kamar untuk memilih Ubah atau Hapus; tombol bulat
 * di kanan bawah untuk menambah kamar. Logika bisnis ada di KamarService.
 */
public class KamarPanel extends ScreenPanel {
    private final JLabel countLabel;
    private final RoomListCard card = new RoomListCard("Semua Kamar", false,
            k -> k.getTipe() + " \u00B7 " + FormatUtil.rupiah(k.getHarga()));
    private final CircleButton fab = CircleButton.gradient(Icons.Name.PLUS, 26, Color.WHITE, 56, "Tambah kamar");
    private ActionListener ubahListener;
    private ActionListener hapusListener;
    private int selectedId = -1;

    public KamarPanel() {
        setLayout(new BorderLayout());

        HeaderPanel header = HeaderPanel.titled("Data Kamar", "0 kamar terdaftar", 140);
        countLabel = header.getSubtitleLabel();
        add(header, BorderLayout.NORTH);

        card.setOnRowClick(this::showRowMenu);
        JPanel holder = new JPanel(new BorderLayout());
        holder.setOpaque(false);
        holder.setBorder(new EmptyBorder(8, 8, 8, 8));
        holder.add(card, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(holder);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 22, 6));
        south.setOpaque(false);
        south.add(fab);
        add(south, BorderLayout.SOUTH);
    }

    public void setData(List<Kamar> list) {
        countLabel.setText(list.size() + " kamar terdaftar");
        card.setRows(list);
        revalidate();
        repaint();
    }

    /** @return id kamar yang terakhir diketuk, atau -1 jika belum ada. */
    public int getSelectedId() {
        return selectedId;
    }

    private void showRowMenu(Kamar kamar, MouseEvent e) {
        selectedId = kamar.getId();
        JPopupMenu menu = new JPopupMenu();
        JMenuItem ubah = new JMenuItem("Ubah");
        ubah.addActionListener(a -> fire(ubahListener, a));
        JMenuItem hapus = new JMenuItem("Hapus");
        hapus.addActionListener(a -> fire(hapusListener, a));
        menu.add(ubah);
        menu.add(hapus);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    private static void fire(ActionListener listener, ActionEvent event) {
        if (listener != null) {
            listener.actionPerformed(event);
        }
    }

    /** Menampilkan form tambah/ubah. @return Kamar hasil input, atau null jika dibatalkan atau tidak valid. */
    public Kamar showForm(Kamar existing) {
        JTextField nomor = new JTextField(existing == null ? "" : existing.getNomor(), 12);
        JComboBox<TipeKamar> tipe = new JComboBox<>(TipeKamar.values());
        JTextField harga = new JTextField(existing == null ? "" : String.valueOf(existing.getHarga()), 12);
        if (existing != null) {
            tipe.setSelectedItem(existing.getTipe());
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nomor Kamar"));
        form.add(nomor);
        form.add(new JLabel("Tipe"));
        form.add(tipe);
        form.add(new JLabel("Harga (Rp)"));
        form.add(harga);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Tambah Kamar" : "Ubah Kamar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        try {
            long hargaValue = Long.parseLong(harga.getText().trim());
            StatusKamar status = existing == null ? StatusKamar.KOSONG : existing.getStatus();
            return new Kamar(0, nomor.getText(), (TipeKamar) tipe.getSelectedItem(), hargaValue, status);
        } catch (NumberFormatException e) {
            showError("Harga harus berupa angka");
        } catch (ValidationException e) {
            showError(e.getMessage());
        }
        return null;
    }

    public boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Konfirmasi",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Gagal", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onTambah(ActionListener l) {
        fab.addActionListener(l);
    }

    public void onUbah(ActionListener l) {
        this.ubahListener = l;
    }

    public void onHapus(ActionListener l) {
        this.hapusListener = l;
    }
}
