package Sho.CircuitObject.HighLevelConnect;

import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.ElecomBehavior.*;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * 電子部品など、複数のグループが各々の振る舞いを全うできるようにまとめるクラス。
 * @author 翔
 * @version 1.1
 */
public class HighLevelExecuteGroup {
    /** 大元の実行パネル */
    private ExecuteUnitPanel exePanel;
    /**
     * グループに含まれるもののリスト
     */
    private ArrayList<HighLevelConnectInfo> executeInfos;
    /**
     * 絶対座標
     */
    private IntegerDimension abco;
    /**
     * このグループの振る舞いを決定する情報
     */
    private ElecomBehavior_ behavior;

    public HighLevelExecuteGroup(ExecuteUnitPanel exePanel, ElecomInfo info, IntegerDimension abco) {
        executeInfos = new ArrayList<>();
        this.exePanel = exePanel;
        this.abco = abco;
        setBehavior(exePanel, info);
    }

    public ArrayList<HighLevelConnectInfo> getExecuteInfos() {
        return executeInfos;
    }

    public void setExecuteInfos(ArrayList<HighLevelConnectInfo> executeInfos) {
        this.executeInfos = executeInfos;
    }

    public IntegerDimension getAbco() {
        return abco;
    }

    public void setAbco(IntegerDimension abco) {
        this.abco = abco;
    }

    public ElecomBehavior_ getBehavior() {
        return behavior;
    }

    public void setBehavior(ElecomBehavior_ behavior) {
        this.behavior = behavior;
    }

    /**
     * elecomInfoの情報を基に、適切な振る舞いクラスのオブジェクトを生成する。
     */
    private void setBehavior(ExecuteUnitPanel exePanel, ElecomInfo info) {
        switch (info.getPartsVarieties()) {
//            case CAPACITANCE:
//                behavior = new CondenserBehavior_(exePanel, info);
//                break;
            case DIODE:
                behavior = new DiodeBehavior_(exePanel, info);
                break;
            case LED:
                behavior = new LEDBehavior_(exePanel, info);
                break;
            case LOGIC_IC:
                behavior = new Logic_ICBehavior_(exePanel, info);
                break;
            case PIC:
                behavior = new PICBehavior_(exePanel, info);
                break;
            case POWER:
                behavior = new PowerBehavior_(exePanel, info);
                break;
            case RESISTANCE:
                behavior = new ResistanceBehavior_(exePanel, info);
                break;
            case SWITCH:
                behavior = new SwitchBehavior_(exePanel, info);
                break;
            case TRANSISTOR:
                behavior = new TransistorBehavior_(exePanel, info);
                break;
            case MEASURE:
                behavior = new MeasureBehavior_(exePanel, info);
                break;
            case PULSE:
                behavior = new PulseBehavior_(exePanel, info);
                break;
        }
    }

    /**
     * 振る舞いクラスが定義する端子番号に対応するグループ情報を見つけ出し、そのグループに固有のインデックスを与える。
     */
    public void setCorrespondGroup() {
        behavior.setCorrespondGroup(executeInfos, abco);
    }
}
