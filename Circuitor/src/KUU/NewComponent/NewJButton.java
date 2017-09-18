package KUU.NewComponent;

import KUU.BaseComponent.BaseFrame;

import javax.swing.*;

/**
 * フレーム管理と手動リサイズメソッドを追加したJButton。
 */
public abstract class NewJButton extends JButton {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    /** 継承したクラスでそれぞれ処理を書いてください */
    abstract public void handResize(int width, int height);

    public NewJButton(BaseFrame frame) {
        super();
        this.frame = frame;
    }

    public BaseFrame getFrame() {
        return frame;
    }
}
