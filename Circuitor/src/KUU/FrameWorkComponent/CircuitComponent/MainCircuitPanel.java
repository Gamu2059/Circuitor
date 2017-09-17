package KUU.FrameWorkComponent.CircuitComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;
import KUU.GeneralComponent.GeneralItemPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;

import javax.swing.*;
import java.awt.event.*;


/**
 * 回路モードでのメイン操作領域の画面。
 */
public class MainCircuitPanel extends NewJPanel implements MouseListener {
    private GeneralItemPanel partsAddLabel;
    private GeneralItemPanel wireExpansionLabel;
    private GeneralItemPanel partsMoveLabel;
    private GeneralItemPanel partsDeleteLabel;
    private GeneralItemPanel operateEditorLabel;
    private GeneralItemPanel partsEditLabel;
    private GeneralItemPanel executeStartLabel;

    public MainCircuitPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        setBackground(ColorMaster.getBackColor());

        add(partsAddLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getAddIcon(), "部品の追加"));
        add(wireExpansionLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getBondIcon(), "導線の結合"));
        add(partsMoveLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getPartsMoveIcon(), "部品の移動"));
        add(partsDeleteLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getDeleteIcon(), "部品の削除"));
        add(operateEditorLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getMoveIcon(), "基板の移動"));
        add(partsEditLabel = new GeneralItemPanel(null, ImageMaster.getImageMaster().getEditIcon(), "部品の設定"));
        add(executeStartLabel = new GeneralItemPanel(true, ImageMaster.getImageMaster().getExecuteStartIcon(), "実行する"));

        partsAddLabel.addMouseListener(this);
        wireExpansionLabel.addMouseListener(this);
        partsMoveLabel.addMouseListener(this);
        partsDeleteLabel.addMouseListener(this);
        operateEditorLabel.addMouseListener(this);
        partsEditLabel.addMouseListener(this);
        executeStartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getFrame().getBasePanel().runExecuteMode();
            }
        });

        partsAddLabel.setBackground(ColorMaster.getSelectedColor());
    }

    public GeneralItemPanel getPartsAddLabel() {
        return partsAddLabel;
    }

    public GeneralItemPanel getWireExpansionLabel() {
        return wireExpansionLabel;
    }

    public GeneralItemPanel getPartsMoveLabel() {
        return partsMoveLabel;
    }

    public GeneralItemPanel getPartsDeleteLabel() {
        return partsDeleteLabel;
    }

    public GeneralItemPanel getOperateEditorLabel() {
        return operateEditorLabel;
    }

    public GeneralItemPanel getPartsEditLabel() {
        return partsEditLabel;
    }

    @Override
    public void handResize(int width, int height) {
        int partsHeight = height / 8 - 20;

        partsAddLabel.setBounds(0, 0, width, partsHeight);
        wireExpansionLabel.setBounds(0, partsHeight, width, partsHeight);
        partsMoveLabel.setBounds(0, partsHeight * 2, width, partsHeight);
        partsDeleteLabel.setBounds(0, partsHeight * 3, width, partsHeight);
        operateEditorLabel.setBounds(0, partsHeight * 4, width, partsHeight);
        partsEditLabel.setBounds(0, partsHeight * 5, width, partsHeight);
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
        /** パネルの色を初期化する */
        partsAddLabel.setBackground(ColorMaster.getNotSelectedColor());
        wireExpansionLabel.setBackground(ColorMaster.getNotSelectedColor());
        partsMoveLabel.setBackground(ColorMaster.getNotSelectedColor());
        partsDeleteLabel.setBackground(ColorMaster.getNotSelectedColor());
        operateEditorLabel.setBackground(ColorMaster.getNotSelectedColor());
        partsEditLabel.setBackground(ColorMaster.getNotSelectedColor());
        /** サブパネルを不可視にする */
        getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().invisiblePanel();
        JPanel panel;
        if (e.getSource() instanceof JPanel) {
            panel = (JPanel) e.getSource();
            /** 実行開始以外は押されたパネルは色を付ける */
            panel.setBackground(ColorMaster.getSelectedColor());
            /** モードを切り替える */
            getFrame().getBasePanel().getEditCircuitPanel().changeMode(panel);
            /** サブパネルを切り替える */
            getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().visiblePanel();
        }
    }
}
