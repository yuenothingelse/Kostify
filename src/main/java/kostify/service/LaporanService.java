 package kostify.service;

import kostify.dao.KamarDao;
import kostify.model.enums.StatusKamar;

/**
 * Ringkasan untuk dashboard.
 * TODO: tambahkan daftarTunggakan() dan hitungPendapatan(bulan) setelah PembayaranDao selesai.
 */
public class LaporanService {
    private final KamarDao kamarDao;

    public LaporanService(KamarDao kamarDao) {
        this.kamarDao = kamarDao;
    }

    public int totalKamar() {
        return kamarDao.findAll().size();
    }

    public int kamarTerisi() {
        return kamarDao.findByStatus(StatusKamar.TERISI).size();
    }

    public int kamarKosong() {
        return kamarDao.findByStatus(StatusKamar.KOSONG).size();
    }
}
