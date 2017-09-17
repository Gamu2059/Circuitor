package Sho.CircuitObject.Circuit;

import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static Sho.CircuitObject.UnitPanel.UnitPanel.UNIT_PIXEL;

/**
 * 点の描画位置を管理するクラス。
 * Executeパッケージに含みたいが、不具合が起こるのでCircuitパッケージに含める。
 */
public class DrawCood {
    public enum DrawPoint {
        /** 中心 */
        CENTER,
        /** 上辺 */
        UP,
        /** 右辺 */
        RIGHT,
        /** 下辺 */
        DOWN,
        /** 左辺 */
        LEFT,
    }
    /* 描画用の円 */
    private static Ellipse2D elip = new Ellipse2D.Double();
    /* 描画する点の大きさを保持する */
    private double pointSize;
    /* 点の最大移動カウント */
    private double pointMaxCount;
    /* 点のカウント用変数 */
    private double pointCount;
    /* 点描画の基準となる位置 */
    private int basePoint;
    /* pointsとcoodsはサイズが等しくなければならない */
    private ArrayList<DrawPoint> points;
    private ArrayList<IntegerDimension> coods;

    public DrawCood() {
        points = new ArrayList<>();
        coods = new ArrayList<>();
        pointMaxCount = 0;
        pointCount = 0;
        pointSize = 0;
    }

    public double getPointSize() {
        return pointSize;
    }

    public void setPointSize(double pointSize) {
        this.pointSize = pointSize;
    }

    public double getPointMaxCount() {
        return pointMaxCount;
    }

    public void setPointMaxCount(double pointMaxCount) {
        this.pointMaxCount = pointMaxCount;
    }

    public double getPointCount() {
        return pointCount;
    }

    public void setPointCount(double pointCount) {
        this.pointCount = pointCount;
    }

    public int getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(int basePoint) {
        this.basePoint = basePoint;
    }

    /**
     * 描画点の位置をずらす。
     * dirがtrueなら、枝の順方向にずれる。
     */
    public void incBasePoint(boolean dir) {
        if (dir) {
            basePoint++;
            if (basePoint >= coods.size()) {
                basePoint = coods.size() % 3;
            }
        } else {
            basePoint--;
            if (basePoint < 0) {
                basePoint = 2;
            }
        }
    }

    public ArrayList<DrawPoint> getPoints() {
        return points;
    }

    public ArrayList<IntegerDimension> getCoods() {
        return coods;
    }

    public void setCoods(ArrayList<IntegerDimension> coods) {
        this.coods = coods;
    }

    /**
     * 枝の根元を0として、n番目の描画円を描画できる状態で返す。
     */
    public Ellipse2D getDrawCoordinate(UnitPanel panel, int index) {
        switch (points.get(index)) {
            case UP:
                elip.setFrame(
                    (coods.get(index).getWidth() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getWidth(),
                    (coods.get(index).getHeight() * UNIT_PIXEL - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getHeight(),
                    pointSize * 2 * panel.getPaintRatio() - 1,
                    pointSize * 2 * panel.getPaintRatio() - 1
                );
                return elip;
            case RIGHT:
                elip.setFrame(
                    (coods.get(index).getWidth() * UNIT_PIXEL + UNIT_PIXEL - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getWidth(),
                    (coods.get(index).getHeight() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getHeight(),
                    pointSize * 2 * panel.getPaintRatio() - 1,
                    pointSize * 2 * panel.getPaintRatio() - 1
                );
                return elip;
            case DOWN:
                elip.setFrame(
                    (coods.get(index).getWidth() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getWidth(),
                    (coods.get(index).getHeight() * UNIT_PIXEL + UNIT_PIXEL - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getHeight(),
                    pointSize * 2 * panel.getPaintRatio() - 1,
                    pointSize * 2 * panel.getPaintRatio() - 1
                );
                return elip;
            case LEFT:
                elip.setFrame(
                    (coods.get(index).getWidth() * UNIT_PIXEL - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getWidth(),
                    (coods.get(index).getHeight() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getHeight(),
                    pointSize * 2 * panel.getPaintRatio() - 1,
                    pointSize * 2 * panel.getPaintRatio() - 1
                );
                return elip;
            default:
                elip.setFrame(
                    (coods.get(index).getWidth() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getWidth(),
                    (coods.get(index).getHeight() * UNIT_PIXEL + UNIT_PIXEL / 2 - pointSize) * panel.getPaintRatio() + panel.getPaintBaseCo().getHeight(),
                    pointSize * 2 * panel.getPaintRatio() - 1,
                    pointSize * 2 * panel.getPaintRatio() - 1
                );
                return elip;
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DrawCood :\n");
        for (int i=0;i<points.size();i++) {
            stringBuffer.append("   " + coods.get(i) + " : " + points.get(i) + "\n");
        }
        return stringBuffer.toString();
    }
}
