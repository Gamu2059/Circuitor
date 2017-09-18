package Sho.CircuitObject.ElecomBehavior;

import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * 抵抗の振る舞いを定義するクラス。
 */
public class ResistanceBehavior_ extends ElecomBehavior_ {
    public ResistanceBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
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
                                switch (getElecomInfo().getPartsStandards()) {
                                    case _10:
                                        connectInfo.getHighLevelExecuteInfo().setResistance(5e0);
                                        break;
                                    case _100:
                                        connectInfo.getHighLevelExecuteInfo().setResistance(5e1);
                                        break;
                                    case _1000:
                                        connectInfo.getHighLevelExecuteInfo().setResistance(5e2);
                                        break;
                                    case _10000:
                                        connectInfo.getHighLevelExecuteInfo().setResistance(5e3);
                                        break;
                                    case _variable:
                                        if (getElecomInfo().getEtcStatus() < 1) {
                                            connectInfo.getHighLevelExecuteInfo().setResistance(5e-1);
                                        } else if (getElecomInfo().getEtcStatus() > 1e6) {
                                            connectInfo.getHighLevelExecuteInfo().setResistance(5e5);
                                        } else {
                                            connectInfo.getHighLevelExecuteInfo().setResistance(getElecomInfo().getEtcStatus() / 2d);
                                        }
                                        break;
                                }
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
    }

    @Override
    public void preBehavior(boolean isFirst) {}

    @Override
    public void behavior() {}

    @Override
    public void disposeBehavior() {

    }
}
