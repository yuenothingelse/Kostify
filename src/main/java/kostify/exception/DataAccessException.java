package kostify.exception;

/** Membungkus SQLException agar lapisan atas tidak bergantung pada java.sql. */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
