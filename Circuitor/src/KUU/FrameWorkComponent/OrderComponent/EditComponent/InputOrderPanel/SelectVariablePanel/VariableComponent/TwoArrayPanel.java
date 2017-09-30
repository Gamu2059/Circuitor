package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel.SelectVariablePanel.VariableComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
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
 * 二次元配列を入力するパネル。
 */
public class TwoArrayPanel extends NewJPanel implements MouseListener,DocumentListener,ItemListener{
    private JComboBox twoArrayBox;

    private JRadioButton constantRadioButton1;
    private JRadioButton constantRadioButton2;
    private JRadioButton variableRadioButton1;
    private JRadioButton variableRadioButton2;
    private ButtonGroup  buttonGroup1;
    private ButtonGroup  buttonGroup2;
    private JTextField constantText1;
    private JTextField constantText2;
    private JComboBox variableBox1;
    private JComboBox variableBox2;

    private GeneralItemPanel indexTitleLabel1;
    private GeneralItemPanel indexTitleLabel2;
    private JLabel leftBracketLabel1;
    private JLabel leftBracketLabel2;
    private JLabel rightBracketLabel1;
    private JLabel rightBracketLabel2;

    private GeneralItemPanel frameLabel;

    public TwoArrayPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        /** コンポーネントの追加 */
        add(twoArrayBox = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("2次元配列").toArray()));
        add(constantRadioButton1  = new JRadioButton("定数"));
        add(constantRadioButton2  = new JRadioButton("定数"));
        add(variableRadioButton1  = new JRadioButton("変数"));
        add(variableRadioButton2  = new JRadioButton("変数"));
        buttonGroup1 = new ButtonGroup();
        buttonGroup2 = new ButtonGroup();
        buttonGroup1.add(constantRadioButton1);
        buttonGroup2.add(constantRadioButton2);
        buttonGroup1.add(variableRadioButton1);
        buttonGroup2.add(variableRadioButton2);
        add(constantText1 = new JTextField());
        add(constantText2 = new JTextField());
        add(variableBox1 = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("変数").toArray()));
        add(variableBox2 = new JComboBox<>(getFrame().getMasterTerminal().getVariableStringList("変数").toArray()));
        add(indexTitleLabel1 = new GeneralItemPanel("添え字1"));
        add(indexTitleLabel2 = new GeneralItemPanel("添え字2"));
        add(leftBracketLabel1 = new JLabel("["));
        add(leftBracketLabel2 = new JLabel("["));
        add(rightBracketLabel1 = new JLabel("]"));
        add(rightBracketLabel2 = new JLabel("]"));
        add(frameLabel = new GeneralItemPanel(""));
        frameLabel.setBackground(null);

        /** リスナ登録 */
        twoArrayBox.addItemListener(this);
        constantRadioButton1.addMouseListener(this);
        constantRadioButton2.addMouseListener(this);
        variableRadioButton1.addMouseListener(this);
        variableRadioButton2.addMouseListener(this);
        constantText1.getDocument().addDocumentListener(this);
        constantText2.getDocument().addDocumentListener(this);
        variableBox1.addItemListener(this);
        variableBox2.addItemListener(this);

        /** 定数･変数の有効/無効設定 */
        constantRadioButton1.setSelected(true);
        constantRadioButton2.setSelected(true);
        constantRadioButton1.setBackground(ColorMaster.getSelectedColor());
        constantRadioButton2.setBackground(ColorMaster.getSelectedColor());
        variableRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
        variableRadioButton2.setBackground(ColorMaster.getNotSelectedColor());
        constantText1.setVisible(true);
        constantText2.setVisible(true);
        variableBox1.setVisible(false);
        variableBox2.setVisible(false);

        leftBracketLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        leftBracketLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        rightBracketLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        rightBracketLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void resetSelect(){
        constantRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
        constantRadioButton2.setBackground(ColorMaster.getNotSelectedColor());
        variableRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
        variableRadioButton2.setBackground(ColorMaster.getNotSelectedColor());
        constantText1.setVisible(false);
        constantText2.setVisible(false);
        variableBox1.setVisible(false);
        variableBox2.setVisible(false);
    }

    @Override
    public void handResize(int width, int height) {
        int partsWidth = width/10;
        int partsHeight = height/7;

        twoArrayBox.setBounds(partsWidth, partsHeight*2, partsWidth*2, partsHeight*3);

        constantRadioButton1.setBounds(partsWidth*4, 5, partsWidth*2, partsHeight);
        constantRadioButton2.setBounds(partsWidth*7, 5, partsWidth*2, partsHeight);
        variableRadioButton1.setBounds(partsWidth*4, partsHeight + 5, partsWidth*2, partsHeight);
        variableRadioButton2.setBounds(partsWidth*7, partsHeight + 5, partsWidth*2, partsHeight);

        leftBracketLabel1.setBounds(partsWidth*4 - 20, partsHeight*3, 20, partsHeight);
        leftBracketLabel2.setBounds(partsWidth*7 - 20, partsHeight*3, 20, partsHeight);

        constantText1.setBounds(partsWidth*4, partsHeight*3, partsWidth*2, partsHeight);
        constantText2.setBounds(partsWidth*7, partsHeight*3, partsWidth*2, partsHeight);
        variableBox1.setBounds(partsWidth*4, partsHeight*3, partsWidth*2, partsHeight);
        variableBox2.setBounds(partsWidth*7, partsHeight*3, partsWidth*2, partsHeight);

        rightBracketLabel1.setBounds(partsWidth*6, partsHeight*3, 20, partsHeight);
        rightBracketLabel2.setBounds(partsWidth*9, partsHeight*3, 20, partsHeight);

        frameLabel.setBounds(0, 0, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!getFrame().getMasterTerminal().getVariableStringList("変数").equals(new ArrayList<>())) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            if (radioButton == constantRadioButton1) {
                constantRadioButton1.setBackground(ColorMaster.getSelectedColor());
                variableRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
                constantText1.setVisible(true);
                variableBox1.setVisible(false);
            } else if (radioButton == variableRadioButton1) {
                constantRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
                variableRadioButton1.setBackground(ColorMaster.getSelectedColor());
                constantText1.setVisible(false);
                variableBox1.setVisible(true);
            } else if (radioButton == constantRadioButton2) {
                constantRadioButton2.setBackground(ColorMaster.getSelectedColor());
                variableRadioButton2.setBackground(ColorMaster.getNotSelectedColor());
                constantText2.setVisible(true);
                variableBox2.setVisible(false);
            } else if (radioButton == variableRadioButton2) {
                constantRadioButton2.setBackground(ColorMaster.getNotSelectedColor());
                variableRadioButton2.setBackground(ColorMaster.getSelectedColor());
                constantText2.setVisible(false);
                variableBox2.setVisible(true);
            }
        }else {
            constantRadioButton1.setSelected(true);
            constantRadioButton2.setSelected(true);
            JOptionPane.showMessageDialog(this, "使用できる変数がありません！\n変数の作成を行ってください。");
        }
        /** 命令プレビューの更新 */
        getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().updateOrderIndicateLabel();
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

    public JComboBox getTwoArrayBox() {
        return twoArrayBox;
    }

    public JRadioButton getConstantRadioButton1() {
        return constantRadioButton1;
    }

    public JRadioButton getConstantRadioButton2() {
        return constantRadioButton2;
    }

    public JRadioButton getVariableRadioButton1() {
        return variableRadioButton1;
    }

    public JRadioButton getVariableRadioButton2() {
        return variableRadioButton2;
    }

    public JTextField getConstantText1() {
        return constantText1;
    }

    public JTextField getConstantText2() {
        return constantText2;
    }

    public JComboBox getVariableBox1() {
        return variableBox1;
    }

    public JComboBox getVariableBox2() {
        return variableBox2;
    }
}
