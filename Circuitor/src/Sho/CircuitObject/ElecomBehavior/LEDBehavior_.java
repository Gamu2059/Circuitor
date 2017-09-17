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

import static Sho.CircuitObject.Execute.Execute.THRESHOLD_CURRENT;
import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * ＬＥＤの振る舞いを定義するクラス。
 */
public class LEDBehavior_ extends ElecomBehavior_ {
    public LEDBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
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
        int dirCur = getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE);
        if (isFirst) {
            getInfos().get(0).setDirCur(dirCur);
            if (dirCur < 0) {
                getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(100);
            } else {
                getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
            }
        } else {
            if (dirCur == getInfos().get(0).getDirCur()) {
                if (dirCur < 0) {
                    setState(PartsStates.ON);
                    getInfos().get(0).getInfo().getHighLevelExecuteInfo().setResistance(100);
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
        /* ＬＥＤのステートがオンかつ電流値が小さくなければ点灯 */
        if (getState() == PartsStates.ON && Math.abs(getExePanel().getExecutor().getBranchCurrent().getMatrix().get(0).get(getExePanel().getExecutor().getBranchCurrent().getColumnRelatedIndex().indexOf(getInfos().get(0).getInfo().getIndex()))) >= THRESHOLD_CURRENT) {
            if (getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE) < 0) {
                getExePanel().getOperateOperate().setPartsStates(getExePanel(), getInfos().get(0).getInfo().getAbcos().get(0), PartsStates.ON);
            }
        } else {
            getExePanel().getOperateOperate().setPartsStates(getExePanel(), getInfos().get(0).getInfo().getAbcos().get(0), PartsStates.OFF);
        }
    }

    @Override
    public void disposeBehavior() {

    }
}
