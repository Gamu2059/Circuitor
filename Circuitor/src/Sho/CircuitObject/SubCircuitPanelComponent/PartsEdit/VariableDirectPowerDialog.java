package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import KUU.GeneralComponent.GeneralItemPanel;
import Master.BorderMaster.BorderMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
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
 * 可変直流電源の値を設定するためのダイアログ。
 */
public class VariableDirectPowerDialog extends GeneralVariableDialog {
    private HighLevelExecuteInfo he;

    /**
     * 回路エディタ専用の設定ダイアログ。
     */
    public VariableDirectPowerDialog(CircuitUnitPanel circuitUnitPanel, CircuitBlock b) {
        super(circuitUnitPanel.getFrame(), true, 1e-3, 1e2);
        commonSetting(new DialogBasePanel(circuitUnitPanel, b, b.getElecomInfo().getEtcStatus(), min, max));
        setVisible(true);
    }

    /**
     * 実行画面専用の設定ダイアログ。
     */
    public VariableDirectPowerDialog(ExecuteUnitPanel executeUnitPanel, HighLevelExecuteGroup group) {
        super(executeUnitPanel.getFrame(), true, 1e-3, 1e2);
        this.exePanel = executeUnitPanel;
        stopLabel = executeUnitPanel.getFrame().getBasePanel().getMainExecutePanel().getExecuteStopLabel();
        he = group.getExecuteInfos().get(0).getHighLevelExecuteInfo();
        setRunning(false);
        commonSetting(new DialogBasePanel(executeUnitPanel, group, he.getPotential(), min, max));
        setVisible(true);
        setRunning(true);
    }

    @Override
    protected void setExecuteVariableDialog(ExecuteUnitPanel panel, HighLevelExecuteGroup group) {
        try {
            double value = Double.parseDouble(valueText);
            if (value < min || value > max) {
                throw new Exception();
            }
            he.setPotential(value);
            // 起電力をダイアログ反映させるためにetcStatusにも代入
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
            return builder.append("起電力(").append(this.min).append("～").append(this.max).append(")[V]を設定して下さい").toString();
        }
    }
}
