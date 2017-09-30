package Sho.CircuitObject.ElecomBehavior;

import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;

import java.util.ArrayList;

/**
 * パルス出力器の振る舞いを定義するクラス。
 */
public class PulseBehavior_ extends ElecomBehavior_ {
    /** パルス信号の切り替えを管理するスレッド */
    private PulseGenerate pulseGenerator;
    private double freq;

    public PulseBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        boolean flg;
        int cnt = 0;
        /* 0番も1番も適当に定める */
        setInfos(new ArrayList<>());
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            flg = false;
            getInfos().add(null);
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.EXIST) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(cnt, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                connectInfo.getHighLevelExecuteInfo().setResistance(DoubleMatrix.MAXVALUE / 2);
                                cnt++;
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
        }
        freq = getElecomInfo().getEtcStatus();
        if (freq < 1) {
            freq = 1;
        } else if (freq > 100) {
            freq = 100;
        }
        pulseGenerator = new PulseGenerate(freq);
    }

    @Override
    public void preBehavior(boolean isFirst) {}

    @Override
    public void behavior() {
        if (pulseGenerator != null) {
            for (CorrespondInfo info : getInfos()) {
                if (info != null) {
                    info.getInfo().getHighLevelExecuteInfo().setResistance(pulseGenerator.isPulseOutput() ? 0.5 : (DoubleMatrix.MAXVALUE / 2));
                }
            }
            pulseGenerator.setChanged(true);
        }
    }

    @Override
    public void disposeBehavior() {
        if (pulseGenerator != null) {
            pulseGenerator.setRunStop(true);
            pulseGenerator = null;
        }
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
        getElecomInfo().setEtcStatus(freq);
        pulseGenerator.setTiming(freq);
    }

    /**
     * パルス切り替えを管理する専用スレッド。
     */
    private class PulseGenerate extends Thread {
        /**
         * trueになるとスレッドが終了する。
         */
        private boolean runStop;
        /**
         * パルス切り替えタイミング。
         * 例：100Hzの時、この値は5(ms)となる。10(ms)は周期なので注意。
         */
        private double timing;
        /**
         * HighLevelかLowLevelかのフラグ。
         * trueの時High。
         */
        private boolean pulseOutput;
        /**
         * 適用されたかどうかを判定するフラグ。
         * 適用された場合はtrueが、そうでない場合はfalseが入る。
         * これがfalseのままの場合は、パルスのHighとLowが変更されない。
         */
        private boolean isChanged;

        PulseGenerate(double frequency) {
            super();
            runStop = false;
            pulseOutput = false;
            isChanged = false;
            this.timing = 500 / frequency;
            start();
        }

        @Override
        public void run() {
            try {
                while (!runStop && getExePanel().getExecutor() != null) {
                    try {
                        for (int i = 0; i < timing; i++) {
                            if (!runStop) {
                                sleep(1);
                            }
                        }
                        if (getExePanel().getExecutor() != null) {
                            while (!getExePanel().getExecutor().isRunning() && !getExePanel().getExecutor().isRunStop()) {
                                sleep(10);
                            }
                        }
                        if (isChanged) {
                            pulseOutput = !pulseOutput;
                            isChanged = false;
                        }
                    } catch (Exception e) {
                        System.out.println("パルス管理スレッド内で例外が発生しました。");
                    }
                }
            } finally {
                System.out.println("パルス管理スレッドが正常終了しました。");
            }
        }

        public void setRunStop(boolean runStop) {
            this.runStop = runStop;
        }

        public void setTiming(double frequency) {
            this.timing = 500 / frequency;
        }

        public boolean isPulseOutput() {
            return pulseOutput;
        }

        public boolean isChanged() {
            return isChanged;
        }

        public void setChanged(boolean changed) {
            isChanged = changed;
        }
    }
}
