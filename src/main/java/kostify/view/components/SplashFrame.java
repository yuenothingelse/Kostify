package kostify.view;

import javax.swing.JFrame;
import javax.swing.Timer;

/** Jendela splash tanpa title bar yang tampil sebentar lalu membuka layar berikutnya. */
public class SplashFrame extends JFrame {
    private static final int DURATION_MS = 2200;

    public SplashFrame() {
        setUndecorated(true);
        setContentPane(new SplashPanel());
        pack();
        setLocationRelativeTo(null);
    }

    public static void show(Runnable next) {
        SplashFrame frame = new SplashFrame();
        frame.setVisible(true);
        Timer timer = new Timer(DURATION_MS, e -> {
            frame.dispose();
            next.run();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
