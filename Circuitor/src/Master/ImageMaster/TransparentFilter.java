package Master.ImageMaster;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * イメージの透かしフィルタクラス。
 */
public class TransparentFilter extends RGBImageFilter {
    public TransparentFilter() {
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        Color c = new Color(rgb);
        c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 120);
        return c.getRGB();
    }
}
