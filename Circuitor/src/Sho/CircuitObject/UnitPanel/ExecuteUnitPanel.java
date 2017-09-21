package Sho.CircuitObject.UnitPanel;

import KUU.BaseComponent.BaseFrame;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.Execute.Execute;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.VariableDirectPowerDialog;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.VariablePulseDialog;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.VariableResistanceDialog;
import Sho.Matrix.DoubleMatrix;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * CircuitUnitを内包した実行エディタ用パネルクラス。
 */
public class ExecuteUnitPanel extends UnitPanel {
    /** 実行用スレッドとなる実行処理管轄クラス */
    private Execute executor;
    /** 実行速度を保持する。 */
    private int exeSpeed;
    /** 電圧計を保持する。 */
    private HighLevelExecuteGroup voltmeter;
    /** 電流計を保持する */
    private HighLevelExecuteGroup ammeter;
    /** 割り込みで描画しているのかどうかのフラグ */
    private boolean interrupt;

    public ExecuteUnitPanel(BaseFrame frame) {
        super(frame);
        /** リスナの設定 */
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        exeSpeed = 100;
    }

    /**
     * circuitUnitオブジェクトを生成する。
     * なお、この段階でモードを初期化する。
     */
    @Override
    public void createCircuitUnit() {
        super.createCircuitUnit();
        getCircuitUnit().getMode().setMode(CircuitOperateMode.Mode.EXECUTE);
        getCircuitUnit().getCommand().setCommand(CircuitOperateCommand.Command.EXECUTE);
    }

    /**
     * executor
     */
    public Execute getExecutor() {
        return executor;
    }

    public void setExecutor(Execute executor) {
        this.executor = executor;
    }

    /**
     * exeSpeed
     */
    public int getExeSpeed() {
        return exeSpeed;
    }

    public void setExeSpeed(int exeSpeed) {
        this.exeSpeed = exeSpeed;
    }

    /**
     * voltmeter
     */
    public HighLevelExecuteGroup getVoltmeter() {
        return voltmeter;
    }

    public void setVoltmeter(HighLevelExecuteGroup voltmeter) {
        this.voltmeter = voltmeter;
    }

    /**
     * ammeter
     */
    public HighLevelExecuteGroup getAmmeter() {
        return ammeter;
    }

    public void setAmmeter(HighLevelExecuteGroup ammeter) {
        this.ammeter = ammeter;
    }

    /**
     * インタラプトフラグを立てる。
     * 折ることはできない。
     */
    public void goOnInterrupt() {
        interrupt = true;
    }

