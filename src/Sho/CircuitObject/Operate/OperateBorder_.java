package Sho.CircuitObject.Operate;

import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

/**
 * UnitPanelのボーダ変更に特化した処理系クラス。
 */
public class OperateBorder_ {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;

    public OperateBorder_(BaseFrame frame) {
        this.frame = frame;
    }

    /**
     * 回路上の全てのボーダ情報を削除する。
     */
    public void removeAllBorder(UnitPanel panel) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c;
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                c = u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo();
                setCoordinateBorder(panel, c.getAbco(), null);
            }
        }
    }

    /**
     * 指定した始点と終点の領域内に含まれる全てのグループにボーダ描画するメソッド。
     * isWireUnitは、導線の扱い方を指定する。trueの場合は導線単位で、falseの場合はマス単位で扱う。
     * ただし、始点と終点がnullの場合は描画しない。
     * しかし、始点と終点が基板の領域外の時は、領域内に強制的に修正する。
     */
    public void setRangeAllGroupBorder(UnitPanel panel, IntegerDimension start, IntegerDimension end, CircuitBorder.Borders border, boolean isWireUnit) {
        /* nullなら何もしない */
        if (start == null || end == null) {
            return;
        }
        setRangeAllGroupBorder(panel, start.getHeight(), start.getWidth(), end.getHeight(), end.getWidth(), border, isWireUnit);
    }

    /**
     * 指定した始点と終点の領域内に含まれる全てのグループにボーダ描画するメソッド。
     * isWireUnitは、導線の扱い方を指定する。trueの場合は導線単位で、falseの場合はマス単位で扱う。
     * ただし、始点と終点がnullの場合は描画しない。
     * しかし、始点と終点が基板の領域外の時は、領域内に強制的に修正する。
     */
    public void setRangeAllGroupBorder(UnitPanel panel, int startY, int startX, int endY, int endX, CircuitBorder.Borders border, boolean isWireUnit) {
        /* 念のため、始点と終点を明示的に取得する */
        IntegerDimension start = panel.getOperateOperate().getStartCoordinate(startY, startX, endY, endX);
        IntegerDimension end = panel.getOperateOperate().getEndCoordinate(startY, startX, endY, endX);
        /* 始点の修正 */
        if (start.getHeight() < 0) {
            start.setHeight(0);
        }
        if (start.getWidth() < 0) {
            start.setWidth(0);
        }
        if (end.getHeight() >= panel.getCircuitSize().getHeight()) {
            end.setHeight(panel.getCircuitSize().getHeight() - 1);
        }
        if (end.getWidth() >= panel.getCircuitSize().getWidth()) {
            end.setWidth(panel.getCircuitSize().getWidth() - 1);
        }
        /* 始点から終点「まで」に存在する部品に描画する */
        CircuitUnit u = panel.getCircuitUnit();
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;
        for (int i = start.getHeight(); i <= end.getHeight(); i++) {
            for (int j = start.getWidth(); j <= end.getWidth(); j++) {
                b = u.getCircuitBlock().getMatrix().get(i).get(j);
                if (b.isExist()) {
                    e = b.getElecomInfo();
                    c = b.getCircuitInfo();
                    if (e.isBranch()) {
                        /* 導線 */
                        if (isWireUnit) {
                            /* 導線単位で、同じボーダで描画されていない導線ならば描画 */
                            if (b.getBorder() != border) {
                                setGroupBorder(panel, c.getAbco(), border);
                            }
                        } else {
                            /* マス単位で扱う場合はその座標にだけ描画 */
                            setCoordinateBorder(panel, c.getAbco(), border);
                        }
                    } else {
                        /* 部品もしくは結合点 */
                        setGroupBorder(panel, c.getAbco(), border);
                    }
                }
            }
        }
    }

    /**
     * 指定した絶対座標を含むグループを検出し、そのグループの構成座標にボーダ描画する高位のボーダ描画メソッド。
     * また、指定した絶対座標で複数のグループが検出された場合は、そのすべてに描画が及ぶ。
     * 部品領域の一括変更や導線を１本の導線として一括変更するのに適している。
     */
    public void setGroupBorder(UnitPanel panel, IntegerDimension abco, CircuitBorder.Borders border) {
        CircuitUnit u = panel.getCircuitUnit();
        boolean flg = false;

        if (u.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()).getElecomInfo().isBranch()) {
            for (HighLevelConnectInfo info : u.getHighLevelConnectList().getBranch()) {
                /* virtualがtrueのグループは無視する */
                if (!info.isVirtual()) {
                    /* 導線はBRANCHのみ探索する */
                    if (info.getRole() == HighLevelConnectGroup.BRANCH) {
                        for (IntegerDimension infoAbco : info.getAbcos()) {
                            if (infoAbco.equals(abco)) {
                                setGroupBorder(panel, info, border);
                                break;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (HighLevelConnectInfo info : u.getHighLevelConnectList().getNode()) {
                /* virtualがtrueのグループは無視する */
                PartsVarieties varieties = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()).getElecomInfo().getPartsVarieties();
                /* 部品の場合はCENTER_NODE、導線の節点の場合はNODEのみ探索する */
                if (varieties != PartsVarieties.WIRE && info.getRole() == HighLevelConnectGroup.CENTER_NODE || varieties == PartsVarieties.WIRE && info.getRole() == HighLevelConnectGroup.NODE) {
                    for (IntegerDimension infoAbco : info.getAbcos()) {
                        if (infoAbco.equals(abco)) {
                            setGroupBorder(panel, info, border);
                            flg = true;
                            break;
                        }
                    }
                }
                /* 節で複数グループに該当するものは存在しないはずのため、flgがtrueならばそこで終了する */
                if (flg) {
                    break;
                }
            }
        }
    }

    /**
     * 指定された始点と終点の範囲にボーダ描画する低位のボーダ描画メソッド。
     * 範囲指定領域や移動、複製時の空間上の予想描画に適している。
     * ただし、始点と終点がnullの場合は描画しない。
     * しかし、始点と終点が基板の領域外の時は、領域内に強制的に修正する。
     */
    public void setRangeBorder(UnitPanel panel, IntegerDimension start, IntegerDimension end, CircuitBorder.Borders border) {
        /* nullなら何もしない */
        if (start == null || end == null) {
            return;
        }
        setRangeBorder(panel, start.getHeight(), start.getWidth(), end.getHeight(), end.getWidth(), border);
    }

    /**
     * 指定された始点と終点の範囲にボーダ描画する低位のボーダ描画メソッド。
     * 範囲指定領域や移動、複製時の空間上の予想描画に適している。
     * しかし、始点と終点が基板の領域外の時は、領域内に強制的に修正する。
     */
    public void setRangeBorder(UnitPanel panel, int startY, int startX, int endY, int endX, CircuitBorder.Borders border) {
        /* 念のため、始点と終点を明示的に取得する */
        IntegerDimension start = panel.getOperateOperate().getStartCoordinate(startY, startX, endY, endX);
        IntegerDimension end = panel.getOperateOperate().getEndCoordinate(startY, startX, endY, endX);
        /* 始点の修正 */
        if (start.getHeight() < 0) {
            start.setHeight(0);
        }
        if (start.getWidth() < 0) {
            start.setWidth(0);
        }
        if (end.getHeight() >= panel.getCircuitSize().getHeight()) {
            end.setHeight(panel.getCircuitSize().getHeight() - 1);
        }
        if (end.getWidth() >= panel.getCircuitSize().getWidth()) {
            end.setWidth(panel.getCircuitSize().getWidth() - 1);
        }
        /* 始点から終点「まで」を描画する */
        for (int i = start.getHeight(); i <= end.getHeight(); i++) {
            for (int j = start.getWidth(); j <= end.getWidth(); j++) {
                setCoordinateBorder(panel, i, j, border);
            }
        }
    }

    /**
     * 指定したグループのすべての構成座標にボーダ描画する低位のボーダ描画メソッド。
     * 部品領域の一括変更や、導線を１本の導線として一括変更する場合に適している。
     */
    public void setGroupBorder(UnitPanel panel, HighLevelConnectInfo info, CircuitBorder.Borders border) {
        for (IntegerDimension abco : info.getAbcos()) {
            setCoordinateBorder(panel, abco, border);
        }
    }

    /**
     * 指定した絶対座標のみにボーダ描画するボーダ描画メソッド。
     */
    public void setCoordinateBorder(UnitPanel panel, IntegerDimension abco, CircuitBorder.Borders border) {
        setCoordinateBorder(panel, abco.getHeight(), abco.getWidth(), border);
    }

    /**
     * 指定した座標のみにボーダ描画するボーダ描画メソッド。
     */
    public void setCoordinateBorder(UnitPanel panel, int y, int x, CircuitBorder.Borders border) {
        panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x).setBorder(border);
    }
}
