package Sho.CircuitObject.HighLevelConnect;

import Master.ImageMaster.PartsVarieties;
import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * 節、枝同士のグループ化された１つの単位として振る舞う情報を管理するクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class HighLevelConnectInfo implements CopyTo,Cloneable {
    /** そのグループに分類される電子部品の絶対座標 */
    private ArrayList<IntegerDimension> abcos;
    /** 自身のグループとしての役割 */
    private HighLevelConnectGroup role;
    /** 自身の電子部品的種類 */
    private PartsVarieties groupVarieties;
    /** 自身のリスト内でのインデックス */
    private int index;
    /** 端子枝および端子節だった場合の構成ブロックとの接続方向 */
    private int connectDirection;
    /** 回路上に存在しないグループ情報かどうか */
    private boolean virtual;
    /** 枝かどうか */
    private boolean branch;
    /** 木を構成しているかどうか */
    private boolean tree;
    /** そのグループの隣接しているグループのうち、そのグループの始点となるグループ */
    private HighLevelConnectInfo direction;
    /** 隣接グループのリスト */
    private HighLevelNextInfo nextGroups;
    /** グループの電流、電圧などを保持する */
    private HighLevelExecuteInfo highLevelExecuteInfo;

    /* 本来のグループ情報指定 */
    public HighLevelConnectInfo() {
        abcos = new ArrayList<>();
        nextGroups = new HighLevelNextInfo();
        virtual = false;
        branch = false;
        tree = false;
    }

    /* 存在しない枝をグループ情報にする場合に指定 */
    public HighLevelConnectInfo(IntegerDimension abco1, IntegerDimension abco2) {
        abcos = new ArrayList<>();
        abcos.add(abco1);
        abcos.add(abco2);
        nextGroups = new HighLevelNextInfo();
        nextGroups.getAbcos().add(abco1);
        nextGroups.getAbcos().add(abco2);
        virtual = true;
        branch = true;
        tree = false;
    }

    public ArrayList<IntegerDimension> getAbcos() {
        return abcos;
    }

    public void setAbcos(ArrayList<IntegerDimension> abcos) {
        this.abcos = abcos;
    }

    public HighLevelConnectGroup getRole() {
        return role;
    }

    public void setRole(HighLevelConnectGroup role) {
        this.role = role;
    }

    public PartsVarieties getGroupVarieties() {
        return groupVarieties;
    }

    public void setGroupVarieties(PartsVarieties groupVarieties) {
        this.groupVarieties = groupVarieties;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getConnectDirection() {
        return connectDirection;
    }

    public void setConnectDirection(int connectDirection) {
        this.connectDirection = connectDirection;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }

    public boolean isTree() {
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public HighLevelConnectInfo getDirection() {
        return direction;
    }

    public void setDirection(HighLevelConnectInfo direction) {
        this.direction = direction;
    }

    public HighLevelNextInfo getNextGroups() {
        return nextGroups;
    }

    public void setNextGroups(HighLevelNextInfo nextGroups) {
        this.nextGroups = nextGroups;
    }

    public HighLevelExecuteInfo getHighLevelExecuteInfo() {
        return highLevelExecuteInfo;
    }

    public void setHighLevelExecuteInfo(HighLevelExecuteInfo highLevelExecuteInfo) {
        this.highLevelExecuteInfo = highLevelExecuteInfo;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HighLevelConnectInfo :\n");
        stringBuffer.append("   role : " + role + "\n");
        stringBuffer.append("   varieties : " + groupVarieties + "\n");
        stringBuffer.append("   index : " + index + "\n");
        if (role == HighLevelConnectGroup.TERMINAL_NODE || role == HighLevelConnectGroup.TERMINAL_BRANCH || role == HighLevelConnectGroup.IN_BRANCH || role == HighLevelConnectGroup.OUT_BRANCH) {
            stringBuffer.append("   connectDirection : " + connectDirection + "\n");
        }
        stringBuffer.append("   abcos : " + abcos + "\n");
        if (virtual) {
            stringBuffer.append("   virtual!\n");
        }
        if (tree) {
            stringBuffer.append("   tree!\n");
        }
        if (direction == null) {
            stringBuffer.append("   direction : Nothing!\n");
        }
        else {
            stringBuffer.append("   direction : " + direction.getIndex() + "\n");
        }
        stringBuffer.append(nextGroups);
        return stringBuffer.toString();
    }

    @Override
    public HighLevelConnectInfo clone() {
        HighLevelConnectInfo clone = new HighLevelConnectInfo();
        for (IntegerDimension abco : this.abcos) {
            clone.getAbcos().add(abco.clone());
        }
        clone.role = this.role;
        clone.groupVarieties = this.groupVarieties;
        clone.index = this.index;
        clone.connectDirection = this.connectDirection;
        clone.virtual = this.virtual;
        clone.branch = this.branch;
        clone.tree = this.tree;
        clone.direction = this.direction;
        clone.nextGroups = this.nextGroups.clone();
        return clone;
    }

    @Override
    public void copyTo(Object o) {
        if (o instanceof HighLevelConnectInfo) {
            HighLevelConnectInfo highLevelConnectInfo = (HighLevelConnectInfo)o;
            highLevelConnectInfo.getAbcos().clear();
            for (IntegerDimension abco : this.abcos) {
                highLevelConnectInfo.getAbcos().add(abco);
            }
            highLevelConnectInfo.role = this.role;
            highLevelConnectInfo.groupVarieties = this.groupVarieties;
            highLevelConnectInfo.index = this.index;
            highLevelConnectInfo.connectDirection = this.connectDirection;
            highLevelConnectInfo.virtual = this.virtual;
            highLevelConnectInfo.branch = this.branch;
            highLevelConnectInfo.tree = this.tree;
            highLevelConnectInfo.direction = this.direction;
            this.nextGroups.copyTo(highLevelConnectInfo.getNextGroups());
        }
        else {
            new CannotCopyToException("HighLevelConnectInfo");
        }
    }
}
