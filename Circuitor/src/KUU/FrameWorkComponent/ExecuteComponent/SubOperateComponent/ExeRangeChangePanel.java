package KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent;

import KUU.GeneralComponent.GeneralItemPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * グラフの罫線の単位を変更するための機能を備えたクラス。
 */
public class ExeRangeChangePanel extends JPanel {
    public ExeRangeChangePanel(ExeGraphPanel graphPanel) {
        super();
        setLayout(new GridLayout(1, 2));
        GeneralItemPanel upper, lower;
        add(upper = new GeneralItemPanel(true,null,"罫線の単位を大きくする"));
        add(lower = new GeneralItemPanel(true,null,"罫線の単位を小さくする"));
        upper.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (graphPanel != null) {
                    graphPanel.rangeUpper();
                }
            }
        });
        lower.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (graphPanel != null) {
                    graphPanel.rangeLower();
                }
            }
        });
    }
}
