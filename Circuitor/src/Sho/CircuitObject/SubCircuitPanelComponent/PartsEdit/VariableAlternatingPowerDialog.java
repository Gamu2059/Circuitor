package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.ElecomBehavior.PowerBehavior_;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 可変直流電源の値を設定するためのダイアログ。
 */
public class VariableAlternatingPowerDialog extends GeneralVariableDialog {
    private HighLevelExecuteInfo he;

    /**
     * 回路エディタ専用の設定ダイアログ。
     */
    public VariableAlternatingPowerDialog(CircuitUnitPanel circuitUnitPanel, CircuitBlock b) {
        super(circuitUnitPanel.getFrame(), true, 1e-4, 5e1);
        commonSetting(new DialogBasePanel(circuitUnitPanel, b, b.getElecomInfo().getEtcStatus(), min, max));
        setVisible(true);
    }

    /**
     * 実行画面専用の設定ダイアログ。
     */
    public VariableAlternatingPowerDialog(ExecuteUnitPanel executeUnitPanel, HighLevelExecuteGroup group) {
        super(executeUnitPanel.getFrame(), true, 1e-4, 5e1);
        this.exePanel = executeUnitPanel;
        stopLabel = executeUnitPanel.getFrame().getBasePanel().getMainExecutePanel().getExecuteStopLabel();
        he = group.getExecuteInfos().get(0).getHighLevelExecuteInfo();
        setRunning(false);
        if (group.getBehavior() instanceof PowerBehavior_) {
            PowerBehavior_ behavior = (PowerBehavior_) group.getBehavior();
            commonSetting(new DialogBasePanel(executeUnitPanel, group, behavior.getHertz(), min, max));
            setVisible(true);
            setRunning(true);
        }
    }

    @Override
    protected void setExecuteVariableDialog(ExecuteUnitPanel panel, HighLevelExecuteGroup group) {
        try {
            if (!(group.getBehavior() instanceof PowerBehavior_)) {
                return;
            }
            double value = Double.parseDouble(valueText);
            PowerBehavior_ behavior = (PowerBehavior_) group.getBehavior();
            if (value < min || value > max) {
                throw new Exception();
            }
            behavior.setHertz(value);
            // 周波数をダイアログ反映させるためにetcStatusにも代入
            group.getBehavior().getElecomInfo().setEtcStatus(value);
            dispose();
        } catch (NumberFormatException nfe) {
            showFormatExceptionDialog();
        } catch (Exception exc) {
            showRangeExceptionDialog();
        }
    }

    private class DialogBasePanel extends GeneralDialogBasePanel {
        public DialogBasePanel(CircuitUnitPanel circuitUnitPanel, CircuitBlock b, double status, double min, double max) {
            super(status, min, max);
            confirmLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    valueText = textField.getText();
                    setCircuitVariableDialog(circuitUnitPanel, b);
                }
            });
        }

        public DialogBasePanel(ExecuteUnitPanel executeUnitPanel, HighLevelExecuteGroup group, double status, double min, double max) {
            super(status, min, max);
            confirmLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    valueText = textField.getText();
                    setExecuteVariableDialog(executeUnitPanel, group);
                }
            });
        }

        @Override
        protected String getLabelTitle() {
            StringBuilder builder = new StringBuilder();
            return builder.append("周波数(").append(this.min).append("～").append(this.max).append(")[Hz]を設定して下さい").toString();
        }
    }
}
