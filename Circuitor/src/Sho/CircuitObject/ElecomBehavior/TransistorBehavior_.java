package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * トランジスタの振る舞いを定義するクラス。
 */
public class TransistorBehavior_ extends ElecomBehavior_ {
    public TransistorBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        boolean flg;
        /* 0番はベース、1番はコレクタ、2番はエミッタとする */
        setInfos(new ArrayList<>());
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.BASE) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(0, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
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
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.COLLECTOR) {
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
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.EMITTER) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(2, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
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
        int dirCur = getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE);
        if (isFirst) {
            getInfos().get(0).setDirCur(dirCur);
            if (dirCur < 0) {
                getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(1);
            } else {
                getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
            }
        } else {
            if (dirCur == getInfos().get(0).getDirCur()) {
                if (dirCur < 0) {
                    setState(PartsStates.ON);
                    getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(1);
                } else {
                    setState(PartsStates.OFF);
                    getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                }
            } else {
                setState(PartsStates.OFF);
                getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
            }
        }
    }

    @Override
    public void behavior() {
        int dirCur = getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE);
        if (dirCur < 0) {
            setState(PartsStates.ON);
        } else {
            setState(PartsStates.OFF);
        }
        if (getState() == PartsStates.ON) {
            getInfos().get(1).getInfo().getHighLevelExecuteInfo().setResistance(1);
        } else {
            getInfos().get(1).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
        }
    }

    @Override
    public void disposeBehavior() {

    }
}