    /**
     * 基板、ボーダ、部品、電流の順に描画する。
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;
        HighLevelExecuteInfo h;
        int sub;

        /* 基板の描画 */
        g2.setColor(ColorMaster.getSubstrateColor());
        getPaintRect().setRect(getPaintBaseCo().getWidth(), getPaintBaseCo().getHeight(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getWidth(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getHeight());
        g2.fill(getPaintRect());
        /* ボーダの描画 */
        for (int i = 0; i < getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < getCircuitSize().getWidth(); j++) {
                b = getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                if (b.getBorder() != null) {
                    g2.setColor(CircuitBorder.getColor(b.getBorder()));
                    getPaintRect().setRect(
                            UNIT_PIXEL * getPaintRatio() * j + getPaintBaseCo().getWidth() + 1,
                            UNIT_PIXEL * getPaintRatio() * i + getPaintBaseCo().getHeight() + 1,
                            UNIT_PIXEL * getPaintRatio() - 2,
                            UNIT_PIXEL * getPaintRatio() - 2
                    );
                    g2.fill(getPaintRect());
                }
            }
        }
        /* 部品の描画 */
        for (int i = 0; i < getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < getCircuitSize().getWidth(); j++) {
                b = getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
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
        /* 点の描画 */
        for (HighLevelConnectInfo branch : getCircuitUnit().getHighLevelConnectList().getBranch()) {
            if (branch.getRole() == HighLevelConnectGroup.BRANCH) {
                if (branch.getHighLevelExecuteInfo().getDrawCood().getPoints().size() > 0) {
                    /* 電流の大きさが1000を超える場合はショートしているとみなし、赤色にする */
                    if (Math.abs(branch.getHighLevelExecuteInfo().getCurrent()) > 1e3) {
                        g2.setColor(Color.RED);
                    } else {
                        g2.setColor(ColorMaster.getRegularCurrentColor());
                    }
                    h = branch.getHighLevelExecuteInfo();
                    g2.fill(h.getDrawCood().getDrawCoordinate(this, h.getDrawCood().getBasePoint()));
                    for (sub = h.getDrawCood().getBasePoint() + 3; sub < h.getDrawCood().getCoods().size(); sub += 3) {
                        g2.fill(h.getDrawCood().getDrawCoordinate(this, sub));
                    }
                    for (sub = h.getDrawCood().getBasePoint() - 3; sub >= 0; sub -= 3) {
                        g2.fill(h.getDrawCood().getDrawCoordinate(this, sub));
                    }
                }
            }
        }
        if (interrupt) {
        /* 電圧計と電流計のリアルタイムな値をラベルとパネルに送信する */
            if (voltmeter != null) {
                getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getValueIndicateLabel().setFormattedValue(voltmeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getVoltage());
                getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getGraphPanel().setValueAndGraph(voltmeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getVoltage());
            }
            if (ammeter != null) {
                getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getValueIndicateLabel().setFormattedValue(ammeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getCurrent());
                getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getGraphPanel().setValueAndGraph(ammeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getCurrent());
            }
            interrupt = false;
        }
    }

    /**
     * ボーダ情報を設定した上で再描画する。
     * 再描画する際は、repaintではなく、これを呼び出す。
     */
    public void reDirection() {
        getOperateDetection().execute_execute_noAction(this);
    }

    /**
     * UnitPanel内でマウスを押下した地点を、基板の基準座標からの相対座標に直してpressedCoに格納する。
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        reDirection();
    }

    /**
     * スイッチのオンオフ切り替えや可変抵抗、計測器のフォーカス切り替えを行う。
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        /* マウスカーソルの回路上の座標を取得 */
        getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
        if (getCursorCo() != null) {
            CircuitBlock b;
            CircuitInfo c, c1;
            ElecomInfo ele;
            b = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth());
            c = b.getCircuitInfo();
            ele = b.getElecomInfo();
            if (ele.getPartsVarieties() == PartsVarieties.SWITCH) {
                /* オンオフを切り替える */
                if (ele.getPartsStates() == PartsStates.ON) {
                    getOperateOperate().setPartsStates(this, getCursorCo(), PartsStates.OFF);
                } else {
                    getOperateOperate().setPartsStates(this, getCursorCo(), PartsStates.ON);
                }
            } else if (ele.getPartsVarieties() == PartsVarieties.RESISTANCE || ele.getPartsVarieties() == PartsVarieties.PULSE ||ele.getPartsVarieties() == PartsVarieties.MEASURE) {
                c1 = getCircuitUnit().getCircuitBlock().getMatrix().get(c.getAbco().getHeight() - c.getReco().getHeight()).get(c.getAbco().getWidth() - c.getReco().getWidth()).getCircuitInfo();
                for (HighLevelExecuteGroup group : executor.getExecuteGroups()) {
                    if (group.getAbco().equals(c1.getAbco())) {
                        if (ele.getPartsStandards() == PartsStandards.DC) {
                            /* 直流電源の電圧値を変更する */
                            new VariableDirectPowerDialog(this, group);
                        } else if (ele.getPartsStandards() == PartsStandards._variable) {
                            /* 抵抗値を変更する */
                            new VariableResistanceDialog(this, group.getExecuteInfos());
                        } else if (ele.getPartsStandards() == PartsStandards.PULSE) {
                            /* 周波数を変更する */
                            new VariablePulseDialog(this, group);
                        } else if (ele.getPartsStandards() == PartsStandards.VOLTMETER) {
                            if (voltmeter != group) {
                                voltmeter = group;
                                getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getGraphPanel().initValueAndGraph();
                            }
                        } else if (ele.getPartsStandards() == PartsStandards.AMMETER) {
                            if (ammeter != group) {
                                ammeter = group;
                                getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getGraphPanel().initValueAndGraph();
                            }
                        }
                    }
                }
            }
        }
        reDirection();
    }

    /**
     * UnitPanel内でドラッグした地点を初期押下地点からの相対座標に直し、基板の描画基準座標を変更する。
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        reDirection();
    }

    /**
     * 拡大率を変更し、マウスカーソルのある座標を基準に変更後の画面を再描画する。
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        reDirection();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        reDirection();
    }
}
