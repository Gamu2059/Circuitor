package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 部品編集操作を表示するためのパネル。
 */
public class PartsEditPanel extends NewJPanel {
    private GeneralItemPanel partsEdit;
    private GeneralItemPanel partsEditInit;

    public PartsEditPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getSubBackColor());

        add(partsEdit     = new GeneralItemPanel(null, ImageMaster.getImageMaster().getEditIcon(), "部品の設定"));
        add(partsEditInit = new GeneralItemPanel(true, ImageMaster.getImageMaster().getCancelIcon(), "設定の初期化"));

        partsEditInit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /* 初期化をパネルのマウス操作の処理に投げる */
                getFrame().getBasePanel().getEditCircuitPanel().mouseClicked(e);
            }
        });

        partsEdit.setBackground(ColorMaster.getSelectedColor());
    }

    @Override
    public void handResize(int w, int h) {
        /* メイン操作パネルの高さと同期させる */
        if (getFrame().getBasePanel() != null) {
            int partsh = getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel().getHeight();

            partsEdit.setBounds(0, 0, w, partsh);
            partsEditInit.setBounds(0, partsh, w, partsh);
        }
    }
}
