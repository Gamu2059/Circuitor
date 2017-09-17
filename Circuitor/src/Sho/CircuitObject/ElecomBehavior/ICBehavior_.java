package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;

import java.util.ArrayList;

import static Master.ImageMaster.PartsStandards.*;
import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * 論理ＩＣやマイコンの共通な振る舞いを定義するクラス。
 */
public abstract class ICBehavior_ extends ElecomBehavior_ {
    public ICBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    private void preBehaviorSetting(int i, int dirCur) {
        switch (getInfos().get(i).getRole()) {
            case POWER:
                if (dirCur < 0) {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(1);
                    getInputList()[i] = true;
                } else {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                    getInputList()[i] = false;
                }
                break;
            case GND:
                if (dirCur > 0) {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(1);
                    getOutputList()[i] = true;
                } else {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                    getOutputList()[i] = false;
                }
                break;
            case IN:
                if (dirCur < 0) {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(100);
                } else {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                }
                break;
        }
    }

    @Override
    public void preBehavior(boolean isFirst) {
        int dirCur = 0;
        /* 各端子の役割ごとに抵抗特性を考慮する */
        for (int i = 0; i < getInfos().size(); i++) {
            if (getInfos().get(i) != null) {
                switch (getInfos().get(i).getRole()) {
                    case POWER:
                    case OUT:
                        dirCur = getDirectionWithCurrent(i, HighLevelConnectGroup.OUT_NODE);
                        break;
                    case GND:
                    case IN:
                        dirCur = getDirectionWithCurrent(i, HighLevelConnectGroup.IN_NODE);
                        break;
                }
                if (isFirst) {
                    getInfos().get(i).setDirCur(dirCur);
                    preBehaviorSetting(i, dirCur);
                } else {
                    if (dirCur == getInfos().get(i).getDirCur()) {
                        preBehaviorSetting(i, dirCur);
                    } else {
                        getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                        switch (getInfos().get(i).getRole()) {
                            case POWER:
                                getInputList()[i] = false;
                                break;
                            case GND:
                                getOutputList()[i] = false;
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void behavior() {
        for (int i = 0; i < getInfos().size(); i++) {
            if (getInfos().get(i) != null) {
                if (getInfos().get(i).getRole() == TerminalDirection.OUT) {
                    getOutputList()[i] = false;
                }
            }
        }
    }

    @Override
    public void disposeBehavior() {

    }

    /**
     * 渡された(y,x)座標を持つ端子をn番で登録する
     */
    protected void add(int y, int x, int n, ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco, int dir) {
        getInfos().add(n, null);
        for (HighLevelConnectInfo group : groups) {
            if (group.getAbcos().get(0).equals(y, x)) {
                for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
                    if (y == abco.getHeight() + linkInfo.getReco().getHeight() && x == abco.getWidth() + linkInfo.getReco().getWidth()) {
                        if (group.getRole() != HighLevelConnectGroup.TERMINAL_BRANCH) {
                            getInfos().set(n, new CorrespondInfo(group, linkInfo.getTerminalDirection()[dir]));
                            return;
                        }
                    }
                }
            }
        }
    }
}
