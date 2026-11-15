package kostify.model;

public class Pengelola extends Person {
    private final String email;
    private final String passwordHash;

    public Pengelola(int id, String nama, String email, String passwordHash) {
        super(id, nama);
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String getPeran() {
        return "Pengelola";
    }
}
