package kostify.model.enums;

public enum StatusKamar {
    KOSONG("Kosong"),
    TERISI("Terisi");

    private final String label;

    StatusKamar(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
