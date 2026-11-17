package kostify.service;

import kostify.dao.PembayaranDao;
import kostify.dao.PenghuniDao;

/**
 * TODO: catatBayar(), ubah(), hapus(), riwayatPenghuni(), semuaPembayaran().
 * Aturan bisnis: satu penghuni tidak boleh punya dua pembayaran untuk periode yang sama.
 */
public class PembayaranService {
    private final PembayaranDao pembayaranDao;
    private final PenghuniDao penghuniDao;

    public PembayaranService(PembayaranDao pembayaranDao, PenghuniDao penghuniDao) {
        this.pembayaranDao = pembayaranDao;
        this.penghuniDao = penghuniDao;
    }
}
