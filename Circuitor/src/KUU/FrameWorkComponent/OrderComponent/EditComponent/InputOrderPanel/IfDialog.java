package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.SelectConstantPinVariablePanel;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.SelectPinVariablePanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.EditOrderVariableMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import ProcessTerminal.SyntaxSettings.Evaluation;
import ProcessTerminal.SyntaxSettings.Factor;
import ProcessTerminal.SyntaxSettings.Index;
import ProcessTerminal.SyntaxSettings.Syntax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * if文ダイアログを生成するクラス。
 */
public class IfDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private SelectPinVariablePanel firstSelectPinVariablePanel;
    private GeneralItemPanel       firstSelectPinVariableTitleLabel;
    private String                 firstSelectPinVariableString;
    private JComboBox        conditionBox;
    private GeneralItemPanel conditionBoxTitleLabel;
    private String[] conditionStrings = {">",">=","==","!=","<=","<"};
    private String   conditionString;
    private SelectConstantPinVariablePanel secondSelectConstantVariablePanel;
    private GeneralItemPanel               secondSelectConstantVariableTitleLabel;
    private String                         secondSelectConstantVariableString;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public IfDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("if文の作成");
        }else {
            setTitle("if文の編集");
        }

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(firstSelectPinVariablePanel = new SelectPinVariablePanel(frame));
        panel.add(firstSelectPinVariableTitleLabel = new GeneralItemPanel(null,null,"比べられる数"));
        panel.add(conditionBox = new JComboBox<>(conditionStrings));
        panel.add(conditionBoxTitleLabel = new GeneralItemPanel(null,null,"演算子"));
        panel.add(secondSelectConstantVariablePanel = new SelectConstantPinVariablePanel(frame));
        panel.add(secondSelectConstantVariableTitleLabel = new GeneralItemPanel(null,null,"比べる数"));

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(null,null,""));
        basePanel.add(confirmLabel       = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);

        add(basePanel);

        conditionBox.addItemListener(this);

        /** 命令挿入 */
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                Factor base  = firstSelectPinVariablePanel.getFactor();
                Factor adder = secondSelectConstantVariablePanel.getFactor();
                Syntax.COMPARES compares = null;
                switch ((String)conditionBox.getSelectedItem()){
                    case ">":
                        compares = Syntax.COMPARES.LARGE;
                        break;
                    case ">=":
                        compares = Syntax.COMPARES.LARGE_EQUAL;
                        break;
                    case "==":
                        compares = Syntax.COMPARES.EQUAL;
                        break;
                    case "!=":
                        compares = Syntax.COMPARES.NOT;
                        break;
                    case "<=":
                        compares = Syntax.COMPARES.SMALL_EQUAL;
                        break;
                    case "<":
                        compares = Syntax.COMPARES.SMALL;
                        break;
                }

                if (base!=null && adder!=null) {
                    if (mode == DialogOpenMode.ADD) {
                        getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(new Evaluation(Syntax.S_TYPE.IF, base, compares, adder)));
                    }else {
                        getFrame().getMasterTerminal().setOrder(functionName, lineNumber, new Syntax(new Evaluation(Syntax.S_TYPE.IF, base, compares, adder)));
                        dispose();
                    }
                    getFrame().getBasePanel().getEditOrderPanel().setLineNumber(lineNumber + 1);
                    getFrame().updateOrderPanel(false);
                }
            }
        });


        /** 編集モードで開かれた場合、内容を格納する */
        if (mode == DialogOpenMode.EDIT) {
            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
            int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();

            /** firstVariable */
            Factor firstVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getBase();
            firstSelectPinVariablePanel.resetSelect();
            switch (firstVariableFactor.getFactor()){
                case VARIABLE:
                    if (firstVariableFactor.isPinVariable()) {
                        firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        firstSelectPinVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getPinPanel().getPinBox().setSelectedItem(firstVariableFactor.getName());
                        firstSelectPinVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        firstSelectPinVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(firstVariableFactor.getName());
                        firstSelectPinVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    firstSelectPinVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    firstSelectPinVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(firstVariableFactor.getName());
                    if (firstVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(firstVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(firstVariableFactor.getFact1().getVariableName());
                    }
                    firstSelectPinVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    firstSelectPinVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (firstVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(firstVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(firstVariableFactor.getFact1().getVariableName());
                    }
                    if (firstVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(firstVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(firstVariableFactor.getFact2().getVariableName());
                    }
                    firstSelectPinVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }

            /** conditionBox */
            conditionBox.setSelectedItem(((Syntax) getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().convertToComparesSymbol());

            /** secondVariable */
            Factor secondVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getAdder();
            secondSelectConstantVariablePanel.resetSelect();
            switch (secondVariableFactor.getFactor()){
                case BOOL:
                case LITERAL:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    secondSelectConstantVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getValue()));
                    secondSelectConstantVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    if (secondVariableFactor.isPinVariable()) {
                        secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        secondSelectConstantVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getPinPanel().getPinBox().setSelectedItem(secondVariableFactor.getName());
                        secondSelectConstantVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        secondSelectConstantVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(secondVariableFactor.getName());
                        secondSelectConstantVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    secondSelectConstantVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(secondVariableFactor.getName());
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(secondVariableFactor.getFact1().getVariableName());
                    }
                    secondSelectConstantVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    secondSelectConstantVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(secondVariableFactor.getFact1().getVariableName());
                    }
                    if (secondVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(secondVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(secondVariableFactor.getFact2().getVariableName());
                    }
                    secondSelectConstantVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }
            updateOrderIndicateLabel();
        }

        setBounds(e.getXOnScreen() - 430, e.getYOnScreen() - 350, 860, 300);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        firstSelectPinVariableString = firstSelectPinVariablePanel.getString();
        secondSelectConstantVariableString = secondSelectConstantVariablePanel.getString();
        conditionString = (String)conditionBox.getSelectedItem();
        orderIndicateString = "if (" + firstSelectPinVariableString + " " + conditionString + " " + secondSelectConstantVariableString + ") {";
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
            /** コンポーネント１つの基準幅 */
            int partsWidth = 85;

            firstSelectPinVariableTitleLabel.setBounds(0, 0, partsWidth*4, 20);
            firstSelectPinVariablePanel.setBounds(0, 20, partsWidth*4, partsHeight);
            firstSelectPinVariablePanel.handResize(partsWidth*4, partsHeight);

            conditionBoxTitleLabel.setBounds(partsWidth*4, 0, partsWidth, 20);
            conditionBox.setBounds(partsWidth*4, partsHeight/3 + 20, partsWidth, partsHeight/3);

            secondSelectConstantVariableTitleLabel.setBounds(partsWidth*5, 0, basePanel.getWidth() - partsWidth*5, 20);
            secondSelectConstantVariablePanel.setBounds(partsWidth*5, 20, basePanel.getWidth() - partsWidth*5, partsHeight);
            secondSelectConstantVariablePanel.handResize(basePanel.getWidth() - partsWidth*5, partsHeight);
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