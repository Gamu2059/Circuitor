package KUU.FrameWorkComponent.OrderComponent.EditComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.*;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.EditDialogMode;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
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
    private boolean clickAddFlg;

    public SelectOrderPanel(BaseFrame frame){
        super(frame);
        setLayout(null);

        /** 命令選択ラベル */
        add(substitutionLabel = new GeneralItemPanel(false,null,"代入"));
        add(calcLabel         = new GeneralItemPanel(false,null,"計算"));
        add(ifLabel           = new GeneralItemPanel(false,null,"if文"));
        add(forLabel          = new GeneralItemPanel(false,null,"for文"));
        add(whileLabel        = new GeneralItemPanel(false,null,"while文"));
        add(functionLabel     = new GeneralItemPanel(false,null,"関数の呼び出し"));
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

        clickAddFlg = true;
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
            case LOADFUNCTION:
                loadFunctionDialog.updateOrderIndicateLabel();
                break;
        }
    }

    /** 命令文の編集 */
    public void openEditDialog(Command.C_TYPE cType, MouseEvent e){
        switch (cType){
            case RET:
                orderMode = EditDialogMode.SUBSTITUTION;
                if (substitutionDialog == null) {
                    substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                substitutionDialog.setVisible(true);
                break;
            case CALC:
                orderMode = EditDialogMode.CALC;
                if (calcDialog == null) {
                    calcDialog = new CalcDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                calcDialog = new CalcDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                calcDialog.setVisible(true);
                break;
            case WAIT:
                break;
        }
    }

    /** 構文の編集 */
    public void openEditDialog(Syntax.S_TYPE sType, MouseEvent e){
        switch (sType) {
            case IF:
                orderMode = EditDialogMode.IF;
                if (ifDialog == null) {
                    ifDialog = new IfDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                ifDialog = new IfDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                ifDialog.setVisible(true);
                break;
            case WHILE:
                orderMode = EditDialogMode.WHILE;
                if (whileDialog == null) {
                    whileDialog = new WhileDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                whileDialog = new WhileDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                whileDialog.setVisible(true);
                break;
            case FOR:
                orderMode = EditDialogMode.FOR;
                if (forDialog == null) {
                    forDialog = new ForDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                forDialog = new ForDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                forDialog.setVisible(true);
                break;
            case FUNCTION:
                orderMode = EditDialogMode.LOADFUNCTION;
                if (loadFunctionDialog == null) {
                    loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                }
                loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.EDIT, e);
                updateOrderIndicateLabel();
                loadFunctionDialog.setVisible(true);
                break;
        }
    }

    /** SETUP時に選べなくする命令の設定 */
    public void setPanelVisible(String str){
        if (str.equals("SETUP")){
//            substitutionLabel.setVisible(false);
//            calcLabel.setVisible(false);
//            ifLabel.setVisible(false);
//            forLabel.setVisible(false);
//            whileLabel.setVisible(false);
            functionLabel.setVisible(false);
        }else {
//            substitutionLabel.setVisible(true);
//            calcLabel.setVisible(true);
//            ifLabel.setVisible(true);
//            forLabel.setVisible(true);
//            whileLabel.setVisible(true);
            functionLabel.setVisible(true);
        }
    }

    /** 命令挿入パネルがクリックできるかを設定する */
    public void setAddCanClick(boolean flg){
        if (flg){
            clickAddFlg = true;
            substitutionLabel.setBackground(ColorMaster.getSelectableColor());
            calcLabel.setBackground(ColorMaster.getSelectableColor());
            ifLabel.setBackground(ColorMaster.getSelectableColor());
            forLabel.setBackground(ColorMaster.getSelectableColor());
            whileLabel.setBackground(ColorMaster.getSelectableColor());
            functionLabel.setBackground(ColorMaster.getSelectableColor());
        }else {
            clickAddFlg = false;
            substitutionLabel.setBackground(ColorMaster.getBackColor());
            calcLabel.setBackground(ColorMaster.getBackColor());
            ifLabel.setBackground(ColorMaster.getBackColor());
            forLabel.setBackground(ColorMaster.getBackColor());
            whileLabel.setBackground(ColorMaster.getBackColor());
            functionLabel.setBackground(ColorMaster.getBackColor());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** 選択された変数のダイアログを有効にする */
        JPanel panel = (JPanel) e.getSource();
        if (panel == substitutionLabel && clickAddFlg) {
            orderMode = EditDialogMode.SUBSTITUTION;
            substitutionDialog = new SubstitutionDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            substitutionDialog.setVisible(true);
        } else if (panel == calcLabel && clickAddFlg) {
            orderMode = EditDialogMode.CALC;
            calcDialog = new CalcDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            calcDialog.setVisible(true);
        } else if (panel == ifLabel && clickAddFlg) {
            orderMode = EditDialogMode.IF;
            ifDialog = new IfDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            ifDialog.setVisible(true);
        } else if (panel == forLabel && clickAddFlg) {
            orderMode = EditDialogMode.FOR;
            if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>()) ||
                !getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>()) ||
                !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())){
                forDialog = new ForDialog(getFrame(), DialogOpenMode.ADD, e);
                updateOrderIndicateLabel();
                forDialog.setVisible(true);
            }else {
                JOptionPane.showMessageDialog(calcLabel, "カウント変数が必要です！\n変数の作成を行ってください。");
            }
        } else if (panel == whileLabel && clickAddFlg) {
            orderMode = EditDialogMode.WHILE;
            whileDialog = new WhileDialog(getFrame(), DialogOpenMode.ADD, e);
            updateOrderIndicateLabel();
            whileDialog.setVisible(true);
        } else if(panel == functionLabel && clickAddFlg){
            if (getFrame().getBasePanel().getSubOrderPanel().getProgramModel().getSize() >= 3){
                orderMode = EditDialogMode.LOADFUNCTION;
                loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                updateOrderIndicateLabel();
                loadFunctionDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(calcLabel, "呼び出す関数が必要です！\nSETUP、MAIN以外の関数を作成してください。");
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
        getFrame().setOrderPanelCanClick(false, false, clickAddFlg);
    }
    /** クリック時パネルが有効なら色変更 */
    @Override
    public void mousePressed(MouseEvent e) {
        JPanel panel = (JPanel)e.getSource();
        if (clickAddFlg) {
            panel.setBackground(ColorMaster.getClickedColor());
        }
    }
    /** クリック終了時パネルの有効無効にあわせ色変更 */
    @Override
    public void mouseReleased(MouseEvent e) {
        JPanel panel = (JPanel)e.getSource();
        if (clickAddFlg){
            panel.setBackground(ColorMaster.getSelectableColor());
        }else {
            panel.setBackground(ColorMaster.getBackColor());
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean getClickAddFlg() {
        return clickAddFlg;
    }
}