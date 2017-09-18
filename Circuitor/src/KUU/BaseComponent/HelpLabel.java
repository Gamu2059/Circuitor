package KUU.BaseComponent;

import KUU.NewComponent.NewJLabel;
import Master.ColorMaster.ColorMaster;

import java.awt.*;

/**
 * フレーム直下に存在する説明文を表示するパネル。
 */
public class HelpLabel extends NewJLabel {
    public HelpLabel(BaseFrame frame) {
        super(frame);
        /* これを設定しないと潰れてしまう */
        setPreferredSize(new Dimension(0, 20));
        setVerticalAlignment(NORTH);
        /* 色の設定 */
        setBackground(ColorMaster.getHelpLabelColor());
        setOpaque(true);
        setText("");
    }

    @Override
    public void handResize(int width, int height) {

    }
}
