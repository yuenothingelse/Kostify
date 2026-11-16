package kostify.dao.impl;

import kostify.dao.PengelolaDao;
import kostify.exception.DataAccessException;
import kostify.model.Pengelola;
import kostify.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class PengelolaDaoImpl implements PengelolaDao {

    @Override
    public Optional<Pengelola> findByEmail(String email) {
        String sql = "SELECT id, nama, email, password_hash FROM pengelola WHERE email = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Pengelola(
                            rs.getInt("id"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("password_hash")));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca data pengelola", e);
        }
    }

    @Override
    public void insert(Pengelola p) {
        String sql = "INSERT INTO pengelola(nama, email, password_hash) VALUES(?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getPasswordHash());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan data pengelola", e);
        }
    }

    @Override
    public int count() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM pengelola")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghitung data pengelola", e);
        }
    }
}
