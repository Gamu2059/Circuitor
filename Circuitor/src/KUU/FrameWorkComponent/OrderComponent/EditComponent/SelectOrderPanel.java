package KUU.FrameWorkComponent.OrderComponent.EditComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.*;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.EditDialogMode;
import KUU.Mode.MainOrderVariableMode;
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
    private GeneralItemPanel breakLabel;
    private GeneralItemPanel continueLabel;
    private GeneralItemPanel loadFunctionLabel;
    private GeneralItemPanel variableSettingLabel;
    /** 命令ダイアログ */
    private SubstitutionDialog substitutionDialog;
    private CalcDialog         calcDialog;
    private IfDialog           ifDialog;
    private ForDialog          forDialog;
    private WhileDialog        whileDialog;
    private LoadFunctionDialog loadFunctionDialog;
    private SelectVariableDialog selectVariableDialog;

    private EditDialogMode orderMode;
    private boolean clickAddFlg;

    public SelectOrderPanel(BaseFrame frame){
        super(frame);
        setLayout(null);

        /** 命令選択ラベル */
        add(substitutionLabel    = new GeneralItemPanel(true,null,"代入"));
        add(calcLabel            = new GeneralItemPanel(true,null,"計算"));
        add(ifLabel              = new GeneralItemPanel(true,null,"if文"));
        add(forLabel             = new GeneralItemPanel(true,null,"for文"));
        add(whileLabel           = new GeneralItemPanel(true,null,"while文"));
        add(breakLabel           = new GeneralItemPanel(true,null,"break文"));
        add(continueLabel        = new GeneralItemPanel(true,null,"continue文"));
        add(loadFunctionLabel    = new GeneralItemPanel(true,null,"関数の呼び出し"));
        add(variableSettingLabel = new GeneralItemPanel(false,null,"配列の初期化"));

        substitutionLabel.addMouseListener(this);
        calcLabel.addMouseListener(this);
        ifLabel.addMouseListener(this);
        forLabel.addMouseListener(this);
        whileLabel.addMouseListener(this);
        breakLabel.addMouseListener(this);
        continueLabel.addMouseListener(this);
        loadFunctionLabel.addMouseListener(this);
        variableSettingLabel.addMouseListener(this);

        variableSettingLabel.setVisible(false);

        clickAddFlg = true;
    }

    @Override
    public void handResize(int width, int height){
//        int partsWidth = width/6;
//        int partsHeight = height/3;
//
//        /** 命令選択ラベル
//         *  代入  計算
//         *  if    for
//         *  while 関数or変数設定 */
//        substitutionLabel.setBounds(0, 0, partsWidth, partsHeight);
//        calcLabel.setBounds(partsWidth, 0, partsWidth, partsHeight);
//        ifLabel.setBounds(0, partsHeight, partsWidth, partsHeight);
//        forLabel.setBounds(partsWidth, partsHeight, partsWidth, partsHeight);
//        whileLabel.setBounds(0, partsHeight*2, partsWidth, height - partsHeight*2);
//        loadFunctionLabel.setBounds(partsWidth, partsHeight*2, partsWidth, height - partsHeight*2);
//        variableSettingLabel.setBounds(partsWidth, partsHeight*2, partsWidth, height - partsHeight*2);

        int partsWidth = width/6;
        int partsHeight = height/4;

        /** 命令選択ラベル
         *  代入     計算
         *  if       for
         *  while    break
         *  continue 関数or変数設定*/
        substitutionLabel.setBounds(0, 0, partsWidth, partsHeight);
        calcLabel.setBounds(partsWidth, 0, partsWidth, partsHeight);
        ifLabel.setBounds(0, partsHeight, partsWidth, partsHeight);
        forLabel.setBounds(partsWidth, partsHeight, partsWidth, partsHeight);
        whileLabel.setBounds(0, partsHeight*2, partsWidth, partsHeight);
        breakLabel.setBounds(partsWidth, partsHeight*2, partsWidth, partsHeight);
        continueLabel.setBounds(0, partsHeight*3, partsWidth, height - partsHeight*3);
        loadFunctionLabel.setBounds(partsWidth, partsHeight*3, partsWidth, height - partsHeight*3);
        variableSettingLabel.setBounds(partsWidth, partsHeight*3, partsWidth, height - partsHeight*3);
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
            case SELECTVARIABLE:
                selectVariableDialog.updateOrderIndicateLabel();
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
            loadFunctionLabel.setVisible(false);
            variableSettingLabel.setVisible(true);
        }else {
            variableSettingLabel.setVisible(false);
            loadFunctionLabel.setVisible(true);
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
            breakLabel.setBackground(ColorMaster.getSelectableColor());
            continueLabel.setBackground(ColorMaster.getSelectableColor());
            loadFunctionLabel.setBackground(ColorMaster.getSelectableColor());
            variableSettingLabel.setBackground(ColorMaster.getSelectableColor());
        }else {
            clickAddFlg = false;
            substitutionLabel.setBackground(ColorMaster.getBackColor());
            calcLabel.setBackground(ColorMaster.getBackColor());
            ifLabel.setBackground(ColorMaster.getBackColor());
            forLabel.setBackground(ColorMaster.getBackColor());
            whileLabel.setBackground(ColorMaster.getBackColor());
            breakLabel.setBackground(ColorMaster.getBackColor());
            continueLabel.setBackground(ColorMaster.getBackColor());
            loadFunctionLabel.setBackground(ColorMaster.getBackColor());
            variableSettingLabel.setBackground(ColorMaster.getBackColor());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** 選択された変数のダイアログを有効にする */
        JPanel panel = (JPanel) e.getSource();
        if (clickAddFlg) {
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
                        !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())) {
                    forDialog = new ForDialog(getFrame(), DialogOpenMode.ADD, e);
                    updateOrderIndicateLabel();
                    forDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(calcLabel, "カウント変数が必要です！\n変数の作成を行ってください。");
                    /** 変数作成モードに移行 */
                    getFrame().getBasePanel().getMainOrderPanel().setVariableMode(MainOrderVariableMode.VARIABLE);
                    getFrame().getBasePanel().getMainOrderPanel().getFunctionLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getOneDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getTwoDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().updateOrderPanel(true);
                }
            } else if (panel == whileLabel) {
                orderMode = EditDialogMode.WHILE;
                whileDialog = new WhileDialog(getFrame(), DialogOpenMode.ADD, e);
                updateOrderIndicateLabel();
                whileDialog.setVisible(true);
            } else if (panel == loadFunctionLabel) {
                if (getFrame().getBasePanel().getSubOrderPanel().getProgramModel().getSize() >= 3) {
                    orderMode = EditDialogMode.LOADFUNCTION;
                    loadFunctionDialog = new LoadFunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                    updateOrderIndicateLabel();
                    loadFunctionDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(calcLabel, "呼び出す関数が必要です！\nSETUP、MAIN以外の関数を作成してください。");
                    /** 関数作成モードに移行 */
                    getFrame().getBasePanel().getMainOrderPanel().setVariableMode(MainOrderVariableMode.FUNCTION);
                    getFrame().getBasePanel().getMainOrderPanel().getFunctionLabel().setBackground(ColorMaster.getSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getVariableLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getOneDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getTwoDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().updateOrderPanel(true);
                }
            } else if (panel == variableSettingLabel) {
                if (!getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>()) ||
                        !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())) {
                    orderMode = EditDialogMode.SELECTVARIABLE;
                    selectVariableDialog = new SelectVariableDialog(getFrame(), e);
                    updateOrderIndicateLabel();
                    selectVariableDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(calcLabel, "設定する変数がありません。\n変数の作成を行ってください。");
                    /** 1次元配列作成モードに移行 */
                    getFrame().getBasePanel().getMainOrderPanel().setVariableMode(MainOrderVariableMode.ARRAY);
                    getFrame().getBasePanel().getMainOrderPanel().getFunctionLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getVariableLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getOneDimensionArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getTwoDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().updateOrderPanel(true);
                }
            } else if (panel == continueLabel) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                if (getFrame().getMasterTerminal().getFunctionGroup().searchFunction(functionName).searchBeginningSyntax(lineNumber) != null) {
                    getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(Syntax.S_TYPE.CONTINUE));
                    getFrame().getBasePanel().getEditOrderPanel().updateProgramList();
                } else {
                    JLabel label = new JLabel("if文、for文、while文の中でのみ使用できます。");
                    JOptionPane.showMessageDialog(this, label);
                }
            } else if (panel == breakLabel) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                int lineNumber = getFrame().getBasePanel().getEditOrderPanel().getLineNumber();
                if (getFrame().getMasterTerminal().getFunctionGroup().searchFunction(functionName).searchBeginningSyntax(lineNumber) != null) {
                    getFrame().getMasterTerminal().addOrder(functionName, lineNumber, new Syntax(Syntax.S_TYPE.BREAK));
                    getFrame().getBasePanel().getEditOrderPanel().updateProgramList();
                } else {
                    JLabel label = new JLabel("if文、for文、while文の中でのみ使用できます。");
                    JOptionPane.showMessageDialog(this, label);
                }
            }
        }
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
        JPanel panel = (JPanel) e.getSource();
        String str = "";
        if (clickAddFlg) {
            if (panel == substitutionLabel) {
                str = "代入文を作成します。";
            } else if (panel == calcLabel) {
                str = "計算文を作成します。";
            } else if (panel == ifLabel) {
                str = "if文を作成します。";
            } else if (panel == forLabel) {
                str = "for文を作成します。";
                if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>()) ||
                        !getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>()) ||
                        !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())) {
                } else {
                    str += "変数が必要です。サブ操作パネルで変数の作成を行ってください。";
                }
            } else if (panel == whileLabel) {
                str = "while文を作成します。";
            } else if (panel == loadFunctionLabel) {
                str = "関数を挿入します。";
                if (getFrame().getBasePanel().getSubOrderPanel().getProgramModel().getSize() < 3) {
                    str += "呼び出す関数が必要です。左のパネルで関数を作成してください。";
                }
            } else if (panel == variableSettingLabel) {
                str = "配列の初期値を設定します。";
                if (!getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>()) ||
                        !getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())) {
                } else {
                    str += "変数が必要です。サブ操作パネルで変数の作成を行ってください。";
                }
            } else if (panel == breakLabel) {
                str += "break文を挿入します。if文、for文、while文の中でのみ使用できます。";
            } else if (panel == continueLabel) {
                str += "continue文を挿入します。if文、for文、while文の中でのみ使用できます。";
            }
            getFrame().getHelpLabel().setText(str);
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean getClickAddFlg() {
        return clickAddFlg;
    }
}