CREATE DATABASE IF NOT EXISTS kostify_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE kostify_db;

-- 1. Tabel Pengelola (Admin)
CREATE TABLE IF NOT EXISTS pengelola (
    id_pengelola INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabel Kamar
CREATE TABLE IF NOT EXISTS kamar (
    id_kamar INT AUTO_INCREMENT PRIMARY KEY,
    nomor VARCHAR(20) NOT NULL UNIQUE,
    tipe VARCHAR(50) NOT NULL,
    harga DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    status ENUM('Kosong', 'Terisi') NOT NULL DEFAULT 'Kosong',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Tabel Penghuni
CREATE TABLE IF NOT EXISTS penghuni (
    id_penghuni INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    no_hp VARCHAR(25) NOT NULL,
    alamat TEXT NOT NULL,
    id_kamar INT NULL,
    no_kamar VARCHAR(20) NULL,
    tanggal_masuk DATE NOT NULL,
    tanggal_keluar DATE NULL,
    status_aktif ENUM('Aktif', 'Keluar') NOT NULL DEFAULT 'Aktif',
    CONSTRAINT fk_penghuni_kamar
        FOREIGN KEY (id_kamar) REFERENCES kamar(id_kamar)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- 4. Tabel Pembayaran / Tagihan Sewa
CREATE TABLE IF NOT EXISTS pembayaran (
    id_pembayaran INT AUTO_INCREMENT PRIMARY KEY,
    kode_pembayaran VARCHAR(30) NOT NULL UNIQUE,
    id_penghuni INT NULL,
    penghuni VARCHAR(100) NOT NULL,
    no_hp VARCHAR(25) NOT NULL,
    no_kamar VARCHAR(20) NOT NULL,
    periode VARCHAR(30) NOT NULL, -- contoh: '2026-11' atau 'November 2026'
    tanggal_jatuh_tempo DATE NOT NULL,
    tanggal_bayar DATE NULL,
    nominal DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    status ENUM('Lunas', 'Belum Lunas', 'Jatuh Tempo') NOT NULL DEFAULT 'Belum Lunas',
    CONSTRAINT fk_pembayaran_penghuni
        FOREIGN KEY (id_penghuni) REFERENCES penghuni(id_penghuni)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Data awal Pengelola (password contoh: admin123 -> simpan hash/verifikasi aman di service)
INSERT IGNORE INTO pengelola (nama, email, password_hash)
VALUES ('Admin 1', 'admin@kostify.id', 'admin123');

-- Data awal Kamar (sesuai nuansa mockup)
INSERT IGNORE INTO kamar (nomor, tipe, harga, status) VALUES
('A1', 'Standard AC', 1500000.00, 'Terisi'),
('A2', 'VIP Kamar Mandi Dalam', 2000000.00, 'Terisi'),
('A3', 'Standard Kipas', 1100000.00, 'Kosong'),
('A4', 'Standard AC', 1500000.00, 'Terisi'),
('B1', 'VIP Kamar Mandi Dalam', 2000000.00, 'Kosong'),
('B2', 'Standard Kipas', 1100000.00, 'Kosong');