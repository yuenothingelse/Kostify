package com.kostify.dao;

import com.kostify.config.DatabaseConfig;
import com.kostify.model.Kamar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KamarDAO {

    public List<Kamar> tampilkan(String statusFilter, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT k.id_kamar, k.nomor, k.tipe, k.harga, k.status, " +
                "  (SELECT p.nama FROM penghuni p " +
                "   WHERE p.id_kamar = k.id_kamar AND p.status_aktif = 'Aktif' " +
                "   ORDER BY p.id_penghuni DESC LIMIT 1) AS penghuni_aktif " +
                "FROM kamar k WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (statusFilter != null && !statusFilter.isBlank() && !"Semua".equalsIgnoreCase(statusFilter)) {
            sql.append("AND k.status = ? ");
            params.add(statusFilter.trim());
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND (LOWER(k.nomor) LIKE ? OR LOWER(k.tipe) LIKE ?) ");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }
        sql.append("ORDER BY k.nomor ASC");

        List<Kamar> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Kamar kamar = new Kamar(
                            rs.getInt("id_kamar"),
                            rs.getString("nomor"),
                            rs.getString("tipe"),
                            rs.getBigDecimal("harga"),
                            rs.getString("status")
                    );
                    kamar.setNamaPenghuniAktif(rs.getString("penghuni_aktif"));
                    list.add(kamar);
                }
            }
        }
        return list;
    }

    public Kamar findById(int idKamar) throws SQLException {
        String sql = "SELECT id_kamar, nomor, tipe, harga, status FROM kamar WHERE id_kamar = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKamar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Kamar(
                            rs.getInt("id_kamar"),
                            rs.getString("nomor"),
                            rs.getString("tipe"),
                            rs.getBigDecimal("harga"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public Kamar findByNomor(String nomor) throws SQLException {
        String sql = "SELECT id_kamar, nomor, tipe, harga, status FROM kamar WHERE LOWER(nomor) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomor.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Kamar(
                            rs.getInt("id_kamar"),
                            rs.getString("nomor"),
                            rs.getString("tipe"),
                            rs.getBigDecimal("harga"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public int tambah(Kamar kamar) throws SQLException {
        String sql = "INSERT INTO kamar (nomor, tipe, harga, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kamar.getNomor().trim());
            stmt.setString(2, kamar.getTipe().trim());
            stmt.setBigDecimal(3, kamar.getHarga());
            stmt.setString(4, kamar.getStatus() != null ? kamar.getStatus() : "Kosong");
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public void ubah(Kamar kamar) throws SQLException {
        String sql = "UPDATE kamar SET nomor = ?, tipe = ?, harga = ?, status = ? WHERE id_kamar = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kamar.getNomor().trim());
            stmt.setString(2, kamar.getTipe().trim());
            stmt.setBigDecimal(3, kamar.getHarga());
            stmt.setString(4, kamar.getStatus());
            stmt.setInt(5, kamar.getIdKamar());
            stmt.executeUpdate();
        }
    }

    public void updateStatus(Connection conn, int idKamar, String status) throws SQLException {
        String sql = "UPDATE kamar SET status = ? WHERE id_kamar = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idKamar);
            stmt.executeUpdate();
        }
    }

    public void hapus(int idKamar) throws SQLException {
        String sql = "DELETE FROM kamar WHERE id_kamar = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKamar);
            stmt.executeUpdate();
        }
    }
}