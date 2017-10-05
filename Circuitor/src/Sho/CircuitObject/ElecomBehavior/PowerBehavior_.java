package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStandards;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * 電源の振る舞いを定義するクラス。
 */
public class PowerBehavior_ extends ElecomBehavior_ {
    /** 起電力の周期を管理するスレッド */
    private PulseGenerate pulseGenerator;
    private double hertz;

    public PowerBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        boolean flg;
        /* 0番は＋側、1番は－側とする */
        setInfos(new ArrayList<>());
        /* POWERをもつ座標と同じ場所にある端子枝を0番に指定する */
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.POWER) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(0, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                if (getElecomInfo().getPartsStandards() == PartsStandards.DC) {
                                    connectInfo.getHighLevelExecuteInfo().setPotential(getElecomInfo().getEtcStatus());
                                } else {
                                    connectInfo.getHighLevelExecuteInfo().setPotential(5);
                                }
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
        /* GNDをもつ座標と同じ場所にある端子枝を1番に指定する */
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.GND) {
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

        // 周波数を設定する
        if (getElecomInfo().getPartsStandards() == PartsStandards.AC) {
            hertz = getElecomInfo().getEtcStatus();
            if (hertz < 1e-4) {
                hertz = 1e-4;
            } else if (hertz > 5e1) {
                hertz = 5e1;
            }
            pulseGenerator = new PulseGenerate(5, hertz);
        }
    }

    @Override
    public void preBehavior(boolean isFirst) {}

    @Override
    public void behavior() {
        if (pulseGenerator != null) {
            getInfos().get(0).getInfo().getHighLevelExecuteInfo().setPotential(pulseGenerator.getACPotential());
        }
    }

    @Override
    public void disposeBehavior() {
        if (pulseGenerator != null) {
            pulseGenerator.setRunStop(true);
            pulseGenerator = null;
        }
    }

    public double getHertz() {
        return hertz;
    }

    public void setHertz(double hertz) {
        this.hertz = hertz;
        getElecomInfo().setEtcStatus(hertz);
        pulseGenerator.setAngV(hertz);
    }

    /**
     * パルス切り替えを管理する専用スレッド。
     * 交流電源のパルスは1msごとに波形を更新するため、周波数が1kHzで固定。
     */
    private class PulseGenerate extends Thread {
        /**
         * trueになるとスレッドが終了する。
         */
        private boolean runStop;
        /**
         * 最大起電力。
         * 値は変動しない。
         */
        private double potential;
        private double acPotential;
        /**
         * 現在の位相。
         */
        private double currentAng;
        private double angV;

        /**
         * 最初に電圧値と周波数を渡す。
         */
        PulseGenerate(double potential, double hertz) {
            super();
            runStop = false;
            this.potential = potential;
            acPotential = 0;
            currentAng = 0;
            angV = 2 * Math.PI * hertz / 1e3;
            start();
        }

        @Override
        public void run() {
            try {
                while (!runStop && getExePanel().getExecutor() != null) {
                    try {
                        if (!runStop) {
                            sleep(1);
                        }
                        if (getExePanel().getExecutor() != null) {
                            while (!getExePanel().getExecutor().isRunning() && !getExePanel().getExecutor().isRunStop()) {
                                sleep(10);
                            }
                        }
                        // 角速度に応じて値が周期する
                        acPotential = potential * Math.sin(currentAng);
                        currentAng += angV;
                        currentAng %= 2 * Math.PI;
                    } catch (Exception e) {
                        System.out.println("パルス管理スレッド内で例外が発生しました。");
                    }
                }
            } finally {
                System.out.println("パルス管理スレッドが正常終了しました。");
            }
        }

        public double getACPotential() {
            return acPotential;
        }

        public void setAngV(double hertz) {
            angV = 2 * Math.PI * hertz / 1e3;
        }

        public void setRunStop(boolean runStop) {
            this.runStop = runStop;
        }
    }
}
