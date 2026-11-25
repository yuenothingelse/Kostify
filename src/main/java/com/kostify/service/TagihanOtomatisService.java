package com.kostify.service;

import com.kostify.config.DatabaseConfig;
import com.kostify.dao.KamarDAO;
import com.kostify.dao.PembayaranDAO;
import com.kostify.dao.PenghuniDAO;
import com.kostify.model.Kamar;
import com.kostify.model.Pembayaran;
import com.kostify.model.Penghuni;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TagihanOtomatisService {

    private final PenghuniDAO penghuniDAO;
    private final KamarDAO kamarDAO;
    private final PembayaranDAO pembayaranDAO;

    public TagihanOtomatisService(PenghuniDAO penghuniDAO, KamarDAO kamarDAO, PembayaranDAO pembayaranDAO) {
        this.penghuniDAO = penghuniDAO;
        this.kamarDAO = kamarDAO;
        this.pembayaranDAO = pembayaranDAO;
    }

    public static String formatPeriodeBulanan(LocalDate tanggal) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"));
        return tanggal.format(fmt);
    }

    public static LocalDate hitungJatuhTempo(LocalDate tanggalReferensi) {
        YearMonth ym = YearMonth.from(tanggalReferensi);
        int hariJatuhTempo = Math.min(10, ym.lengthOfMonth());
        return ym.atDay(hariJatuhTempo);
    }

    public void buatTagihanUntukPenghuni(Connection conn, Penghuni penghuni, LocalDate tanggalAcuan) throws SQLException {
        if (penghuni == null || !"Aktif".equalsIgnoreCase(penghuni.getStatusAktif())) return;

        String periode = formatPeriodeBulanan(tanggalAcuan);
        if (pembayaranDAO.existsTagihan(conn, penghuni.getIdPenghuni(), periode)) {
            return;
        }

        BigDecimal nominal = BigDecimal.ZERO;
        String noKamar = penghuni.getNoKamar() != null ? penghuni.getNoKamar() : "-";
        if (penghuni.getIdKamar() != null) {
            Kamar kamar = kamarDAO.findById(penghuni.getIdKamar());
            if (kamar != null) {
                nominal = kamar.getHarga();
                noKamar = kamar.getNomor();
            }
        }

        LocalDate jatuhTempo = hitungJatuhTempo(tanggalAcuan);
        String statusAwal = LocalDate.now().isAfter(jatuhTempo) ? "Jatuh Tempo" : "Belum Lunas";

        Pembayaran tagihan = new Pembayaran();
        tagihan.setKodePembayaran("INV-" + tanggalAcuan.format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        tagihan.setIdPenghuni(penghuni.getIdPenghuni());
        tagihan.setPenghuni(penghuni.getNama());
        tagihan.setNoHp(penghuni.getNoHp());
        tagihan.setNoKamar(noKamar);
        tagihan.setPeriode(periode);
        tagihan.setTanggalJatuhTempo(jatuhTempo);
        tagihan.setTanggalBayar(null);
        tagihan.setNominal(nominal);
        tagihan.setStatus(statusAwal);

        pembayaranDAO.tambah(conn, tagihan);
    }

    public void sinkronkanTagihanPeriodeBerjalan() throws SQLException {
        LocalDate hariIni = LocalDate.now();
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                List<Penghuni> daftarAktif = penghuniDAO.tampilkan("Aktif", null);
                for (Penghuni p : daftarAktif) {
                    buatTagihanUntukPenghuni(conn, p, hariIni);
                }
                pembayaranDAO.tandaiJatuhTempo(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}