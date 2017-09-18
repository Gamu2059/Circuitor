package Sho.IntegerDimension;

import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;

/**
 * 整数型でheightとwidthを返すDimensionクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class IntegerDimension implements Cloneable,CopyTo {
    /** 高さ */
    private int height;
    /** 幅 */
    private int width;

    /**
     * 初期値ゼロでインスタンスを生成する。
     *
     * @since 1.1
     */
    public IntegerDimension() {
        this.height = 0;
        this.width = 0;
    }

    /**
     * 初期値を指定してインスタンスを生成する。
     *
     * @param height 高さ
     * @param width 幅
     * @since 1.1
     */
    public IntegerDimension(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * 初期値をIntegerDimensionで指定してインスタンスを生成する。
     *
     * @param integerDimension IntegerDimensionのインスタンス
     * @exception NullPointerException 引数インスタンスがnullの場合
     * @since 1.1
     */
    public IntegerDimension(IntegerDimension integerDimension) throws NullPointerException{
        this.height = integerDimension.getHeight();
        this.width = integerDimension.getWidth();
    }

    /**
     *  高さを取得する。
     *
     *  @return 高さ
     *  @since 1.1
     */
    public int getHeight() {
        return height;
    }

    /**
     * 高さを設定する。
     *
     * @param height 高さ
     * @since 1.1
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *  幅を取得する。
     *
     *  @return 幅
     *  @since 1.1
     */
    public int getWidth() {
        return width;
    }

    /**
     * 幅を設定する。
     *
     * @param width 幅
     * @since 1.1
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 等価判定を行う。
     *
     * @param obj 等価判定を行いたいインスタンス
     * @return 等価であった場合はtrue、そうでない場合はfalseを返す
     * @since 1.1
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof IntegerDimension)) return false;
        IntegerDimension id = (IntegerDimension)obj;
        if (this.height != id.getHeight() || this.width != id.getWidth()) return false;
        return true;
    }

    /**
     * 等価判定を行う。
     *
     * @param height 縦
     * @param width 横
     * @return 等価であった場合はtrue、そうでない場合はfalseを返す
     * @since 1.2
     */
    public boolean equals(int height, int width) {
        if (this.height == height && this.width == width) {
            return true;
        }
        return false;
    }

    /**
     * 等値判定を行う。
     *
     * @param h1 一つ目のY座標
     * @param w1 一つ目のX座標
     * @param h2 二つ目のY座標
     * @param w2 二つ目のX座標
     * @return 等値であった場合はtrue、そうでない場合はfalseを返す
     * @since 1.3
     */
    public static boolean equals(int h1, int w1, int h2, int w2) {
        return h1 == h2 && w1 == w2;
    }

    /**
     * 自身を複製する。
     *
     * @return 自身のフィールドを複製したインスタンス
     * @since 1.1
     */
    @Override
    public IntegerDimension clone() {
        return new IntegerDimension(this);
    }

    /**
     * 自身を既存のインスタンスに複製する。
     *
     * @see CopyTo
     * @see CannotCopyToException
     * @since 1.1
     */
    @Override
    public void copyTo(Object o) {
        if (o instanceof IntegerDimension) {
            IntegerDimension integerDimension = (IntegerDimension)o;
            integerDimension.height = this.height;
            integerDimension.width = this.width;
        }
        else {
            new CannotCopyToException("IntegerDimension");
        }
    }

    @Override
    public String toString() {
        return "height = " + height + " width = " + width;
    }
}
