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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private OrderUnitPanel   orderUnitPanel;
    /** ダイアログ */
    private FunctionDialog functionDialog;
    private VariableDialog variableDialog;
    private OneArrayDialog oneArrayDialog;
    private TwoArrayDialog twoArrayDialog;

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
        add(editLabel      = new GeneralItemPanel(true, ImageMaster.getImageMaster().getEditIcon(),"編集"));
        add(deleteLabel    = new GeneralItemPanel(true, ImageMaster.getImageMaster().getDeleteIcon(),"削除"));
        add(orderUnitPanel = new OrderUnitPanel(frame));

        programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        programList.addMouseListener(this);

        addLabel.addMouseListener(this);
        editLabel.addMouseListener(this);
        deleteLabel.addMouseListener(this);

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
        /** panel */
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
            } else if (panel == editLabel) {
                /** 編集ボタン */
                if (lineNumber != -1) {
                    switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                        case FUNCTION:
                            String selectVariable = programList.getSelectedValue();
                            switch (selectVariable) {
                                case "SETUP":
                                    JOptionPane.showMessageDialog(this, "SETUP関数の名前の編集は出来ません。");
                                    break;
                                case "MAIN":
                                    JOptionPane.showMessageDialog(this, "MAIN関数の名前の編集は出来ません。");
                                    break;
                                default:
                                    functionDialog = new FunctionDialog(getFrame(), DialogOpenMode.EDIT, e);
                                    functionDialog.setVisible(true);
                                    break;
                            }
                            break;
                        case VARIABLE:
                            variableDialog = new VariableDialog(getFrame(), DialogOpenMode.EDIT, e);
                            variableDialog.setVisible(true);
                            break;
                        case ARRAY:
                            oneArrayDialog = new OneArrayDialog(getFrame(), DialogOpenMode.EDIT, e);
                            oneArrayDialog.setVisible(true);
                            break;
                        case SQUARE:
                            twoArrayDialog = new TwoArrayDialog(getFrame(), DialogOpenMode.EDIT, e);
                            twoArrayDialog.setVisible(true);
                            break;
                    }
                }else {
                    if (getFrame().getBasePanel().getMainOrderPanel().getVariableMode() == MainOrderVariableMode.FUNCTION){
                        JOptionPane.showMessageDialog(this, "関数を選択してください。");
                    }else {
                        JOptionPane.showMessageDialog(this, "変数を選択してください。");
                    }
                }
            } else if (panel == deleteLabel) {
                /**
                 * 削除ボタン
                 * メイン操作パネルからリストの種類を判定し
                 * 選択されたものを削除する
                 */
                ArrayList arrayList;
                String selectVariable;
                switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()) {
                    case FUNCTION:
                        try {
                            selectVariable = programList.getSelectedValue();
                            switch (selectVariable) {
                                case "SETUP":
                                    JOptionPane.showMessageDialog(this, "SETUP関数は削除出来ません。");
                                    break;
                                case "MAIN":
                                    JOptionPane.showMessageDialog(this, "MAIN関数は削除出来ません。");
                                    break;
                                default:
                                    if (JOptionPane.showConfirmDialog(this, selectVariable + "を削除しますか？", "関数削除の確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                        getFrame().getMasterTerminal().deleteFunction(selectVariable);
                                    }
                                    break;
                            }
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(this, "関数を選択してください。");
                        }
                        break;
                    case VARIABLE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("変数");
                        try {
                            selectVariable = (String) arrayList.get(lineNumber);
                            getFrame().getMasterTerminal().deleteVariable("変数", selectVariable);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(this, "変数を選択してください。");
                        }
                        break;
                    case ARRAY:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("配列");
                        try {
                            selectVariable = (String) arrayList.get(lineNumber);
                            getFrame().getMasterTerminal().deleteVariable("配列", selectVariable);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(this, "変数を選択してください。");
                        }
                        break;
                    case SQUARE:
                        arrayList = getFrame().getMasterTerminal().getVariableStringList("2次元配列");
                        try {
                            selectVariable = (String) arrayList.get(lineNumber);
                            getFrame().getMasterTerminal().deleteVariable("2次元配列", selectVariable);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(this, "変数を選択してください。");
                        }
                        break;
                }
                functionName = "MAIN";
                getFrame().updateOrderPanel(true);
            }
        }

        /** programList */
        if (e.getSource() instanceof JList) {
            lineNumber = programList.getSelectedIndex();
            if (getFrame().getBasePanel().getMainOrderPanel().getVariableMode() == MainOrderVariableMode.FUNCTION) {
                /**
                 * メイン編集モードが関数のとき
                 * リストを選択するとエディタパネルを更新し
                 * lineNumberをプログラムの一番下にセットする
                 */
                functionName = programList.getSelectedValue();
                getFrame().updateOrderPanel(false);
                getFrame().getBasePanel().getEditOrderPanel().setLineNumber(getFrame().getBasePanel().getEditOrderPanel().getProgramModel().getSize() - 1);
            }
        }
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

    public JScrollPane getProgramScroll() {
        return programScroll;
    }

    public FunctionDialog getFunctionDialog() {
        return functionDialog;
    }

    public VariableDialog getVariableDialog() {
        return variableDialog;
    }

    public OneArrayDialog getOneArrayDialog() {
        return oneArrayDialog;
    }

    public TwoArrayDialog getTwoArrayDialog() {
        return twoArrayDialog;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
