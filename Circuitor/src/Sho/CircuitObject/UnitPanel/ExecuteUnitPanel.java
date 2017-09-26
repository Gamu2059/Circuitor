package Sho.CircuitObject.UnitPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.ExecuteComponent.SubExecutePanel;
import KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent.ExeMeasurePanel;
import Master.ColorMaster.ColorMaster;
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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
        HighLevelExecuteInfo h;
        int sub;

        paintBase(g2);
        paintBorder(g2);
        paintParts(g2);

        /* 点の描画 */
        for (HighLevelConnectInfo branch : getCircuitUnit().getHighLevelConnectList().getBranch()) {
            if (branch.getRole() == HighLevelConnectGroup.BRANCH) {
                if (branch.getHighLevelExecuteInfo().getDrawCood().getPoints().size() > 0) {
                    /* 電流の大きさが1e3を超える場合はショートしているとみなし、赤色にする */
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

        /* 電圧計と電流計のリアルタイムな値をラベルとパネルに送信する */
        if (interrupt) {
            SubExecutePanel subExe = getFrame().getBasePanel().getSubExecutePanel();
            ExeMeasurePanel measure;
            double value;
            if (voltmeter != null) {
                measure = subExe.getVoltagePanel();
                value = voltmeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getVoltage();
                measure.getValueIndicateLabel().setFormattedValue(value);
                measure.getGraphPanel().setValueAndGraph(value);
            }
            if (ammeter != null) {
                measure = subExe.getCurrentPanel();
                value = ammeter.getExecuteInfos().get(0).getHighLevelExecuteInfo().getCurrent();
                measure.getValueIndicateLabel().setFormattedValue(value);
                measure.getGraphPanel().setValueAndGraph(value);
            }
            interrupt = false;
        }

        /* 部品説明の描画 */
        getPartsPopMenu().drawIndicate(g2, this);
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

            c1 = getCircuitUnit().getCircuitBlock().getMatrix().get(c.getAbco().getHeight() - c.getReco().getHeight()).get(c.getAbco().getWidth() - c.getReco().getWidth()).getCircuitInfo();
            for (HighLevelExecuteGroup group : executor.getExecuteGroups()) {
                if (!isSettablePartsVarieties(ele.getPartsVarieties())) {
                    continue;
                }
                switch (ele.getPartsStandards()) {
                    case TACT:
                        getOperateOperate().setPartsStates(this, c1.getAbco(), (ele.getPartsStates() == PartsStates.ON ? PartsStates.OFF : PartsStates.ON));
                        getPartsPopMenu().changeContent(ele, ele.getPartsStates());
                        break;
                    case DC:
                        /* 直流電源の電圧値を変更する */
                        new VariableDirectPowerDialog(this, group);
                        break;
                    case _variable:
                        /* 抵抗値を変更する */
                        new VariableResistanceDialog(this, group.getExecuteInfos());
                        break;
                    case PULSE:
                        /* 周波数を変更する */
                        new VariablePulseDialog(this, group);
                        break;
                    case VOLTMETER:
                        if (voltmeter != group) {
                            voltmeter = group;
                            getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getGraphPanel().initValueAndGraph();
                        }
                        break;
                    case AMMETER:
                        if (ammeter != group) {
                            ammeter = group;
                            getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getGraphPanel().initValueAndGraph();
                        }
                        break;
                }
            }
        }
        reDirection();
    }

    private boolean isSettablePartsVarieties(PartsVarieties pV) {
        return
                pV == PartsVarieties.SWITCH ||
                pV == PartsVarieties.POWER ||
                pV == PartsVarieties.RESISTANCE ||
                pV == PartsVarieties.PULSE ||
                pV == PartsVarieties.MEASURE;
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

        if (getCursorCo() == null) {
            getPartsPopMenu().hidePop();
            return;
        }
        boolean flg = false;
        CircuitBlock b = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth());
        CircuitInfo c = b.getCircuitInfo();
        c = getCircuitUnit().getCircuitBlock().getMatrix().get(c.getAbco().getHeight() - c.getReco().getHeight()).get(c.getAbco().getWidth() - c.getReco().getWidth()).getCircuitInfo();
        for (HighLevelExecuteGroup group : executor.getExecuteGroups()) {
            if (group.getAbco().equals(c.getAbco())) {
                getPartsPopMenu().controlPop(this, group);
                flg = true;
                break;
            }
        }
        if (!flg) {
            getPartsPopMenu().hidePop();
        }
    }
}
