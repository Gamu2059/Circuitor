package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent.ConstantPanel;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent.OneArrayPanel;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent.TwoArrayPanel;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent.VariablePanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.EditOrderVariableMode;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import ProcessTerminal.SyntaxSettings.Factor;
import ProcessTerminal.SyntaxSettings.Index;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 定数,変数,一次元配列,二次元配列から使用する変数を選択するパネルを生成するクラス
 */
public class SelectConstantVariablePanel extends NewJPanel implements MouseListener{
    private GeneralItemPanel constantLabel;
    private GeneralItemPanel variableLabel;
    private GeneralItemPanel oneArrayLabel;
    private GeneralItemPanel twoArrayLabel;

    private ConstantPanel constantPanel;
    private VariablePanel variablePanel;
    private OneArrayPanel oneArrayPanel;
    private TwoArrayPanel twoArrayPanel;

    private EditOrderVariableMode editOrderVariableMode;

    public SelectConstantVariablePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(constantLabel = new GeneralItemPanel(null,null,"定数"));
        add(variableLabel = new GeneralItemPanel(null,null,"変数"));
        add(oneArrayLabel = new GeneralItemPanel(null,null,"一次元配列"));
        add(twoArrayLabel = new GeneralItemPanel(null,null,"二次元配列"));

        add(constantPanel = new ConstantPanel(frame));
        add(variablePanel = new VariablePanel(frame));
        add(oneArrayPanel = new OneArrayPanel(frame));
        add(twoArrayPanel = new TwoArrayPanel(frame));

        constantLabel.addMouseListener(this);
        variableLabel.addMouseListener(this);
        oneArrayLabel.addMouseListener(this);
        twoArrayLabel.addMouseListener(this);

        constantPanel.setLayout(null);
        variablePanel.setLayout(null);
        oneArrayPanel.setLayout(null);
        twoArrayPanel.setLayout(null);

        constantPanel.setVisible(false);
        variablePanel.setVisible(false);
        oneArrayPanel.setVisible(false);
        twoArrayPanel.setVisible(false);

