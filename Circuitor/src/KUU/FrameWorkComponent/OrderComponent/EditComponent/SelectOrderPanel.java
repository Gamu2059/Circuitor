package KUU.FrameWorkComponent.OrderComponent.EditComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.*;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.EditDialogMode;
import KUU.NewComponent.NewJPanel;
import ProcessTerminal.SyntaxSettings.Command;
import ProcessTerminal.SyntaxSettings.Syntax;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 命令選択パネルの生成、命令入力パネルの設定をするクラス
 */
public class SelectOrderPanel extends NewJPanel implements MouseListener{
    /** 命令選択ラベル */
    private GeneralItemPanel substitutionLabel;
    private GeneralItemPanel calcLabel;
    private GeneralItemPanel ifLabel;
    private GeneralItemPanel forLabel;
    private GeneralItemPanel whileLabel;
    private GeneralItemPanel functionLabel;
//    private GeneralItemPanel breakLabel;
//    private GeneralItemPanel continueLabel;
    /** 命令ダイアログ */
    private SubstitutionDialog substitutionDialog;
    private CalcDialog         calcDialog;
    private IfDialog           ifDialog;
    private ForDialog          forDialog;
    private WhileDialog        whileDialog;
    private LoadFunctionDialog loadFunctionDialog;

    private EditDialogMode orderMode;

    public SelectOrderPanel(BaseFrame frame){
        super(frame);
        setLayout(null);

        /** 命令選択ラベル */
        add(substitutionLabel = new GeneralItemPanel(true,null,"代入"));
        add(calcLabel         = new GeneralItemPanel(true,null,"計算"));
        add(ifLabel           = new GeneralItemPanel(true,null,"if文"));
        add(forLabel          = new GeneralItemPanel(true,null,"for文"));
        add(whileLabel        = new GeneralItemPanel(true,null,"while文"));
        add(functionLabel     = new GeneralItemPanel(true,null,"関数の呼び出し"));
//        add(breakLabel        = new GeneralItemPanel(true,null,"break文"));
//        add(continueLabel     = new GeneralItemPanel(true,null,"continue文"));

        substitutionLabel.addMouseListener(this);
        calcLabel.addMouseListener(this);
        ifLabel.addMouseListener(this);
        forLabel.addMouseListener(this);
        whileLabel.addMouseListener(this);
        functionLabel.addMouseListener(this);
//        breakLabel.addMouseListener(this);
//        continueLabel.addMouseListener(this);
    }

    @Override
    public void handResize(int width, int height){
        int partsWidth = width/6;
        int partsHeight = height/3;

        /** 命令選択ラベル
         *  代入  計算
         *  if    for
         *  while 関数 */
        substitutionLabel.setBounds(0, 0, partsWidth, partsHeight);
        calcLabel.setBounds(partsWidth, 0, partsWidth, partsHeight);
        ifLabel.setBounds(0, partsHeight, partsWidth, partsHeight);
        forLabel.setBounds(partsWidth, partsHeight, partsWidth, partsHeight);
        whileLabel.setBounds(0, partsHeight*2, partsWidth, height - partsHeight*2);
        functionLabel.setBounds(partsWidth, partsHeight*2, partsWidth, height - partsHeight*2);

//        int partsWidth = width/6;
//        int partsHeight = height/4;
//
//        /** 命令選択ラベル
//         *  代入  計算
//         *  if    for
//         *  while 関数
//         *  break continue */
//        substitutionLabel.setBounds(0, 0, partsWidth, partsHeight);
//        calcLabel.setBounds(partsWidth, 0, partsWidth, partsHeight);
//        ifLabel.setBounds(0, partsHeight, partsWidth, partsHeight);
//        forLabel.setBounds(partsWidth, partsHeight, partsWidth, partsHeight);
//        whileLabel.setBounds(0, partsHeight*2, partsWidth, partsHeight);
//        functionLabel.setBounds(partsWidth, partsHeight*2, partsWidth, partsHeight);
//        breakLabel.setBounds(0, partsHeight*3, partsWidth, height - partsHeight*3);
//        continueLabel.setBounds(partsWidth, partsHeight*3, partsWidth, height - partsHeight*3);
    }

    /** 命令プレビューの更新　ダイアログ内から呼ばれる */
    public void updateOrderIndicateLabel(){
        switch (orderMode){
            case SUBSTITUTION:
                substitutionDialog.updateOrderIndicateLabel();
                break;
            case CALC:
                calcDialog.updateOrderIndicateLabel();
                break;
            case IF:
                ifDialog.updateOrderIndicateLabel();
                break;
            case FOR:
                forDialog.updateOrderIndicateLabel();
                break;
            case WHILE:
                whileDialog.updateOrderIndicateLabel();
                break;
            case FUNCTION:
                loadFunctionDialog.updateOrderIndicateLabel();
                break;
        }
    }

