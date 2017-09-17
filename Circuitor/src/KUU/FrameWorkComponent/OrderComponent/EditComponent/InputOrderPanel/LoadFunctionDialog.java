package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import ProcessTerminal.SyntaxSettings.Syntax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 代入パネルを生成するクラス。
 */
public class LoadFunctionDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private JComboBox        firstFunctionBox;
    private ArrayList        firstFunctionArray;
    private GeneralItemPanel firstFunctionTitleLabel;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public LoadFunctionDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("呼び出す関数の作成");
        }else {
            setTitle("呼び出す関数の編集");
        }

        panel = new JPanel();
        panel.setLayout(null);
        firstFunctionArray = getFrame().getMasterTerminal().getFunctionGroup().getFunctionString();
        firstFunctionArray.remove("SETUP");
        firstFunctionArray.remove("MAIN");
        panel.add(firstFunctionBox = new JComboBox<>(firstFunctionArray.toArray()));
        panel.add(firstFunctionTitleLabel = new GeneralItemPanel(null,null,"追加したい関数"));

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(null,null,""));
        basePanel.add(confirmLabel       = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);

        firstFunctionBox.addItemListener(this);

        add(basePanel);

        /** 命令挿入 */
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String functionBaseName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                String functionAddName  = (String)firstFunctionBox.getSelectedItem();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                if (mode == DialogOpenMode.ADD) {
                    getFrame().getMasterTerminal().addOrder(functionBaseName, lineNumber, new Syntax(functionAddName));
                }else {
                    getFrame().getMasterTerminal().setOrder(functionBaseName, lineNumber, new Syntax(functionAddName));
                    dispose();
                }
                getFrame().getBasePanel().getEditOrderPanel().setLineNumber(lineNumber + 1);
                getFrame().updateOrderPanel(false);
            }
        });


        /** 編集モードで開かれた場合、内容を格納する */
        if (mode == DialogOpenMode.EDIT) {
            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
            int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();

            String targetFunctionName = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getFunctionName();

            firstFunctionBox.setSelectedItem(targetFunctionName);
        }

        setBounds(e.getXOnScreen() - 125, e.getYOnScreen() - 250, 250, 200);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        orderIndicateString = firstFunctionBox.getSelectedItem() + " start";
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

            firstFunctionTitleLabel.setBounds(0, 0, width, 20);
            firstFunctionBox.setBounds(width/4, partsHeight/3 + 20, width/2, partsHeight/3);
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