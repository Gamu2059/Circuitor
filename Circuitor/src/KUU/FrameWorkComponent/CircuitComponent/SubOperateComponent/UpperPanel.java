package KUU.FrameWorkComponent.CircuitComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;
import Sho.CircuitObject.SubCircuitPanelComponent.EditMove.EditMovePanel;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd.PartsAddPanel;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsDelete.PartsDeletePanel;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.PartsEditPanel;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsMove.PartsMovePanel;
import Sho.CircuitObject.SubCircuitPanelComponent.WireBond.WireBondPanel;

/**
 * キャンセルラベルの上側のパネル。
 * 内部に、それぞれのモードで表示するためのパネルを保持する。
 */
public class UpperPanel extends NewJPanel {
    private PartsAddPanel partsAddPanel;
    private WireBondPanel wireBondPanel;
    private PartsMovePanel partsMovePanel;
    private PartsDeletePanel partsDeletePanel;
    private EditMovePanel editMovePanel;
    private PartsEditPanel partsEditPanel;

    public UpperPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);
        add(partsAddPanel = new PartsAddPanel(frame));
        add(wireBondPanel = new WireBondPanel(frame));
        add(partsMovePanel = new PartsMovePanel(frame));
        add(partsDeletePanel = new PartsDeletePanel(frame));
        add(editMovePanel = new EditMovePanel(frame));
        add(partsEditPanel = new PartsEditPanel(frame));
    }

    @Override
    public void handResize(int w, int h) {
        partsAddPanel.setBounds(0,0,w,h);
        wireBondPanel.setBounds(0,0,w,h);
        partsMovePanel.setBounds(0,0,w,h);
        partsDeletePanel.setBounds(0,0,w,h);
        editMovePanel.setBounds(0,0,w,h);
        partsEditPanel.setBounds(0,0,w,h);

        partsAddPanel.handResize(w, h);
        wireBondPanel.handResize(w, h);
        partsMovePanel.handResize(w, h);
        partsDeletePanel.handResize(w, h);
        editMovePanel.handResize(w, h);
        partsEditPanel.handResize(w, h);
    }

    public PartsAddPanel getPartsAddPanel() {
        return partsAddPanel;
    }

    public WireBondPanel getWireBondPanel() {
        return wireBondPanel;
    }

    public PartsMovePanel getPartsMovePanel() {
        return partsMovePanel;
    }

    public PartsDeletePanel getPartsDeletePanel() {
        return partsDeletePanel;
    }

    public EditMovePanel getEditMovePanel() {
        return editMovePanel;
    }

    /**
     * サブパネルを一度見えなくする。
     */
    public void invisiblePanel() {
        partsAddPanel.setVisible(false);
        wireBondPanel.setVisible(false);
        partsMovePanel.setVisible(false);
        partsDeletePanel.setVisible(false);
        editMovePanel.setVisible(false);
    }

    /**
     * モードに対応したパネルだけ見えるようにする。
     */
    public void visiblePanel() {
        switch (getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                partsAddPanel.setVisible(true);
                break;
            case WIRE_BOND:
                wireBondPanel.setVisible(true);
                break;
            case PARTS_MOVE:
                partsMovePanel.setVisible(true);
                break;
            case PARTS_DELETE:
                partsDeletePanel.setVisible(true);
                break;
            case EDIT_MOVE:
                editMovePanel.setVisible(true);
                break;
            case PARTS_EDIT:
                partsEditPanel.setVisible(true);
                break;
        }
    }
}
