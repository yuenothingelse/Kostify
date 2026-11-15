package kostify.model.enums;

public enum StatusPembayaran {
    BELUM_BAYAR("Belum Bayar"),
    LUNAS("Lunas");

    private final String label;

    StatusPembayaran(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
