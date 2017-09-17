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
 *  while文ダイアログを生成するクラス。
 */
public class WhileDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private SelectPinVariablePanel firstSelectPinVariablePanel;
    private GeneralItemPanel firstSelectPinVariableTitleLabel;
    private String                 firstSelectVariableString;
    private JComboBox        conditionBox;
    private GeneralItemPanel conditionBoxTitleLabel;
    private String[] conditionStrings = {">",">=","==","!=","<=","<"};
    private String   conditionString;
    private SelectConstantPinVariablePanel secondSelectConstantPinVariablePanel;
    private GeneralItemPanel               secondSelectConstantPinVariableTitleLabel;
    private String                         secondSelectConstantPinVariableString;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public WhileDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("while文の作成");
        }else {
            setTitle("while文の編集");
        }

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(firstSelectPinVariablePanel = new SelectPinVariablePanel(frame));
        panel.add(firstSelectPinVariableTitleLabel = new GeneralItemPanel(null,null,"比べられる数"));
        panel.add(conditionBox = new JComboBox<>(conditionStrings));
        panel.add(conditionBoxTitleLabel = new GeneralItemPanel(null,null,"演算子"));
        panel.add(secondSelectConstantPinVariablePanel = new SelectConstantPinVariablePanel(frame));
        panel.add(secondSelectConstantPinVariableTitleLabel = new GeneralItemPanel(null,null,"比べる数"));

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(null,null,""));
        basePanel.add(confirmLabel = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);

        add(basePanel);

        conditionBox.addItemListener(this);

        /** 命令挿入 */
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                Factor base  = firstSelectPinVariablePanel.getFactor();
                Factor adder = secondSelectConstantPinVariablePanel.getFactor();
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
                        getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(new Evaluation(Syntax.S_TYPE.WHILE, base, compares, adder)));
                    }else {
                        getFrame().getMasterTerminal().setOrder(functionName, lineNumber, new Syntax(new Evaluation(Syntax.S_TYPE.WHILE, base, compares, adder)));
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
            Factor firstFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getBase();
            firstSelectPinVariablePanel.resetSelect();
            switch (firstFactor.getFactor()){
                case VARIABLE:
                    if (firstFactor.isPinVariable()) {
                        firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        firstSelectPinVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getPinPanel().getPinBox().setSelectedItem(firstFactor.getName());
                        firstSelectPinVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        firstSelectPinVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(firstFactor.getName());
                        firstSelectPinVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    firstSelectPinVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    firstSelectPinVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(firstFactor.getName());
                    if (firstFactor.getFact1().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setVisible(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(firstFactor.getFact1().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setVisible(true);
                        firstSelectPinVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(firstFactor.getFact1().getVariableName());
                    }
                    firstSelectPinVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    firstSelectPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    firstSelectPinVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (firstFactor.getFact1().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(firstFactor.getFact1().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(firstFactor.getFact1().getVariableName());
                    }
                    if (firstFactor.getFact2().getType() == Index.Type.CONSTANT){
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(firstFactor.getFact2().getConstantIndex()));
                    }else {
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        firstSelectPinVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(firstFactor.getFact2().getVariableName());
                    }
                    firstSelectPinVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }
            
            /** conditionBox */
            conditionBox.setSelectedItem(((Syntax) getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().convertToComparesSymbol());

            /** secondVariable */
            Factor thirdVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getAdder();
            secondSelectConstantPinVariablePanel.resetSelect();
            switch (thirdVariableFactor.getFactor()){
                case BOOL:
                case LITERAL:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    secondSelectConstantPinVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantPinVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getValue()));
                    secondSelectConstantPinVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    if (thirdVariableFactor.isPinVariable()) {
                        secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        secondSelectConstantPinVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getPinPanel().getPinBox().setSelectedItem(thirdVariableFactor.getName());
                        secondSelectConstantPinVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        secondSelectConstantPinVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(thirdVariableFactor.getName());
                        secondSelectConstantPinVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    secondSelectConstantPinVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantPinVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(thirdVariableFactor.getName());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    secondSelectConstantPinVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    secondSelectConstantPinVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    if (thirdVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(thirdVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(thirdVariableFactor.getFact2().getVariableName());
                    }
                    secondSelectConstantPinVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }
            updateOrderIndicateLabel();
        }

        setBounds(e.getXOnScreen() - 330, e.getYOnScreen() - 350, 660, 300);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        firstSelectVariableString = firstSelectPinVariablePanel.getString();
        secondSelectConstantPinVariableString = secondSelectConstantPinVariablePanel.getString();
        conditionString = (String)conditionBox.getSelectedItem();
        orderIndicateString = "while (" + firstSelectVariableString + " " + conditionString + " " + secondSelectConstantPinVariableString + ") {";
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

            firstSelectPinVariableTitleLabel.setBounds(0, 0, 270, 20);
            firstSelectPinVariablePanel.setBounds(0, 20, 270, partsHeight);
            firstSelectPinVariablePanel.handResize(270, partsHeight);

            conditionBoxTitleLabel.setBounds(270, 0, 70, 20);
            conditionBox.setBounds(270, partsHeight/3 + 20, 70, partsHeight/3);

            secondSelectConstantPinVariableTitleLabel.setBounds(340, 0, 320, 20);
            secondSelectConstantPinVariablePanel.setBounds(340, 20, 320, partsHeight);
            secondSelectConstantPinVariablePanel.handResize(340, partsHeight);
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