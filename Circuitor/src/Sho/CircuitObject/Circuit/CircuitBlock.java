package Sho.CircuitObject.Circuit;

import Sho.CircuitObject.ElecomBehavior.ElecomBehavior_;
import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;

/**
 * 電子部品の最小構成単位が保持すべき情報をまとめたクラス。
 *
 * @author 翔
 * @version 1.2
 */
public class CircuitBlock implements CopyTo {
    private ElecomBehavior_ elecomBehavior;
    private ElecomInfo elecomInfo;
    private CircuitInfo circuitInfo;
    private boolean exist;
    private CircuitBorder.Borders border;

    /**
     * 絶対座標を指定して生成する。
     *
     * @param abcoY Y軸の絶対座標
     * @param abcoX X軸の絶対座標
     * @since 1.2
     */
    public CircuitBlock(int abcoY, int abcoX) {
        elecomInfo = new ElecomInfo();
        circuitInfo = new CircuitInfo(abcoY, abcoX);
        exist = false;
        border = null;
    }

    /**
     * この電子部品に設定されている振る舞いを取得する。
     *
     * @see ElecomBehavior_
     * @since 1.2
     */
    public ElecomBehavior_ getElecomBehavior() {
        return elecomBehavior;
    }

    /**
     * この電子部品に振る舞いを設定する。
     *
     * @param elecomBehavior 設定する振る舞いのインスタンス
     * @see ElecomBehavior_
     * @since 1.2
     */
    public void setElecomBehavior(ElecomBehavior_ elecomBehavior) {
        this.elecomBehavior = elecomBehavior;
    }

    /**
     * この構成単位が保持する回路情報を取得する。
     *
     * @see CircuitInfo
     * @since 1.2
     */
    public CircuitInfo getCircuitInfo() {
        return circuitInfo;
    }

    /**
     * この構成単位が保持する電子部品情報を取得する。
     *
     * @see ElecomInfo
     * @since 1.2
     */
    public ElecomInfo getElecomInfo() {
        return elecomInfo;
    }

    /**
     * この構成単位が保持する電子部品を設定する。
     *
     * @param elecomInfo 設定する電子部品のインスタンス
     * @see ElecomInfo
     * @since 1.2
     */
    public void setElecomInfo(ElecomInfo elecomInfo) {
        this.elecomInfo = elecomInfo;
    }

    /**
     * この構成単位が部品として存在しているかどうかを取得する。
     *
     * @return 存在していればtrue、そうでなければfalse
     * @since 1.2
     */
    public boolean isExist() {
        return exist;
    }

    /**
     * この構成単位が部品として存在しているかどうかを設定する。
     *
     * @param exist 存在していればtrue、そうでなければfalse
     * @since 1.2
     */
    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public CircuitBorder.Borders getBorder() {
        return border;
    }

    public void setBorder(CircuitBorder.Borders border) {
        this.border = border;
    }

    /**
     * 自身を既存のインスタンスに複製する。
     *
     * @see CopyTo
     * @since 1.2
     */
    @Override
    public void copyTo(Object o) {
        if (o instanceof CircuitBlock) {
            CircuitBlock circuitBlock = (CircuitBlock)o;
            circuitBlock.elecomBehavior = this.elecomBehavior;
            this.getElecomInfo().copyTo(circuitBlock.getElecomInfo());
            this.getCircuitInfo().copyTo(circuitBlock.getCircuitInfo());
            circuitBlock.exist = this.exist;
        }
        else {
            new CannotCopyToException("CircuitBlock");
        }
    }

    @Override
    public String toString() {
        return "CircuitBlock :\n" +
                "exist : " + exist + "\n" +
                circuitInfo.toString() + "\n" +
                elecomInfo.toString() + "\n"
                ;
    }
}
