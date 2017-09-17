package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralTextField;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 定数を入力するパネル
 */
public class ConstantPanel extends NewJPanel implements DocumentListener{
    private GeneralTextField constantText;
    public ConstantPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(constantText = new GeneralTextField());

        constantText.getDocument().addDocumentListener(this);
    }

    @Override
    public void handResize(int width, int height) {
        constantText.setBounds((width/7) * 2, (height/7) * 2, (width/7) * 3, (height/7) * 3);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public JTextField getConstantText() {
        return constantText;
    }
}
