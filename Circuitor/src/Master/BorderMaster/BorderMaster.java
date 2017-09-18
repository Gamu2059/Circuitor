package Master.BorderMaster;

import Master.ColorMaster.ColorMaster;

import javax.swing.border.LineBorder;

/**
 * 全体の色情報を統括するクラス。
 */
public class BorderMaster {
    /** 一般的なボーダ */
    private static LineBorder regularBorder = new LineBorder(ColorMaster.getRegularBorderColor(), 1);

    public static LineBorder getRegularBorder() {
        return regularBorder;
    }
}
