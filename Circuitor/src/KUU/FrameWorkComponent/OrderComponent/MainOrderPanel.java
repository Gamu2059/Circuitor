package KUU.FrameWorkComponent.OrderComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.MainOrderVariableMode;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * 命令モードでのエディタ領域の画面。
 */
public class MainOrderPanel extends NewJPanel implements MouseListener{
    private GeneralItemPanel functionLabel;
    private GeneralItemPanel variableLabel;
    private GeneralItemPanel oneDimensionArrayLabel;
    private GeneralItemPanel twoDimensionArrayLabel;
    private GeneralItemPanel executeStartLabel;
    private MainOrderVariableMode variableMode;

    public MainOrderPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getBackColor());

        add(functionLabel = new GeneralItemPanel(null,ImageMaster.getImageMaster().getFunctionIcon(),"関数"));
        add(variableLabel = new GeneralItemPanel(null,ImageMaster.getImageMaster().getVariableIcon(),"変数"));
        add(oneDimensionArrayLabel = new GeneralItemPanel(null,ImageMaster.getImageMaster().getOneDimensionArrayIcon(),"一次元配列"));
        add(twoDimensionArrayLabel = new GeneralItemPanel(null,ImageMaster.getImageMaster().getTwoDimensionArrayIcon(),"二次元配列"));
        add(executeStartLabel  = new GeneralItemPanel(true,ImageMaster.getImageMaster().getExecuteStartIcon(),"実行する"));

        functionLabel.addMouseListener(this);
        variableLabel.addMouseListener(this);
        oneDimensionArrayLabel.addMouseListener(this);
        twoDimensionArrayLabel.addMouseListener(this);
        executeStartLabel.addMouseListener(this);

        functionLabel.setBackground(ColorMaster.getSelectedColor());
        variableMode = MainOrderVariableMode.FUNCTION;
    }

    public MainOrderVariableMode getVariableMode() {
        return variableMode;
    }

    public void setVariableMode(MainOrderVariableMode variableMode) {
        this.variableMode = variableMode;
    }

    public GeneralItemPanel getFunctionLabel() {
        return functionLabel;
    }

    public GeneralItemPanel getVariableLabel() {
        return variableLabel;
    }

    public GeneralItemPanel getOneDimensionArrayLabel() {
        return oneDimensionArrayLabel;
    }

    public GeneralItemPanel getTwoDimensionArrayLabel() {
        return twoDimensionArrayLabel;
    }

    @Override
    public void handResize(int width, int height) {
        int partsHeight = height/8 - 20;

        functionLabel.setBounds(0, 0, width, partsHeight);
        variableLabel.setBounds(0, partsHeight, width, partsHeight);
        oneDimensionArrayLabel.setBounds(0, partsHeight*2, width, partsHeight);
        twoDimensionArrayLabel.setBounds(0, partsHeight*3, width, partsHeight);
        executeStartLabel.setBounds(0, height - partsHeight, width, partsHeight);
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
    public void mouseClicked(MouseEvent e) {
        getFrame().setOrderPanelCanClick(false, false, true);
        JPanel panel = (JPanel) e.getSource();
        if (panel == executeStartLabel) {
            getFrame().getHelpLabel().setText("");
            getFrame().getBasePanel().runExecuteMode();
        }else {
            functionLabel.setBackground(ColorMaster.getNotSelectedColor());
            variableLabel.setBackground(ColorMaster.getNotSelectedColor());
            oneDimensionArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
            twoDimensionArrayLabel.setBackground(ColorMaster.getNotSelectedColor());
            panel.setBackground(ColorMaster.getSelectedColor());
            if (panel == functionLabel) {
                variableMode = MainOrderVariableMode.FUNCTION;
                getFrame().getHelpLabel().setText("関数モード：サブ操作パネルから編集できます。");
            } else if (panel == variableLabel) {
                variableMode = MainOrderVariableMode.VARIABLE;
                getFrame().getHelpLabel().setText("変数モード：サブ操作パネルから編集できます。");
            } else if (panel == oneDimensionArrayLabel) {
                variableMode = MainOrderVariableMode.ARRAY;
                getFrame().getHelpLabel().setText("一次元配列モード：サブ操作パネルから編集できます。");
            } else if (panel == twoDimensionArrayLabel) {
                variableMode = MainOrderVariableMode.SQUARE;
                getFrame().getHelpLabel().setText("二次元配列モード：サブ操作パネルから編集できます。");
            }
            getFrame().updateOrderPanel(true);
        }
    }
}