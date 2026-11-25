package com.kostify.dao;

import com.kostify.config.DatabaseConfig;
import com.kostify.model.Pembayaran;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembayaranDAO {

    public List<Pembayaran> tampilkan(String statusFilter, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id_pembayaran, kode_pembayaran, id_penghuni, penghuni, no_hp, no_kamar, " +
                "periode, tanggal_jatuh_tempo, tanggal_bayar, nominal, status FROM pembayaran WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (statusFilter != null && !statusFilter.isBlank() && !"Semua".equalsIgnoreCase(statusFilter)) {
            sql.append("AND status = ? ");
            params.add(statusFilter.trim());
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND (LOWER(penghuni) LIKE ? OR LOWER(no_kamar) LIKE ? OR LOWER(kode_pembayaran) LIKE ? OR LOWER(periode) LIKE ?) ");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }
        sql.append("ORDER BY FIELD(status, 'Jatuh Tempo', 'Belum Lunas', 'Lunas'), tanggal_jatuh_tempo DESC, id_pembayaran DESC");

        List<Pembayaran> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Pembayaran findById(int idPembayaran) throws SQLException {
        String sql = "SELECT id_pembayaran, kode_pembayaran, id_penghuni, penghuni, no_hp, no_kamar, " +
                     "periode, tanggal_jatuh_tempo, tanggal_bayar, nominal, status FROM pembayaran WHERE id_pembayaran = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPembayaran);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean existsTagihan(Connection conn, int idPenghuni, String periode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pembayaran WHERE id_penghuni = ? AND periode = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPenghuni);
            stmt.setString(2, periode);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void tambah(Connection conn, Pembayaran p) throws SQLException {
        String sql = "INSERT INTO pembayaran (kode_pembayaran, id_penghuni, penghuni, no_hp, no_kamar, " +
                     "periode, tanggal_jatuh_tempo, tanggal_bayar, nominal, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getKodePembayaran());
            if (p.getIdPenghuni() != null) {
                stmt.setInt(2, p.getIdPenghuni());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, p.getPenghuni());
            stmt.setString(4, p.getNoHp());
            stmt.setString(5, p.getNoKamar());
            stmt.setString(6, p.getPeriode());
            stmt.setDate(7, Date.valueOf(p.getTanggalJatuhTempo()));
            if (p.getTanggalBayar() != null) {
                stmt.setDate(8, Date.valueOf(p.getTanggalBayar()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            stmt.setBigDecimal(9, p.getNominal());
            stmt.setString(10, p.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateBayar(Pembayaran p) throws SQLException {
        String sql = "UPDATE pembayaran SET status = ?, tanggal_bayar = ? WHERE id_pembayaran = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getStatus());
            stmt.setDate(2, Date.valueOf(p.getTanggalBayar()));
            stmt.setInt(3, p.getIdPembayaran());
            stmt.executeUpdate();
        }
    }

    public void tandaiJatuhTempo(Connection conn) throws SQLException {
        String sql = "UPDATE pembayaran SET status = 'Jatuh Tempo' " +
                     "WHERE status = 'Belum Lunas' AND tanggal_jatuh_tempo < CURRENT_DATE()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    private Pembayaran mapRow(ResultSet rs) throws SQLException {
        Pembayaran p = new Pembayaran();
        p.setIdPembayaran(rs.getInt("id_pembayaran"));
        p.setKodePembayaran(rs.getString("kode_pembayaran"));
        int idPenghuni = rs.getInt("id_penghuni");
        p.setIdPenghuni(rs.wasNull() ? null : idPenghuni);
        p.setPenghuni(rs.getString("penghuni"));
        p.setNoHp(rs.getString("no_hp"));
        p.setNoKamar(rs.getString("no_kamar"));
        p.setPeriode(rs.getString("periode"));
        Date jatuhTempo = rs.getDate("tanggal_jatuh_tempo");
        p.setTanggalJatuhTempo(jatuhTempo != null ? jatuhTempo.toLocalDate() : null);
        Date bayar = rs.getDate("tanggal_bayar");
        p.setTanggalBayar(bayar != null ? bayar.toLocalDate() : null);
        p.setNominal(rs.getBigDecimal("nominal"));
        p.setStatus(rs.getString("status"));
        return p;
    }
}