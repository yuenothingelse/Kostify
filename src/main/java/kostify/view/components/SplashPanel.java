package kostify.view;

import kostify.view.components.BackgroundPanel;
import kostify.view.components.HouseIllustration;
import kostify.view.components.Logo;
import kostify.view.components.Theme;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Isi splash screen: logo, nama aplikasi, ilustrasi rumah, dan tagline. */
public class SplashPanel extends BackgroundPanel {

    public SplashPanel() {
        setPreferredSize(new Dimension(Theme.WIDTH, Theme.HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);
        int cx = getWidth() / 2;

        Logo.draw(g2, cx - 40, 164, 80, false);
        Theme.drawCentered(g2, "Kostify", Theme.font(Font.BOLD, 44), Theme.TITLE, cx, 296);
        Theme.drawCentered(g2, "Boarding House Management", Theme.font(Font.PLAIN, 16), Theme.SUBTITLE, cx, 318);
        HouseIllustration.draw(g2, cx - 170, 388);
        Theme.drawCentered(g2, "Kelola kos lebih mudah,", Theme.font(Font.PLAIN, 14), new java.awt.Color(0x4A7F62), cx, 641);
        Theme.drawCentered(g2, "hidup lebih tenang.", Theme.font(Font.PLAIN, 14), new java.awt.Color(0x4A7F62), cx, 663);
        g2.dispose();
    }
}
