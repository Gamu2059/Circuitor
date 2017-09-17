package KUU.FrameWorkComponent.OrderComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import KUU.Mode.DialogOpenMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import ProcessTerminal.VariableSettings.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 変数を追加するダイアログ
 */
public class VariableDialog extends NewJDialog implements MouseListener{
    private GeneralItemPanel buttonTitleLabel;
    private GeneralItemPanel variableTitleLabel;
    private JRadioButton intButton;
    private JRadioButton boolButton;
    private GeneralTextField variableTextField;
    private ButtonGroup group;
    private GeneralItemPanel confirmLabel;

    private DialogOpenMode openMode;
    private Variable.Type type;
    private String oldVariableName;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public VariableDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("変数の追加");
        }else {
            setTitle("変数名の編集");
        }
        this.openMode = mode;

        add(basePanel = new DialogBasePanel(frame));

        basePanel.add(buttonTitleLabel   = new GeneralItemPanel(null,null,"変数の型"));
        basePanel.add(variableTitleLabel = new GeneralItemPanel(null,null,"変数名"));
        basePanel.add(confirmLabel      = new GeneralItemPanel(true,null,"確定"));

        basePanel.add(panel = new JPanel());
        panel.setLayout(null);
        panel.add(intButton  = new JRadioButton("int型"));
        panel.add(boolButton = new JRadioButton("bool型"));
        panel.add(variableTextField = new GeneralTextField());

        group = new ButtonGroup();
        group.add(intButton);
        group.add(boolButton);

        intButton.setBackground(ColorMaster.getSelectedColor());
        boolButton.setBackground(ColorMaster.getNotSelectedColor());
        intButton.setSelected(true);
        type = Variable.Type.INT;

        intButton.addMouseListener(this);
        boolButton.addMouseListener(this);
        confirmLabel.addMouseListener(this);

        /** 変数名の編集 */
        if (mode == DialogOpenMode.EDIT){
            if (getFrame().getMasterTerminal().getVariableGroup().searchVariableList("変数").getElement(getFrame().getBasePanel().getSubOrderPanel().getLineNumber()).getType() == Variable.Type.INT){
                intButton.setBackground(ColorMaster.getSelectedColor());
                boolButton.setBackground(ColorMaster.getNotSelectedColor());
                boolButton.setEnabled(false);
                boolButton.removeMouseListener(this);
                intButton.setSelected(true);
            }else {
                intButton.setBackground(ColorMaster.getNotSelectedColor());
                intButton.setEnabled(false);
                intButton.removeMouseListener(this);
                boolButton.setBackground(ColorMaster.getSelectedColor());
                boolButton.setSelected(true);
            }
            ArrayList arrayList = getFrame().getMasterTerminal().getVariableStringList("変数");
            oldVariableName = (String) arrayList.get(getFrame().getBasePanel().getSubOrderPanel().getLineNumber());
            variableTextField.setText(oldVariableName);
        }

        setBounds(e.getXOnScreen() - 105, e.getYOnScreen() - 150, 210, 130);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** JRadioButton */
        if (e.getSource() instanceof JRadioButton){
            intButton.setBackground(ColorMaster.getNotSelectedColor());
            boolButton.setBackground(ColorMaster.getNotSelectedColor());
            JRadioButton radioButton = (JRadioButton)e.getSource();
            radioButton.setBackground(ColorMaster.getSelectedColor());
            if (radioButton == intButton){
                type = Variable.Type.INT;
            }else {
                type = Variable.Type.BOOL;
            }
        }
        /** 確定ボタン */
        if (e.getSource() instanceof JPanel){
            if ("".equals(variableTextField.getText())){
                JOptionPane.showMessageDialog(this, "変数名を入力してください。");
            }else if (openMode == DialogOpenMode.ADD){
                getFrame().getMasterTerminal().generateNewVariable("変数", new Variable(type, variableTextField.getText()));
                getFrame().updateOrderPanel(false);
                dispose();
            }else {
                getFrame().getMasterTerminal().renameVariable("変数", oldVariableName, variableTextField.getText());
                getFrame().updateOrderPanel(false);
                dispose();
            }
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
            int partsWidth = width/5;
            /** 上下端を除いた高さ */
            int partsHeight = height - 50;

            buttonTitleLabel.setBounds(0, 0, partsWidth*2, 20);
            variableTitleLabel.setBounds(partsWidth*2, 0, width - partsWidth*2, 20);

            panel.setBounds(0, 20, width, partsHeight);
            intButton.setBounds(0, 0, partsWidth*2, partsHeight/2);
            boolButton.setBounds(0, partsHeight/2, partsWidth*2, partsHeight - partsHeight/2);
            variableTextField.setBounds(partsWidth*2, 0, width - partsWidth*2, partsHeight);

            confirmLabel.setBounds(0, height - 30, width, 30);
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
