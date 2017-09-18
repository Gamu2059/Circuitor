package KUU.NewComponent;

import KUU.BaseComponent.BaseFrame;

import javax.swing.*;

/**
 * フレーム管理と手動リサイズメソッドを追加したJTabbedPane。
 */
public abstract class NewJTabbedPane extends JTabbedPane {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    /** 継承したクラスでそれぞれ処理を書いてください */
    abstract public void handResize(int width, int height);

    public NewJTabbedPane(BaseFrame frame) {
        super();
        this.frame = frame;
    }

    public BaseFrame getFrame() {
        return frame;
    }
}
