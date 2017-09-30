package Sho.CircuitObject.Operate;

import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.Graphed.BranchNodeConnect;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;


import static Sho.CircuitObject.Circuit.CircuitBorder.Borders.*;

/**
 * UnitPanelの検知に特化した処理系クラス。
 */
public class OperateDetection_ {
    /**
     * 大元のJFrameのアドレスを保持する
     */
    private BaseFrame frame;

    public OperateDetection_(BaseFrame frame) {
        this.frame = frame;
    }

    /**
     * パネルの回路ユニットの座標範囲内かどうかを判定する。
     * 範囲内ならtrue、範囲外ならfalseを返す。
     * 全ウィンドウ共通なのでstaticメソッド。
     */
    public static boolean judgeOutOfBound(UnitPanel panel, IntegerDimension abco) {
        return judgeOutOfBound(panel, abco.getHeight(), abco.getWidth());
    }

    /**
     * パネルの回路ユニットの座標範囲内かどうかを判定する。
     * 範囲内ならtrue、範囲外ならfalseを返す。
     * 全ウィンドウ共通なのでstaticメソッド。
     */
    public static boolean judgeOutOfBound(UnitPanel panel, int y, int x) {
        return y >= 0 && y < panel.getCircuitSize().getHeight() && x >= 0 && x < panel.getCircuitSize().getWidth();
    }

    /*****************/
    /** NO DETECTION */
    /*****************/

    public void noDetection(UnitPanel panel) {
        panel.getOperateBorder().removeAllBorder(panel);
        panel.repaint();
    }

