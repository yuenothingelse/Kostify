package com.kostify.model;

public class Pengelola {
    private int idPengelola;
    private String nama;
    private String email;
    private String passwordHash;

    public Pengelola() {}

    public Pengelola(int idPengelola, String nama, String email, String passwordHash) {
        this.idPengelola = idPengelola;
        this.nama = nama;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public boolean login(String inputEmail, String inputPassword) {
        if (inputEmail == null || inputPassword == null) return false;
        return this.email.equalsIgnoreCase(inputEmail.trim())
                && this.passwordHash.equals(inputPassword);
    }

    public void logout() {
    }

    public int getIdPengelola() { return idPengelola; }
    public void setIdPengelola(int idPengelola) { this.idPengelola = idPengelola; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
