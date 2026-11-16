package kostify.controller;

import kostify.service.PenghuniService;
import kostify.view.PenghuniPanel;

/** TODO: ikuti pola KamarController (pasang listener tombol, panggil service, tampilkan hasil). */
public class PenghuniController {
    private final PenghuniPanel view;
    private final PenghuniService service;

    public PenghuniController(PenghuniPanel view, PenghuniService service) {
        this.view = view;
        this.service = service;
    }
}
