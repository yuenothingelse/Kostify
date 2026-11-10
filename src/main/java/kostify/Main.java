package kostify;

import com.formdev.flatlaf.FlatLightLaf;
import kostify.controller.LoginController;
import kostify.dao.impl.PengelolaDaoImpl;
import kostify.exception.DataAccessException;
import kostify.service.AuthService;
import kostify.util.DatabaseConnection;
import kostify.view.LoginFrame;
import kostify.view.SplashFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 16);
        UIManager.put("Component.borderColor", new java.awt.Color(0xCDE8D6));
        FlatLightLaf.setup();

        try {
            DatabaseConnection.initSchema();
            new AuthService(new PengelolaDaoImpl()).seedDefaultAdmin();
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Kostify", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> SplashFrame.show(Main::showLogin));
    }

    /** Menampilkan halaman login. Juga dipakai saat logout. */
    public static void showLogin() {
        LoginFrame frame = new LoginFrame();
        new LoginController(frame, new AuthService(new PengelolaDaoImpl()));
        frame.setVisible(true);
    }
}
