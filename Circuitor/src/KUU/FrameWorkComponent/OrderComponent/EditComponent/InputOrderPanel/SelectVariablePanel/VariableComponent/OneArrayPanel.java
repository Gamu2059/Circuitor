package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 一次元配列を入力するパネル。
 */
public class OneArrayPanel extends NewJPanel implements MouseListener,ItemListener,DocumentListener{
    private JComboBox oneArrayBox;

    private JRadioButton constantRadioButton;
    private JRadioButton variableRadioButton;
    private ButtonGroup  buttonGroup;
    private GeneralTextField constantText;
    private JComboBox    variableBox;

    private GeneralItemPanel indexTitleLabel;
    private JLabel leftBracketLabel;
    private JLabel rightBracketLabel;

    public OneArrayPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        /** コンポーネントの追加 */
        add(oneArrayBox = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("配列").toArray()));
        add(constantRadioButton  = new JRadioButton("定数"));
        add(variableRadioButton  = new JRadioButton("変数"));
        buttonGroup = new ButtonGroup();
        buttonGroup.add(constantRadioButton);
        buttonGroup.add(variableRadioButton);
        add(constantText = new GeneralTextField());
        add(variableBox = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("変数").toArray()));
        add(indexTitleLabel = new GeneralItemPanel("添え字"));
        add(leftBracketLabel = new JLabel("["));
        add(rightBracketLabel = new JLabel("]"));

        /** リスナ登録 */
        oneArrayBox.addItemListener(this);
        constantRadioButton.addMouseListener(this);
        variableRadioButton.addMouseListener(this);
        constantText.getDocument().addDocumentListener(this);
        variableBox.addItemListener(this);

        /** 定数/変数の有効/無効設定 */
        constantRadioButton.setBackground(ColorMaster.getSelectedColor());
        variableRadioButton.setBackground(ColorMaster.getNotSelectedColor());
        constantRadioButton.setSelected(true);
        constantText.setVisible(true);
        variableBox.setVisible(false);

        leftBracketLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightBracketLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void resetSelect(){
        constantRadioButton.setBackground(ColorMaster.getNotSelectedColor());
        variableRadioButton.setBackground(ColorMaster.getNotSelectedColor());
        constantText.setVisible(false);
        variableBox.setVisible(false);
    }

    @Override
    public void handResize(int width, int height) {
        int partsWidth = width/10;
        int partsHeight = height/7;

        oneArrayBox.setBounds(partsWidth, partsHeight*2, partsWidth*2, partsHeight*3);

        constantRadioButton.setBounds(partsWidth*4, 5, partsWidth*2, partsHeight);
        variableRadioButton.setBounds(partsWidth*4, partsHeight + 5, partsWidth*2, partsHeight);

        leftBracketLabel.setBounds(partsWidth*4 - 20, partsHeight*3, 20, partsHeight);

        constantText.setBounds(partsWidth*4, partsHeight*3, partsWidth*2, partsHeight);
        variableBox.setBounds(partsWidth*4, partsHeight*3, partsWidth*2, partsHeight);

        rightBracketLabel.setBounds(partsWidth*6, partsHeight*3, 20, partsHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>())) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            if (radioButton == constantRadioButton) {
                constantRadioButton.setBackground(ColorMaster.getSelectedColor());
                variableRadioButton.setBackground(ColorMaster.getNotSelectedColor());
                constantText.setVisible(true);
                variableBox.setVisible(false);
            } else if (radioButton == variableRadioButton) {
                constantRadioButton.setBackground(ColorMaster.getNotSelectedColor());
                variableRadioButton.setBackground(ColorMaster.getSelectedColor());
                constantText.setVisible(false);
                variableBox.setVisible(true);
            }
            /** 命令プレビューの更新 */
            getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
        } else {
            constantRadioButton.setSelected(true);
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public JComboBox getOneArrayBox() {
        return oneArrayBox;
    }

    public JRadioButton getConstantRadioButton() {
        return constantRadioButton;
    }

    public JRadioButton getVariableRadioButton() {
        return variableRadioButton;
    }

    public GeneralTextField getConstantText() {
        return constantText;
    }

    public JComboBox getVariableBox() {
        return variableBox;
    }
}
