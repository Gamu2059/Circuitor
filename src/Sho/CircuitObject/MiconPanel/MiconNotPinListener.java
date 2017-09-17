package Sho.CircuitObject.MiconPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.BaseComponent.BasePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 入出力用端子以外のピンのマウスリウナ。
 */
public class MiconNotPinListener extends MouseAdapter {
    private BaseFrame frame;

    public MiconNotPinListener(BaseFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (frame.getBasePanel().getOverAllMode() != BasePanel.OverAllMode.EXECUTE) {
            frame.getHelpLabel().setText("このピンは設定を変更出来ません。");
        } else {
            frame.getHelpLabel().setText("現在、この画面は編集出来ません。");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        frame.getHelpLabel().setText("");
    }
}