    /******************************/
    /** PARTS_ADD MODE DETECTIONS */
    /******************************/
    /**
     * PARTS_ADDモードのPARTS_ADDコマンドのNO_ACTION場面での検知パターン。
     * パネルに一時情報が無い場合は何も検知しない。
     * 一時情報がある場合は、とりあえず何も配置されていないマスを全て検知する。
     * １：部品情報が存在しない領域
     * ２：（部品追加状態の場合のみ）マウスカーソルを中心とした部品サイズ領域
     */
    public void partsAdd_partsAdd_noAction(UnitPanel panel) {
        if (panel.getTmp() != null) {
            CircuitUnit u = panel.getCircuitUnit();
            /** 配置可能領域のボーダ */
            for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
                for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                    /* マスに部品情報が存在しない場合に検知 */
                    if (!u.getCircuitBlock().getMatrix().get(i).get(j).isExist()) {
                        panel.getOperateBorder().setCoordinateBorder(panel, i, j, SELECTABLE);
                    } else {
                        panel.getOperateBorder().setCoordinateBorder(panel, i, j, null);
                    }
                }
            }
            /** マウスカーソルを基準点とした予測領域のボーダ */
            if (panel.getCursorCo() != null) {
                IntegerDimension size = panel.getTmp().getElecomInfo().getSize();
                IntegerDimension start = new IntegerDimension(panel.getCursorCo().getHeight() - size.getHeight() / 2, panel.getCursorCo().getWidth() - size.getWidth() / 2);
                IntegerDimension end = new IntegerDimension(panel.getCursorCo().getHeight() + size.getHeight() / 2, panel.getCursorCo().getWidth() + size.getWidth() / 2);
                /* エラーならERRORボーダに変える */
                if (panel.getOperateOperate().isError(panel, start, end)) {
                    panel.getOperateBorder().setRangeBorder(panel, start, end, ERROR);
                } else {
                    panel.getOperateBorder().setRangeBorder(panel, start, end, PREDICTION);
                }
            }
        } else {
            panel.getOperateBorder().removeAllBorder(panel);
        }
        panel.repaint();
    }

    /******************************/
    /** WIRE_BOND MODE DETECTIONS */
    /******************************/
    /**
     * WIRE_BONDモードのWIRE_BONDコマンドのNO_ACTION場面での検知パターン。
     * 導線結合を行える始点を検知する。
     * 後の検知で検知パターンを用いるため、この段階で一時情報として保持する。
     * １：結合の始点
     * ２：（結合の始点上にマウスがある場合のみ）マウスカーソル上の領域
     */
    public void wireBond_wireBond_noAction(UnitPanel panel) {
        int abcoY, abcoX, nextAbcoY, nextAbcoX;
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c, c1, c2;
        ElecomInfo e, e2;
        boolean flg;
        panel.resetIdTmps();
        /** 導線結合の始点のボーダ */
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                c = u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo();
                e = u.getCircuitBlock().getMatrix().get(i).get(j).getElecomInfo();
                if (u.getCircuitBlock().getMatrix().get(i).get(j).isExist()) {
                    /* 部品の時は、端子がある相対座標をわりだして端子が接続性を持っていない、かつその方向の隣接ブロックが空か導線で端子がない場合に検知する */
                    if (c.getReco().equals(0, 0) && e.getPartsVarieties() != PartsVarieties.WIRE) {
                        for (int k = 0; k < e.getSize().getHeight(); k++) {
                            for (int l = 0; l < e.getSize().getWidth(); l++) {
                                /* 部品の辺だけ探索すればよい */
                                if (k == 0 || k == e.getSize().getHeight() - 1 || l == 0 || l == e.getSize().getWidth() - 1) {
                                    abcoY = c.getAbco().getHeight() + k;
                                    abcoX = c.getAbco().getWidth() + l;
                                    c1 = u.getCircuitBlock().getMatrix().get(abcoY).get(abcoX).getCircuitInfo();
                                    for (int m = 0; m < c1.getTerminal().length; m++) {
                                        /* 端子はあるが接続性はない部分を検出 */
                                        if (c1.getTerminal()[m] && !c1.getConnection()[m]) {
                                            panel.getOperateBorder().setCoordinateBorder(panel, c1.getAbco(), SELECTABLE);
                                            panel.addTmps(c1.getAbco());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /* 導線の時は、４端子のうち少なくとも１つは接続性のないもので、かつその接続性がない方向の隣接ブロックが導線側に接続性をもたない場合に検知する */
                    else if (c.getReco().equals(0, 0) && e.getPartsVarieties() == PartsVarieties.WIRE) {
                        flg = false;
                        for (int k = 0; k < c.getConnection().length; k++) {
                            if (!c.getConnection()[k]) {
                                nextAbcoY = BranchNodeConnect.nextCoordinateY(c.getAbco().getHeight(), k);
                                nextAbcoX = BranchNodeConnect.nextCoordinateX(c.getAbco().getWidth(), k);
                                /* 回路の範囲外でないことを確認する */
                                if (OperateDetection_.judgeOutOfBound(panel, nextAbcoY, nextAbcoX)) {
                                    c2 = u.getCircuitBlock().getMatrix().get(nextAbcoY).get(nextAbcoX).getCircuitInfo();
                                    e2 = u.getCircuitBlock().getMatrix().get(nextAbcoY).get(nextAbcoX).getElecomInfo();
                                    /* 隣接ブロックが空ではない時、部品なら端子があって接続性がない場合、導線なら接続性がない場合に検知する */
                                    if (u.getCircuitBlock().getMatrix().get(nextAbcoY).get(nextAbcoX).isExist()) {
                                        if (e2.getPartsVarieties() != PartsVarieties.WIRE) {
                                            if (c2.getTerminal()[(k + 2) % 4] && !c2.getConnection()[(k + 2) % 4]) {
                                                flg = true;
                                            }
                                        } else {
                                            if (!c2.getConnection()[(k + 2) % 4]) {
                                                flg = true;
                                            }
                                        }
                                    }
                                    /* 隣接ブロックが空の場合は問答無用で検知する */
                                    else {
                                        flg = true;
                                    }
                                }
                            }
                            if (flg) {
                                break;
                            }
                        }
                        if (flg) {
                            panel.getOperateBorder().setCoordinateBorder(panel, c.getAbco(), SELECTABLE);
                            panel.addTmps(c.getAbco());
                        }
                    }
                }
            }
        }
        /** マウスカーソル上のボーダ */
        if (panel.getCursorCo() != null) {
            /* 選択可能ボーダが存在する場所の上 */
            if (u.getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() == SELECTABLE) {
                panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }

    /**
     * WIRE_BONDモードのWIRE_BONDコマンドのRANGING場面での検知パターン。
     * 一時情報を始点にしたマウスまでの２方角最短経路上の領域を検知する。
     * さらに、結合の終点も検知する。
     * １：始点からの拡張可能な全領域
     * ２：始点からの結合可能な全領域
     * ３：始点からマウスカーソルまでの最短経路領域
     * ４：始点
     */
    public void wireBond_wireBond_ranging(UnitPanel panel) {
        /** 部品や導線、空間をすべて含んだ、始点から拡張可能な領域にボーダを描画する */
        for (int i = 0; i < 4; i++) {
            detectExpansionPoint(panel, panel.getTmp(), i, i);
        }
        /** 始点から結合可能な部品の端子と導線にボーダを描画する */
        detectBondPoint(panel);
        /** 始点からマウスカーソルまでの最短経路上の領域にボーダを描画する */
        /* 一時情報をリセットし、最短経路座標を登録するために使い回す */
        panel.resetIdTmps();
        if (panel.getCursorCo() != null) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() != null) {
                boolean stop[][] = new boolean[panel.getCircuitSize().getHeight()][panel.getCircuitSize().getWidth()];
                int direction;
                int dy, dx;
                dy = Math.abs(panel.getTmp().getCircuitInfo().getAbco().getHeight() - panel.getCursorCo().getHeight());
                dx = Math.abs(panel.getTmp().getCircuitInfo().getAbco().getWidth() - panel.getCursorCo().getWidth());
                if (dy >= dx) {
                    direction = getNearY(panel.getTmp().getCircuitInfo().getAbco().getHeight(), panel.getCursorCo().getHeight());
                } else {
                    direction = getNearX(panel.getTmp().getCircuitInfo().getAbco().getWidth(), panel.getCursorCo().getWidth());
                }
                if (detectShortestRoot(panel, panel.getTmp().getCircuitInfo().getAbco(), panel.getCursorCo(), direction, stop)) {
                    for (IntegerDimension id : panel.getIdTmps()) {
                        panel.getOperateBorder().setCoordinateBorder(panel, id, PREDICTION);
                    }
                }
            }
        }
        /** 始点に選択ボーダを描画する */
        panel.getOperateBorder().setCoordinateBorder(panel, panel.getTmp().getCircuitInfo().getAbco(), SELECTED);
        panel.repaint();
    }

    /**
     * 部品や導線を含め、始点から導線を拡張できる領域にボーダを描画する。
     * 始点からは再帰的に探索を行うが、二方角にしか曲がれないという制限がある。
     */
    private void detectExpansionPoint(UnitPanel panel, CircuitBlock base, int direction, int subDirection) {
        /* スタックオーバフローしても無視する */
        try {
            CircuitUnit u = panel.getCircuitUnit();
            CircuitBlock nextB;
            int nextAbcoY, nextAbcoX;
            boolean flg;
            for (int i = 0; i < base.getCircuitInfo().getConnection().length; i++) {
                flg = false;
                if (base == panel.getTmp()) {
                    /** 始点座標の場合 */
                    /* 始点座標は問答無用でボーダ描画 */
                    if (panel.getTmp().getBorder() == null) {
                        panel.getOperateBorder().setCoordinateBorder(panel, panel.getTmp().getCircuitInfo().getAbco(), SELECTABLE);
                    }
                    if (base.getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
                        /* 部品の一部だった場合、端子が有りかつ接続性が無い方向にしか探索できない */
                        if (base.getCircuitInfo().getTerminal()[i] && !base.getCircuitInfo().getConnection()[i]) {
                            /* さらに、指定方向にしか探索させない */
                            if (i == direction) {
                                flg = true;
                            }
                        }
                    } else {
                        /* 導線だった場合、接続性がない方向ならば探索させる */
                        if (!base.getCircuitInfo().getConnection()[i]) {
                            /* 指定方向のみ */
                            if (i == direction) {
                                flg = true;
                            }
                        }
                    }
                } else {
                    /** 始点座標でない場合 */
                    if (!base.getCircuitInfo().getTerminal()[i] && !base.getCircuitInfo().getConnection()[i]) {
                        /* 指定方向の逆向きは探索させない */
                        if (i != (direction + 2) % 4) {
                            /* 始点座標と垂直平行の関係にある座標では、２番目の方向制限を設けられる */
                            if (base.getCircuitInfo().getAbco().getHeight() == panel.getTmp().getCircuitInfo().getAbco().getHeight() || base.getCircuitInfo().getAbco().getWidth() == panel.getTmp().getCircuitInfo().getAbco().getWidth()) {
                                flg = true;
                                if (i != direction) {
                                    subDirection = i;
                                }
                            } else if (i != (subDirection + 2) % 4) {
                                flg = true;
                            }
                        }
                    }
                }
                /* 条件を満たしたら探索する */
                if (flg) {
                    nextAbcoY = BranchNodeConnect.nextCoordinateY(base.getCircuitInfo().getAbco().getHeight(), i);
                    nextAbcoX = BranchNodeConnect.nextCoordinateX(base.getCircuitInfo().getAbco().getWidth(), i);
                    /* 回路の範囲外でないことを確認 */
                    if (judgeOutOfBound(panel, nextAbcoY, nextAbcoX)) {
                        nextB = u.getCircuitBlock().getMatrix().get(nextAbcoY).get(nextAbcoX);
                        /* ボーダが何も設定されていない、かつ基準のブロックでなければ探索する */
                        if (nextB.getBorder() == null && nextB != base) {
                            if (nextB.isExist()) {
                                /* 隣接ブロックが空でない場合は、導線かつこちら向きに端子がない時だけ再起処理 */
                                if (nextB.getElecomInfo().getPartsVarieties() == PartsVarieties.WIRE) {
                                    /* 隣接ブロックが空でない場合は、導線かつこちら向きに端子がない時だけ再起処理 */
                                    if (!nextB.getCircuitInfo().getTerminal()[(i + 2) % 4]) {
                                        panel.getOperateBorder().setCoordinateBorder(panel, nextB.getCircuitInfo().getAbco(), SELECTABLE);
                                        detectExpansionPoint(panel, nextB, direction, subDirection);
                                    }
                                } else {
                                    /* 部品の場合は、こちら向きに端子があってかつ接続性がない時だけボーダ描画する(再帰処理はしない) */
                                    if (nextB.getCircuitInfo().getTerminal()[(i + 2) % 4] && !nextB.getCircuitInfo().getConnection()[(i + 2) % 4]) {
                                        panel.getOperateBorder().setCoordinateBorder(panel, nextB.getCircuitInfo().getAbco(), SELECTABLE);
                                    }
                                }
                            }
                            /* 隣接ブロックが空の場合は問答無用で再起処理 */
                            else {
                                panel.getOperateBorder().setCoordinateBorder(panel, nextB.getCircuitInfo().getAbco(), SELECTABLE);
                                detectExpansionPoint(panel, nextB, direction, subDirection);
                            }
                        }
                    }
                }
            }
        } catch (StackOverflowError e) {
        }
    }

    /**
     * 結合可能な部品の端子や導線にボーダを描画する。
     * 検出には、保持している一時情報を用いる。
     */
    private void detectBondPoint(UnitPanel panel) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitBlock b1;
        CircuitInfo c, c1;
        ElecomInfo e, e1;
        IntegerDimension tmp = panel.getTmp().getCircuitInfo().getAbco();
        IntegerDimension next = new IntegerDimension();
        for (CircuitBlock b : panel.getTmps()) {
            c = b.getCircuitInfo();
            e = b.getElecomInfo();
            for (int i = 0; i < c.getTerminal().length; i++) {
                /* 接続性がない導線または、接続性がないが端子はある部品ならば探索開始 */
                if (!c.getConnection()[i] && (e.getPartsVarieties() == PartsVarieties.WIRE || e.getPartsVarieties() != PartsVarieties.WIRE && c.getTerminal()[i])) {
                    next.setHeight(BranchNodeConnect.nextCoordinateY(c.getAbco().getHeight(), i));
                    next.setWidth(BranchNodeConnect.nextCoordinateX(c.getAbco().getWidth(), i));
                    if (judgeOutOfBound(panel, next)) {
                        b1 = u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth());
                        c1 = b1.getCircuitInfo();
                        e1 = b1.getElecomInfo();
                        if (b1.getBorder() != null) {
                            if (b1.isExist()) {
                                if (e1.getPartsVarieties() == PartsVarieties.WIRE || e1.getPartsVarieties() != PartsVarieties.WIRE && c1.getTerminal()[(i + 2) % 4]) {
                                    /* 始点に近い二方角ならば検知する */
                                    if (i == getNearY(c.getAbco().getHeight(), tmp.getHeight()) || i == getNearX(c.getAbco().getWidth(), tmp.getWidth())) {
                                        panel.getOperateBorder().setCoordinateBorder(panel, c.getAbco(), BONDABLE);
                                    }
                                }
                            } else {
                                /* 始点に近い二方角ならば検知する */
                                if (i == getNearY(c.getAbco().getHeight(), tmp.getHeight()) || i == getNearX(c.getAbco().getWidth(), tmp.getWidth())) {
                                    panel.getOperateBorder().setCoordinateBorder(panel, c.getAbco(), BONDABLE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 始点と終点を結ぶ最短距離(嘘)のルートを導出する。
     * endがnullの時や、その地点にボーダが描画されていない時は何もしない。
     * 終点に到達した時、はじめてtrueが返される。
     */
    private boolean detectShortestRoot(UnitPanel panel, IntegerDimension base, IntegerDimension end, int direction, boolean[][] stop) {
        /* 終点に到達したら、リストに登録してtrueを返す */
        if (base.equals(end)) {
            if (judgeParts(panel, base.getHeight(), base.getWidth(), end, direction)) {
                panel.getIdTmps().add(base);
                return true;
            } else {
                return false;
            }
        }
        CircuitUnit u = panel.getCircuitUnit();
        boolean near[] = new boolean[4];
        int ny, nx;
        IntegerDimension next = new IntegerDimension();
        ny = getNearY(base.getHeight(), end.getHeight());
        nx = getNearX(base.getWidth(), end.getWidth());

        /* 優先度１：終点に近い方向かつ来た方向と同じ */
        if (direction == ny || direction == nx) {
            if (direction == ny) {
                ny = -1;
            } else {
                nx = -1;
            }
            next.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), direction));
            next.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), direction));
            /* ボーダが描画されていなければ探索しない */
            if (u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth()).getBorder() != null) {
                if (panel.getTmp().getCircuitInfo().getAbco().equals(base) || !u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo().getTerminal()[direction]) {
                    if (judgeOutOfBound(panel, next.getHeight(), next.getWidth())) {
                        if (judgeParts(panel, next.getHeight(), next.getWidth(), end, direction)) {
                            if (!stop[next.getHeight()][next.getWidth()]) {
                                if (detectShortestRoot(panel, next, end, direction, stop)) {
                                    panel.getIdTmps().add(base);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        /* 優先度２：優先度１でない方向 */
        for (int i = 0; i < 4; i++) {
            if (i == ny || i == nx) {
                direction = i;
                next.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), direction));
                next.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), direction));
                /* ボーダが描画されていなければ探索しない */
                if (u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth()).getBorder() != null) {
                    if (panel.getTmp().getCircuitInfo().getAbco().equals(base) || !u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo().getTerminal()[direction]) {
                        if (judgeOutOfBound(panel, next.getHeight(), next.getWidth())) {
                            if (judgeParts(panel, next.getHeight(), next.getWidth(), end, direction)) {
                                if (!stop[next.getHeight()][next.getWidth()]) {
                                    if (detectShortestRoot(panel, next, end, direction, stop)) {
                                        panel.getIdTmps().add(base);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
        /* 探索候補から外れたらストップ行列に追加する */
        stop[base.getHeight()][base.getWidth()] = true;
        return false;
    }

    /**
     * 現在の座標から終点の座標まで、その経路を考慮せずに近い方向のY方向を返す。
     * 等しければ、-1を返す。
     * 全ウィンドウ共通なのでstaticメソッド。
     */
    public static int getNearY(int n, int e) {
        if (n - e > 0) {
            return 0;
        } else if (n - e < 0) {
            return 2;
        }
        return -1;
    }

    /**
     * 現在の座標から終点の座標まで、その経路を考慮せずに近い方向のX方向を返す。
     * 等しければ、-1を返す。
     * 全ウィンドウ共通なのでstaticメソッド。
     */
    public static int getNearX(int n, int e) {
        if (n - e > 0) {
            return 3;
        } else if (n - e < 0) {
            return 1;
        }
        return -1;
    }

    /**
     * 第二制限。
     * 空の場合は問答無用にクリア。
     * 部品の場合、終点かつ進んできた向きの逆に端子があればクリア。
     * 導線の場合、始点または終点または進んできた向きと逆に端子がなければクリア。
     */
    private boolean judgeParts(UnitPanel panel, int y, int x, IntegerDimension end, int direction) {
        if (!panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x).isExist()) {
            return true;
        } else {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x).getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
                if (IntegerDimension.equals(y, x, end.getHeight(), end.getWidth()) &&
                        panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x).getCircuitInfo().getTerminal()[(direction + 2) % 4]) {
                    return true;
                }
            } else {
                if (IntegerDimension.equals(y, x, panel.getTmp().getCircuitInfo().getAbco().getHeight(), panel.getTmp().getCircuitInfo().getAbco().getWidth()) ||
                        IntegerDimension.equals(y, x, end.getHeight(), end.getWidth()) ||
                        !panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x).getCircuitInfo().getTerminal()[(direction + 2) % 4]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * WIRE_BONDモードのWIRE_CROSSコマンドのNO_ACTION場面での検知パターン。
     * 四方向に端子がある導線のみを検知する。
     * １：四方向に端子がある導線領域
     * ２：マウスカーソルと重なっている領域
     */
    public void wireBond_wireCross_noAction(UnitPanel panel) {
        ElecomInfo e;
        CircuitInfo c;
        /** 四方向に端子がある導線にボーダを描画する */
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                c = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo();
                e = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j).getElecomInfo();
                if (e.getPartsVarieties() == PartsVarieties.WIRE) {
                    if (e.getPartsStandards() == PartsStandards._4 || e.getPartsStandards() == PartsStandards._5 || e.getPartsStandards() == PartsStandards._6) {
                        panel.getOperateBorder().setCoordinateBorder(panel, c.getAbco(), SELECTABLE);
                    }
                }
            }
        }
        /** マウスカーソルと重なってる領域にボーダを描画する */
        if (panel.getCursorCo() != null) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() == SELECTABLE) {
                panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }

    /**
     * WIRE_BONDモードのHAND_BONDコマンドのNO_ACTION場面での検知パターン。
     * 隣接マスの部品端子と未接続な導線を検知する。
     * 及び、隣接マスの導線と未接続な部品端子を検知する。
     * １：隣接マスの部品端子と未接続な導線の領域
     * ２：隣接マスの導線と未接続な部品端子の領域
     * ３：マウスカーソルと重なっている領域
     */
    public void wireBond_handBond_noAction(UnitPanel panel) {
        CircuitBlock b, b1;
        CircuitInfo c, c1;
        ElecomInfo e, e1;
        int nextY, nextX;
        boolean flg;
        /** 隣接マスと未接続な導線や端子にボーダを描画する */
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                c = b.getCircuitInfo();
                e = b.getElecomInfo();
                if (b.isExist()) {
                    if (e.getPartsVarieties() == PartsVarieties.WIRE) {
                        flg = false;
                        for (int k = 0; k < c.getConnection().length; k++) {
                            /* 接続性がない方向かつ、その隣接マスに部品があり、端子がこちらを向いている場合は検知する */
                            if (!c.getConnection()[k]) {
                                nextY = BranchNodeConnect.nextCoordinateY(c.getAbco().getHeight(), k);
                                nextX = BranchNodeConnect.nextCoordinateX(c.getAbco().getWidth(), k);
                                if (judgeOutOfBound(panel, nextY, nextX)) {
                                    b1 = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(nextY).get(nextX);
                                    c1 = b1.getCircuitInfo();
                                    e1 = b1.getElecomInfo();
                                    if (b1.isExist()) {
                                        if (e1.getPartsVarieties() != PartsVarieties.WIRE && c1.getTerminal()[(k + 2) % 4]) {
                                            /* 未接続な部品端子にボーダを描画する */
                                            flg = true;
                                            panel.getOperateBorder().setCoordinateBorder(panel, c1.getAbco(), SELECTABLE);
                                        }
                                    }
                                }
                            }
                        }
                        /* 一方向にでも未接続な部品端子があった導線にボーダを描画する */
                        if (flg) {
                            panel.getOperateBorder().setCoordinateBorder(panel, c.getAbco(), BONDABLE);
                        }
                    }
                }
            }
        }
        /** マウスカーソルと重なってる領域にボーダを描画する */
        if (panel.getCursorCo() != null) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() != null) {
                panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }

    /*******************************/
    /** PARTS_MOVE MODE DETECTIONS */
    /*******************************/
    /**
     * PARTS_MOVEモードのDETAIL系コマンドのNO_ACTION場面での汎用検知パターン。
     * 全ての部品と導線を検知する。
     * １：全ての部品と導線の領域
     * ２：マウスカーソルと重なっている領域
     * ３：マウスカーソル直下の領域
     */
    public void partsMove_commonDetail_noAction(UnitPanel panel) {
        CircuitBlock b;
        /** 全部品領域にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(panel, 0, 0, panel.getCircuitSize().getHeight(), panel.getCircuitSize().getWidth(), SELECTABLE, true);
        if (panel.getCursorCo() != null) {
            /** マウスカーソルと重なってる領域にボーダを描画する */
            b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth());
            if (b.getBorder() == SELECTABLE) {
                if (b.getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
                    panel.getOperateBorder().setGroupBorder(panel, panel.getCursorCo(), OVERLAP);
                } else {
                    panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
                }
            }
            /** マウスカーソル直下の領域 */
            panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
        }
        panel.repaint();
    }

    /**
     * PARTS_MOVEモードのRANGING場面での汎用検知パターン。
     * 指定範囲領域にボーダ描画し、さらにそこに含まれる部品や導線にも描画する。
     * １：全ての部品と導線の領域
     * ２：指定範囲領域
     * ３：指定範囲に含まれる部品や導線の領域
     */
    public void partsMove_common_ranging(UnitPanel panel) {
        /** 全部品領域にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(panel, 0, 0, panel.getCircuitSize().getHeight(), panel.getCircuitSize().getWidth(), SELECTABLE, true);
        /** 指定範囲領域にボーダを描画する */
        CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
        panel.getOperateBorder().setRangeBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), RANGING);
        /** 指定範囲に含まれる部品や導線にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), OVERLAP, false);
        cPanel.repaint();
    }

    /**
     * PARTS_MOVEモードのRANGED場面での汎用検知パターン。
     * 指定範囲領域にボーダ描画し、さらにそこに含まれる部品や導線にも描画する。
     * １：全ての部品と導線の領域
     * ２：指定範囲領域
     * ３：指定範囲に含まれる部品や導線の領域
     */
    public void partsMove_common_ranged(UnitPanel panel) {
        CircuitBlock b;
        /** 全部品領域にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(panel, 0, 0, panel.getCircuitSize().getHeight(), panel.getCircuitSize().getWidth(), SELECTABLE, true);
        /** 指定範囲領域にボーダを描画する */
        CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
        panel.getOperateBorder().setRangeBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), RANGED);
        /** 指定範囲に含まれる部品や導線にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), SELECTED, false);
        if (panel.getCursorCo() != null) {
            /** マウスカーソルと重なってる領域にボーダを描画する */
            b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth());
            if (b.getBorder() == SELECTABLE) {
                if (b.getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
                    panel.getOperateBorder().setGroupBorder(panel, panel.getCursorCo(), OVERLAP);
                } else {
                    panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
                }
            }
            /** マウスカーソル直下の領域 */
            if (b.getBorder() == null) {
                panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        cPanel.repaint();
    }

    /**
     * PARTS_MOVEモードのMOVE場面での検知パターン。
     * 一時情報の相対位置が少しでも他の部品に重なっている場合はボーダが変更されます。
     * １：元の位置の指定範囲(COPY系のみ)
     * ２：元の位置の選択されている部品や導線(COPY系のみ)
     * ３：現在の指定範囲の領域
     * ４：現在の選択されている部品や導線
     */
    public void partsMove_common_move(UnitPanel panel, boolean isMove) {
        if (panel instanceof CircuitUnitPanel) {
            boolean flg = false;
            int abcoY, abcoX, recoY, recoX;
            CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
            if (!isMove) {
                /** 指定範囲領域にボーダを描画する */
                cPanel.getOperateBorder().setRangeBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), RANGED);
                /** 指定範囲に含まれる部品や導線にボーダを描画する */
                panel.getOperateBorder().setRangeAllGroupBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), OVERLAP, false);
            }
            /* 現在の移動中の情報が他の部品や導線に重複していないか検証する */
            if (cPanel.getCursorCo() != null) {
                recoY = cPanel.getCursorCo().getHeight() - cPanel.getMoveCursorCo().getHeight();
                recoX = cPanel.getCursorCo().getWidth() - cPanel.getMoveCursorCo().getWidth();
                for (CircuitBlock b : cPanel.getTmps()) {
                    abcoY = b.getCircuitInfo().getAbco().getHeight() + recoY;
                    abcoX = b.getCircuitInfo().getAbco().getWidth() + recoX;
                    if (judgeOutOfBound(cPanel, abcoY, abcoX)) {
                        if (cPanel.getCircuitUnit().getCircuitBlock().getMatrix().get(abcoY).get(abcoX).isExist()) {
                            flg = true;
                            break;
                        }
                    } else {
                        flg = true;
                        break;
                    }
                }
                IntegerDimension start = new IntegerDimension(cPanel.getRangeStart().getHeight() + recoY, cPanel.getRangeStart().getWidth() + recoX);
                IntegerDimension end = new IntegerDimension(cPanel.getRangeEnd().getHeight() + recoY, cPanel.getRangeEnd().getWidth() + recoX);
                /** 現在の指定範囲の領域 */
                cPanel.getOperateBorder().setRangeBorder(cPanel, start, end, flg ? ERROR : RANGING);
                /** 選択されている部品や導線の領域 */
                for (CircuitBlock b : cPanel.getTmps()) {
                    abcoY = b.getCircuitInfo().getAbco().getHeight() + recoY;
                    abcoX = b.getCircuitInfo().getAbco().getWidth() + recoX;
                    if (judgeOutOfBound(cPanel, abcoY, abcoX)) {
                        cPanel.getOperateBorder().setCoordinateBorder(cPanel, abcoY, abcoX, flg ? ERROR : SELECTED);
                    }
                }
            }
            cPanel.repaint();
        }
    }

    /*********************************/
    /** PARTS_DELETE MODE DETECTIONS */
    /*********************************/
    /**
     * PARTS_DELETEモードのNO_ACTION場面での汎用的な検知パターン。
     * 複数箇所に点在していても選択されていれば全て検知する。
     * １：全ての部品と導線の領域
     * ２：選択されている部品や導線の領域
     * ３：マウスカーソルと重なっている領域
     * ４：マウスカーソル直下の領域
     */
    public void partsDelete_noAction(UnitPanel panel, boolean isCollect) {
        CircuitBlock b;
        /** 全部品領域にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(panel, 0, 0, panel.getCircuitSize().getHeight(), panel.getCircuitSize().getWidth(), SELECTABLE, true);
        /** 選択されている部品や導線の領域 */
        if (isCollect) {
            /* 全てをグループ単位で扱う */
            if (panel.getHcTmps() != null) {
                for (HighLevelConnectInfo hc : panel.getHcTmps()) {
                    panel.getOperateBorder().setGroupBorder(panel, hc, SELECTED);
                }
            }
        } else {
            /* 全てを座標単位で扱う */
            if (panel.getIdTmps() != null) {
                for (IntegerDimension id : panel.getIdTmps()) {
                    panel.getOperateBorder().setCoordinateBorder(panel, id, SELECTED);
                }
            }
        }
        /** マウスカーソルと重なっている領域 */
        if (panel.getCursorCo() != null) {
            /** マウスカーソルと重なってる領域にボーダを描画する */
            b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth());
            if (b.getBorder() == SELECTABLE) {
                if (b.getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE || isCollect) {
                    panel.getOperateBorder().setGroupBorder(panel, panel.getCursorCo(), OVERLAP);
                } else {
                    panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
                }
            }
            /** マウスカーソル直下の領域 */
            if (b.getBorder() == null) {
                panel.getOperateBorder().setCoordinateBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }

    /**
     * PARTS_DELETEモードのRANGING場面での汎用的な検知パターン。
     * 複数箇所に点在していても選択されていれば全て検知する。
     * １：全ての部品と導線の領域
     * ２：指定範囲の領域
     * ３：指定範囲に含まれている全ての部品や導線
     * ４：選択されている部品や導線の領域
     */
    public void partsDelete_ranging(UnitPanel panel, boolean isCollect) {
        CircuitBlock b;
        /** 全部品領域にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(panel, 0, 0, panel.getCircuitSize().getHeight(), panel.getCircuitSize().getWidth(), SELECTABLE, true);
        /** 指定範囲領域にボーダを描画する */
        CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
        panel.getOperateBorder().setRangeBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), RANGING);
        /** 指定範囲に含まれる部品や導線にボーダを描画する */
        panel.getOperateBorder().setRangeAllGroupBorder(cPanel, cPanel.getRangeStart(), cPanel.getRangeEnd(), OVERLAP, isCollect);
        /** 選択されている部品や導線の領域 */
        if (isCollect) {
            /* 全てをグループ単位で扱う */
            if (panel.getHcTmps() != null) {
                for (HighLevelConnectInfo hc : panel.getHcTmps()) {
                    panel.getOperateBorder().setGroupBorder(panel, hc, SELECTED);
                }
            }
        } else {
            /* 全てを座標単位で扱う */
            if (panel.getIdTmps() != null) {
                for (IntegerDimension id : panel.getIdTmps()) {
                    panel.getOperateBorder().setCoordinateBorder(panel, id, SELECTED);
                }
            }
        }
        panel.repaint();
    }

    /*******************************/
    /** PARTS_EDIT MODE DETECTIONS */
    /*******************************/
    /**
     * PARTS_EDITモードのPARTS_EDITコマンドのNO_ACTION場面での検知パターン。
     * １：電源、可変抵抗、可変パルス出力器
     * ２：マウスと重なった領域
     */
    public void partsEdit_partsEdit_noAction(UnitPanel panel) {
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;
        /** ボーダを描画する */
        for (int i=0;i<panel.getCircuitSize().getHeight();i++) {
            for (int j=0;j<panel.getCircuitSize().getWidth();j++) {
                b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                c = b.getCircuitInfo();
                e = b.getElecomInfo();
                if (c.getReco().equals(0, 0)) {
                    if (e.getPartsVarieties() == PartsVarieties.POWER) {
                        panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE);
                    } else if (e.getPartsVarieties() == PartsVarieties.RESISTANCE && e.getPartsStandards() == PartsStandards._variable) {
                        panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE);
                    } else if (e.getPartsVarieties() == PartsVarieties.PULSE && e.getPartsStandards() == PartsStandards.PULSE) {
                        panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE);
                    }
                }
            }
        }
        /** マウスカーソルと重なった領域 */
        if (panel.getCursorCo() != null) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() != null) {
                panel.getOperateBorder().setGroupBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }

    /**
     * EXECUTEモードのEXECUTEコマンドのNO_ACTION場面での検知パターン。
     * スイッチ、可変抵抗、計測器を検知し、オンオフやフォーカスを行う。
     * １：スイッチ
     * ２：電源、可変抵抗、可変パルス出力器
     * ３：電圧計、電流計
     * ４：フォーカスされた電圧計、電流計
     * ５：マウスと重なった領域
     */
    public void execute_execute_noAction(UnitPanel panel) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c;
        ElecomInfo e;
        /** スイッチ、計測器、可変抵抗の領域にボーダを描画する */
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                c = u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo();
                e = u.getCircuitBlock().getMatrix().get(i).get(j).getElecomInfo();
                if (u.getCircuitBlock().getMatrix().get(i).get(j).isExist()) {
                    if (c.getReco().equals(0, 0)) {
                        if (e.getPartsVarieties() == PartsVarieties.SWITCH) {
                            if (e.getPartsStates() == PartsStates.ON) {
                                panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), ON_SWITCH);
                            } else {
                                panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), OFF_SWITCH);
                            }
                        } else if (e.getPartsVarieties() == PartsVarieties.POWER) {
                            panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE_VAR);
                        } else if (e.getPartsVarieties() == PartsVarieties.RESISTANCE && e.getPartsStandards() == PartsStandards._variable) {
                            panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE_VAR);
                        } else if (e.getPartsVarieties() == PartsVarieties.PULSE && e.getPartsStandards() == PartsStandards.PULSE) {
                            panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE_VAR);
                        } else if (e.getPartsVarieties() == PartsVarieties.MEASURE) {
                            if (e.getPartsStandards() == PartsStandards.VOLTMETER) {
                                panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE_VOL);
                            } else {
                                panel.getOperateBorder().setGroupBorder(panel, c.getAbco(), SELECTABLE_AMM);
                            }
                        }
                    }
                }
            }
        }
        /** 選択された計測器、可変抵抗を描画する */
        if (panel instanceof ExecuteUnitPanel) {
            ExecuteUnitPanel ePanel = (ExecuteUnitPanel) panel;
            if (ePanel.getVoltmeter() != null) {
                ePanel.getOperateBorder().setGroupBorder(ePanel, ePanel.getVoltmeter().getAbco(), SELECTED_VOL);
            }
            if (ePanel.getAmmeter() != null) {
                ePanel.getOperateBorder().setGroupBorder(ePanel, ePanel.getAmmeter().getAbco(), SELECTED_AMM);
            }
        }
        /** マウスカーソルと重なった領域 */
        if (panel.getCursorCo() != null) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() != null) {
                panel.getOperateBorder().setGroupBorder(panel, panel.getCursorCo(), OVERLAP);
            }
        }
        panel.repaint();
    }
}
