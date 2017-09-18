package Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJTabbedPane;
import Master.ColorMaster.ColorMaster;

import java.util.ArrayList;

/**
 * 電子部品追加部品に用いるタブドペインクラス。
 */
public class PartsAddTabbedPane extends NewJTabbedPane {
    /**
     * タブペインに内包する電子部品追加用パネル。
     */
    private ArrayList<PartsAddTabPanel> partsAddTabPanels;

    public PartsAddTabbedPane(BaseFrame frame) {
        super(frame);
        setTabPlacement(LEFT);
        setBackground(ColorMaster.getNotSelectedColor());
        setFocusable(false);
        partsAddTabPanels = new ArrayList<>();
    }

    @Override
    public void handResize(int w, int h) {

    }

    /** partsAddTabPanels */
    public ArrayList<PartsAddTabPanel> getPartsAddTabPanels() {
        return partsAddTabPanels;
    }

    public void setPartsAddTabPanels(ArrayList<PartsAddTabPanel> partsAddTabPanels) {
        this.partsAddTabPanels = partsAddTabPanels;
    }
}
