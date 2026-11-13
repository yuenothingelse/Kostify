package kostify.util;

import kostify.model.Pengelola;

/** Menyimpan pengelola yang sedang login. */
public final class Session {
    private static Pengelola current;

    private Session() {
    }

    public static void login(Pengelola pengelola) {
        current = pengelola;
    }

    public static void logout() {
        current = null;
    }

    public static Pengelola getCurrent() {
        return current;
    }
}
