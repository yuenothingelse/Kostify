package kostify.view;

import javax.swing.JFrame;
import java.awt.event.ActionListener;

/** Jendela login berukuran layar iPhone 16 (393 x 852). */
public class LoginFrame extends JFrame {
    private final LoginPanel panel = new LoginPanel();

    public LoginFrame() {
        super("Kostify - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel);
        panel.setDefaultButtonOn(getRootPane());
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public String getEmail() {
        return panel.getEmail();
    }

    public String getPassword() {
        return panel.getPassword();
    }

    public void showError(String message) {
        panel.showError(message);
    }

    public void onLogin(ActionListener listener) {
        panel.onLogin(listener);
    }
}
