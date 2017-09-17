package Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.PartsVarieties;

import java.awt.*;

/**
 * 部品追加画面を表示するためのパネル。
 */
public class PartsAddPanel extends NewJPanel {
    /**
     * 各部品ごとにスクロールペインを格納するためのタブペイン。
     */
    private PartsAddTabbedPane tabbedPane;

    public PartsAddPanel(BaseFrame frame) {
        super(frame);
        setLayout(new GridLayout(1, 1));
        setBackground(ColorMaster.getSubBackColor());
        add(tabbedPane = new PartsAddTabbedPane(frame));
        /* 部品を追加する */
        for (int i = 0; i < PartsVarieties.values().length - 1; i++) {
            getTabbedPane().getPartsAddTabPanels().add(new PartsAddTabPanel(frame, PartsVarieties.values()[i]));
            getTabbedPane().addTab(getTabbedPane().getPartsAddTabPanels().get(i).getTitle(), getTabbedPane().getPartsAddTabPanels().get(i));
        }
    }

    @Override
    public void handResize(int w, int h) {

    }

    /** tabbedPane */
    public PartsAddTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(PartsAddTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
