package kostify.controller;

import kostify.service.PembayaranService;
import kostify.view.PembayaranPanel;

/** TODO: ikuti pola KamarController (pasang listener tombol, panggil service, tampilkan hasil). */
public class PembayaranController {
    private final PembayaranPanel view;
    private final PembayaranService service;

    public PembayaranController(PembayaranPanel view, PembayaranService service) {
        this.view = view;
        this.service = service;
    }
}
