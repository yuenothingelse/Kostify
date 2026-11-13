package kostify.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {
    private static final NumberFormat RUPIAH = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));

    static {
        RUPIAH.setMaximumFractionDigits(0);
    }

    private FormatUtil() {
    }

    public static String rupiah(long amount) {
        return RUPIAH.format(amount);
    }
}
