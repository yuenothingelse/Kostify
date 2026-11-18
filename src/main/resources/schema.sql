CREATE TABLE IF NOT EXISTS pengelola (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    nama          TEXT NOT NULL,
    email         TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS kamar (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    nomor  TEXT NOT NULL UNIQUE,
    tipe   TEXT NOT NULL,
    harga  INTEGER NOT NULL,
    status TEXT NOT NULL DEFAULT 'KOSONG'
);

CREATE TABLE IF NOT EXISTS penghuni (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    nama           TEXT NOT NULL,
    no_hp          TEXT NOT NULL,
    alamat         TEXT,
    kamar_id       INTEGER REFERENCES kamar(id),
    tanggal_masuk  TEXT NOT NULL,
    tanggal_keluar TEXT
);

CREATE TABLE IF NOT EXISTS pembayaran (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    penghuni_id   INTEGER NOT NULL REFERENCES penghuni(id) ON DELETE CASCADE,
    periode       TEXT NOT NULL,
    tanggal_bayar TEXT,
    nominal       INTEGER NOT NULL,
    status        TEXT NOT NULL DEFAULT 'BELUM_BAYAR'
)
