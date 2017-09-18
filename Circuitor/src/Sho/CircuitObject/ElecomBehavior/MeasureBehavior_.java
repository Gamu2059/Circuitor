package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStandards;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;

import java.util.ArrayList;

import static Sho.CircuitObject.Execute.Execute.THRESHOLD_CURRENT;


/**
 * スイッチの振る舞いを定義するクラス。
 */
public class MeasureBehavior_ extends ElecomBehavior_ {
    private double num;

    public MeasureBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
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
                                if (getElecomInfo().getPartsStandards() == PartsStandards.VOLTMETER) {
                                    connectInfo.getHighLevelExecuteInfo().setResistance(DoubleMatrix.MAXVALUE);
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
        if (!isFirst) {
            getInfos().get(0).setDirCur(getDirectionWithCurrent(0, HighLevelConnectGroup.CENTER_NODE));
        }
    }

    @Override
    public void behavior() {
        HighLevelExecuteInfo exeInfo = getInfos().get(0).getInfo().getHighLevelExecuteInfo();
        num = Math.abs(exeInfo.getRealCurrent());
        if (getInfos().get(0).getDirCur() < 0) {
            num *= 1;
        } else if (getInfos().get(0).getDirCur() > 0) {
            num *= -1;
        }
        if (getElecomInfo().getPartsStandards() == PartsStandards.VOLTMETER) {
            num *= exeInfo.getResistance();
            if (Math.abs(num) < THRESHOLD_CURRENT) {
                num = 0;
            }
            exeInfo.setVoltage(num);
        } else {
            if (Math.abs(num) < THRESHOLD_CURRENT) {
                num = 0;
            }
            exeInfo.setCurrent(num);
        }
    }

    @Override
    public void disposeBehavior() {

    }
}