package KUU.FrameWorkComponent.ExecuteComponent.MainOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import Master.ColorMaster.*;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 実行行数を変更するためのダイアログ。
 */
public class OptionChangeDialog extends NewJDialog implements MouseListener{
    private GeneralItemPanel optionDetailLabel;
    private GeneralItemPanel optionCollectLabel;
    private GeneralItemPanel optionConfirmLabel;
    private DialogBasePanel  panel;
    private String           optionMode;

    public OptionChangeDialog(BaseFrame frame, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1, 1));

        panel = new DialogBasePanel(frame);
        panel.setLayout(null);

        panel.add(optionDetailLabel = new GeneralItemPanel(null,null,"一行ずつ"));
        panel.add(optionCollectLabel = new GeneralItemPanel(null,null,"一気に"));
        panel.add(optionConfirmLabel = new GeneralItemPanel(true,null,"確定"));

        optionDetailLabel.addMouseListener(this);
        optionCollectLabel.addMouseListener(this);
        optionConfirmLabel.addMouseListener(this);

        switch (getFrame().getBasePanel().getMainExecutePanel().getOptionIndicateLabel().getText()){
            case "一行ずつ":
                optionDetailLabel.setBackground(ColorMaster.getSelectedColor());
                optionCollectLabel.setBackground(ColorMaster.getNotSelectedColor());
                break;
            case "一気に":
                optionDetailLabel.setBackground(ColorMaster.getNotSelectedColor());
                optionCollectLabel.setBackground(ColorMaster.getSelectedColor());
                break;
        }

        optionMode = getFrame().getBasePanel().getMainExecutePanel().getOptionIndicateLabel().getText();
        add(panel);

        setBounds(e.getXOnScreen(), e.getYOnScreen(), 300, 200);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        optionDetailLabel.setBackground(ColorMaster.getNotSelectedColor());
        optionCollectLabel.setBackground(ColorMaster.getNotSelectedColor());
        JPanel panel;
        if (e.getSource() instanceof JPanel){
            panel = (JPanel) e.getSource();
            panel.setBackground(ColorMaster.getSelectedColor());
            if (panel == optionDetailLabel){
                optionMode = "一行ずつ";
                optionDetailLabel.setBackground(ColorMaster.getSelectedColor());
                optionCollectLabel.setBackground(ColorMaster.getNotSelectedColor());
            }else if (panel == optionCollectLabel){
                optionMode = "一気に";
                optionDetailLabel.setBackground(ColorMaster.getNotSelectedColor());
                optionCollectLabel.setBackground(ColorMaster.getSelectedColor());
            }else if (panel == optionConfirmLabel){
                getFrame().getBasePanel().getMainExecutePanel().getOptionIndicateLabel().setText(optionMode);
                getFrame().getBasePanel().getMainExecutePanel().getOptionIndicateLabel().repaint();
                dispose();
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

    private class DialogBasePanel extends NewJPanel implements ComponentListener {

        public DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        @Override
        public void handResize(int width, int height) {
            optionDetailLabel.setBounds(0, 0, width / 2, (height / 8) * 7);
            optionCollectLabel.setBounds(width / 2, 0, width - width / 2, (height / 8) * 7);
            optionConfirmLabel.setBounds(0, (height / 8) * 7, width, height - (height / 8) * 7);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            handResize(getWidth(), getHeight());
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