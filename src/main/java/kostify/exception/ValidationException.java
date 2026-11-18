package kostify.exception;

/** Dilempar saat input atau aturan bisnis tidak terpenuhi. Pesannya aman ditampilkan ke pengguna. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
