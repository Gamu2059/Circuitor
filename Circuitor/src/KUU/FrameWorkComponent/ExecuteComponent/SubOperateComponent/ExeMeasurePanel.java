package KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;

/**
 * 計測器エリアのコンポーネントを設定するクラス。
 */
public class ExeMeasurePanel extends NewJPanel {
    private GeneralItemPanel indicateLabel;
    private GeneralItemPanel titleLabel;
    private ExeIndiCateLabel valueIndicateLabel;
    private ExeGraphPanel graphPanel;
    private ExeValueLinePanel valueLinePanel;
    private ExeRangeChangePanel rangeChangePanel;

    public ExeMeasurePanel(BaseFrame frame, boolean isVolt) {
        super(frame);
        setLayout(null);

        add(indicateLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getModelImage(PartsVarieties.MEASURE, isVolt ? PartsStandards.VOLTMETER : PartsStandards.AMMETER), isVolt ? "電圧計" : "電流計"));
        add(titleLabel = new GeneralItemPanel("実際値:"));
        add(valueIndicateLabel = new ExeIndiCateLabel(isVolt ? "V" : "A"));
        add(graphPanel = new ExeGraphPanel());
        add(valueLinePanel = new ExeValueLinePanel(isVolt ? "V" : "A"));
        add(rangeChangePanel = new ExeRangeChangePanel(graphPanel));

        graphPanel.setValueLinePanel(valueLinePanel);
        valueLinePanel.setGraphPanel(graphPanel);
        indicateLabel.setBackground(ColorMaster.getSelectedColor());
    }

    @Override
    public void handResize(int width, int height) {
        indicateLabel.setBounds(0, 0, width, height / 7);
        titleLabel.setBounds(0, height / 7, (width / 5) * 2, 20);
        valueIndicateLabel.setBounds((width / 5) * 2, height / 7, width - (width / 5) * 2, 20);
        valueLinePanel.setBounds(0, (height / 7) + 20, (width / 5) * 2, height - (height / 7) - 50);
        graphPanel.setBounds((width / 5) * 2, (height / 7) + 20, width - (width / 5) * 2, height - (height / 7) - 50);
        rangeChangePanel.setBounds(0,height - 30, width, 30);
    }

    public ExeIndiCateLabel getValueIndicateLabel() {
        return valueIndicateLabel;
    }

    public ExeGraphPanel getGraphPanel() {
        return graphPanel;
    }
}
