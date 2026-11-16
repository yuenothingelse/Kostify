package kostify.dao.impl;

import kostify.dao.KamarDao;
import kostify.exception.DataAccessException;
import kostify.model.Kamar;
import kostify.model.enums.StatusKamar;
import kostify.model.enums.TipeKamar;
import kostify.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementasi JDBC lengkap. Pakai file ini sebagai contoh untuk DAO lain. */
public class KamarDaoImpl implements KamarDao {
    private static final String SELECT = "SELECT id, nomor, tipe, harga, status FROM kamar";

    @Override
    public List<Kamar> findAll() {
        return query(SELECT + " ORDER BY nomor");
    }

    @Override
    public Optional<Kamar> findById(int id) {
        return query(SELECT + " WHERE id = ?", id).stream().findFirst();
    }

    @Override
    public Optional<Kamar> findByNomor(String nomor) {
        return query(SELECT + " WHERE nomor = ?", nomor).stream().findFirst();
    }

    @Override
    public List<Kamar> findByStatus(StatusKamar status) {
        return query(SELECT + " WHERE status = ? ORDER BY nomor", status.name());
    }

    @Override
    public void insert(Kamar k) {
        String sql = "INSERT INTO kamar(nomor, tipe, harga, status) VALUES(?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, k.getNomor());
            ps.setString(2, k.getTipe().name());
            ps.setLong(3, k.getHarga());
            ps.setString(4, k.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    k.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan data kamar", e);
        }
    }

    @Override
    public void update(Kamar k) {
        String sql = "UPDATE kamar SET nomor = ?, tipe = ?, harga = ?, status = ? WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNomor());
            ps.setString(2, k.getTipe().name());
            ps.setLong(3, k.getHarga());
            ps.setString(4, k.getStatus().name());
            ps.setInt(5, k.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah data kamar", e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM kamar WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus data kamar", e);
        }
    }

    private List<Kamar> query(String sql, Object... params) {
        List<Kamar> result = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca data kamar", e);
        }
        return result;
    }

    private Kamar map(ResultSet rs) throws SQLException {
        return new Kamar(
                rs.getInt("id"),
                rs.getString("nomor"),
                TipeKamar.valueOf(rs.getString("tipe")),
                rs.getLong("harga"),
                StatusKamar.valueOf(rs.getString("status")));
    }
}
