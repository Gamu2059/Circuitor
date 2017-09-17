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
 * 電源の振る舞いを定義するクラス。
 */
public class PowerBehavior_ extends ElecomBehavior_ {
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
                                connectInfo.getHighLevelExecuteInfo().setPotential(1.5);
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
    }

    @Override
    public void preBehavior(boolean isFirst) {}

    @Override
    public void behavior() {}

    @Override
    public void disposeBehavior() {

    }
}
