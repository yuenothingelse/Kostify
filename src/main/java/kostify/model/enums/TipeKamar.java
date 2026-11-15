package kostify.model.enums;

public enum TipeKamar {
    STANDAR("Standar"),
    VIP("VIP");

    private final String label;

    TipeKamar(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
