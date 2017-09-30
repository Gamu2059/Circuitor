package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import KUU.GeneralComponent.GeneralItemPanel;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.Matrix;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 可変部品の汎用的な処理をまとめたクラス。
 */
public abstract class GeneralVariableDialog extends JDialog {
    protected GeneralItemPanel stopLabel;
    protected ExecuteUnitPanel exePanel;
    protected double min, max;
    protected String valueText;

    public GeneralVariableDialog(Frame owner, boolean modal, double min, double max) {
        super(owner, modal);
        this.min = min;
        this.max = max;
    }

    /**
     * 回路エディタ上でのダイアログ設定を行う。
     */
    protected void setCircuitVariableDialog(CircuitUnitPanel panel, CircuitBlock b) {
        try {
            double value = Double.parseDouble(valueText);
            if (value < min || value > max) {
                throw new Exception();
            }

            Matrix<CircuitBlock> matrix = panel.getCircuitUnit().getCircuitBlock();
            ArrayList<CircuitBlock> list;
            IntegerDimension abco = b.getCircuitInfo().getAbco();

            for (int i = 0; i < b.getElecomInfo().getSize().getHeight(); i++) {
                list = matrix.getMatrix().get(abco.getHeight() + i);
                for (int j = 0; j < b.getElecomInfo().getSize().getWidth(); j++) {
                    list.get(abco.getWidth() + j).getElecomInfo().setEtcStatus(value);
                }
            }
            dispose();
        } catch (NumberFormatException nfe) {
            showFormatExceptionDialog();
        } catch (Exception exc) {
            showRangeExceptionDialog();
        }
    }

    /**
     * 実行画面でのダイアログ設定を行う。
     */
    protected abstract void setExecuteVariableDialog(ExecuteUnitPanel panel, HighLevelExecuteGroup group);

    protected void setRunning(boolean beginRunning) {
        if (stopLabel == null || exePanel == null) {
            return;
        }
        if (beginRunning) {
            if (stopLabel.getIcon() == ImageMaster.getImageMaster().getExecuteStopIcon()) {
                exePanel.getExecutor().setRunning(true);
            }
        } else {
            if (stopLabel.getIcon() == ImageMaster.getImageMaster().getExecuteStopIcon()) {
                exePanel.getExecutor().setRunning(false);
            }
        }
    }

    /**
     * 共通的な見た目の設定を行う。
     */
    protected void commonSetting(JPanel dialogPanel) {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds((int)(rect.getWidth() - 300) / 2,(int)(rect.getHeight() - 200) / 2,300,200);
        setResizable(false);
        setLayout(new GridLayout(1,1));
        add(dialogPanel);
    }

    protected void showFormatExceptionDialog() {
        JOptionPane.showMessageDialog(this, "実数値で入力して下さい。", "入力エラー", JOptionPane.ERROR_MESSAGE);
    }

    protected void showRangeExceptionDialog() {
        StringBuilder builder = new StringBuilder();
        builder.append("値が範囲外です。\n").append(min).append("～").append(max).append("の範囲で入力して下さい。");
        JOptionPane.showMessageDialog(this, builder.toString(), "入力エラー", JOptionPane.ERROR_MESSAGE);
    }
}
