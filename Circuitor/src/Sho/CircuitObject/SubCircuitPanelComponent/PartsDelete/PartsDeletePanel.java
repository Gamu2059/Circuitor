package Sho.CircuitObject.SubCircuitPanelComponent.PartsDelete;

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
 * 部品削除操作を表示するためのパネル。
 */
public class PartsDeletePanel extends NewJPanel implements MouseListener {
    private GeneralItemPanel deleteDetail;
    private GeneralItemPanel deleteCollect;
    private GeneralItemPanel deleteLabel;
    private GeneralItemPanel allDeleteLabel;

    public PartsDeletePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getSubBackColor());

        add(deleteCollect = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsDeleteIcon(), "部品の削除（導線単位）"));
        add(deleteDetail = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsDeleteIcon(), "部品の削除（マス単位）"));
        add(deleteLabel = new GeneralItemPanel(false, ImageMaster.getImageMaster().getDeleteIcon(), "削除"));
        add(allDeleteLabel = new GeneralItemPanel(true, ImageMaster.getImageMaster().getDeleteIcon(), "全削除"));

        deleteDetail.addMouseListener(this);
        deleteCollect.addMouseListener(this);
        deleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /* 削除をパネルのマウス操作の処理に投げる */
                getFrame().getBasePanel().getEditCircuitPanel().mouseClicked(e);
                deleteLabel.processFinished();
            }
        });
        allDeleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /* 全削除をパネルのマウス操作の処理に投げる */
                getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCommand().setCommand(Command.DELETE_ALL);
                getFrame().getBasePanel().getEditCircuitPanel().mouseClicked(e);
            }
        });
    }

    public GeneralItemPanel getDeleteDetail() {
        return deleteDetail;
    }

    public GeneralItemPanel getDeleteCollect() {
        return deleteCollect;
    }

    public GeneralItemPanel getDeleteLabel() {
        return deleteLabel;
    }

    public GeneralItemPanel getAllDeleteLabel() {
        return allDeleteLabel;
    }

    @Override
    public void handResize(int w, int h) {
        /* メイン操作パネルの高さと同期させる */
        if (getFrame().getBasePanel() != null) {
            int partsh = getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel().getHeight();

            deleteCollect.setBounds(0, 0, w, partsh);
            deleteDetail.setBounds(0, partsh, w, partsh);
            deleteLabel.setBounds(0, h - partsh * 2, w, partsh);
            allDeleteLabel.setBounds(0, h - partsh, w, partsh);
        }
    }

    /**
     * changeModeから呼び出される。
     * モード切替が発生したらサブ操作パネルの一番上にある操作がデフォルトになる。
     */
    public void resetCommand() {
        deleteCollect.setBackground(ColorMaster.getSelectedColor());
        deleteDetail.setBackground(ColorMaster.getNotSelectedColor());
        getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCommand().setCommand(Command.DELETE_COLLECT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel panel;
        if (e.getSource() instanceof JPanel) {
            panel = (JPanel) e.getSource();
            deleteDetail.setBackground(ColorMaster.getNotSelectedColor());
            deleteCollect.setBackground(ColorMaster.getNotSelectedColor());
            /** パネルの色を初期化する */
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