        /** 初期設定 */
        editOrderVariableMode = EditOrderVariableMode.CONSTANT;
        constantLabel.setBackground(ColorMaster.getSelectedColor());
        constantPanel.setVisible(true);
    }


    /**
     * handResizeでは270の横幅を持つ。
     */
    @Override
    public void handResize(int width, int height) {
        int partsHeight = height/6;

        /** 変数選択パネル */
        constantLabel.setBounds(0, 0, 50, partsHeight);
        variableLabel.setBounds(50, 0, 50, partsHeight);
        oneArrayLabel.setBounds(100, 0, 85, partsHeight);
        twoArrayLabel.setBounds(185, 0, 85, partsHeight);

        /** 変数ごとの詳細ラベル*/
        constantPanel.setBounds(0, partsHeight, width, height - partsHeight);
        variablePanel.setBounds(0, partsHeight, width, height - partsHeight);
        oneArrayPanel.setBounds(0, partsHeight, width, height - partsHeight);
        twoArrayPanel.setBounds(0, partsHeight, width, height - partsHeight);

        constantPanel.handResize(width, height - partsHeight);
        variablePanel.handResize(width, height - partsHeight);
        oneArrayPanel.handResize(width, height - partsHeight);
        twoArrayPanel.handResize(width, height - partsHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel panel = (JPanel)e.getSource();
        boolean panelPaintFlg = true;
        /** 変数が用意されているかの判定、モード変更 */
        if (panel == constantLabel){
            constantPanel.setVisible(true);
            editOrderVariableMode = EditOrderVariableMode.CONSTANT;
        }
        else if(panel == variableLabel){
            if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>())) {
                variablePanel.setVisible(true);
                editOrderVariableMode = EditOrderVariableMode.VARIABLE;
            }else {
                panelPaintFlg = false;
            }
        }
        else if(panel == oneArrayLabel){
            if (!getFrame().getMasterTerminal().getVariableStringList("配列").equals(new ArrayList<>())) {
                oneArrayPanel.setVisible(true);
                editOrderVariableMode = EditOrderVariableMode.ARRAY;
            }else {

                panelPaintFlg = false;
            }
        }
        else if(panel == twoArrayLabel){
            if (!getFrame().getMasterTerminal().getVariableStringList("2次元配列").equals(new ArrayList<>())) {
                twoArrayPanel.setVisible(true);
                editOrderVariableMode = EditOrderVariableMode.SQUARE;
            }else {
                panelPaintFlg = false;
            }
        }

        if (panelPaintFlg) {
            /** パネルの再設定 */
            constantLabel.setBackground(ColorMaster.getNotSelectedColor());
            variableLabel.setBackground(ColorMaster.getNotSelectedColor());
            oneArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
            twoArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
            constantPanel.setVisible(false);
            variablePanel.setVisible(false);
            oneArrayPanel.setVisible(false);
            twoArrayPanel.setVisible(false);

            switch (editOrderVariableMode){
                case CONSTANT:
                    constantPanel.setVisible(true);
                    constantLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case VARIABLE:
                    variablePanel.setVisible(true);
                    variableLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case ARRAY:
                    oneArrayPanel.setVisible(true);
                    oneArrayLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case SQUARE:
                    twoArrayPanel.setVisible(true);
                    twoArrayLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
            }

            /** 命令プレビューの更新 */
            getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
        }else {
            JOptionPane.showMessageDialog(this, "使用できる変数がありません！\n変数の作成を行ってください。");
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

    public void resetSelect(){
        constantLabel.setBackground(ColorMaster.getNotSelectedColor());
        variableLabel.setBackground(ColorMaster.getNotSelectedColor());
        oneArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
        twoArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
        constantPanel.setVisible(false);
        variablePanel.setVisible(false);
        oneArrayPanel.setVisible(false);
        twoArrayPanel.setVisible(false);
        oneArrayPanel.resetSelect();
        twoArrayPanel.resetSelect();
    }

    public Factor getFactor() {
        Factor target = null;
        String str;
        int i;
        boolean bool;
        Index index1, index2;
        switch (editOrderVariableMode) {
            case CONSTANT:
                if ("".equals(constantPanel.getConstantText().getText())) {
                    JOptionPane.showMessageDialog(this, "定数は整数で入力してください。");
                }else {
                    str = constantPanel.getConstantText().getText();
                    try{
                        i = Integer.parseInt(str);
                        target = new Factor(i);
                    }catch (Exception e) {
                        try {
                            bool = Boolean.parseBoolean(str);
                            target = new Factor(bool);
                        }catch (Exception e1) {
                            str = constantPanel.getConstantText().getText();
                            target = new Factor(str);
                        }
                    }
                }
                break;
            case VARIABLE:
                str = (String) variablePanel.getVariableBox().getSelectedItem();
                target = new Factor(str);
                break;
            case ARRAY:
                str = (String) oneArrayPanel.getOneArrayBox().getSelectedItem();
                try {
                    if (oneArrayPanel.getConstantRadioButton().isSelected()) {
                        if ("".equals(oneArrayPanel.getConstantText().getText())){
                            throw new Exception();
                        }else {
                            index1 = new Index(Integer.parseInt(oneArrayPanel.getConstantText().getText()));
                        }
                    } else {
                        index1 = new Index((String) oneArrayPanel.getVariableBox().getSelectedItem());
                    }
                    target = new Factor(str, index1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "定数は整数で入力してください。");
                    oneArrayPanel.getConstantText().setText("");
                    target = null;
                }
                break;
            case SQUARE:
                str = (String) twoArrayPanel.getTwoArrayBox().getSelectedItem();
                try {
                    if (twoArrayPanel.getConstantRadioButton1().isSelected()) {
                        if ("".equals(twoArrayPanel.getConstantText1().getText())){
                            throw new Exception();
                        }else {
                            index1 = new Index(Integer.parseInt(twoArrayPanel.getConstantText1().getText()));
                        }
                    } else {
                        index1 = new Index((String) twoArrayPanel.getVariableBox1().getSelectedItem());
                    }
                    if (twoArrayPanel.getConstantRadioButton2().isSelected()) {
                        if ("".equals(twoArrayPanel.getConstantText2().getText())){
                            throw new Exception();
                        }else {
                            index2 = new Index(Integer.parseInt(twoArrayPanel.getConstantText2().getText()));
                        }
                    } else {
                        index2 = new Index((String) twoArrayPanel.getVariableBox2().getSelectedItem());
                    }
                    target = new Factor(str, index1, index2);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "定数は整数で入力してください。");
                    twoArrayPanel.getConstantText1().setText("");
                    twoArrayPanel.getConstantText2().setText("");
                    target = null;
                }
                break;
        }
        return target;
    }

    public void setEditOrderVariableMode(EditOrderVariableMode editOrderVariableMode) {
        this.editOrderVariableMode = editOrderVariableMode;
    }

    public EditOrderVariableMode getEditOrderVariableMode() {
        return editOrderVariableMode;
    }

    public GeneralItemPanel getConstantLabel() {
        return constantLabel;
    }

    public GeneralItemPanel getVariableLabel() {
        return variableLabel;
    }

    public GeneralItemPanel getOneArrayLabel() {
        return oneArrayLabel;
    }

    public GeneralItemPanel getTwoArrayLabel() {
        return twoArrayLabel;
    }

    public ConstantPanel getConstantPanel() {
        return constantPanel;
    }

    public VariablePanel getVariablePanel() {
        return variablePanel;
    }

    public OneArrayPanel getOneArrayPanel() {
        return oneArrayPanel;
    }

    public TwoArrayPanel getTwoArrayPanel() {
        return twoArrayPanel;
    }

    /** 選ばれている変数を文字列として取得 */
    public String getString(){
        String str = null;
        switch (editOrderVariableMode){
            case CONSTANT:
                str = constantPanel.getConstantText().getText();
                break;
            case VARIABLE:
                str = (String)variablePanel.getVariableBox().getSelectedItem();
                break;
            case ARRAY:
                str = (String)oneArrayPanel.getOneArrayBox().getSelectedItem();
                if (oneArrayPanel.getConstantRadioButton().isSelected()){
                    str += "["+ oneArrayPanel.getConstantText().getText() +"]";
                }else {
                    str += "["+ oneArrayPanel.getVariableBox().getSelectedItem() + "]";
                }
                break;
            case SQUARE:
                str = (String)twoArrayPanel.getTwoArrayBox().getSelectedItem();
                if (twoArrayPanel.getConstantRadioButton1().isSelected()){
                    str += "["+ twoArrayPanel.getConstantText1().getText() + "]";
                }else {
                    str += "["+ twoArrayPanel.getVariableBox1().getSelectedItem() + "]";
                }
                if (twoArrayPanel.getConstantRadioButton2().isSelected()){
                    str += "["+ twoArrayPanel.getConstantText2().getText() + "]";
                }else {
                    str += "["+ twoArrayPanel.getVariableBox2().getSelectedItem() + "]";
                }
                break;
        }
        return str;
    }
}
