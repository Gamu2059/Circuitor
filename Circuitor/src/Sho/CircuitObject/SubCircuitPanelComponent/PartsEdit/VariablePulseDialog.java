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
public class VariablePulseDialog extends JDialog {
    private JLabel label;
    private JPanel panel;
    private JTextField textField;
    private GeneralItemPanel confirmLabel;

    /**
     * 回路エディタ専用の設定ダイアログ。
     */
    public VariablePulseDialog(CircuitUnitPanel panel, CircuitBlock b) {
        super(panel.getFrame(), true);
        commonSetting(panel, b.getElecomInfo().getEtcStatus());
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value < 1 || value > 100) {
                        throw new Exception();
                    }
                    for (int i = 0; i < b.getElecomInfo().getSize().getHeight(); i++) {
                        for (int j = 0; j < b.getElecomInfo().getSize().getWidth(); j++) {
                            panel.getCircuitUnit().getCircuitBlock().getMatrix().get(b.getCircuitInfo().getAbco().getHeight() + i).get(b.getCircuitInfo().getAbco().getWidth() + j).getElecomInfo().setEtcStatus(value);
                        }
                    }
                    dispose();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(VariablePulseDialog.this, "整数値で入力して下さい。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(VariablePulseDialog.this, "値が範囲外です。\n1～100の範囲で入力して下さい。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setVisible(true);
    }

    /**
     * 実行画面専用の設定ダイアログ。
     */
    public VariablePulseDialog(ExecuteUnitPanel panel, HighLevelExecuteGroup group) {
        super(panel.getFrame(), true);
        if (panel.getFrame().getBasePanel().getMainExecutePanel().getExecuteStopLabel().getIcon() == ImageMaster.getImageMaster().getExecuteStopIcon()) {
            panel.getExecutor().setRunning(false);
        }
        if (group.getBehavior() instanceof PulseBehavior_) {
            PulseBehavior_ behavior = (PulseBehavior_) group.getBehavior();
            commonSetting(panel, behavior.getFreq());
            confirmLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        int value = Integer.parseInt(textField.getText());
                        if (value < 1 || value > 100) {
                            throw new Exception();
                        }
                        behavior.setFreq(value);
                        dispose();
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(VariablePulseDialog.this, "整数値で入力して下さい。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(VariablePulseDialog.this, "値が範囲外です。\n1～100の範囲で入力して下さい。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            setVisible(true);
            if (panel.getFrame().getBasePanel().getMainExecutePanel().getExecuteStopLabel().getIcon() == ImageMaster.getImageMaster().getExecuteStopIcon()) {
                panel.getExecutor().setRunning(true);
            }
        }
    }

    /**
     * 汎用的な設定を行う。
     */
    private void commonSetting(UnitPanel panel, int status) {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds((int)(rect.getWidth() - 300) / 2,(int)(rect.getHeight() - 200) / 2,300,200);
        setResizable(false);
        setLayout(new GridLayout(1,1));
        add(new DialogBasePanel(status));
    }

    private class DialogBasePanel extends JPanel implements ComponentListener {
        public DialogBasePanel(int status) {
            super();
            setLayout(null);
            addComponentListener(this);
            add(label = new JLabel("周波数(1～100)[Hz]を設定して下さい"));
            add(panel = new JPanel());
            add(confirmLabel = new GeneralItemPanel(true, null, "確定"));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderMaster.getRegularBorder());
            panel.setBorder(BorderMaster.getRegularBorder());
            if (status < 1) {
                status = 1;
            } else if (status > 1e6) {
                status = 1000000;
            }
            panel.add(textField = new JTextField(String.valueOf(status), 80));
        }

        @Override
        public void componentResized(ComponentEvent e) {
            label.setBounds(0,0, getWidth(), 20);
            panel.setBounds(0, 20, getWidth(), getHeight() - 80);
            confirmLabel.setBounds(0,getHeight() - 60, getWidth(), 60);
            textField.setBounds((panel.getWidth() - 80) / 2,(panel.getHeight() - 20) / 2,80 ,20);
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}
