package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * 電子部品全体に共通する振る舞いを定義するクラス。
 */
public abstract class ElecomBehavior_ {
    /** 大元の実行パネル */
    private ExecuteUnitPanel exePanel;
    /** 処理しやすいように、大事な端子をインデックス定数で登録する */
    private ArrayList<CorrespondInfo> infos;
    /** 汎用的な入力リスト。使わなくても構わない */
    private boolean[] inputList;
    /** 汎用的な出力リスト。使わなくても構わない */
    private boolean[] outputList;
    /** 電子部品の状態を保持する。使わなくても構わない */
    private PartsStates state;
    private ElecomInfo elecomInfo;

    protected ElecomBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        state = PartsStates.OFF;
        elecomInfo = new ElecomInfo();
        info.copyTo(elecomInfo);
        this.exePanel = exePanel;
    }

    public ExecuteUnitPanel getExePanel() {
        return exePanel;
    }

    public ArrayList<CorrespondInfo> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<CorrespondInfo> infos) {
        this.infos = infos;
    }

    public boolean[] getInputList() {
        return inputList;
    }

    public void setInputList(boolean[] inputList) {
        this.inputList = inputList;
    }

    public boolean[] getOutputList() {
        return outputList;
    }

    public void setOutputList(boolean[] outputList) {
        this.outputList = outputList;
    }

    public PartsStates getState() {
        return state;
    }

    public void setState(PartsStates states) {
        this.state = states;
    }

    public ElecomInfo getElecomInfo() {
        return elecomInfo;
    }

    /**
     * 対応する端子を見つけ出す
     */
    public abstract void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco);

    /**
     * 枝電流に対する振る舞いを行う
     * 必要ないなら処理なしのままオーバーラードする
     */
    public abstract void preBehavior(boolean isFirst);

    /**
     * 電子部品としての本来の振る舞いを行う
     * 必要ないなら処理なしのままオーバーライドする
     */
    public abstract void behavior();

    /**
     * 実行終了時の振る舞い
     * 必要ないなら処理なしのままオーバーライドする
     */
    public abstract void disposeBehavior();

    /**
     * 指定した端子枝が指定した節に対してどのように接続されており、またそこを電流がどのように流れているかを数値情報として返す。
     *   １：内部から外部に向かって電流が流れていることを意味する。
     *   ０：電流が流れていないことを意味する。もしくは正常に計算できない状態にあることを意味する。
     * －１：外部から内部に向かって電流が流れていることを意味する。
     */
    protected int getDirectionWithCurrent(int groupIndex, HighLevelConnectGroup baseNode) {
        int index, dir, cur;
        if (infos.get(groupIndex) != null) {
            index = exePanel.getExecutor().getBranchCurrent().getColumnRelatedIndex().indexOf(infos.get(groupIndex).getInfo().getIndex());
            if (infos.get(groupIndex).getInfo().getDirection().getRole() == baseNode) {
                dir = 1;
            } else {
                dir = -1;
            }
            if (exePanel.getExecutor().getBranchCurrent().getMatrix().get(0).get(index) > 0) {
                cur = 1;
            } else if (exePanel.getExecutor().getBranchCurrent().getMatrix().get(0).get(index) < 0) {
                cur = -1;
            } else {
                cur = 0;
            }
            return dir * cur;
        } else {
            return 0;
        }
    }

    /**
     * INでマスクを掛けて入力電流を入力リストに記録する
     */
    protected void setInput() {
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i) != null) {
                if (infos.get(i).getRole() == TerminalDirection.IN) {
                    inputList[i] = getDirectionWithCurrent(i, HighLevelConnectGroup.IN_NODE) < 0;
                }
            }
        }
    }

    /**
     * OUTでマスクを掛けて出力リストから出力抵抗を調整する
     */
    protected void setOutput() {
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i) != null) {
                if (infos.get(i).getRole() == TerminalDirection.OUT) {
                    if (outputList[i]) {
                        infos.get(i).getInfo().getHighLevelExecuteInfo().setResistance(100);
                    } else {
                        infos.get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                    }
                }
            }
        }
    }
}
