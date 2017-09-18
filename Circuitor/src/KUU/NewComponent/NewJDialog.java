package KUU.NewComponent;

import KUU.BaseComponent.BaseFrame;

import javax.swing.*;

/**
 * フレーム管理と手動リサイズメソッドを追加したJDialog。
 */
public abstract class NewJDialog extends JDialog {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;

    public NewJDialog(BaseFrame frame) {
        super(frame, true);
        this.frame = frame;
        setResizable(false);
    }

    public BaseFrame getFrame() {
        return frame;
    }
}
