package Sho.CircuitObject.SubCircuitPanelComponent.PartsMove;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitOperateCommand.Command;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 部品移動操作を表示するためのパネル。
 */
public class PartsMovePanel extends NewJPanel implements MouseListener {
    private GeneralItemPanel partsMovePanel;
    private GeneralItemPanel partsCopyPanel;
    private GeneralItemPanel partsRotatePanel;

    public PartsMovePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getSubBackColor());

        add(partsMovePanel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsMoveIcon(), "部品の移動"));
        add(partsCopyPanel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsCopyIcon(), "部品の複製"));
        add(partsRotatePanel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsRotateIcon(), "部品の回転"));

        partsMovePanel.addMouseListener(this);
        partsCopyPanel.addMouseListener(this);
        partsRotatePanel.addMouseListener(this);
    }

    public GeneralItemPanel getPartsMovePanel() {
        return partsMovePanel;
    }

    public GeneralItemPanel getPartsCopyPanel() {
        return partsCopyPanel;
    }

    public GeneralItemPanel getPartsRotatePanel() {
        return partsRotatePanel;
    }

    @Override
    public void handResize(int w, int h) {
        /* メイン操作パネルの高さと同期させる */
        if (getFrame().getBasePanel() != null) {
            int partsh = getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel().getHeight();

            partsMovePanel.setBounds(0, 0, w, partsh);
            partsCopyPanel.setBounds(0, partsh, w, partsh);
            partsRotatePanel.setBounds(0, partsh*2, w, partsh);
        }
    }

    /**
     * changeModeから呼び出される。
     * モード切替が発生したらサブ操作パネルの一番上にある操作がデフォルトになる。
     */
    public void resetCommand() {
        partsMovePanel.setBackground(ColorMaster.getSelectedColor());
        partsCopyPanel.setBackground(ColorMaster.getNotSelectedColor());
        partsRotatePanel.setBackground(ColorMaster.getNotSelectedColor());
        getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCommand().setCommand(Command.PARTS_MOVE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** パネルの色を初期化する */
        partsMovePanel.setBackground(ColorMaster.getNotSelectedColor());
        partsCopyPanel.setBackground(ColorMaster.getNotSelectedColor());
        partsRotatePanel.setBackground(ColorMaster.getNotSelectedColor());
        JPanel panel;
        if (e.getSource() instanceof JPanel) {
            panel = (JPanel) e.getSource();
            /** 押されたパネルは色を付ける */
            panel.setBackground(ColorMaster.getSelectedColor());
            /** コマンド切替を行い再描画 */
            getFrame().getBasePanel().getEditCircuitPanel().changeCommnad(panel);
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
}
