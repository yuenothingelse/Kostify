package com.kostify.bridge;

import com.kostify.model.Kamar;
import com.kostify.model.Pembayaran;
import com.kostify.model.Penghuni;
import com.kostify.model.Pengelola;
import com.kostify.service.KostService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FrontendBridge {

    private final KostService kostService;

    public FrontendBridge(KostService kostService) {
        this.kostService = kostService;
    }

    public String login(String email, String password) {
        try {
            Pengelola user = kostService.login(email, password);
            String json = "{\"idPengelola\":" + user.getIdPengelola()
                    + ",\"nama\":" + esc(user.getNama())
                    + ",\"email\":" + esc(user.getEmail()) + "}";
            return okJson(json);
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String logout() {
        try {
            kostService.logout();
            return okJson("true");
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String getDashboardData() {
        try {
            Map<String, Object> d = kostService.getDashboardData();
            @SuppressWarnings("unchecked")
            List<Kamar> preview = (List<Kamar>) d.get("previewKamar");
            String json = "{"
                    + "\"adminNama\":" + esc(String.valueOf(d.get("adminNama"))) + ","
                    + "\"totalKamar\":" + d.get("totalKamar") + ","
                    + "\"kamarTerisi\":" + d.get("kamarTerisi") + ","
                    + "\"kamarKosong\":" + d.get("kamarKosong") + ","
                    + "\"totalPendapatan\":" + ((BigDecimal) d.get("totalPendapatan")).toPlainString() + ","
                    + "\"totalTunggakan\":" + ((BigDecimal) d.get("totalTunggakan")).toPlainString() + ","
                    + "\"jumlahTunggakan\":" + d.get("jumlahTunggakan") + ","
                    + "\"previewKamar\":" + kamarListToJson(preview)
                    + "}";
            return okJson(json);
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    // --- KAMAR ---
    public String getDaftarKamar(String status, String keyword) {
        try {
            List<Kamar> list = kostService.getDaftarKamar(status, keyword);
            return okJson(kamarListToJson(list));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String tambahKamar(String nomor, String tipe, String hargaStr) {
        try {
            BigDecimal harga = parseBigDecimal(hargaStr);
            Kamar k = kostService.tambahKamar(nomor, tipe, harga);
            return okJson(kamarToJson(k));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String ubahKamar(int idKamar, String nomor, String tipe, String hargaStr, String status) {
        try {
            BigDecimal harga = parseBigDecimal(hargaStr);
            Kamar k = kostService.ubahKamar(idKamar, nomor, tipe, harga, status);
            return okJson(kamarToJson(k));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String hapusKamar(int idKamar) {
        try {
            kostService.hapusKamar(idKamar);
            return okJson("true");
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    // --- PENGHUNI ---
    public String getDaftarPenghuni(String status, String keyword) {
        try {
            List<Penghuni> list = kostService.getDaftarPenghuni(status, keyword);
            return okJson(penghuniListToJson(list));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String tambahPenghuni(String nama, String noHp, String alamat, int idKamar,
                                 String tanggalMasukStr, String tanggalKeluarStr) {
        try {
            LocalDate masuk = parseDate(tanggalMasukStr);
            LocalDate keluar = (tanggalKeluarStr == null || tanggalKeluarStr.isBlank())
                    ? null : parseDate(tanggalKeluarStr);
            Penghuni p = kostService.tambahPenghuni(nama, noHp, alamat, idKamar, masuk, keluar);
            return okJson(penghuniToJson(p));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String ubahPenghuni(int idPenghuni, String nama, String noHp, String alamat,
                               int idKamar, String tanggalMasukStr, String tanggalKeluarStr,
                               String statusAktif) {
        try {
            LocalDate masuk = parseDate(tanggalMasukStr);
            LocalDate keluar = (tanggalKeluarStr == null || tanggalKeluarStr.isBlank())
                    ? null : parseDate(tanggalKeluarStr);
            Penghuni p = kostService.ubahPenghuni(idPenghuni, nama, noHp, alamat, idKamar, masuk, keluar, statusAktif);
            return okJson(penghuniToJson(p));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String keluarPenghuni(int idPenghuni, String tanggalKeluarStr) {
        try {
            LocalDate keluar = (tanggalKeluarStr == null || tanggalKeluarStr.isBlank())
                    ? LocalDate.now() : parseDate(tanggalKeluarStr);
            kostService.keluarPenghuni(idPenghuni, keluar);
            return okJson("true");
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String hapusPenghuni(int idPenghuni) {
        try {
            kostService.hapusPenghuni(idPenghuni);
            return okJson("true");
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    // --- PEMBAYARAN ---
    public String getDaftarPembayaran(String status, String keyword) {
        try {
            List<Pembayaran> list = kostService.getDaftarPembayaran(status, keyword);
            return okJson(pembayaranListToJson(list));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    public String bayarTagihan(int idPembayaran, String tanggalBayarStr) {
        try {
            LocalDate tglBayar = (tanggalBayarStr == null || tanggalBayarStr.isBlank())
                    ? LocalDate.now() : parseDate(tanggalBayarStr);
            Pembayaran pay = kostService.bayarTagihan(idPembayaran, tglBayar);
            return okJson(pembayaranToJson(pay));
        } catch (Exception e) {
            return errJson(e.getMessage());
        }
    }

    // --- HELPER SERIALISASI & PARSING ---
    private BigDecimal parseBigDecimal(String raw) {
        if (raw == null || raw.isBlank()) throw new IllegalArgumentException("Nominal harga wajib diisi.");
        String cleaned = raw.replaceAll("[^0-9.]", "");
        return new BigDecimal(cleaned);
    }

    private LocalDate parseDate(String raw) {
        return LocalDate.parse(raw.trim());
    }

    private String okJson(String dataJson) {
        return "{\"ok\":true,\"data\":" + dataJson + "}";
    }

    private String errJson(String msg) {
        return "{\"ok\":false,\"error\":" + esc(msg != null ? msg : "Terjadi kesalahan sistem.") + "}";
    }

    private String esc(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    private String kamarToJson(Kamar k) {
        return "{"
                + "\"idKamar\":" + k.getIdKamar() + ","
                + "\"nomor\":" + esc(k.getNomor()) + ","
                + "\"tipe\":" + esc(k.getTipe()) + ","
                + "\"harga\":" + (k.getHarga() != null ? k.getHarga().toPlainString() : "0") + ","
                + "\"status\":" + esc(k.getStatus()) + ","
                + "\"namaPenghuniAktif\":" + esc(k.getNamaPenghuniAktif())
                + "}";
    }

    private String kamarListToJson(List<Kamar> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(kamarToJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private String penghuniToJson(Penghuni p) {
        return "{"
                + "\"idPenghuni\":" + p.getIdPenghuni() + ","
                + "\"nama\":" + esc(p.getNama()) + ","
                + "\"noHp\":" + esc(p.getNoHp()) + ","
                + "\"alamat\":" + esc(p.getAlamat()) + ","
                + "\"idKamar\":" + (p.getIdKamar() != null ? p.getIdKamar() : "null") + ","
                + "\"noKamar\":" + esc(p.getNoKamar()) + ","
                + "\"tanggalMasuk\":" + esc(p.getTanggalMasuk() != null ? p.getTanggalMasuk().toString() : null) + ","
                + "\"tanggalKeluar\":" + esc(p.getTanggalKeluar() != null ? p.getTanggalKeluar().toString() : null) + ","
                + "\"statusAktif\":" + esc(p.getStatusAktif())
                + "}";
    }

    private String penghuniListToJson(List<Penghuni> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(penghuniToJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private String pembayaranToJson(Pembayaran p) {
        return "{"
                + "\"idPembayaran\":" + p.getIdPembayaran() + ","
                + "\"kodePembayaran\":" + esc(p.getKodePembayaran()) + ","
                + "\"idPenghuni\":" + (p.getIdPenghuni() != null ? p.getIdPenghuni() : "null") + ","
                + "\"penghuni\":" + esc(p.getPenghuni()) + ","
                + "\"noHp\":" + esc(p.getNoHp()) + ","
                + "\"noKamar\":" + esc(p.getNoKamar()) + ","
                + "\"periode\":" + esc(p.getPeriode()) + ","
                + "\"tanggalJatuhTempo\":" + esc(p.getTanggalJatuhTempo() != null ? p.getTanggalJatuhTempo().toString() : null) + ","
                + "\"tanggalBayar\":" + esc(p.getTanggalBayar() != null ? p.getTanggalBayar().toString() : null) + ","
                + "\"nominal\":" + (p.getNominal() != null ? p.getNominal().toPlainString() : "0") + ","
                + "\"status\":" + esc(p.getStatus())
                + "}";
    }

    private String pembayaranListToJson(List<Pembayaran> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(pembayaranToJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}