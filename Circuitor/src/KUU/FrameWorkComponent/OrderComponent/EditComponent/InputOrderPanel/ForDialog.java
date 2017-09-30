package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.SelectConstantVariablePanel;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.SelectVariablePanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.EditOrderVariableMode;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import ProcessTerminal.SyntaxSettings.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * for文ダイアログを生成するクラス。
 */
public class ForDialog extends NewJDialog implements ItemListener{
    private GeneralItemPanel orderIndicateLabel;
    private String           orderIndicateString;
    private GeneralItemPanel confirmLabel;

    private SelectVariablePanel firstSelectVariablePanel;
    private GeneralItemPanel    firstSelectVariableTitleLabel;
    private String              firstSelectVariableString;
    private SelectConstantVariablePanel secondSelectConstantVariablePanel;
    private GeneralItemPanel            secondSelectConstantVariableTitleLabel;
    private String                      secondSelectConstantVariableString;
    private JComboBox        conditionBox;
    private GeneralItemPanel conditionBoxTitleLabel;
    private String[]         conditionStrings = {">=",">","<=","<"};
    private String           conditionString;
    private GeneralItemPanel frameLabel1;
    private SelectConstantVariablePanel thirdSelectConstantVariablePanel;
    private GeneralItemPanel            thirdSelectConstantVariableTitleLabel;
    private String                      thirdSelectConstantVariableString;
    private JComboBox        incrementBox;
    private GeneralItemPanel incrementBoxTitleLabel;
    private String[]         incrementStrings = {"+","-"};
    private String           incrementString;
    private GeneralItemPanel frameLabel2;
    private SelectConstantVariablePanel fourthSelectConstantVariablePanel;
    private GeneralItemPanel            fourthSelectConstantVariableTitleLabel;
    private String                      fourthSelectConstantVariableString;

    private DialogBasePanel basePanel;
    private JPanel panel;

