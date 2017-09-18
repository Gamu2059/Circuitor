package Sho.CircuitObject.Circuit;

import KUU.BaseComponent.BaseFrame;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectList;
import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;
import Sho.Matrix.IntegerMatrix;
import Sho.Matrix.Matrix;

import java.util.ArrayList;

/**
 * 回路全体が保持すべき情報をまとめたクラス。
 */
public class CircuitUnit implements CopyTo {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    /*************************/
    /** 回路全体に関わる情報 */
    /*************************/
    /** ブロック単位で電子部品の情報を管理する行列。 */
    private Matrix<CircuitBlock> circuitBlock;
    /** 複数のブロック単位で電子部品の情報を管理する高レベルの接続情報リスト。 */
    private HighLevelConnectList highLevelConnectList;
    /** 閉回路単位で接続情報を管理する最高レベルの接続情報行列。 */
    private IntegerMatrix loopMatrix;

    /*************************************/
    /** 回路エディタの操作を管理する情報 */
    /*************************************/
    /**
     * 回路エディタの操作を絞るための情報
     */
    private CircuitOperateMode mode;
    /**
     * さらに細かく操作に絞るための情報
     */
    private CircuitOperateCommand command;
    /**
     * 操作の場面を明確に区分するための情報
     */
    private CircuitOperateBehavior behavior;

    public CircuitUnit(BaseFrame frame) {
        this.frame = frame;
        /** 基板上のブロック情報の生成 */
        circuitBlock = new Matrix<>();
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            circuitBlock.getRowRelatedIndex().add(i);
            circuitBlock.getMatrix().add(i, new ArrayList<>());
            for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
                circuitBlock.getMatrix().get(i).add(j, new CircuitBlock(i, j));
            }
        }
        /** 縦と横の生成 */
        circuitBlock.setRowRelatedIndex(new ArrayList<>());
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            circuitBlock.getRowRelatedIndex().add(i);
        }
        circuitBlock.setColumnRelatedIndex(new ArrayList<>());
        for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
            circuitBlock.getColumnRelatedIndex().add(j);
        }
        /** その他初期化 */
        mode = new CircuitOperateMode();
        command = new CircuitOperateCommand();
        behavior = new CircuitOperateBehavior();
    }

    /** circuitBlock */
    public Matrix<CircuitBlock> getCircuitBlock() {
        return circuitBlock;
    }

    public void setCircuitBlock(Matrix<CircuitBlock> circuitBlock) {
        this.circuitBlock = circuitBlock;
    }

    /** highLevelConnectList */
    public HighLevelConnectList getHighLevelConnectList() {
        return highLevelConnectList;
    }

    public void setHighLevelConnectList(HighLevelConnectList highLevelConnectList) {
        this.highLevelConnectList = highLevelConnectList;
    }

    /** LoopMatrix */
    public IntegerMatrix getLoopMatrix() {
        return loopMatrix;
    }

    public void setLoopMatrix(IntegerMatrix loopMatrix) {
        this.loopMatrix = loopMatrix;
    }

    /**
     * mode
     */
    public CircuitOperateMode getMode() {
        return mode;
    }

    public void setMode(CircuitOperateMode mode) {
        this.mode = mode;
    }

    /**
     * command
     */
    public CircuitOperateCommand getCommand() {
        return command;
    }

    public void setCommand(CircuitOperateCommand command) {
        this.command = command;
    }

    /**
     * behavior
     */
    public CircuitOperateBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(CircuitOperateBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public void copyTo(Object o) {
        if (o instanceof CircuitUnit) {
            CircuitUnit circuitUnit = (CircuitUnit) o;

            circuitUnit.circuitBlock.clear();
            for (int i : this.circuitBlock.getRowRelatedIndex()) {
                circuitUnit.circuitBlock.getRowRelatedIndex().add(i);
            }
            for (int i : this.circuitBlock.getColumnRelatedIndex()) {
                circuitUnit.circuitBlock.getColumnRelatedIndex().add(i);
            }
            for (int i = 0; i < this.circuitBlock.getMatrix().size(); i++) {
                circuitUnit.circuitBlock.getMatrix().add(i, new ArrayList<>());
                for (int j = 0; j < this.circuitBlock.getMatrix().get(0).size(); j++) {
                    CircuitBlock b = new CircuitBlock(i, j);
                    this.circuitBlock.getMatrix().get(i).get(j).copyTo(b);
                    circuitUnit.circuitBlock.getMatrix().get(i).add(j, b);
                }
            }
        }
        else {
            new CannotCopyToException("CircuitUnit");
        }
    }
}
