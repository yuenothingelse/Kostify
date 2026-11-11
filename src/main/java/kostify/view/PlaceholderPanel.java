package kostify.view;

import kostify.view.components.HeaderPanel;
import kostify.view.components.ScreenPanel;
import kostify.view.components.Theme;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

/** Layar sementara untuk modul yang belum dikerjakan. Ganti isinya, contoh lengkap: KamarPanel. */
public class PlaceholderPanel extends ScreenPanel {

    public PlaceholderPanel(String title) {
        setLayout(new BorderLayout());
        add(HeaderPanel.titled(title, "Segera hadir", 140), BorderLayout.NORTH);
        JLabel todo = Theme.label("Modul ini belum dikerjakan (TODO)", 14, Font.PLAIN, Theme.MUTED);
        todo.setHorizontalAlignment(JLabel.CENTER);
        add(todo, BorderLayout.CENTER);
    }
}
