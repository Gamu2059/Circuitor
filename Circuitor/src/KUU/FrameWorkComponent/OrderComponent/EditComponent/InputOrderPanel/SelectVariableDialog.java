package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import ProcessTerminal.VariableSettings.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SelectVariableDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private JComboBox<String> variableBox;
    private int[]            variableCount;

    private VariableSettingDialog variableSettingDialog;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public SelectVariableDialog(BaseFrame frame, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1,1));
        setTitle("配列の初期化");

        panel = new JPanel();
        panel.setLayout(null);
        String[] str = {"配列","2次元配列"};
        variableBox = new JComboBox<>();
        variableCount = new int[2];
        for (int i = 0; i < 2; i++) {
            if (!getFrame().getMasterTerminal().getVariableStringList(str[i]).equals(new ArrayList<>())) {
                for (String s:getFrame().getMasterTerminal().getVariableStringList(str[i])) {
                    variableBox.addItem(s);
                }
                variableCount[i] = getFrame().getMasterTerminal().getVariableStringList(str[i]).size();
            } else {
                variableCount[i]=0;
            }
        }
        panel.add(variableBox);
        variableBox.setSelectedIndex(0);
        variableBox.addItemListener(this);

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(""));
        basePanel.add(confirmLabel = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);


        add(basePanel);

        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int itemNumber=variableBox.getSelectedIndex();
                String variableName = variableBox.getSelectedItem().toString();
                String variableArrayType;
                Variable.Type variableRapperType;
                if (itemNumber<variableCount[0]){
                    variableArrayType="配列";
                    variableRapperType = getFrame().getMasterTerminal().searchVariable("配列", variableName).getType();
                } else {
                    variableArrayType="2次元配列";
                    variableRapperType = getFrame().getMasterTerminal().searchVariable("2次元配列", variableName).getType();
                }
                dispose();
                variableSettingDialog = new VariableSettingDialog(getFrame(), variableRapperType, variableName, variableArrayType, e);
                variableSettingDialog.setVisible(true);
            }
        });

        setBounds(e.getXOnScreen() - 125, e.getYOnScreen() - 250, 250, 200);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        orderIndicateString = variableBox.getSelectedItem() + "を選択中";
        orderIndicateLabel.setText(orderIndicateString);
        orderIndicateLabel.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        updateOrderIndicateLabel();
    }

    private class DialogBasePanel extends NewJPanel implements ComponentListener {

        DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        /** 生成時に呼ばれる */
        @Override
        public void handResize(int width, int height) {
            /** 上下端のラベル */
            orderIndicateLabel.setBounds(0, 0, width, 20);
            confirmLabel.setBounds(0, height - 30, width, 30);
            /** 内側のラベル */
            panel.setBounds(0, 20, width, height - 40);

            /** 下の空欄の幅/高さ */
            int partsHeight = panel.getHeight() - 30;

            variableBox.setBounds(width/4, partsHeight/3, width/2, partsHeight/3);
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
