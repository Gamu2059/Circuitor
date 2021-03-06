package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * PINを入力するパネル。
 */
public class PinPanel extends NewJPanel implements ItemListener{
    private JComboBox pinBox;
    private GeneralItemPanel frameLabel;
    public PinPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(pinBox = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("ピン").toArray()));
        add(frameLabel = new GeneralItemPanel(""));
        frameLabel.setBackground(null);

        pinBox.addItemListener(this);
    }

    @Override
    public void handResize(int width, int height) {
        pinBox.setBounds((width/7) * 2, (height/7) * 2, (width/7) * 3, (height/7) * 3);
        frameLabel.setBounds(0, 0, width, height);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    public JComboBox getPinBox() {
        return pinBox;
    }
}
