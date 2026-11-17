package kostify.service;

import kostify.dao.KamarDao;
import kostify.dao.PenghuniDao;

/**
 * TODO: tambah(), ubah(), keluar()/hapus(), semuaPenghuni().
 * Aturan bisnis yang perlu ada:
 *  - kamar yang dipilih harus berstatus KOSONG
 *  - setelah penghuni masuk, status kamar menjadi TERISI
 *  - saat penghuni keluar, status kamar kembali KOSONG
 */
public class PenghuniService {
    private final PenghuniDao penghuniDao;
    private final KamarDao kamarDao;

    public PenghuniService(PenghuniDao penghuniDao, KamarDao kamarDao) {
        this.penghuniDao = penghuniDao;
        this.kamarDao = kamarDao;
    }
}
