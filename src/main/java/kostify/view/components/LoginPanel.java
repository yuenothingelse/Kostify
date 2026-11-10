package kostify.view;

import kostify.view.components.BackgroundPanel;
import kostify.view.components.Logo;
import kostify.view.components.PillButton;
import kostify.view.components.Theme;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionListener;

/** Isi halaman login. Hanya tampilan, tanpa logika bisnis. */
public class LoginPanel extends BackgroundPanel {
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final PillButton loginButton = new PillButton("Masuk");
    private final JLabel errorLabel = Theme.label(" ", 12, Font.PLAIN, Theme.DANGER);

    public LoginPanel() {
        setPreferredSize(new Dimension(Theme.WIDTH, Theme.HEIGHT));

        addField("Email", emailField, "admin@kostify.com", 356);
        addField("Password", passwordField, "Password", 436);
        errorLabel.setBounds(28, 514, 337, 18);
        add(errorLabel);
        loginButton.setBounds(28, 542, 337, 52);
        add(loginButton);
    }

    private void addField(String label, JTextField field, String placeholder, int y) {
        JLabel caption = Theme.label(label, 13, Font.BOLD, Theme.CARD_TITLE);
        caption.setBounds(28, y, 337, 18);
        add(caption);
        field.setFont(Theme.font(Font.PLAIN, 14));
        field.setMargin(new Insets(0, 16, 0, 16));
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JComponent.roundRect", true);
        field.setBounds(28, y + 22, 337, 48);
        add(field);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        int cx = getWidth() / 2;
        Logo.draw(g2, cx - 36, 150, 72, false);
        Theme.drawCentered(g2, "Kostify", Theme.font(Font.BOLD, 36), Theme.TITLE, cx, 254);
        Theme.drawCentered(g2, "Boarding House Management", Theme.font(Font.PLAIN, 14), Theme.SUBTITLE, cx, 278);
        g2.dispose();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    public void onLogin(ActionListener listener) {
        loginButton.addActionListener(listener);
        passwordField.addActionListener(listener);
    }

    public void setDefaultButtonOn(javax.swing.JRootPane rootPane) {
        rootPane.setDefaultButton(loginButton);
    }
}
