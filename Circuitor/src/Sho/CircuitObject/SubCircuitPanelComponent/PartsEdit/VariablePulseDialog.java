package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import KUU.GeneralComponent.GeneralItemPanel;
import Master.BorderMaster.BorderMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.ElecomBehavior.PulseBehavior_;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 可変パルス出力器の値を設定するためのダイアログ。
 */
public class VariablePulseDialog extends GeneralVariableDialog {
    /**
     * 回路エディタ専用の設定ダイアログ。
     */
    public VariablePulseDialog(CircuitUnitPanel circuitUnitPanel, CircuitBlock b) {
        super(circuitUnitPanel.getFrame(), true, 1, 1e2);
        commonSetting(new DialogBasePanel(circuitUnitPanel, b, b.getElecomInfo().getEtcStatus(), min, max));
        setVisible(true);
    }

    /**
     * 実行画面専用の設定ダイアログ。
     */
    public VariablePulseDialog(ExecuteUnitPanel executeUnitPanel, HighLevelExecuteGroup group) {
        super(executeUnitPanel.getFrame(), true, 1, 1e2);
        this.exePanel = executeUnitPanel;
        stopLabel = executeUnitPanel.getFrame().getBasePanel().getMainExecutePanel().getExecuteStopLabel();
        setRunning(false);
        if (group.getBehavior() instanceof PulseBehavior_) {
            PulseBehavior_ behavior = (PulseBehavior_) group.getBehavior();
            commonSetting(new DialogBasePanel(executeUnitPanel, group, behavior.getFreq(), min, max));
            setVisible(true);
            setRunning(true);
        }
    }

    @Override
    protected void setExecuteVariableDialog(ExecuteUnitPanel panel, HighLevelExecuteGroup group) {
        try {
            if (!(group.getBehavior() instanceof PulseBehavior_)) {
                return;
            }
            double value = Double.parseDouble(valueText);
            PulseBehavior_ behavior = (PulseBehavior_) group.getBehavior();
            if (value < min || value > max) {
                throw new Exception();
            }
            behavior.setFreq(value);
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
