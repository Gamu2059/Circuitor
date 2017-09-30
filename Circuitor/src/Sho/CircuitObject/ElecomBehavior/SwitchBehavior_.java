package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.CircuitBorder;
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
 * スイッチの振る舞いを定義するクラス。
 */
public class SwitchBehavior_ extends ElecomBehavior_ {
    public SwitchBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
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
    }

    @Override
    public void preBehavior(boolean isFirst) {}

    @Override
    public void behavior() {
        for (CorrespondInfo info : getInfos()) {
            if (info != null) {
                /* ボタンは、クリック判定が入っていたらON、そうでなければOFFにする */
                if (getExePanel().getCircuitUnit().getCircuitBlock().getMatrix().get(info.getInfo().getAbcos().get(0).getHeight()).get(info.getInfo().getAbcos().get(0).getWidth()).getElecomInfo().getPartsStates() == PartsStates.ON) {
                    info.getInfo().getHighLevelExecuteInfo().setResistance(0);
                    setState(PartsStates.ON);
                } else {
                    info.getInfo().getHighLevelExecuteInfo().setResistance(DoubleMatrix.MAXVALUE / 2);
                    setState(PartsStates.OFF);
                }
            }
        }
    }

    @Override
    public void disposeBehavior() {

    }
}
