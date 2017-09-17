package Sho.CircuitObject.UnitPanel;

import KUU.BaseComponent.BaseFrame;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.Circuit.CircuitInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * CircuitUnitを内包した命令モード時確認用パネルクラス。
 */
public class OrderUnitPanel extends UnitPanel {
    public OrderUnitPanel(BaseFrame frame) {
        super(frame);
        /** リスナの設定 */
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    /**
     * circuitUnitオブジェクトを生成する。
     */
    @Override
    public void createCircuitUnit() {
        super.createCircuitUnit();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;

        /* 基板の描画 */
        g2.setColor(ColorMaster.getSubstrateColor());
        getPaintRect().setRect(getPaintBaseCo().getWidth(), getPaintBaseCo().getHeight(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getWidth(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getHeight());
        g2.fill(getPaintRect());
        /* 部品の描画 */
        for (int i = 0; i < getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < getCircuitSize().getWidth(); j++) {
                b = getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                e = b.getElecomInfo();
                c = b.getCircuitInfo();
                if (b.isExist()) {
                    g2.drawImage(
                            ImageMaster.getImageMaster().getImage(e.getPartsVarieties(), e.getPartsStandards(), e.getPartsStates(), e.getPartsDirections(), c.getReco().getHeight(), c.getReco().getWidth()).getImage(),
                            UNIT_PIXEL * getPaintRatio() * j + getPaintBaseCo().getWidth(),
                            UNIT_PIXEL * getPaintRatio() * i + getPaintBaseCo().getHeight(),
                            UNIT_PIXEL * getPaintRatio(),
                            UNIT_PIXEL * getPaintRatio(),
                            this
                    );
                }
            }
        }
    }

    /**
     * UnitPanel内でマウスを押下した地点を、基板の基準座標からの相対座標に直してpressedCoに格納する。
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        repaint();
    }

    /**
     * UnitPanel内でドラッグした地点を初期押下地点からの相対座標に直し、基板の描画基準座標を変更する。
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        repaint();
    }

    /**
     * 拡大率を変更し、マウスカーソルのある座標を基準に変更後の画面を再描画する。
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        repaint();
    }
}
