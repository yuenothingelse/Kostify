package kostify.controller;

import kostify.exception.DataAccessException;
import kostify.exception.ValidationException;
import kostify.service.AuthService;
import kostify.view.LoginFrame;
import kostify.view.MainFrame;

public class LoginController {
    private final LoginFrame view;
    private final AuthService authService;

    public LoginController(LoginFrame view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        view.onLogin(e -> handleLogin());
    }

    private void handleLogin() {
        try {
            authService.login(view.getEmail(), view.getPassword());
            view.dispose();
            new MainFrame().setVisible(true);
        } catch (ValidationException e) {
            view.showError(e.getMessage());
        } catch (DataAccessException e) {
            view.showError("Terjadi kesalahan database");
        }
    }
}
