package KUU.FrameWorkComponent.OrderComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import KUU.Mode.DialogOpenMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import ProcessTerminal.VariableSettings.SingleArray;
import ProcessTerminal.VariableSettings.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 一次元配列を追加するダイアログ
 */
public class OneArrayDialog extends NewJDialog implements MouseListener{
    private GeneralItemPanel buttonTitleLabel;
    private GeneralItemPanel oneArrayTitleLabel;
    private GeneralItemPanel indexTitleLabel;
    private JRadioButton intButton;
    private JRadioButton boolButton;
    private GeneralTextField oneArrayTextField;
    private GeneralTextField indexTextField;
    private ButtonGroup  group;
    private GeneralItemPanel confirmLabel;

    private DialogOpenMode openMode;
    private Variable.Type type;
    private String oldVariableName;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public OneArrayDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("一次元配列の追加");
        }else {
            setTitle("変数名の変更");
        }
        this.openMode = mode;

        add(basePanel = new DialogBasePanel(frame));

        basePanel.add(buttonTitleLabel = new GeneralItemPanel(null,null,"変数の型"));
        basePanel.add(oneArrayTitleLabel = new GeneralItemPanel(null,null,"変数名"));
        basePanel.add(indexTitleLabel  = new GeneralItemPanel(null,null,"要素数"));
        basePanel.add(confirmLabel      = new GeneralItemPanel(true,null,"確定"));

        basePanel.add(panel = new JPanel());
        panel.setLayout(null);
        panel.add(intButton = new JRadioButton("int型"));
        panel.add(boolButton = new JRadioButton("bool型"));
        panel.add(oneArrayTextField = new GeneralTextField());
        panel.add(indexTextField = new GeneralTextField());

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
        if (mode == DialogOpenMode.EDIT) {
            if (getFrame().getMasterTerminal().getVariableGroup().searchVariableList("配列").getElement(getFrame().getBasePanel().getSubOrderPanel().getLineNumber()).getType() == Variable.Type.INT){
                intButton.setBackground(ColorMaster.getSelectedColor());
                intButton.setSelected(true);
                boolButton.setBackground(ColorMaster.getNotSelectedColor());
                boolButton.setEnabled(false);
                boolButton.removeMouseListener(this);
            }else {
                intButton.setBackground(ColorMaster.getNotSelectedColor());
                intButton.setEnabled(false);
                intButton.removeMouseListener(this);
                boolButton.setBackground(ColorMaster.getSelectedColor());
                boolButton.setSelected(true);
            }
            ArrayList arrayList = getFrame().getMasterTerminal().getVariableStringList("配列");
            oldVariableName = (String) arrayList.get(getFrame().getBasePanel().getSubOrderPanel().getLineNumber());
            int index = ((SingleArray)getFrame().getMasterTerminal().searchVariable("配列", oldVariableName)).getArrays().length;

            oneArrayTextField.setText(oldVariableName);
            indexTextField.setText(String.valueOf(index));
            indexTextField.setEnabled(false);
        }

        setBounds(e.getXOnScreen() - 135, e.getYOnScreen() - 150, 270, 130);
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
            try {
                int index = Integer.parseInt(indexTextField.getText());
                if ("".equals(oneArrayTextField.getText())) {
                    JOptionPane.showMessageDialog(this, "変数名を入力してください。");
                } else if (openMode == DialogOpenMode.ADD){
                    getFrame().getMasterTerminal().generateNewVariable("配列", new SingleArray(type, oneArrayTextField.getText(), index));
                    getFrame().updateOrderPanel(false);
                    oneArrayTextField.setText("");
                    indexTextField.setText("");
                    getFrame().getHelpLabel().setText("");
                } else {
                    getFrame().getMasterTerminal().renameVariable("配列", oldVariableName, oneArrayTitleLabel.getText());
                    getFrame().updateOrderPanel(false);
                    dispose();
                    getFrame().getHelpLabel().setText("");
                }
            }catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "添え字は正の整数で入力してください。");
                indexTextField.setText("");
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
            int partsWidth = width/13;
            /** 上下端を除いた高さ */
            int partsHeight = height - 45;

            buttonTitleLabel.setBounds(0, 0, partsWidth*4, 20);
            oneArrayTitleLabel.setBounds(partsWidth*4, 0, partsWidth*6, 20);
            indexTitleLabel.setBounds(partsWidth*10, 0, width - partsWidth*10, 20);

            panel.setBounds(0, 20, width, partsHeight);
            intButton.setBounds(0, 0, partsWidth*4, partsHeight/2);
            boolButton.setBounds(0, partsHeight/2, partsWidth*4, partsHeight - partsHeight/2);
            oneArrayTextField.setBounds(partsWidth*4, 0, partsWidth*6, partsHeight);
            indexTextField.setBounds(partsWidth*10, 0, width - partsWidth*10, partsHeight);

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