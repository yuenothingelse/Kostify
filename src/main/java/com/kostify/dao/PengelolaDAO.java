package com.kostify.dao;

import com.kostify.config.DatabaseConfig;
import com.kostify.model.Pengelola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PengelolaDAO {

    public Pengelola findByEmail(String email) throws SQLException {
        String sql = "SELECT id_pengelola, nama, email, password_hash FROM pengelola WHERE LOWER(email) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pengelola(
                            rs.getInt("id_pengelola"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("password_hash")
                    );
                }
            }
        }
        return null;
    }
}