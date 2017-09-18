package Master.FontMaster;

import java.awt.*;

/**
 * 全体のフォント情報を統括するクラス。
 */
public class FontMaster {
    /** 一般的なフォント */
    private static Font regularFont = new Font(Font.DIALOG, Font.BOLD, 12);

    public static Font getRegularFont() {
        return regularFont;
    }
}
