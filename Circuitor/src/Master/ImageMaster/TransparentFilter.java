package Master.ImageMaster;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * イメージの透かしフィルタクラス。
 */
public class TransparentFilter extends RGBImageFilter {
    TransparentFilter() {
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        Color c = new Color(rgb);
        // 完全な白(RGB == 255)は見えなくする
        if (c.getRed() == 255 && c.getBlue() == 255 && c.getGreen() == 255) {
            c = new Color(0, 0, 0, 0);
        } else {
            c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 120);
        }
        return c.getRGB();
    }
}
