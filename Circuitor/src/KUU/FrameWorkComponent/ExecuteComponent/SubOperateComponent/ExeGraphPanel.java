package KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent;

import Master.BorderMaster.BorderMaster;
import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 電圧計や電流計の実際値を視覚的に表示するための機能を備えたクラス。
 */
public class ExeGraphPanel extends JPanel implements MouseInputListener, MouseWheelListener {
    /**
     * リスト削除を行う要素数。
     * この数を満たしている状態で値を受け取った時、一番最後の値を削除する。
     */
    private final static int REMOVE_NUM = 200;
    /**
     * グラフとして描画するための数値リスト。
     * REMOVE_NUMより多くなったら容量節約のため逐次削除していく。
     */
    private ArrayList<Double> values;
    /**
     * 罫線の値を表示するパネル。
     */
    private ExeValueLinePanel valueLinePanel;
    /**
     * マウスの押下地点のy座標。
     */
    private int pressY;
    /**
     * グラフの描画基準となるy座標。
     */
    private int baseY;
    /**
     * グラフの罫線単位。
     */
    private BigDecimal baseUnit, preUnit;
    /**
     * 罫線限界値。
     */
    private static final BigDecimal min = new BigDecimal("0.000000001");
    private static final BigDecimal max = new BigDecimal("1000000000");
    /**
     * 罫線描画用。
     */
    private Line2D.Double line;
    /**
     * 点描画用。
     */
    private Ellipse2D.Double elip;

    public ExeGraphPanel() {
        super();
        setLayout(null);
        setOpaque(true);
        setBorder(BorderMaster.getRegularBorder());
        setBackground(ColorMaster.getGraphBackColor());
        values = new ArrayList<>();
        pressY = 0;
        baseY = 0;
        baseUnit = BigDecimal.ONE;
        preUnit = BigDecimal.ONE;
        line = new Line2D.Double();
        elip = new Ellipse2D.Double();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void setValueLinePanel(ExeValueLinePanel valueLinePanel) {
        this.valueLinePanel = valueLinePanel;
    }

    /**
     * 計測器のターゲットを切り替えた時に呼び出される。
     * リストをクリアし、グラフの描画位置、単位をリセットして再描画する。
     */
    public void initValueAndGraph() {
        values.clear();
        baseY = 0;
        baseUnit = BigDecimal.ONE;
        preUnit = BigDecimal.ONE;
        repaint();
        valueLinePanel.repaint();
    }

    /**
     * 値を渡されたらリストを後ろに詰めて更新し、さらにパネルを再描画する。
     */
    public void setValueAndGraph(double value) {
        values.add(0, value);
        if (values.size() > REMOVE_NUM) {
            values.remove(REMOVE_NUM);
        }
        repaint();
    }

    /**
     * 罫線の座標を取得する。
     */
    private ArrayList<Integer> getLineCoordinates(int zero, int interval) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < getHeight(); i++) {
            if ((Math.abs(i - zero) % interval) == 0) {
                list.add(i);
            }
        }
        return list;
    }

    public HashMap<Integer, Double> getLineAndValue() {
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i : getLineCoordinates(getZeroLineCo(), getInterval())) {
            map.put(i, ((getZeroLineCo() - i) / getInterval()) * baseUnit.doubleValue());
        }
        return map;
    }

    /**
     * 罫線の間隔を取得する。
     */
    public int getInterval() {
        return getHeight() / 4;
    }

    /**
     * 値０の罫線の座標を取得する。
     */
    public int getZeroLineCo() {
        return baseY + getInterval() * 2;
    }

    /**
     * パネルにグラフを描画する。
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int interval = getInterval();
        int zero = getZeroLineCo();
        int y;
        /* x軸の罫線を描画する */
        if (zero >= 0 && zero <= getHeight()) {
            g2.setColor(Color.RED);
            line.setLine(0, zero, getWidth(), zero);
            g2.draw(line);
        }
        /* 他の罫線を描画する */
        g2.setColor(Color.WHITE);
        for (int i : getLineCoordinates(zero, interval)) {
            if (i != zero) {
                line.setLine(0, i, getWidth(), i);
                g2.draw(line);
            }
        }
        /* 値の描画 */
        g2.setColor(ColorMaster.getGraphPointColor());
        for (int i = 0; i < values.size(); i++) {
            y = zero + (int)(-values.get(i) * (interval / baseUnit.doubleValue()));
            if (y >= -interval && y < getHeight() + interval) {
                elip.setFrame(getWidth() - ((double)getWidth() / REMOVE_NUM) * i - 2, y - 2, 5, 5);
                g2.fill(elip);
            }
        }
    }

    /**
     * 罫線の単位を大きくする。
     */
    public void rangeUpper() {
        preUnit = baseUnit;
        if (baseUnit.compareTo(max) < 0) {
            baseUnit = baseUnit.multiply(BigDecimal.TEN);
        }
        if (!values.isEmpty()) {
            double x = -(-values.get(0) * ((getHeight() / 4) / baseUnit.doubleValue()));
            if (x <= Integer.MAX_VALUE && x >= Integer.MIN_VALUE) {
                baseY = (int) x;
            } else {
                baseUnit = preUnit;
            }
        }
        repaint();
        valueLinePanel.repaint();

    }

    /**
     * 罫線の単位を小さくする。
     */
    public void rangeLower() {
        preUnit = baseUnit;
        if (baseUnit.compareTo(min) > 0) {
            baseUnit = baseUnit.divide(BigDecimal.TEN);
        }
        if (!values.isEmpty()) {
            double x = -(-values.get(0) * ((getHeight() / 4) / baseUnit.doubleValue()));
            if (x <= Integer.MAX_VALUE && x >= Integer.MIN_VALUE) {
                baseY = (int) x;
            } else {
                baseUnit = preUnit;
            }
        }
        repaint();
        valueLinePanel.repaint();
    }

    /**
     * 表示位置を変更する。
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        baseY += (e.getWheelRotation() > 0 ? -1 : e.getWheelRotation() < 0 ? 1 : 0) * 100;
        repaint();
        valueLinePanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressY = e.getY() - baseY;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        baseY = e.getY() - pressY;
        repaint();
        valueLinePanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
