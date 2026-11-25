package com.kostify.dao;

import com.kostify.config.DatabaseConfig;
import com.kostify.model.Penghuni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenghuniDAO {

    public List<Penghuni> tampilkan(String statusFilter, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id_penghuni, nama, no_hp, alamat, id_kamar, no_kamar, " +
                "tanggal_masuk, tanggal_keluar, status_aktif FROM penghuni WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (statusFilter != null && !statusFilter.isBlank() && !"Semua".equalsIgnoreCase(statusFilter)) {
            sql.append("AND status_aktif = ? ");
            params.add(statusFilter.trim());
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND (LOWER(nama) LIKE ? OR LOWER(no_kamar) LIKE ? OR LOWER(no_hp) LIKE ?) ");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }
        sql.append("ORDER BY status_aktif ASC, id_penghuni DESC");

        List<Penghuni> list = new ArrayList<>();
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

    public Penghuni findById(int idPenghuni) throws SQLException {
        String sql = "SELECT id_penghuni, nama, no_hp, alamat, id_kamar, no_kamar, " +
                     "tanggal_masuk, tanggal_keluar, status_aktif FROM penghuni WHERE id_penghuni = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPenghuni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int tambah(Connection conn, Penghuni p) throws SQLException {
        String sql = "INSERT INTO penghuni (nama, no_hp, alamat, id_kamar, no_kamar, tanggal_masuk, tanggal_keluar, status_aktif) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNama().trim());
            stmt.setString(2, p.getNoHp().trim());
            stmt.setString(3, p.getAlamat().trim());
            if (p.getIdKamar() != null) {
                stmt.setInt(4, p.getIdKamar());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, p.getNoKamar());
            stmt.setDate(6, Date.valueOf(p.getTanggalMasuk()));
            if (p.getTanggalKeluar() != null) {
                stmt.setDate(7, Date.valueOf(p.getTanggalKeluar()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.setString(8, p.getStatusAktif() != null ? p.getStatusAktif() : "Aktif");
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public void ubah(Connection conn, Penghuni p) throws SQLException {
        String sql = "UPDATE penghuni SET nama = ?, no_hp = ?, alamat = ?, id_kamar = ?, no_kamar = ?, " +
                     "tanggal_masuk = ?, tanggal_keluar = ?, status_aktif = ? WHERE id_penghuni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNama().trim());
            stmt.setString(2, p.getNoHp().trim());
            stmt.setString(3, p.getAlamat().trim());
            if (p.getIdKamar() != null) {
                stmt.setInt(4, p.getIdKamar());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, p.getNoKamar());
            stmt.setDate(6, Date.valueOf(p.getTanggalMasuk()));
            if (p.getTanggalKeluar() != null) {
                stmt.setDate(7, Date.valueOf(p.getTanggalKeluar()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.setString(8, p.getStatusAktif());
            stmt.setInt(9, p.getIdPenghuni());
            stmt.executeUpdate();
        }
    }

    public int countAktifByKamar(Connection conn, int idKamar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM penghuni WHERE id_kamar = ? AND status_aktif = 'Aktif'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKamar);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void hapus(Connection conn, int idPenghuni) throws SQLException {
        String sql = "DELETE FROM penghuni WHERE id_penghuni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPenghuni);
            stmt.executeUpdate();
        }
    }

    private Penghuni mapRow(ResultSet rs) throws SQLException {
        Penghuni p = new Penghuni();
        p.setIdPenghuni(rs.getInt("id_penghuni"));
        p.setNama(rs.getString("nama"));
        p.setNoHp(rs.getString("no_hp"));
        p.setAlamat(rs.getString("alamat"));
        int idKamar = rs.getInt("id_kamar");
        p.setIdKamar(rs.wasNull() ? null : idKamar);
        p.setNoKamar(rs.getString("no_kamar"));
        Date masuk = rs.getDate("tanggal_masuk");
        p.setTanggalMasuk(masuk != null ? masuk.toLocalDate() : null);
        Date keluar = rs.getDate("tanggal_keluar");
        p.setTanggalKeluar(keluar != null ? keluar.toLocalDate() : null);
        p.setStatusAktif(rs.getString("status_aktif"));
        return p;
    }
}