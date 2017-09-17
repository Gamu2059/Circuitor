package Sho.CircuitObject.Circuit;

import Sho.IntegerDimension.IntegerDimension;

import java.util.Arrays;

/**
 * 電子部品の追加時に必要となる、CircuitInfoクラスとの接続対を保持するクラス。
 *
 * @author 翔
 * @version 1.2
 */
public class CircuitLinkInfo implements Cloneable {
    private IntegerDimension reco;
    private boolean link[] = new boolean[4];
    private int terminalCorrespond[] = {-1, -1, -1, -1};
    private TerminalDirection terminalDirection[] = new TerminalDirection[4];

    /**
     * 何も指定せずに生成する。
     *
     * @since 1.2
     */
    public CircuitLinkInfo() {}

    /**
     * 相対座標、リンク配列、端子配列を指定して生成する。
     *
     * @param recoY Y軸の相対座標
     * @param recoX X軸の相対座標
     * @param link 接続判定の配列
     * @param terminalDirection 接続性の配列
     * @see TerminalDirection
     * @since 1.2
     */
    public CircuitLinkInfo(int recoY, int recoX, boolean[] link, TerminalDirection[] terminalDirection) {
        this.reco = new IntegerDimension(recoY, recoX);
        this.link = link;
        this.terminalDirection = terminalDirection;
    }

    /**
     * 相対座標、リンク配列、対応配列、端子配列を指定して生成する。
     *
     * @param recoY Y軸の相対座標
     * @param recoX X軸の相対座標
     * @param link 接続判定の配列
     * @param terminalCorrespond 接続対応の配列
     * @param terminalDirection 接続性の配列
     * @see TerminalDirection
     * @since 1.3
     */
    public CircuitLinkInfo(int recoY, int recoX, boolean[] link, int[] terminalCorrespond, TerminalDirection[] terminalDirection) {
        this.reco = new IntegerDimension(recoY, recoX);
        this.link = link;
        this.terminalCorrespond = terminalCorrespond;
        this.terminalDirection = terminalDirection;
    }

    public IntegerDimension getReco() {
        return reco;
    }

    public void setReco(IntegerDimension reco) {
        this.reco = reco;
    }

    public boolean[] getLink() {
        return link;
    }

    public void setLink(boolean[] link) {
        this.link = link;
    }

    public int[] getTerminalCorrespond() {
        return terminalCorrespond;
    }

    public void setTerminalCorrespond(int[] terminalCorrespond) {
        this.terminalCorrespond = terminalCorrespond;
    }

    public TerminalDirection[] getTerminalDirection() {
        return terminalDirection;
    }

    public void setTerminalDirection(TerminalDirection[] terminalDirection) {
        this.terminalDirection = terminalDirection;
    }

    @Override
    public CircuitLinkInfo clone() {
        CircuitLinkInfo circuitLinkInfo = new CircuitLinkInfo();
        circuitLinkInfo.reco = new IntegerDimension(this.reco);
        for (int i=0;i<circuitLinkInfo.link.length;i++) {
            circuitLinkInfo.link[i] = this.link[i];
        }
        for (int i=0;i<circuitLinkInfo.terminalCorrespond.length;i++) {
            circuitLinkInfo.terminalCorrespond[i] = this.terminalCorrespond[i];
        }
        for (int i=0;i<circuitLinkInfo.terminalDirection.length;i++) {
            circuitLinkInfo.terminalDirection[i] = this.terminalDirection[i];
        }
        return circuitLinkInfo;
    }

    @Override
    public String toString() {
        return "reco = " + reco.toString() +
                "link = " + Arrays.toString(link) +
                "terminalCorrespond = " + Arrays.toString(terminalCorrespond) +
                " terminalDirection = " + Arrays.toString(terminalDirection);
    }
}
