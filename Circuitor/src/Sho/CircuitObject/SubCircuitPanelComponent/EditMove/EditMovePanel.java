package Sho.CircuitObject.SubCircuitPanelComponent.EditMove;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJLabel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitOperateCommand.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 基板移動操作を表示するためのパネル。
 */
public class EditMovePanel extends NewJPanel {
    private GeneralItemPanel editMoveLabel;
    private GeneralItemPanel editResetLabel;

    public EditMovePanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getSubBackColor());

        add(editMoveLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getMoveIcon(), "基板の移動・拡大縮小"));
        add(editResetLabel = new GeneralItemPanel(true, ImageMaster.getImageMaster().getPositionResetIcon(), "基板の位置をリセット"));

        editResetLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /* 位置をリセットする処理をパネルのマウス操作に投げる */
                getFrame().getBasePanel().getEditCircuitPanel().mouseClicked(e);
            }
        });

        editMoveLabel.setBackground(ColorMaster.getSelectedColor());
    }

    @Override
    public void handResize(int w, int h) {
        /* メイン操作パネルの高さと同期させる */
        if (getFrame().getBasePanel() != null) {
            int partsh = getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel().getHeight();

            editMoveLabel.setBounds(0, 0, w, partsh);
            editResetLabel.setBounds(0, partsh, w, partsh);
        }
    }
}
