package Sho.CircuitObject.ElecomBehavior;

import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;

/**
 * グループと対応する枝の情報を保持するクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class CorrespondInfo {
    private HighLevelConnectInfo info;
    private TerminalDirection role;
    private int dirCur;

    public CorrespondInfo(HighLevelConnectInfo info, TerminalDirection role) {
        this.info = info;
        this.role = role;
    }

    public HighLevelConnectInfo getInfo() {
        return info;
    }

    public TerminalDirection getRole() {
        return role;
    }

    public void setRole(TerminalDirection role) {
        this.role = role;
    }

    public int getDirCur() {
        return dirCur;
    }

    public void setDirCur(int dirCur) {
        this.dirCur = dirCur;
    }
}
