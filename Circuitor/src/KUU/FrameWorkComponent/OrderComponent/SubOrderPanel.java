package KUU.FrameWorkComponent.OrderComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.SubOperateComponent.FunctionDialog;
import KUU.FrameWorkComponent.OrderComponent.SubOperateComponent.OneArrayDialog;
import KUU.FrameWorkComponent.OrderComponent.SubOperateComponent.TwoArrayDialog;
import KUU.FrameWorkComponent.OrderComponent.SubOperateComponent.VariableDialog;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.DialogOpenMode;
import KUU.Mode.MainOrderVariableMode;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.UnitPanel.OrderUnitPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * 命令モードでのエディタ領域の画面。
 */
public class SubOrderPanel extends NewJPanel implements MouseListener{
    /** 変数エディタ */
    private DefaultListModel<String> programModel;
    private JList<String>            programList;
    private JScrollPane              programScroll;
    /** 変数編集ボタン */
    private GeneralItemPanel addLabel;
    private GeneralItemPanel editLabel;
    private GeneralItemPanel deleteLabel;
    /** ダイアログ */
    private FunctionDialog functionDialog;
    private VariableDialog variableDialog;
    private OneArrayDialog oneArrayDialog;
    private TwoArrayDialog twoArrayDialog;

    private OrderUnitPanel   orderUnitPanel;

    private boolean clickFlg;
    private int lineNumber;
    private String functionName;

    public SubOrderPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        /** コンポーネントの追加 */
        programModel = new DefaultListModel<>();
        programList  = new JList<>(programModel);
        add(programScroll  = new JScrollPane(programList));
        add(addLabel       = new GeneralItemPanel(true, ImageMaster.getImageMaster().getAddIcon(),"追加"));
        add(editLabel      = new GeneralItemPanel(false, ImageMaster.getImageMaster().getEditIcon(),"編集"));
        add(deleteLabel    = new GeneralItemPanel(false, ImageMaster.getImageMaster().getDeleteIcon(),"削除"));
        add(orderUnitPanel = new OrderUnitPanel(frame));

        programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        programList.addMouseListener(this);
        addLabel.addMouseListener(this);
        editLabel.addMouseListener(this);
        deleteLabel.addMouseListener(this);

