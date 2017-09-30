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
import ProcessTerminal.SyntaxSettings.Command;
import ProcessTerminal.SyntaxSettings.Evaluation;
import ProcessTerminal.SyntaxSettings.Factor;
import ProcessTerminal.SyntaxSettings.Index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * 計算ダイアログを生成するクラス。
 */
public class CalcDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private SelectPinVariablePanel firstSelectPinVariablePanel;
    private GeneralItemPanel       firstSelectPinVariableTitleLabel;
    private String                 firstSelectVariableString;
    private GeneralItemPanel equalsLabel;
    private SelectConstantPinVariablePanel secondSelectConstantPinVariablePanel;
    private GeneralItemPanel               secondSelectConstantPinVariableTitleLabel;
    private String                         secondSelectConstantPinVariableString;
    private JComboBox        operateBox;
    private GeneralItemPanel operateBoxTitleLabel;
    private String[] operateStrings = {"+","-","*","/","%"};
    private String   operateString;
    private GeneralItemPanel frameLabel;
    private SelectConstantPinVariablePanel thirdSelectConstantPinVariablePanel;
    private GeneralItemPanel thirdSelectConstantPinVariableTitleLabel;

    private String                         thirdSelectConstantPinVariableString;
    private DialogBasePanel basePanel;

    private JPanel panel;

    public CalcDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("計算命令の作成");
        }else {
            setTitle("計算命令の編集");
        }

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(firstSelectPinVariablePanel = new SelectPinVariablePanel(frame));
        panel.add(firstSelectPinVariableTitleLabel = new GeneralItemPanel(null,null,"代入される数"));
        panel.add(equalsLabel = new GeneralItemPanel("⇦"));
        panel.add(secondSelectConstantPinVariablePanel = new SelectConstantPinVariablePanel(frame));
        panel.add(secondSelectConstantPinVariableTitleLabel = new GeneralItemPanel(null,null,"計算される数"));
        panel.add(operateBox = new JComboBox<>(operateStrings));
        panel.add(operateBoxTitleLabel = new GeneralItemPanel(null,null,"演算子"));
        panel.add(frameLabel = new GeneralItemPanel(""));
        panel.add(thirdSelectConstantPinVariablePanel = new SelectConstantPinVariablePanel(frame));
        panel.add(thirdSelectConstantPinVariableTitleLabel = new GeneralItemPanel(null,null,"計算する数"));

        equalsLabel.setBackground(null);
        equalsLabel.setFont(new Font("", Font.BOLD, 32));
        frameLabel.setBackground(null);

        /** 編集モードで開かれた場合、内容を格納する */
        if (mode == DialogOpenMode.EDIT) {
            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
            int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();

            /** firstVariable */
            Factor firstVariableFactor = ((Command)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getTarget();
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

            /** secondVariable */
            Factor secondVariableFactor = ((Command)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getBase();
            secondSelectConstantPinVariablePanel.resetSelect();
            switch (secondVariableFactor.getFactor()){
                case BOOL:
                case LITERAL:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    secondSelectConstantPinVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantPinVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getValue()));
                    secondSelectConstantPinVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    if (secondVariableFactor.isPinVariable()) {
                        secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        secondSelectConstantPinVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getPinPanel().getPinBox().setSelectedItem(secondVariableFactor.getName());
                        secondSelectConstantPinVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        secondSelectConstantPinVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(secondVariableFactor.getName());
                        secondSelectConstantPinVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    secondSelectConstantPinVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantPinVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(secondVariableFactor.getName());
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        secondSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(secondVariableFactor.getFact1().getVariableName());
                    }
                    secondSelectConstantPinVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    secondSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    secondSelectConstantPinVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(secondVariableFactor.getFact1().getVariableName());
                    }
                    if (secondVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(secondVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        secondSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(secondVariableFactor.getFact2().getVariableName());
                    }
                    secondSelectConstantPinVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }

            /** operateBox */
            operateBox.setSelectedItem(((Command) getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().convertToCalcSymbol());

            /** thirdVariable */
            Factor thirdVariableFactor = ((Command)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getAdder();
            thirdSelectConstantPinVariablePanel.resetSelect();
            switch (thirdVariableFactor.getFactor()){
                case BOOL:
                case LITERAL:
                    thirdSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    thirdSelectConstantPinVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    thirdSelectConstantPinVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getValue()));
                    thirdSelectConstantPinVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    if (thirdVariableFactor.isPinVariable()) {
                        thirdSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.PIN);
                        thirdSelectConstantPinVariablePanel.getPinLabel().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getPinPanel().getPinBox().setSelectedItem(thirdVariableFactor.getName());
                        thirdSelectConstantPinVariablePanel.getPinPanel().setVisible(true);
                    } else {
                        thirdSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        thirdSelectConstantPinVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(thirdVariableFactor.getName());
                        thirdSelectConstantPinVariablePanel.getVariablePanel().setVisible(true);
                    }
                    break;
                case ARRAY:
                    thirdSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    thirdSelectConstantPinVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    thirdSelectConstantPinVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(thirdVariableFactor.getName());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    thirdSelectConstantPinVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    thirdSelectConstantPinVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    thirdSelectConstantPinVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    if (thirdVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(thirdVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        thirdSelectConstantPinVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(thirdVariableFactor.getFact2().getVariableName());
                    }
                    thirdSelectConstantPinVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }
        }

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(null,null,""));
        basePanel.add(confirmLabel       = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);

        add(basePanel);

        operateBox.addItemListener(this);

        /** 命令挿入 */
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                Factor target = firstSelectPinVariablePanel.getFactor();
                Factor base   = secondSelectConstantPinVariablePanel.getFactor();
                Factor adder  = thirdSelectConstantPinVariablePanel.getFactor();
                Evaluation.CALCMODE calcMode = null;
                switch ((String)operateBox.getSelectedItem()){
                    case "+":
                        calcMode = Evaluation.CALCMODE.PLUS;
                        break;
                    case "-":
                        calcMode = Evaluation.CALCMODE.MINUS;
                        break;
                    case "*":
                        calcMode = Evaluation.CALCMODE.MULTIPLE;
                        break;
                    case "/":
                        calcMode = Evaluation.CALCMODE.DIVIDE;
                        break;
                    case "%":
                        calcMode = Evaluation.CALCMODE.REMIND;
                        break;
                }

                if (target!=null && base!=null && adder!=null) {
                    if (mode == DialogOpenMode.ADD) {
                        getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Command(target, new Evaluation(Command.C_TYPE.CALC, base, calcMode, adder)));
                    }else {
                        getFrame().getMasterTerminal().setOrder(functionName, lineNumber, new Command(target, new Evaluation(Command.C_TYPE.CALC, base, calcMode, adder)));
                        dispose();
                    }
                    getFrame().getBasePanel().getEditOrderPanel().setLineNumber(lineNumber + 1);
                    getFrame().updateOrderPanel(false);
                }
            }
        });

        setBounds(e.getXOnScreen() - 525, e.getYOnScreen() - 350, 1050, 300);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        firstSelectVariableString = firstSelectPinVariablePanel.getString();
        secondSelectConstantPinVariableString = secondSelectConstantPinVariablePanel.getString();
        thirdSelectConstantPinVariableString = thirdSelectConstantPinVariablePanel.getString();
        operateString = (String)operateBox.getSelectedItem();
        orderIndicateString = firstSelectVariableString + " = " + secondSelectConstantPinVariableString + " " + operateString + " " + thirdSelectConstantPinVariableString;
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
            /** 内側のラベル */
            panel.setBounds(0, 20, width, height - 40);

            /** 下の空欄の幅/高さ */
            int partsHeight = panel.getHeight() - 30;

            firstSelectPinVariableTitleLabel.setBounds(0, 0, 270, 20);
            firstSelectPinVariablePanel.setBounds(0, 20, 270, partsHeight);
            firstSelectPinVariablePanel.handResize(270, partsHeight);

            equalsLabel.setBounds(270, 0, 70, partsHeight + 20);

            secondSelectConstantPinVariableTitleLabel.setBounds(340, 0, 320, 20);
            secondSelectConstantPinVariablePanel.setBounds(340, 20, 320, partsHeight);
            secondSelectConstantPinVariablePanel.handResize(320, partsHeight);

            operateBoxTitleLabel.setBounds(660, 0, 70, 20);
            operateBox.setBounds(661, partsHeight/3 + 20, 68, partsHeight/3);
            frameLabel.setBounds(660, 20, 70, partsHeight);

            thirdSelectConstantPinVariableTitleLabel.setBounds(730, 0, 320, 20);
            thirdSelectConstantPinVariablePanel.setBounds(730, 20, 320, partsHeight);
            thirdSelectConstantPinVariablePanel.handResize(320, partsHeight);

            /** 上下端のラベル */
            orderIndicateLabel.setBounds(0, 0, width, 20);
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
