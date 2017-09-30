package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 変数を入力するパネル。
 */
public class VariablePanel extends NewJPanel implements ItemListener{
    private JComboBox variableBox;
    private GeneralItemPanel frameLabel;
    public VariablePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(variableBox = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("変数").toArray()));
        add(frameLabel = new GeneralItemPanel(""));

        frameLabel.setBackground(null);

        variableBox.addItemListener(this);
    }

    @Override
    public void handResize(int width, int height) {
        variableBox.setBounds((width/7)*2, (height/7)*2, (width/7)*3, (height/7)*3);
        frameLabel.setBounds(0, 0, width, height);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    public JComboBox getVariableBox() {
        return variableBox;
    }
}