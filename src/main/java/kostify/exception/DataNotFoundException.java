package kostify.exception;

/** Dilempar saat data yang dicari tidak ada di database. */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
