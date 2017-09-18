package Sho.CircuitObject.HighLevelConnect;

import java.util.ArrayList;

/**
 * HighLevelConnectInfoをリストにするためだけのクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class HighLevelConnectList {
    private ArrayList<HighLevelConnectInfo> node;
    private ArrayList<HighLevelConnectInfo> branch;

    public HighLevelConnectList() {
        node = new ArrayList<>();
        branch = new ArrayList<>();
    }

    public ArrayList<HighLevelConnectInfo> getNode() {
        return node;
    }

    public void setNode(ArrayList<HighLevelConnectInfo> node) {
        this.node = node;
    }

    public ArrayList<HighLevelConnectInfo> getBranch() {
        return branch;
    }

    public void setBranch(ArrayList<HighLevelConnectInfo> branch) {
        this.branch = branch;
    }
}
