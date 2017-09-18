package KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent;

import Master.BorderMaster.BorderMaster;
import Master.ColorMaster.ColorMaster;
import Master.FontMaster.FontMaster;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * グラフの罫線の値を表示する機能を備えたクラス。
 */
public class ExeValueLinePanel extends JPanel {
    /**
     * 対象となるグラフパネル。
     */
    private ExeGraphPanel graphPanel;
    /**
     * 単位。
     */
    private String unit;

    public ExeValueLinePanel(String unit) {
        super();
        setLayout(null);
        setOpaque(true);
        setBorder(BorderMaster.getRegularBorder());
        setBackground(ColorMaster.getNotSelectedColor());
        setFont(FontMaster.getRegularFont());
        this.unit = unit + "]";
    }

    public void setGraphPanel(ExeGraphPanel graphPanel) {
        this.graphPanel = graphPanel;
        addMouseListener(graphPanel);
        addMouseMotionListener(graphPanel);
        addMouseWheelListener(graphPanel);
    }

    /**
     * 罫線の座標と値を同時に取得し、それを元に再描画する。
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        String string;
        HashMap<Integer, Double> map = graphPanel.getLineAndValue();
        for (int key : map.keySet()) {
            string = ExeIndiCateLabel.getFormattedValue(map.get(key), 9).append(unit).toString();
            g2.setColor(map.get(key) == 0d ? Color.RED : Color.BLACK);
            g2.drawString(string, getWidth() - g.getFontMetrics().stringWidth(string)- 1, key);
        }
    }
}