    public SubstitutionDialog getSubstitutionDialog() {
        return substitutionDialog;
    }

    public CalcDialog getCalcDialog() {
        return calcDialog;
    }

    public IfDialog getIfDialog() {
        return ifDialog;
    }

    public ForDialog getForDialog() {
        return forDialog;
    }

    public WhileDialog getWhileDialog() {
        return whileDialog;
    }

    public LoadFunctionDialog getLoadFunctionDialog() {
        return loadFunctionDialog;
    }

    public EditDialogMode getOrderMode() {
        return orderMode;
    }

    /** 命令文の編集　ADDで空のウィンドウを生成することでNullPointerを回避する */
    public void openEditDialog(Command.C_TYPE cType, MouseEvent e){
        switch (cType){
            case RET:
                orderMode = EditDialogMode.SUBSTITUTION;
                substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.ADD, e);
                substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                substitutionDialog.setVisible(true);
                break;
            case CALC:
                orderMode = EditDialogMode.CALC;
                calcDialog = new CalcDialog(getFrame(), DialogOpenMode.ADD, e);
                calcDialog = new CalcDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                calcDialog.setVisible(true);
                break;
            case WAIT:
                break;
        }
    }

    /** 構文の編集　ADDで空のウィンドウを生成することでNullPointerを回避する */
    public void openEditDialog(Syntax.S_TYPE sType, MouseEvent e){
        switch (sType) {
            case IF:
                orderMode = EditDialogMode.IF;
                ifDialog = new IfDialog(getFrame(), DialogOpenMode.ADD, e);
                ifDialog = new IfDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                ifDialog.setVisible(true);
                break;
            case WHILE:
                orderMode = EditDialogMode.WHILE;
                whileDialog = new WhileDialog(getFrame(), DialogOpenMode.ADD, e);
                whileDialog = new WhileDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                whileDialog.setVisible(true);
                break;
            case FOR:
                orderMode = EditDialogMode.FOR;
                forDialog = new ForDialog(getFrame(), DialogOpenMode.ADD, e);
                forDialog = new ForDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                forDialog.setVisible(true);
                break;
            case FUNCTION:
                orderMode = EditDialogMode.FUNCTION;
                loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                loadFunctionDialog.setVisible(true);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** 選択された変数のダイアログを有効にする */
        JPanel panel = (JPanel) e.getSource();
        if (panel == substitutionLabel) {
            orderMode = EditDialogMode.SUBSTITUTION;
            substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            substitutionDialog.setVisible(true);
        } else if (panel == calcLabel) {
            orderMode = EditDialogMode.CALC;
            calcDialog = new CalcDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            calcDialog.setVisible(true);
        } else if (panel == ifLabel) {
            orderMode = EditDialogMode.IF;
            ifDialog = new IfDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            ifDialog.setVisible(true);
        } else if (panel == forLabel) {
            orderMode = EditDialogMode.FOR;
            if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>()) ||
                !getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>()) ||
                !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())){
                forDialog = new ForDialog(getFrame(), DialogOpenMode.ADD, e);
                updateOrderIndicateLabel();
                forDialog.setVisible(true);
            }else {
                JOptionPane.showMessageDialog(forLabel, "使用できる変数がありません！\nまずは変数の作成を行ってください。");
            }
        } else if (panel == whileLabel) {
            orderMode = EditDialogMode.WHILE;
            whileDialog = new WhileDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            whileDialog.setVisible(true);
        } else if(panel == functionLabel){
            if (getFrame().getBasePanel().getSubOrderPanel().getProgramModel().getSize() >= 3){
                orderMode = EditDialogMode.FUNCTION;
                loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                updateOrderIndicateLabel();
                loadFunctionDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "SETUP、MAIN以外の関数があるとき使用できます。");
            }
        }
        /** 後回し */
//        else if(panel == continueLabel){
//            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
//            int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
//            if(getFrame().getMasterTerminal().getFunctionGroup().searchFunction(functionName).searchBeginningSyntax(lineNumber)!=null) {
//                getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(Syntax.S_TYPE.CONTINUE));
//                getFrame().getBasePanel().getEditOrderPanel().updateProgramList();
//            } else {
//                JLabel label = new JLabel("for文、while文の中でのみ使用できます。");
//                JOptionPane.showMessageDialog(this, label);
//            }
//        } else if(panel == breakLabel){
//            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
//            int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
//            if(getFrame().getMasterTerminal().getFunctionGroup().searchFunction(functionName).searchBeginningSyntax(lineNumber)!=null) {
//                getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(Syntax.S_TYPE.BREAK));
//                getFrame().getBasePanel().getEditOrderPanel().updateProgramList();
//            } else {
//                JLabel label = new JLabel("if文、for文、while文の中でのみ使用できます。");
//                JOptionPane.showMessageDialog(this, label);
//            }
//        }
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
}