package KUU.FrameWorkComponent.CircuitComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.CircuitComponent.SubOperateComponent.LowerPanel;
import KUU.FrameWorkComponent.CircuitComponent.SubOperateComponent.UpperPanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.NewComponent.NewJPanel;
import Master.ImageMaster.ImageMaster;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 回路モードでのサブ操作領域の画面。
 */
public class SubCircuitPanel extends NewJPanel {
    private UpperPanel upperPanel;
    private GeneralItemPanel cancelLabel;
    private LowerPanel lowerPanel;

    public SubCircuitPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        add(upperPanel  = new UpperPanel(frame));
        add(cancelLabel = new GeneralItemPanel(false, ImageMaster.getImageMaster().getCancelIcon(),"キャンセル"));
        add(lowerPanel  = new LowerPanel(frame));

        cancelLabel.setIcon(ImageMaster.getImageMaster().getCancelIcon());

        cancelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                getFrame().getBasePanel().getEditCircuitPanel().mouseCansel();
                cancelLabel.processFinished();
            }
        });
    }

    @Override
    public void handResize(int width, int height) {
        int partsHeight = height/12 - 20;

        upperPanel.setBounds(0, 0, width, partsHeight*8 + 20*9);
        cancelLabel.setBounds(0, partsHeight*8 + 20*9, width, partsHeight);
        lowerPanel.setBounds(0, partsHeight*9 + 20*9, width, height - (partsHeight*9 + 20*9));

        upperPanel.handResize(width, partsHeight*8 + 20*9);
        lowerPanel.handResize(width, height - (partsHeight*9 + 20*9));
    }

    public UpperPanel getUpperPanel() {
        return upperPanel;
    }

    public GeneralItemPanel getCancelLabel() {
        return cancelLabel;
    }

    public LowerPanel getLowerPanel() {
        return lowerPanel;
    }
}
