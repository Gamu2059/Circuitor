package KUU.FrameWorkComponent.OrderComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import KUU.Mode.DialogOpenMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 関数を追加するダイアログ
 */
public class FunctionDialog extends NewJDialog implements MouseListener{
    private GeneralItemPanel functionTitleLabel;
    private GeneralTextField functionTextField;
    private GeneralItemPanel confirmLabel;

    private DialogOpenMode openMode;
    private String oldFunctionName;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public FunctionDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1, 1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("関数の追加");
        } else {
            setTitle("関数名の編集");
        }
        this.openMode = mode;

        add(basePanel = new DialogBasePanel(frame));

        basePanel.add(functionTitleLabel = new GeneralItemPanel(null, null, "関数名"));
        basePanel.add(confirmLabel = new GeneralItemPanel(true, null, "確定"));

        basePanel.add(panel = new JPanel());
        panel.setLayout(null);
        panel.add(functionTextField = new GeneralTextField());

        confirmLabel.addMouseListener(this);

        /** 編集モードのとき関数名をセット */
        if (mode == DialogOpenMode.EDIT) {
            oldFunctionName = getFrame().getBasePanel().getSubOrderPanel().getProgramModel().getElementAt(getFrame().getBasePanel().getSubOrderPanel().getLineNumber());
            functionTextField.setText(oldFunctionName);
        }

        setBounds(e.getXOnScreen() - 75, e.getYOnScreen() - 150, 150, 130);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ("".equals(functionTextField.getText())) {
            JOptionPane.showMessageDialog(this, "追加したい関数名を記入してください。");
            functionTextField.setText("");
        }else if (openMode == DialogOpenMode.ADD){
            getFrame().getMasterTerminal().generateNewFunction(functionTextField.getText());
            getFrame().updateOrderPanel(true);
            getFrame().getBasePanel().getSubOrderPanel().setFunctionName(getFrame().getBasePanel().getSubOrderPanel().getProgramModel().get(getFrame().getBasePanel().getSubOrderPanel().getProgramList().getLastVisibleIndex()));
            getFrame().updateOrderPanel(true);
            dispose();
            getFrame().getHelpLabel().setText("");
        }else {
            getFrame().getMasterTerminal().renameFunction(oldFunctionName, functionTextField.getText());
            if (oldFunctionName.equals(getFrame().getBasePanel().getSubOrderPanel().getFunctionName())){
                getFrame().getBasePanel().getSubOrderPanel().setFunctionName(functionTextField.getText());
            }
            getFrame().updateOrderPanel(true);
            dispose();
            getFrame().getHelpLabel().setText("");
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class DialogBasePanel extends NewJPanel implements ComponentListener {

        DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        /** 生成時に呼ばれる */
        @Override
        public void handResize(int width, int height) {
            /** 上下端を除いた高さ */
            int partsHeight = height - 45;

            functionTitleLabel.setBounds(0, 0, width, 20);

            panel.setBounds(0, 20, width, partsHeight);
            functionTextField.setBounds(0, 0, width, partsHeight);

            confirmLabel.setBounds(0, height - 25, width, 25);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            handResize(getWidth(),getHeight());
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