    public ForDialog(BaseFrame frame, DialogOpenMode mode, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));
        if (mode == DialogOpenMode.ADD) {
            setTitle("for文の作成");
        }else {
            setTitle("for文の編集");
        }

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(firstSelectVariablePanel = new SelectVariablePanel(frame));
        panel.add(firstSelectVariableTitleLabel = new GeneralItemPanel(null,null,"カウント変数"));
        panel.add(secondSelectConstantVariablePanel  = new SelectConstantVariablePanel(frame));
        panel.add(secondSelectConstantVariableTitleLabel = new GeneralItemPanel(null,null,"初期値"));
        panel.add(conditionBox = new JComboBox<>(conditionStrings));
        panel.add(conditionBoxTitleLabel = new GeneralItemPanel(null,null,"条件符号"));
        panel.add(frameLabel1 = new GeneralItemPanel(""));
        panel.add(thirdSelectConstantVariablePanel = new SelectConstantVariablePanel(frame));
        panel.add(thirdSelectConstantVariableTitleLabel = new GeneralItemPanel(null,null,"条件数値"));
        panel.add(incrementBox = new JComboBox<>(incrementStrings));
        panel.add(incrementBoxTitleLabel = new GeneralItemPanel(null,null,"増分符号"));
        panel.add(frameLabel2 = new GeneralItemPanel(""));
        panel.add(fourthSelectConstantVariablePanel = new SelectConstantVariablePanel(frame));
        panel.add(fourthSelectConstantVariableTitleLabel = new GeneralItemPanel(null,null,"増分"));

        frameLabel1.setBackground(null);
        frameLabel2.setBackground(null);

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(orderIndicateLabel = new GeneralItemPanel(null,null,""));
        basePanel.add(confirmLabel = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(panel);

        add(basePanel);

        conditionBox.addItemListener(this);
        incrementBox.addItemListener(this);

        /** 命令挿入 */
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                Factor target    = firstSelectVariablePanel.getFactor();
                Factor initValue = secondSelectConstantVariablePanel.getFactor();
                Factor base      = thirdSelectConstantVariablePanel.getFactor();
                Factor increment = fourthSelectConstantVariablePanel.getFactor();
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
                Evaluation.CALCMODE calcMode = null;
                switch ((String)incrementBox.getSelectedItem()){
                    case "+":
                        calcMode = Evaluation.CALCMODE.PLUS;
                        break;
                    case "-":
                        calcMode = Evaluation.CALCMODE.MINUS;
                        break;
                }
                Evaluation initEvaluation = new Evaluation(Command.C_TYPE.RET, initValue);
                Evaluation conditionEvaluation = new Evaluation(Syntax.S_TYPE.WHILE, target, compares, base);
                Evaluation incrementEvaluation = new Evaluation(Command.C_TYPE.CALC, target, calcMode, increment);

                if (target!=null && initValue!=null && base!=null && increment!=null) {
                    if (mode == DialogOpenMode.ADD) {
                        getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(target, initEvaluation, conditionEvaluation, incrementEvaluation));
                    }else {
                        getFrame().getMasterTerminal().setOrder(functionName, lineNumber, new Syntax(target, initEvaluation, conditionEvaluation, incrementEvaluation));
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
            Factor firstVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getTarget();
            firstSelectVariablePanel.resetSelect();
            switch (firstVariableFactor.getFactor()) {
                case VARIABLE:
                    firstSelectVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                    firstSelectVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                    firstSelectVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(firstVariableFactor.getName());
                    firstSelectVariablePanel.getVariablePanel().setVisible(true);
                    break;
                case ARRAY:
                    firstSelectVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    firstSelectVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    firstSelectVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(firstVariableFactor.getName());
                    if (firstVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        firstSelectVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        firstSelectVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(firstVariableFactor.getFact1().getConstantIndex()));
                    } else {
                        firstSelectVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        firstSelectVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        firstSelectVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(firstVariableFactor.getFact1().getVariableName());
                    }
                    firstSelectVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    firstSelectVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    firstSelectVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (firstVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(firstVariableFactor.getFact1().getConstantIndex()));
                    } else {
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(firstVariableFactor.getFact1().getVariableName());
                    }
                    if (firstVariableFactor.getFact2().getType() == Index.Type.CONSTANT) {
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(firstVariableFactor.getFact2().getConstantIndex()));
                    } else {
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        firstSelectVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(firstVariableFactor.getFact2().getVariableName());
                    }
                    firstSelectVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }

            /** secondVariable */
            Factor secondVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getInitValue().getBase();
            secondSelectConstantVariablePanel.resetSelect();
            switch (secondVariableFactor.getFactor()) {
                case BOOL:
                case LITERAL:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    secondSelectConstantVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getValue()));
                    secondSelectConstantVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                    secondSelectConstantVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(secondVariableFactor.getName());
                    secondSelectConstantVariablePanel.getVariablePanel().setVisible(true);
                    break;
                case ARRAY:
                    secondSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    secondSelectConstantVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    secondSelectConstantVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(secondVariableFactor.getName());
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        secondSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    } else {
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
                    if (secondVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(secondVariableFactor.getFact1().getConstantIndex()));
                    } else {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(secondVariableFactor.getFact1().getVariableName());
                    }
                    if (secondVariableFactor.getFact2().getType() == Index.Type.CONSTANT) {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(secondVariableFactor.getFact2().getConstantIndex()));
                    } else {
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        secondSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(secondVariableFactor.getFact2().getVariableName());
                    }
                    secondSelectConstantVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }

            /** conditionBox */
            conditionBox.setSelectedItem(((Syntax) getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().convertToComparesSymbol());

            /** thirdVariable */
            Factor thirdVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getEvaluation().getAdder();
            thirdSelectConstantVariablePanel.resetSelect();
            switch (thirdVariableFactor.getFactor()) {
                case BOOL:
                case LITERAL:
                    thirdSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    thirdSelectConstantVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    thirdSelectConstantVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getValue()));
                    thirdSelectConstantVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                    thirdSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                    thirdSelectConstantVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                    thirdSelectConstantVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(thirdVariableFactor.getName());
                    thirdSelectConstantVariablePanel.getVariablePanel().setVisible(true);
                    break;
                case ARRAY:
                    thirdSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    thirdSelectConstantVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    thirdSelectConstantVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(thirdVariableFactor.getName());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    } else {
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        thirdSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    thirdSelectConstantVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    thirdSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    thirdSelectConstantVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (thirdVariableFactor.getFact1().getType() == Index.Type.CONSTANT) {
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(thirdVariableFactor.getFact1().getConstantIndex()));
                    } else {
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(thirdVariableFactor.getFact1().getVariableName());
                    }
                    if (thirdVariableFactor.getFact2().getType() == Index.Type.CONSTANT) {
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(thirdVariableFactor.getFact2().getConstantIndex()));
                    } else {
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        thirdSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(thirdVariableFactor.getFact2().getVariableName());
                    }
                    thirdSelectConstantVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }

            /** incrementBox */
            incrementBox.setSelectedItem(((Syntax) getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getInclementValue().convertToCalcSymbol());

            /** fourthVariable */
            Factor fourthVariableFactor = ((Syntax)getFrame().getMasterTerminal().searchOrder(functionName, lineNumber)).getInclementValue().getAdder();
            fourthSelectConstantVariablePanel.resetSelect();
            switch (fourthVariableFactor.getFactor()){
                case BOOL:
                case LITERAL:
                    fourthSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.CONSTANT);
                    fourthSelectConstantVariablePanel.getConstantLabel().setBackground(ColorMaster.getSelectedColor());
                    fourthSelectConstantVariablePanel.getConstantPanel().getConstantText().setText(String.valueOf(fourthVariableFactor.getValue()));
                    fourthSelectConstantVariablePanel.getConstantPanel().setVisible(true);
                    break;
                case VARIABLE:
                        fourthSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.VARIABLE);
                        fourthSelectConstantVariablePanel.getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getVariablePanel().getVariableBox().setSelectedItem(fourthVariableFactor.getName());
                        fourthSelectConstantVariablePanel.getVariablePanel().setVisible(true);
                    break;
                case ARRAY:
                    fourthSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.ARRAY);
                    fourthSelectConstantVariablePanel.getOneArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    fourthSelectConstantVariablePanel.getOneArrayPanel().getOneArrayBox().setSelectedItem(fourthVariableFactor.getName());
                    if (fourthVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setSelected(true);
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getConstantRadioButton().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setVisible(true);
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getConstantText().setText(String.valueOf(fourthVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setSelected(true);
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getVariableRadioButton().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setVisible(true);
                        fourthSelectConstantVariablePanel.getOneArrayPanel().getVariableBox().setSelectedItem(fourthVariableFactor.getFact1().getVariableName());
                    }
                    fourthSelectConstantVariablePanel.getOneArrayPanel().setVisible(true);
                    break;
                case SQUARE:
                    fourthSelectConstantVariablePanel.setEditOrderVariableMode(EditOrderVariableMode.SQUARE);
                    fourthSelectConstantVariablePanel.getTwoArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    if (fourthVariableFactor.getFact1().getType() == Index.Type.CONSTANT){
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setSelected(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setVisible(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantText1().setText(String.valueOf(fourthVariableFactor.getFact1().getConstantIndex()));
                    }else {
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setSelected(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton1().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setVisible(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox1().setSelectedItem(fourthVariableFactor.getFact1().getVariableName());
                    }
                    if (fourthVariableFactor.getFact2().getType() == Index.Type.CONSTANT){
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setSelected(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setVisible(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getConstantText2().setText(String.valueOf(fourthVariableFactor.getFact2().getConstantIndex()));
                    }else {
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setSelected(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableRadioButton2().setBackground(ColorMaster.getSelectedColor());
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setVisible(true);
                        fourthSelectConstantVariablePanel.getTwoArrayPanel().getVariableBox2().setSelectedItem(fourthVariableFactor.getFact2().getVariableName());
                    }
                    fourthSelectConstantVariablePanel.getTwoArrayPanel().setVisible(true);
                    break;
            }
            updateOrderIndicateLabel();
        }

        setBounds(getFrame().getWidth()/2 - 585, e.getYOnScreen() - 350, 1170, 300);
    }

    /** 命令プレビューの更新を行う */
    public void updateOrderIndicateLabel(){
        firstSelectVariableString = firstSelectVariablePanel.getString();
        secondSelectConstantVariableString = secondSelectConstantVariablePanel.getString();
        conditionString = (String)conditionBox.getSelectedItem();
        thirdSelectConstantVariableString = thirdSelectConstantVariablePanel.getString();
        incrementString = (String)incrementBox.getSelectedItem();
        fourthSelectConstantVariableString = fourthSelectConstantVariablePanel.getString();
        orderIndicateString = "for ( " +
                              firstSelectVariableString + " = " + secondSelectConstantVariableString + " ; " +
                              firstSelectVariableString + " " + conditionString + " " + thirdSelectConstantVariableString + " ; " +
                              firstSelectVariableString + " = " + firstSelectVariableString + " " + incrementString + " " + fourthSelectConstantVariableString + "){";
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

            /** 表示幅の関係で数値を微調整している */
            firstSelectVariableTitleLabel.setBounds(0, 0, 220, 20);
            firstSelectVariablePanel.setBounds(0, 20, 220, partsHeight);
            firstSelectVariablePanel.handResize(220, partsHeight);

            secondSelectConstantVariableTitleLabel.setBounds(220, 0, 270, 20);
            secondSelectConstantVariablePanel.setBounds(220, 20, 270, partsHeight);
            secondSelectConstantVariablePanel.handResize(270, partsHeight);

            conditionBoxTitleLabel.setBounds(490, 0, 70, 20);
            conditionBox.setBounds(491, partsHeight/3 + 20, 68, partsHeight/3);
            frameLabel1.setBounds(490, 20, 70, partsHeight);

            thirdSelectConstantVariableTitleLabel.setBounds(560, 0, 270, 20);
            thirdSelectConstantVariablePanel.setBounds(560, 20, 270, partsHeight);
            thirdSelectConstantVariablePanel.handResize(270, partsHeight);

            incrementBoxTitleLabel.setBounds(830, 0, 70, 20);
            incrementBox.setBounds(831, partsHeight/3 + 20, 68, partsHeight/3);
            frameLabel2.setBounds(830, 20, 70, partsHeight);

            fourthSelectConstantVariableTitleLabel.setBounds(900, 0, 270, 20);
            fourthSelectConstantVariablePanel.setBounds(900, 20, 270, partsHeight);
            fourthSelectConstantVariablePanel.handResize(270, partsHeight);

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