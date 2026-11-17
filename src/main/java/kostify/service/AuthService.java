package kostify.service;

import kostify.dao.PengelolaDao;
import kostify.exception.ValidationException;
import kostify.model.Pengelola;
import kostify.util.PasswordUtil;
import kostify.util.Session;
import kostify.util.Validator;

public class AuthService {
    private final PengelolaDao dao;

    public AuthService(PengelolaDao dao) {
        this.dao = dao;
    }

    public Pengelola login(String email, String password) {
        Validator.requireNotBlank("Email", email);
        Validator.requireNotBlank("Password", password);

        Pengelola pengelola = dao.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ValidationException("Email atau password salah"));
        if (!PasswordUtil.verify(password, pengelola.getPasswordHash())) {
            throw new ValidationException("Email atau password salah");
        }
        Session.login(pengelola);
        return pengelola;
    }

    public void logout() {
        Session.logout();
    }

    /** Membuat akun admin awal jika tabel pengelola masih kosong. */
    public void seedDefaultAdmin() {
        if (dao.count() == 0) {
            dao.insert(new Pengelola(0, "Admin", "admin@kostify.com", PasswordUtil.hash("admin123")));
        }
    }
}
