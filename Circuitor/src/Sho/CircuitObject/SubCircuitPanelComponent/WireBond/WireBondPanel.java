package Sho.CircuitObject.SubCircuitPanelComponent.WireBond;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitOperateCommand.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 導線結合操作を表示するためのパネル。
 */
public class WireBondPanel extends NewJPanel implements MouseListener {
    private GeneralItemPanel wireBondLabel;
    private GeneralItemPanel wireCrossLabel;
    private GeneralItemPanel handBondLabel;
    private GeneralItemPanel autoBondLabel;

    public WireBondPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getSubBackColor());

        add(wireBondLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getBondIcon(), "導線の結合"));
        add(wireCrossLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getCrossChangeIcon(), "交点の変更"));
        add(handBondLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getHandBondIcon(), "手動で部品と結合"));
        add(autoBondLabel = new GeneralItemPanel(true, ImageMaster.getImageMaster().getHandBondIcon(), "自動で部品と結合"));

        wireBondLabel.addMouseListener(this);
        wireCrossLabel.addMouseListener(this);
        handBondLabel.addMouseListener(this);
        autoBondLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /* AUTO_BONDモードにしてからパネルのマウス操作に処理を投げる */
                getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCommand().setCommand(Command.AUTO_BOND);
                getFrame().getBasePanel().getEditCircuitPanel().mouseClicked(e);
            }
        });
    }

    public GeneralItemPanel getWireBondLabel() {
        return wireBondLabel;
    }

    public GeneralItemPanel getWireCrossLabel() {
        return wireCrossLabel;
    }

    public GeneralItemPanel getHandBondLabel() {
        return handBondLabel;
    }

    public GeneralItemPanel getAutoBondLabel() {
        return autoBondLabel;
    }

    @Override
    public void handResize(int w, int h) {
        /* メイン操作パネルの高さと同期させる */
        if (getFrame().getBasePanel() != null) {
            int partsh = getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel().getHeight();

            wireBondLabel.setBounds(0, 0, w, partsh);
            wireCrossLabel.setBounds(0, partsh, w, partsh);
            handBondLabel.setBounds(0, partsh * 2, w, partsh);
            autoBondLabel.setBounds(0, partsh * 3, w, partsh);
        }
    }

    /**
     * changeModeから呼び出される。
     * モード切替が発生したらサブ操作パネルの一番上にある操作がデフォルトになる。
     */
    public void resetCommand() {
        wireBondLabel.setBackground(ColorMaster.getSelectedColor());
        wireCrossLabel.setBackground(ColorMaster.getNotSelectedColor());
        handBondLabel.setBackground(ColorMaster.getNotSelectedColor());
        getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCommand().setCommand(Command.WIRE_BOND);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel panel;
        if (e.getSource() instanceof JPanel) {
            /** 自動結合以外は押されたパネルは色を付ける */
            panel = (JPanel) e.getSource();
            /** パネルの色を初期化する */
            wireBondLabel.setBackground(ColorMaster.getNotSelectedColor());
            wireCrossLabel.setBackground(ColorMaster.getNotSelectedColor());
            handBondLabel.setBackground(ColorMaster.getNotSelectedColor());
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
