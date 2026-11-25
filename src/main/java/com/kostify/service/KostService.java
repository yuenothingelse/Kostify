package com.kostify.service;

import com.kostify.config.DatabaseConfig;
import com.kostify.dao.KamarDAO;
import com.kostify.dao.PembayaranDAO;
import com.kostify.dao.PenghuniDAO;
import com.kostify.dao.PengelolaDAO;
import com.kostify.model.Kamar;
import com.kostify.model.Pembayaran;
import com.kostify.model.Penghuni;
import com.kostify.model.Pengelola;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KostService {

    private final PengelolaDAO pengelolaDAO = new PengelolaDAO();
    private final KamarDAO kamarDAO = new KamarDAO();
    private final PenghuniDAO penghuniDAO = new PenghuniDAO();
    private final PembayaranDAO pembayaranDAO = new PembayaranDAO();
    private final TagihanOtomatisService tagihanService =
            new TagihanOtomatisService(penghuniDAO, kamarDAO, pembayaranDAO);

    private Pengelola pengelolaAktif;

    public Pengelola login(String email, String password) throws SQLException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email dan password wajib diisi.");
        }
        Pengelola user = pengelolaDAO.findByEmail(email);
        if (user == null || !user.login(email, password)) {
            throw new IllegalArgumentException("Data Tidak Cocok. Periksa kembali email dan password.");
        }
        this.pengelolaAktif = user;
        tagihanService.sinkronkanTagihanPeriodeBerjalan();
        return user;
    }

    public void logout() {
        if (this.pengelolaAktif != null) {
            this.pengelolaAktif.logout();
            this.pengelolaAktif = null;
        }
    }

    public Pengelola getPengelolaAktif() {
        return pengelolaAktif;
    }

    public Map<String, Object> getDashboardData() throws SQLException {
        tagihanService.sinkronkanTagihanPeriodeBerjalan();

        List<Kamar> semuaKamar = kamarDAO.tampilkan("Semua", null);
        int totalKamar = semuaKamar.size();
        int kamarTerisi = 0;
        int kamarKosong = 0;
        for (Kamar k : semuaKamar) {
            if ("Terisi".equalsIgnoreCase(k.getStatus())) kamarTerisi++;
            else kamarKosong++;
        }

        List<Pembayaran> semuaPembayaran = pembayaranDAO.tampilkan("Semua", null);
        BigDecimal totalPendapatan = BigDecimal.ZERO;
        BigDecimal totalTunggakan = BigDecimal.ZERO;
        int jumlahTunggakan = 0;

        for (Pembayaran pay : semuaPembayaran) {
            if ("Lunas".equalsIgnoreCase(pay.getStatus())) {
                totalPendapatan = totalPendapatan.add(pay.getNominal());
            } else {
                totalTunggakan = totalTunggakan.add(pay.getNominal());
                jumlahTunggakan++;
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("adminNama", pengelolaAktif != null ? pengelolaAktif.getNama() : "Admin 1");
        data.put("totalKamar", totalKamar);
        data.put("kamarTerisi", kamarTerisi);
        data.put("kamarKosong", kamarKosong);
        data.put("totalPendapatan", totalPendapatan);
        data.put("totalTunggakan", totalTunggakan);
        data.put("jumlahTunggakan", jumlahTunggakan);
        data.put("previewKamar", semuaKamar.stream().limit(4).toList());
        return data;
    }

    // --- KAMAR ---
    public List<Kamar> getDaftarKamar(String status, String keyword) throws SQLException {
        return kamarDAO.tampilkan(status, keyword);
    }

    public Kamar tambahKamar(String nomor, String tipe, BigDecimal harga) throws SQLException {
        validasiKamar(nomor, tipe, harga);
        if (kamarDAO.findByNomor(nomor) != null) {
            throw new IllegalArgumentException("Nomor kamar " + nomor.trim() + " sudah terdaftar.");
        }
        Kamar kamar = new Kamar(0, nomor.trim(), tipe.trim(), harga, "Kosong");
        int id = kamarDAO.tambah(kamar);
        kamar.setIdKamar(id);
        return kamar;
    }

    public Kamar ubahKamar(int idKamar, String nomor, String tipe, BigDecimal harga, String status) throws SQLException {
        validasiKamar(nomor, tipe, harga);
        Kamar existing = kamarDAO.findById(idKamar);
        if (existing == null) {
            throw new IllegalArgumentException("Data kamar tidak ditemukan.");
        }
        Kamar duplikat = kamarDAO.findByNomor(nomor);
        if (duplikat != null && duplikat.getIdKamar() != idKamar) {
            throw new IllegalArgumentException("Nomor kamar " + nomor.trim() + " sudah digunakan kamar lain.");
        }
        existing.setNomor(nomor.trim());
        existing.setTipe(tipe.trim());
        existing.setHarga(harga);
        if (status != null && !status.isBlank()) {
            existing.setStatus(status);
        }
        kamarDAO.ubah(existing);
        return existing;
    }

    public void hapusKamar(int idKamar) throws SQLException {
        Kamar existing = kamarDAO.findById(idKamar);
        if (existing == null) {
            throw new IllegalArgumentException("Data kamar tidak ditemukan.");
        }
        kamarDAO.hapus(idKamar);
    }

    // --- PENGHUNI ---
    public List<Penghuni> getDaftarPenghuni(String status, String keyword) throws SQLException {
        return penghuniDAO.tampilkan(status, keyword);
    }

    public Penghuni tambahPenghuni(String nama, String noHp, String alamat, int idKamar,
                                   LocalDate tanggalMasuk, LocalDate tanggalKeluar) throws SQLException {
        validasiPenghuni(nama, noHp, alamat, tanggalMasuk);

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Kamar kamar = kamarDAO.findById(idKamar);
                if (kamar == null) {
                    throw new IllegalArgumentException("Kamar yang dipilih tidak ditemukan.");
                }
                if ("Terisi".equalsIgnoreCase(kamar.getStatus()) && penghuniDAO.countAktifByKamar(conn, idKamar) > 0) {
                    throw new IllegalArgumentException("Kamar " + kamar.getNomor() + " sedang terisi.");
                }

                Penghuni p = new Penghuni();
                p.setNama(nama.trim());
                p.setNoHp(noHp.trim());
                p.setAlamat(alamat.trim());
                p.setIdKamar(kamar.getIdKamar());
                p.setNoKamar(kamar.getNomor());
                p.setTanggalMasuk(tanggalMasuk);
                p.setTanggalKeluar(tanggalKeluar);
                p.setStatusAktif("Aktif");

                int idBaru = penghuniDAO.tambah(conn, p);
                p.setIdPenghuni(idBaru);

                kamarDAO.updateStatus(conn, kamar.getIdKamar(), "Terisi");
                tagihanService.buatTagihanUntukPenghuni(conn, p, tanggalMasuk);

                conn.commit();
                return p;
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof SQLException sqle) throw sqle;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Penghuni ubahPenghuni(int idPenghuni, String nama, String noHp, String alamat,
                                 int idKamarBaru, LocalDate tanggalMasuk, LocalDate tanggalKeluar,
                                 String statusAktif) throws SQLException {
        validasiPenghuni(nama, noHp, alamat, tanggalMasuk);

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Penghuni existing = penghuniDAO.findById(idPenghuni);
                if (existing == null) {
                    throw new IllegalArgumentException("Data penghuni tidak ditemukan.");
                }

                Integer idKamarLama = existing.getIdKamar();
                Kamar kamarBaru = kamarDAO.findById(idKamarBaru);
                if (kamarBaru == null) {
                    throw new IllegalArgumentException("Kamar baru tidak ditemukan.");
                }

                String statusFinal = (statusAktif != null && !statusAktif.isBlank()) ? statusAktif : existing.getStatusAktif();
                if ("Aktif".equalsIgnoreCase(statusFinal)
                        && (idKamarLama == null || idKamarLama != idKamarBaru)
                        && penghuniDAO.countAktifByKamar(conn, idKamarBaru) > 0) {
                    throw new IllegalArgumentException("Kamar " + kamarBaru.getNomor() + " sudah ditempati penghuni aktif lain.");
                }

                existing.setNama(nama.trim());
                existing.setNoHp(noHp.trim());
                existing.setAlamat(alamat.trim());
                existing.setIdKamar(kamarBaru.getIdKamar());
                existing.setNoKamar(kamarBaru.getNomor());
                existing.setTanggalMasuk(tanggalMasuk);
                existing.setTanggalKeluar("Keluar".equalsIgnoreCase(statusFinal) && tanggalKeluar == null
                        ? LocalDate.now() : tanggalKeluar);
                existing.setStatusAktif(statusFinal);

                penghuniDAO.ubah(conn, existing);

                if ("Aktif".equalsIgnoreCase(statusFinal)) {
                    kamarDAO.updateStatus(conn, kamarBaru.getIdKamar(), "Terisi");
                    tagihanService.buatTagihanUntukPenghuni(conn, existing, LocalDate.now());
                } else if (penghuniDAO.countAktifByKamar(conn, kamarBaru.getIdKamar()) == 0) {
                    kamarDAO.updateStatus(conn, kamarBaru.getIdKamar(), "Kosong");
                }

                if (idKamarLama != null && idKamarLama != idKamarBaru) {
                    if (penghuniDAO.countAktifByKamar(conn, idKamarLama) == 0) {
                        kamarDAO.updateStatus(conn, idKamarLama, "Kosong");
                    }
                }

                conn.commit();
                return existing;
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof SQLException sqle) throw sqle;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void keluarPenghuni(int idPenghuni, LocalDate tanggalKeluar) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Penghuni p = penghuniDAO.findById(idPenghuni);
                if (p == null) {
                    throw new IllegalArgumentException("Data penghuni tidak ditemukan.");
                }
                p.setStatusAktif("Keluar");
                p.setTanggalKeluar(tanggalKeluar != null ? tanggalKeluar : LocalDate.now());
                penghuniDAO.ubah(conn, p);

                if (p.getIdKamar() != null && penghuniDAO.countAktifByKamar(conn, p.getIdKamar()) == 0) {
                    kamarDAO.updateStatus(conn, p.getIdKamar(), "Kosong");
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof SQLException sqle) throw sqle;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void hapusPenghuni(int idPenghuni) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Penghuni p = penghuniDAO.findById(idPenghuni);
                if (p == null) {
                    throw new IllegalArgumentException("Data penghuni tidak ditemukan.");
                }
                Integer idKamar = p.getIdKamar();
                penghuniDAO.hapus(conn, idPenghuni);

                if (idKamar != null && penghuniDAO.countAktifByKamar(conn, idKamar) == 0) {
                    kamarDAO.updateStatus(conn, idKamar, "Kosong");
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof SQLException sqle) throw sqle;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // --- PEMBAYARAN ---
    public List<Pembayaran> getDaftarPembayaran(String status, String keyword) throws SQLException {
        tagihanService.sinkronkanTagihanPeriodeBerjalan();
        return pembayaranDAO.tampilkan(status, keyword);
    }

    public Pembayaran bayarTagihan(int idPembayaran, LocalDate tanggalBayar) throws SQLException {
        Pembayaran pay = pembayaranDAO.findById(idPembayaran);
        if (pay == null) {
            throw new IllegalArgumentException("Data pembayaran tidak ditemukan.");
        }
        pay.bayar(tanggalBayar != null ? tanggalBayar : LocalDate.now());
        pembayaranDAO.updateBayar(pay);
        return pay;
    }

    private void validasiKamar(String nomor, String tipe, BigDecimal harga) {
        if (nomor == null || nomor.isBlank()) throw new IllegalArgumentException("Nomor kamar wajib diisi.");
        if (tipe == null || tipe.isBlank()) throw new IllegalArgumentException("Tipe kamar wajib diisi.");
        if (harga == null || harga.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Harga sewa harus lebih besar dari 0.");
        }
    }

    private void validasiPenghuni(String nama, String noHp, String alamat, LocalDate tanggalMasuk) {
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama penghuni wajib diisi.");
        if (noHp == null || noHp.isBlank()) throw new IllegalArgumentException("Nomor HP wajib diisi.");
        if (alamat == null || alamat.isBlank()) throw new IllegalArgumentException("Alamat wajib diisi.");
        if (tanggalMasuk == null) throw new IllegalArgumentException("Tanggal masuk wajib diisi.");
    }
}