        clickFlg = false;
        lineNumber = 0;
        functionName = "MAIN";
    }

    @Override
    public void handResize(int width, int height) {
        int partsHeight = height/12 - 20;

        programScroll.setBounds(0, 0, width, partsHeight*6 + 20*9);
        addLabel.setBounds(0, partsHeight*6 + 20*9, width, partsHeight);
        editLabel.setBounds(0, partsHeight*7 + 20*9, width, partsHeight);
        deleteLabel.setBounds(0, partsHeight*8 + 20*9, width, partsHeight);
        orderUnitPanel.setBounds(0, partsHeight*9 + 20*9, width, height - (partsHeight*9 + 20*9));
    }


    /** 編集/削除パネルがクリックできるかを設定する */
    public void setVariableEditDeleteCanClick(boolean flg) {
        if (flg) {
            clickFlg = true;
            editLabel.setBackground(ColorMaster.getSelectableColor());
            deleteLabel.setBackground(ColorMaster.getSelectableColor());
        }else {
            clickFlg = false;
            editLabel.setBackground(ColorMaster.getBackColor());
            deleteLabel.setBackground(ColorMaster.getBackColor());
        }
    }

    /** 変数リストを更新する */
    public void updateVariableList() {
        programModel.removeAllElements();
        switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
            case FUNCTION:
                for (String str : getFrame().getMasterTerminal().getFunctionGroup().getFunctionString()){
                    getFrame().getBasePanel().getSubOrderPanel().getProgramModel().addElement(str);
                }
                break;
            case VARIABLE:
                for (String str : getFrame().getMasterTerminal().getVariableMenuList("変数")){
                    getFrame().getBasePanel().getSubOrderPanel().getProgramModel().addElement(str);
                }
                break;
            case ARRAY:
                for (String str : getFrame().getMasterTerminal().getVariableMenuList("配列")){
                    getFrame().getBasePanel().getSubOrderPanel().getProgramModel().addElement(str);
                }
                break;
            case SQUARE:
                for (String str : getFrame().getMasterTerminal().getVariableMenuList("2次元配列")){
                    getFrame().getBasePanel().getSubOrderPanel().getProgramModel().addElement(str);
                }
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** JPanel */
        if (e.getSource() instanceof  JPanel) {
            JPanel panel = (JPanel) e.getSource();
            if (panel == addLabel) {
                /** 追加ボタン */
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        functionDialog = new FunctionDialog(getFrame(), DialogOpenMode.ADD, e);
                        functionDialog.setVisible(true);
                        break;
                    case VARIABLE:
                        variableDialog = new VariableDialog(getFrame(), DialogOpenMode.ADD, e);
                        variableDialog.setVisible(true);
                        break;
                    case ARRAY:
                        oneArrayDialog = new OneArrayDialog(getFrame(), DialogOpenMode.ADD, e);
                        oneArrayDialog.setVisible(true);
                        break;
                    case SQUARE:
                        twoArrayDialog = new TwoArrayDialog(getFrame(), DialogOpenMode.ADD, e);
                        twoArrayDialog.setVisible(true);
                        break;
                }
            } else if (panel == editLabel && clickFlg) {
                /** 編集ボタン */
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        getFrame().getHelpLabel().setText("関数名の編集を行います。");
                        functionDialog = new FunctionDialog(getFrame(), DialogOpenMode.EDIT, e);
                        functionDialog.setVisible(true);
                        break;
                    case VARIABLE:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        variableDialog = new VariableDialog(getFrame(), DialogOpenMode.EDIT, e);
                        variableDialog.setVisible(true);
                        break;
                    case ARRAY:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        oneArrayDialog = new OneArrayDialog(getFrame(), DialogOpenMode.EDIT, e);
                        oneArrayDialog.setVisible(true);
                        break;
                    case SQUARE:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        twoArrayDialog = new TwoArrayDialog(getFrame(), DialogOpenMode.EDIT, e);
                        twoArrayDialog.setVisible(true);
                        break;
                }
            } else if (panel == deleteLabel && clickFlg) {
                /**
                 * 削除ボタン
                 * メイン操作パネルからリストの種類を判定し
                 * 選択されたものを削除する
                 */
                ArrayList arrayList;
                String selectVariable;
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        selectVariable = programList.getSelectedValue();
                        if (JOptionPane.showConfirmDialog(this, selectVariable + "を削除しますか？", "関数削除の確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            getFrame().getMasterTerminal().deleteFunction(selectVariable);
                            if (functionName.equals(selectVariable)) {
                                functionName = "MAIN";
                            }
                        }
                        getFrame().getHelpLabel().setText("");
                        break;
                    case VARIABLE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("変数");
                        selectVariable = (String) arrayList.get(lineNumber);
                        getFrame().getMasterTerminal().deleteVariable("変数", selectVariable);
                        getFrame().getHelpLabel().setText("");
                        break;
                    case ARRAY:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("配列");
                        selectVariable = (String) arrayList.get(lineNumber);
                        getFrame().getMasterTerminal().deleteVariable("配列", selectVariable);
                        getFrame().getHelpLabel().setText("");
                        break;
                    case SQUARE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("2次元配列");
                        selectVariable = (String) arrayList.get(lineNumber);
                        getFrame().getMasterTerminal().deleteVariable("2次元配列", selectVariable);
                        getFrame().getHelpLabel().setText("");
                        break;
                }
                getFrame().updateOrderPanel(true);
            }
            getFrame().setOrderPanelCanClick(false, false, true);
        }

        /** 変数リスト*/
        if (e.getSource() instanceof JList) {
            /** SETUP関数とMAIN関数以外を選択すると編集/削除パネルがクリックできるようになる */
            try {
                if (!programList.getSelectedValue().equals("SETUP") &&
                        !programList.getSelectedValue().equals("MAIN")) {
                    getFrame().setOrderPanelCanClick(true, false, true);
                } else {
                    getFrame().setOrderPanelCanClick(false, false, true);
                }
                if (getFrame().getBasePanel().getMainOrderPanel().getVariableMode() == MainOrderVariableMode.FUNCTION) {
                    /**
                     * メイン編集モードが関数のとき
                     * リストを選択するとエディタパネルを更新し
                     * lineNumberをプログラムの一番下にセットする
                     */
                    functionName = programList.getSelectedValue();
                    getFrame().updateOrderPanel(false);
                    lineNumber = programList.getSelectedIndex();
                    getFrame().getBasePanel().getEditOrderPanel().setLineNumber(getFrame().getBasePanel().getEditOrderPanel().getProgramModel().getSize() - 1);
                    switch (functionName) {
                        case "SETUP":
                            getFrame().getHelpLabel().setText("SETUP関数です。主に変数の初期化等に使います。");
                            break;
                        case "MAIN":
                            getFrame().getHelpLabel().setText("MAIN関数です。主な処理の流れが入るとよいでしょう。");
                            break;
                            
                    }
                }
                /** SelectOrderPanelのVisible設定 */
                getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().setPanelVisible(functionName);
                lineNumber = programList.getSelectedIndex();
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }
    /** クリック時パネルが有効なら色変更 */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource()instanceof JPanel && clickFlg) {
            JPanel panel = (JPanel) e.getSource();
            if (panel != addLabel) {
                panel.setBackground(ColorMaster.getClickedColor());
            }
        }
    }
    /** クリック終了時パネルの有効無効にあわせ色変更 */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource()instanceof JPanel) {
            JPanel panel = (JPanel) e.getSource();
            if (panel != addLabel) {
                if (clickFlg) {
                    panel.setBackground(ColorMaster.getSelectableColor());
                } else {
                    panel.setBackground(ColorMaster.getBackColor());
                }
            }
        }
    }
    /** マウスオーバー時のヘルプラベルの表示変更 */
    @Override
    public void mouseEntered(MouseEvent e) {
        /** JPanel */
        if (e.getSource() instanceof  JPanel) {
            JPanel panel = (JPanel) e.getSource();
            if (panel == addLabel) {
                /** 追加ボタン */
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        getFrame().getHelpLabel().setText("関数の追加を行います。");
                        break;
                    case VARIABLE:
                        getFrame().getHelpLabel().setText("変数の追加を行います。");
                        break;
                    case ARRAY:
                        getFrame().getHelpLabel().setText("一次元配列の追加を行います。");
                        break;
                    case SQUARE:
                        getFrame().getHelpLabel().setText("二次元配列の追加を行います。");
                        break;
                }
            } else if (panel == editLabel && clickFlg) {
                /** 編集ボタン */
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        String selectVariable = programList.getSelectedValue();
                        switch (selectVariable) {
                            case "SETUP":
                                getFrame().getHelpLabel().setText("関数名の編集を行います。SETUP関数の名前の編集は出来ません。");
                                break;
                            case "MAIN":
                                getFrame().getHelpLabel().setText("関数名の編集を行います。MAIN関数の名前の編集は出来ません。");
                                break;
                            default:
                                getFrame().getHelpLabel().setText("関数名の編集を行います。");
                                break;
                        }
                        break;
                    case VARIABLE:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        break;
                    case ARRAY:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        break;
                    case SQUARE:
                        getFrame().getHelpLabel().setText("変数名の編集を行います。");
                        break;
                }
            } else if (panel == deleteLabel && clickFlg) {
                /** 削除ボタン */
                ArrayList arrayList;
                String selectVariable="";
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        selectVariable = programList.getSelectedValue();
                        break;
                    case VARIABLE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("変数");
                        selectVariable = (String) arrayList.get(lineNumber);
                        break;
                    case ARRAY:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("配列");
                        selectVariable = (String) arrayList.get(lineNumber);
                        break;
                    case SQUARE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("2次元配列");
                        selectVariable = (String) arrayList.get(lineNumber);
                        break;
                }
                getFrame().getHelpLabel().setText(selectVariable + "を削除します。");
            }
        }
    }

    /** パネルからマウスが離れたらヘルプラベルを空白に戻す */
    @Override
    public void mouseExited(MouseEvent e) {
        /** JPanel */
        if (e.getSource() instanceof  JPanel) {
            JPanel panel = (JPanel) e.getSource();
            if (panel == addLabel) {
                getFrame().getHelpLabel().setText("");
            } else if (panel == editLabel && clickFlg) {
                getFrame().getHelpLabel().setText("");
            } else if (panel == deleteLabel && clickFlg) {
                getFrame().getHelpLabel().setText("");
            }
        }
    }

    public OrderUnitPanel getOrderUnitPanel() {
        return orderUnitPanel;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public DefaultListModel<String> getProgramModel() {
        return programModel;
    }

    public JList<String> getProgramList() {
        return programList;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
