package kostify.util;

import kostify.exception.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:kostify.db";

    private DatabaseConnection() {
    }

    /** Membuka koneksi baru. Tutup dengan try-with-resources di DAO. */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        try (Statement st = connection.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    /** Membuat tabel dari schema.sql jika belum ada. Dipanggil sekali di Main. */
    public static void initSchema() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/schema.sql")) {
            if (in == null) {
                throw new DataAccessException("File schema.sql tidak ditemukan", null);
            }
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
                for (String part : sql.split(";")) {
                    if (!part.isBlank()) {
                        st.execute(part.trim());
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new DataAccessException("Gagal menyiapkan database", e);
        }
    }
}
