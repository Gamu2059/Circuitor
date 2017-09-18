package KUU.NewComponent;

import KUU.BaseComponent.BaseFrame;

import javax.swing.*;

/**
 * フレーム管理と手動リサイズメソッドを追加したJLabel。
 */
public abstract class NewJLabel extends JLabel {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    /** 継承したクラスでそれぞれ処理を書いてください */
    abstract public void handResize(int width, int height);

    public NewJLabel(BaseFrame frame) {
        super();
        this.frame = frame;
    }

    public BaseFrame getFrame() {
        return frame;
    }
}