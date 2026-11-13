package kostify.util;

import kostify.exception.ValidationException;

public final class Validator {
    private Validator() {
    }

    public static void requireNotBlank(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field + " tidak boleh kosong");
        }
    }

    public static boolean isEmail(String value) {
        return value != null && value.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }
}
