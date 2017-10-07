package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * コンデンサの振る舞いを定義するクラス。
 */
public class CondenserBehavior_ extends ElecomBehavior_ {
    /** コンデンサ以外の部分の抵抗値 */
    private double extraResistance;
    /** コンデンサの端子電圧 */
    private double terminalVoltage;
    private double maxPotential;
    /** 経過時間 */
    private double time;
    /** 定数：１周期で進む時間 */
    private final double TIME = 5e-5;

    public CondenserBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
        setState(PartsStates.OFF);
        extraResistance = 0;
        terminalVoltage = 0;
        time = 0;
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        boolean flg;
        /* 0番はアノード、1番はカソードとする */
        setInfos(new ArrayList<>());
        /* ANODEをもつ座標と同じ場所にある端子枝を0番に指定する */
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.ANODE) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(0, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                connectInfo.getHighLevelExecuteInfo().setCapacitance(10e-6);
                                flg = true;
                                break;
                            }
                        }
                    }
                }
                if (flg) {
                    break;
                }
            }
            if (flg) {
                break;
            }
        }
        /* CATHODEをもつ座標と同じ場所にある端子枝を1番に指定する */
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.CATHODE) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(1, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                flg = true;
                                break;
                            }
                        }
                    }
                }
                if (flg) {
                    break;
                }
            }
            if (flg) {
                break;
            }
        }
    }

    @Override
    public void preBehavior(boolean isFirst) {
    }

    @Override
    public void behavior() {
        HighLevelExecuteInfo exeInfo = getInfos().get(0).getInfo().getHighLevelExecuteInfo();
        boolean changeFlag = false;

        /* 充放電状態を判定する */
        if (getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE) < 0 && exeInfo.getPreResistance() < MAXVALUE) {
            /* 電流がない、もしくは電流が流入していれば、充電状態 */
            if (getState() == PartsStates.ON) {
                changeFlag = true;
            }
            setState(PartsStates.OFF);
        } else {
            /* 電流が流出していれば、放電状態 */
            if (getState() == PartsStates.OFF) {
                changeFlag = true;
            }
            setState(PartsStates.ON);
        }

        /* 状態変化時の処理 */
        if (changeFlag) {
            if (getState() == PartsStates.OFF) {
                /* 放電から充電へ */
                exeInfo.setPotential(0);
            } else if (getState() == PartsStates.ON) {
                /* 充電から放電へ */
                exeInfo.setPotential(terminalVoltage);
                maxPotential = terminalVoltage;
            }
            System.out.println("--------------changed-------------------");
        }
        /* 充放電処理 */
        if (getState() == PartsStates.OFF) {
            /* 充電処理 */
            extraResistance = exeInfo.getPreResistance();
            if (Double.isInfinite(Math.log(1 - terminalVoltage / exeInfo.getMaxPotential())) || Double.isNaN(Math.log(1 - terminalVoltage / exeInfo.getMaxPotential()))) {
                time = MAXVALUE;
            } else {
                time = -exeInfo.getCapacitance() * extraResistance * Math.log(1 - terminalVoltage / exeInfo.getMaxPotential());
            }
            terminalVoltage += exeInfo.getMaxPotential() * (Math.exp(-time / (exeInfo.getCapacitance() * extraResistance)) - Math.exp(-(time + TIME) / (exeInfo.getCapacitance() * extraResistance)));
            if (terminalVoltage >= exeInfo.getMaxPotential()) {
                exeInfo.setResistance(MAXVALUE);
                terminalVoltage = exeInfo.getMaxPotential();
            } else {
                exeInfo.setResistance(terminalVoltage/Math.abs(exeInfo.getCurrent()));
            }
        } else if (getState() == PartsStates.ON) {
            /* 放電処理 */
            extraResistance = exeInfo.getPreResistance();
            System.out.println("pR"+extraResistance);
            if (Double.isInfinite(Math.log(terminalVoltage / maxPotential)) || Double.isNaN(Math.log(terminalVoltage / maxPotential))) {
                time = MAXVALUE;
                System.out.println("tv:"+terminalVoltage);
                System.out.println("mp:"+maxPotential);
                System.out.println("111111111111111111111111111111111111111111111");
            } else {
                time = -exeInfo.getCapacitance() * extraResistance * Math.log(terminalVoltage / maxPotential);
            }
            terminalVoltage += maxPotential * (Math.exp(-(time + TIME) / (exeInfo.getCapacitance() * extraResistance)) - Math.exp(-time / (exeInfo.getCapacitance() * extraResistance)));
            if (terminalVoltage <= 0) {
                exeInfo.setPotential(0);
                terminalVoltage = 0;
            } else {
                exeInfo.setPotential(terminalVoltage);
            }
            exeInfo.setResistance(0);
        }
    }

    @Override
    public void disposeBehavior() {

    }
}
