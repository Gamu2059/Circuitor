package KUU.FrameWorkComponent.ExecuteComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.ExecuteComponent.MainOperateComponent.SpeedChangeDialog;
import KUU.NewComponent.NewJPanel;
import KUU.GeneralComponent.GeneralItemPanel;
import Master.ColorMaster.*;
import Master.ImageMaster.ImageMaster;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 実行モードでのメイン操作領域の画面。
 */
public class MainExecutePanel extends NewJPanel implements MouseListener{
    /** 実行速度 */
    private GeneralItemPanel speedTitleLabel;
    private GeneralItemPanel speedIndicateLabel;
    private GeneralItemPanel speedChangeLabel;
    private String speedMode;

    /** 実行中断,終了 */
    private GeneralItemPanel executeStopLabel;
    private GeneralItemPanel executeEndLabel;

    public MainExecutePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getBackColor());

        /** 実行速度 */
        add(speedTitleLabel      = new GeneralItemPanel("実行速度"));
        add(speedIndicateLabel   = new GeneralItemPanel("普通"));
        add(speedChangeLabel     = new GeneralItemPanel(true,null,"変更"));
        /** 実行中断,終了 */
        add(executeStopLabel = new GeneralItemPanel(true, ImageMaster.getImageMaster().getExecuteStopIcon(),"実行を中断する"));
        add(executeEndLabel  = new GeneralItemPanel(true, ImageMaster.getImageMaster().getExecuteStopIcon(),"実行を終了する"));

        speedMode = "普通";

        speedChangeLabel.addMouseListener(this);
        executeStopLabel.addMouseListener(this);
        executeEndLabel.addMouseListener(this);
    }

    @Override
    public void handResize(int width, int height) {
        int partsHeight = height/8 - 20;
        int indicateWidth = (width/3) * 2;

        /** 実行速度 */
        speedTitleLabel.setBounds(0, 0, width, 20);
        speedIndicateLabel.setBounds(0, 20, indicateWidth, partsHeight);
        speedChangeLabel.setBounds(indicateWidth, 20, width - indicateWidth, partsHeight);
        /** 実行中断,終了 */
        executeStopLabel.setBounds(0, height - partsHeight*2, width, partsHeight);
        executeEndLabel.setBounds(0, height - partsHeight, width, partsHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel panel;
        if (e.getSource() instanceof JPanel) {
            panel = (JPanel) e.getSource();
            if (panel == speedChangeLabel) {
                new SpeedChangeDialog(getFrame(),e);
            }else if (panel == executeStopLabel){
                getFrame().getBasePanel().getEditExecutePanel().getExecutor().setRunning(!getFrame().getBasePanel().getEditExecutePanel().getExecutor().isRunning());
                if (getFrame().getBasePanel().getEditExecutePanel().getExecutor().isRunning()) {
                    /* 停止状態から再開した場合 */
                    executeStopLabel.setIcon(ImageMaster.getImageMaster().getExecuteStopIcon());
                    executeStopLabel.setText("実行を中断する");
                } else {
                    /* 実行状態から停止した場合 */
                    executeStopLabel.setIcon(ImageMaster.getImageMaster().getExecuteStartIcon());
                    executeStopLabel.setText("実行を再開する");
                }
            } else if (panel == executeEndLabel) {
                getFrame().getBasePanel().stopExecuteMode();
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

    public GeneralItemPanel getSpeedIndicateLabel() {
        return speedIndicateLabel;
    }

    public String getSpeedMode() {
        return speedMode;
    }

    public void setSpeedMode(String speedMode) {
        this.speedMode = speedMode;
    }

    public GeneralItemPanel getExecuteStopLabel() {
        return executeStopLabel;
    }
}
