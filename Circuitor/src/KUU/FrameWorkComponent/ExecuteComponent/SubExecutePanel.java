package KUU.FrameWorkComponent.ExecuteComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent.ExeMeasurePanel;
import KUU.NewComponent.NewJPanel;


/**
 * 実行モードでのサブ操作領域の画面。
 */
public class SubExecutePanel extends NewJPanel {
    /**
     * 電圧計
     */
    private ExeMeasurePanel voltagePanel;
    /**
     * 電流計
     */
    private ExeMeasurePanel currentPanel;

    public SubExecutePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(voltagePanel = new ExeMeasurePanel(frame,true));
        add(currentPanel = new ExeMeasurePanel(frame,false));

        handResize(getWidth(), getHeight());
    }

    @Override
    public void handResize(int width, int height) {
        /** 大枠パネルの配置 */
        voltagePanel.setBounds(0, 0, width, height / 2);
        currentPanel.setBounds(0, height / 2, width, height - height / 2);

        voltagePanel.handResize(voltagePanel.getWidth(), voltagePanel.getHeight());
        currentPanel.handResize(currentPanel.getWidth(), currentPanel.getHeight());
    }

    public ExeMeasurePanel getVoltagePanel() {
        return voltagePanel;
    }

    public ExeMeasurePanel getCurrentPanel() {
        return currentPanel;
    }
}
