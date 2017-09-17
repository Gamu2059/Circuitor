package Sho.CircuitObject.Graphed;

import KUU.BaseComponent.BaseFrame;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectList;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

import static Master.ImageMaster.PartsVarieties.*;
import static Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup.*;

/**
 * 端子と部品のリンク対応、隣接ブロックの端子接続、隣接節、隣接枝とのグループ分けなど、接続情報の形成を行うクラス。
 */
public class BranchNodeConnect {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;

    private CircuitUnit circuitUnit;
    private CircuitBlock circuitBlock;
    private CircuitInfo circuitInfo;
    private ElecomInfo elecomInfo;

    public BranchNodeConnect(BaseFrame frame) {
        this.frame = frame;
    }

    /**
     * 回路の接続関係を解析する。
     */
    public boolean branchNodeConnect(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();
        terminalConnect();
        branchNodeGrouping();

        treeConnect(panel);
        if (!isExistParts(panel)) {
            System.out.println("基板上に部品や結合点が一つも存在しません！");
            return false;
        }
        return true;
    }

    /**
     * 余分なグループを削除し、接続関係を再解析する。
     */
    public boolean branchNodeReconnect(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();
        ArrayList<Integer> removeBranch, removeNode;
        boolean flg;

        /* LoopMatrixの全ての行において0の枝を削除候補とする */
        removeBranch = new ArrayList<>();
        for (int i = 0; i < circuitUnit.getLoopMatrix().getColumnRelatedIndex().size(); i++) {
            flg = false;
            for (int j = 0; j < circuitUnit.getLoopMatrix().getRowRelatedIndex().size(); j++) {
                if (circuitUnit.getLoopMatrix().getMatrix().get(j).get(i) != 0) {
                    flg = true;
                    break;
                }
            }
            if (!flg) {
                removeBranch.add(circuitUnit.getLoopMatrix().getColumnRelatedIndex().get(i));
            }
        }
        /* deleteBranchに登録された枝しか隣接グループに持っていない節を削除候補とする */
        removeNode = new ArrayList<>();
        for (HighLevelConnectInfo info : circuitUnit.getHighLevelConnectList().getNode()) {
            flg = false;
            for (int next : info.getNextGroups().getIndexs()) {
                if (!removeBranch.contains(next)) {
                    flg = true;
                    break;
                }
            }
            if (!flg) {
                removeNode.add(info.getIndex());
            }
        }
        removeGroup(circuitUnit.getHighLevelConnectList().getNode(), removeNode);
        removeGroup(circuitUnit.getHighLevelConnectList().getBranch(), removeBranch);
        /* 接続情報をリセットし、バグを防ぐ */

        /* 再解析 */
        resetNextInfos(panel);
        treeConnect(panel);
        if (!isExistParts(panel)) {
            return false;
        }
        /* 行列の再生成 */
        frame.getBasePanel().getEditCircuitPanel().getCircuitMatrixing().circuitMatrixing(panel);
        return true;
    }

    /**
     * 基板上に部品が一つでも存在すればtrueを返す。
     */
    private boolean isExistParts(UnitPanel panel) {
        if (panel.getCircuitUnit().getHighLevelConnectList().getNode() != null) {
            for (HighLevelConnectInfo hc : panel.getCircuitUnit().getHighLevelConnectList().getNode()) {
                if (hc.getGroupVarieties() != WIRE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定されたグループリストに存在する削除候補を削除する。
     */
    private void removeGroup(ArrayList<HighLevelConnectInfo> infoList, ArrayList<Integer> removeList) {
        int removeIndex;
        for (int index : removeList) {
            removeIndex = -1;
            for (int i=0;i<infoList.size();i++) {
                if (infoList.get(i).getIndex() == index) {
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex != -1) {
                infoList.remove(removeIndex);
            }
        }
    }

    /**
     * 再解析用メソッドで、隣接情報をリセットする。
     */
    public void resetNextInfos(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();
        for (HighLevelConnectInfo node : circuitUnit.getHighLevelConnectList().getNode()) {
            node.getNextGroups().clear();
        }
        for (HighLevelConnectInfo branch : circuitUnit.getHighLevelConnectList().getBranch()) {
            branch.getNextGroups().clear();
        }
    }

    /**
     * グループ情報を基に隣接グループを確認し、木を構築する。
     */
    public void treeConnect(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();
        getNextGroups();
        int i = 0;
        boolean flg = true;
        if (circuitUnit.getHighLevelConnectList().getNode().size() > 0) {
            while (flg) {
                flg = false;
                for (HighLevelConnectInfo info : circuitUnit.getHighLevelConnectList().getNode()) {
                    if (!info.isTree()) {
                        i = info.getIndex();
                        flg = true;
                        break;
                    }
                }
                treeRecursiveSearch(circuitUnit.getHighLevelConnectList().getNode().get(i));
            }
        }
    }

    /**
     * 回路ブロック単体での接続方向確認、隣接する回路ブロックとの接続確認、導線自身の端子の対応確認を行う。
     */
    private void terminalConnect() {
        /* 回路ブロック単体での接続方向確認 */
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
                circuitBlock = circuitUnit.getCircuitBlock().getMatrix().get(i).get(j);
                if (circuitBlock.isExist()) {
                    circuitInfo = circuitBlock.getCircuitInfo();
                    elecomInfo = circuitBlock.getElecomInfo();

                    for (CircuitLinkInfo circuitLinkInfo : elecomInfo.getLinkedTerminal()) {
                        if (circuitInfo.getReco().equals(circuitLinkInfo.getReco())) {
                            for (int k = 0; k < circuitInfo.getTerminal().length; k++) {
                                circuitInfo.getTerminal()[k] = circuitLinkInfo.getLink()[k];
                            }
                        }
                    }
                }
            }
        }

        /* 隣接する回路ブロックとの接続確認 */
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
                circuitBlock = circuitUnit.getCircuitBlock().getMatrix().get(i).get(j);

                if (circuitBlock.isExist()) {
                    circuitInfo = circuitBlock.getCircuitInfo();
                    elecomInfo = circuitBlock.getElecomInfo();

                    int abcoY,abcoX;
                    for (int k = 0; k < circuitInfo.getTerminal().length; k++) {
                        abcoY = circuitInfo.getAbco().getHeight();
                        abcoX = circuitInfo.getAbco().getWidth();

                        abcoY = nextCoordinateY(abcoY, k);
                        abcoX = nextCoordinateX(abcoX, k);

                        /* 隣接ブロックが存在し、かつ、自身の端子と隣接ブロックの端子が接続性を持つならば、connectionをtrueにする */
                        if (abcoY >= 0 && abcoY < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight() && abcoX >= 0 && abcoX < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth()) {
                            if (circuitUnit.getCircuitBlock().getMatrix().get(abcoY).get(abcoX).isExist()) {
                                if (circuitInfo.getTerminal()[k] && circuitUnit.getCircuitBlock().getMatrix().get(abcoY).get(abcoX).getCircuitInfo().getTerminal()[(k + 2) % 4]) {
                                    circuitInfo.getConnection()[k] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        /* 導線自身の端子の対応確認 */
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
                circuitBlock = circuitUnit.getCircuitBlock().getMatrix().get(i).get(j);
                if (circuitBlock.isExist() && circuitBlock.getElecomInfo().getPartsVarieties() == WIRE && circuitBlock.getElecomInfo().isBranch()) {
                    circuitInfo = circuitBlock.getCircuitInfo();
                    elecomInfo = circuitBlock.getElecomInfo();

                    for (int k = 0; k < circuitInfo.getTerminalCorrespond().length; k++) {
                        circuitInfo.getTerminalCorrespond()[k] = elecomInfo.getLinkedTerminal().get(0).getTerminalCorrespond()[k];
                    }
                }
            }
        }
    }

    /**
     * 節候補、枝候補、それぞれが隣接する節、枝同士でグループ化する。
     */
    private void branchNodeGrouping() {
        /* グループ情報リストの作成 */
        circuitUnit.setHighLevelConnectList(new HighLevelConnectList());

        /* 回路ブロック単体での接続方向確認 */
        for (int i = 0; i < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth(); j++) {
                circuitBlock = circuitUnit.getCircuitBlock().getMatrix().get(i).get(j);

                if (circuitBlock.isExist()) {
                    circuitInfo = circuitBlock.getCircuitInfo();
                    elecomInfo = circuitBlock.getElecomInfo();

                    /* すでに規定数までグループ化されているブロックはパスする */
                    if (circuitInfo.getHighLevelConnectIndex() != elecomInfo.getHighLevelConnectSize()) {
                        /* 枝の時は４辺探索する */
                        if (elecomInfo.isBranch()) {
                            HighLevelConnectInfo highLevelConnectInfo[] = new HighLevelConnectInfo[4];

                            /* ４辺を探索し、自身の端子がグループ化されていなければ、再帰的にグループ化する */
                            for (int k = 0; k < circuitInfo.getConnection().length; k++) {
                                /* その方向に端子さえあれば、自分自身は登録する */
                                if (circuitInfo.getTerminal()[k]) {
                                    highLevelConnectInfo[k] = new HighLevelConnectInfo();
                                    /* その方向の端子が外部と接続されているならば再帰的に探索する */
                                    if (circuitInfo.getConnection()[k] && !circuitInfo.getGroupedTerminal()[k]) {
                                        branchRecursiveSearch(circuitInfo.getAbco().getHeight(), circuitInfo.getAbco().getWidth(), k, highLevelConnectInfo[k]);
                                    }
                                    /* 最後に自身を登録する */
                                    circuitInfo.getGroupedTerminal()[k] = true;
                                    highLevelConnectInfo[k].getAbcos().add(circuitInfo.getAbco());
                                    highLevelConnectInfo[k].setBranch(true);
                                }
                            }

                            /* ４辺で対応する端子同士は、グループ情報を統合する */
                            for (int k = 0; k < circuitInfo.getGroupedTerminal().length; k++) {
                                /* 端子がグループ化されていて、かつそれに対応するグループ情報がnullでなければ探索する */
                                if (circuitInfo.getGroupedTerminal()[k] && circuitInfo.getTerminalCorrespond()[k] != -1) {
                                    if (highLevelConnectInfo[k] != null) {
                                        /* 対応する端子のグループ情報がnullでなければ統合する */
                                        if (highLevelConnectInfo[circuitInfo.getTerminalCorrespond()[k]] != null) {
                                            /* 自身のデータを削除する */
                                            for (int l = 0; l < highLevelConnectInfo[k].getAbcos().size(); l++) {
                                                if (circuitInfo.getAbco().equals(highLevelConnectInfo[k].getAbcos().get(l))) {
                                                    highLevelConnectInfo[k].getAbcos().remove(l);
                                                }
                                            }
                                            for (IntegerDimension id : highLevelConnectInfo[circuitInfo.getTerminalCorrespond()[k]].getAbcos()) {
                                                highLevelConnectInfo[k].getAbcos().add(id);
                                            }
                                            for (IntegerDimension id : highLevelConnectInfo[circuitInfo.getTerminalCorrespond()[k]].getNextGroups().getAbcos()) {
                                                highLevelConnectInfo[k].getNextGroups().getAbcos().add(id);
                                            }
                                            for (int direction : highLevelConnectInfo[circuitInfo.getTerminalCorrespond()[k]].getNextGroups().getDirections()) {
                                                highLevelConnectInfo[k].getNextGroups().getDirections().add(direction);
                                            }
                                            /* 統合元のグループ情報を切り離す */
                                            highLevelConnectInfo[circuitInfo.getTerminalCorrespond()[k]] = null;
                                        }
                                    }
                                }
                            }

                            /* ４つのグループ情報のうち、nullではないものだけをグループ情報リストに登録する */
                            for (int k = 0; k < highLevelConnectInfo.length; k++) {
                                if (highLevelConnectInfo[k] != null) {
                                    highLevelConnectInfo[k].setRole(BRANCH);
                                    highLevelConnectInfo[k].setGroupVarieties(WIRE);
                                    circuitUnit.getHighLevelConnectList().getBranch().add(highLevelConnectInfo[k]);
                                }
                            }
                        }
                        /* 節の時は同じ電子部品を構成するもの同士でグループ化し、違う節を検知したら、緊急で仮想の枝をグループ化する */
                        else {
                            /* 導線の節の場合の処理 */
                            if (elecomInfo.getPartsVarieties() == WIRE) {
                                HighLevelConnectInfo highLevelConnectInfo = new HighLevelConnectInfo();
                                /* 外部に隣接した節があるかどうかを探索する */
                                extraGroupSearch(circuitInfo.getAbco().getHeight(), circuitInfo.getAbco().getWidth(), highLevelConnectInfo);
                                /* グループ化個数を増分し、グループに登録する */
                                circuitInfo.setHighLevelConnectIndex(circuitInfo.getHighLevelConnectIndex() + 1);
                                highLevelConnectInfo.getAbcos().add(circuitInfo.getAbco());
                                highLevelConnectInfo.setRole(NODE);
                                highLevelConnectInfo.setGroupVarieties(WIRE);
                                circuitUnit.getHighLevelConnectList().getNode().add(highLevelConnectInfo);
                            }
                            /* 普通の電子部品の場合の処理 */
                            else {
                                /* 電子部品の中心節を作成する */
                                HighLevelConnectInfo centerNode = new HighLevelConnectInfo();
                                centerNode.setVirtual(true);
                                centerNode.setRole(CENTER_NODE);
                                centerNode.setGroupVarieties(elecomInfo.getPartsVarieties());
                                circuitUnit.getHighLevelConnectList().getNode().add(centerNode);
                                /* LOGIC_ICとPICは特殊な中心節を生成する */
                                HighLevelConnectInfo inNode = null,outNode = null;
                                if (elecomInfo.getPartsVarieties() == LOGIC_IC || elecomInfo.getPartsVarieties() == PIC) {
                                    inNode = new HighLevelConnectInfo();
                                    inNode.setVirtual(true);
                                    inNode.setRole(IN_NODE);
                                    inNode.setGroupVarieties(elecomInfo.getPartsVarieties());
                                    circuitUnit.getHighLevelConnectList().getNode().add(inNode);
                                    outNode = new HighLevelConnectInfo();
                                    outNode.setVirtual(true);
                                    outNode.setRole(OUT_NODE);
                                    outNode.setGroupVarieties(elecomInfo.getPartsVarieties());
                                    circuitUnit.getHighLevelConnectList().getNode().add(outNode);
                                }
                                /* 電子部品全体を探索する */
                                for (int k = circuitInfo.getAbco().getHeight() - circuitInfo.getReco().getHeight(); k < circuitInfo.getAbco().getHeight() - circuitInfo.getReco().getHeight() + elecomInfo.getSize().getHeight(); k++) {
                                    for (int l = circuitInfo.getAbco().getWidth() - circuitInfo.getReco().getWidth(); l < circuitInfo.getAbco().getWidth() - circuitInfo.getReco().getWidth() + elecomInfo.getSize().getWidth(); l++) {
                                        /* 中心節に座標を登録する */
                                        centerNode.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                        if (elecomInfo.getPartsVarieties() == LOGIC_IC || elecomInfo.getPartsVarieties() == PIC) {
                                            inNode.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                            outNode.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                        }
                                        /* 辺の構成ブロックのみ探索する */
                                        if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getHeight() == 0 || circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getHeight() == elecomInfo.getSize().getHeight() - 1 ||
                                                circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getWidth() == 0 || circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getWidth() == elecomInfo.getSize().getWidth() - 1) {
                                            /* 接続性を確認し、外部との接続関係があれば、端子節を作成し、端子節と中心節とを枝で結合する */
                                            for (int m = 0; m < circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection().length; m++) {
                                                if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection()[m]) {
                                                    /* 端子節を作成する */
                                                    HighLevelConnectInfo terminalNode = new HighLevelConnectInfo();
                                                    terminalNode.setVirtual(true);
                                                    terminalNode.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    terminalNode.setConnectDirection(m);
                                                    /* 端子節の外部に隣接した節があるかどうかを探索する */
                                                    extraGroupPartSearch(k, l, m, terminalNode);
                                                    /* 結合する枝を作成する */
                                                    HighLevelConnectInfo terminalBranch = new HighLevelConnectInfo();
                                                    terminalBranch.setVirtual(true);
                                                    terminalBranch.setBranch(true);
                                                    terminalBranch.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    terminalBranch.setConnectDirection(m);
                                                    /* 中心節と端子節、端子枝を隣接グループとして対応させる */
                                                    centerNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    terminalNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    terminalBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    terminalBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                    /* 端子節を登録する */
                                                    terminalNode.setRole(TERMINAL_NODE);
                                                    terminalNode.setGroupVarieties(elecomInfo.getPartsVarieties());
                                                    circuitUnit.getHighLevelConnectList().getNode().add(terminalNode);
                                                    /* 端子枝を登録する */
                                                    terminalBranch.setRole(TERMINAL_BRANCH);
                                                    terminalBranch.setGroupVarieties(elecomInfo.getPartsVarieties());
                                                    circuitUnit.getHighLevelConnectList().getBranch().add(terminalBranch);
                                                    /* LOGIC_ICとPICは、端子接続情報のうち、相対座標が等しい情報を抽出し、その方向にIN、OUT、POWER、GNDがあれば、それぞれグループを作成する */
                                                    if (elecomInfo.getPartsVarieties() == LOGIC_IC || elecomInfo.getPartsVarieties() == PIC) {
                                                        for (CircuitLinkInfo linkInfo : elecomInfo.getLinkedTerminal()) {
                                                            if (linkInfo.getReco().equals(k - circuitInfo.getAbco().getHeight(), l - circuitInfo.getAbco().getWidth())) {
                                                                if (linkInfo.getTerminalDirection()[m] == TerminalDirection.IN || linkInfo.getTerminalDirection()[m] == TerminalDirection.GND) {
                                                                    HighLevelConnectInfo inBranch = new HighLevelConnectInfo();
                                                                    inBranch.setVirtual(true);
                                                                    inBranch.setBranch(true);
                                                                    inBranch.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    inBranch.setConnectDirection(m);
                                                                    /* 入力節と端子節、入力枝を隣接グループとして対応させる */
                                                                    inNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    terminalNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    inBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    inBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    /* 入力枝を登録する */
                                                                    inBranch.setRole(IN_BRANCH);
                                                                    inBranch.setGroupVarieties(elecomInfo.getPartsVarieties());
                                                                    circuitUnit.getHighLevelConnectList().getBranch().add(inBranch);
                                                                } else if (linkInfo.getTerminalDirection()[m] == TerminalDirection.OUT || linkInfo.getTerminalDirection()[m] == TerminalDirection.POWER) {
                                                                    HighLevelConnectInfo outBranch = new HighLevelConnectInfo();
                                                                    outBranch.setVirtual(true);
                                                                    outBranch.setBranch(true);
                                                                    outBranch.getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    outBranch.setConnectDirection(m);
                                                                    /* 出力節と端子節、出力枝を隣接グループとして対応させる */
                                                                    outNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    terminalNode.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    outBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    outBranch.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                                                                    /* 出力枝を登録する */
                                                                    outBranch.setRole(OUT_BRANCH);
                                                                    outBranch.setGroupVarieties(elecomInfo.getPartsVarieties());
                                                                    circuitUnit.getHighLevelConnectList().getBranch().add(outBranch);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        /* グループ化個数を増分し、グループに登録する */
                                        circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().setHighLevelConnectIndex(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getHighLevelConnectIndex() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void extraGroupSearch(int k, int l, HighLevelConnectInfo highLevelConnectInfo) {
        /* 相対的に部品の上辺を構成するならその上を探索する */
        if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getHeight() == 0 && circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection()[0]) {
            extraGroupPartSearch(k, l, 0, highLevelConnectInfo);
        }
                                        /* 相対的に部品の下辺を構成するならその下を探索する */
        if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getHeight() == elecomInfo.getSize().getHeight() - 1 && circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection()[2]) {
            extraGroupPartSearch(k, l, 2, highLevelConnectInfo);
        }
                                        /* 相対的に部品の左辺を構成するならその左を探索する */
        if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getWidth() == 0 && circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection()[3]) {
            extraGroupPartSearch(k, l, 3, highLevelConnectInfo);
        }
                                        /* 相対的に部品の右辺を構成するならその右を探索する */
        if (circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getReco().getWidth() == elecomInfo.getSize().getWidth() - 1 && circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getConnection()[1]) {
            extraGroupPartSearch(k, l, 1, highLevelConnectInfo);
        }
    }

    private void extraGroupPartSearch(int k, int l,int direction, HighLevelConnectInfo highLevelConnectInfo) {
        int y,x;
        y = nextCoordinateY(k, direction);
        x = nextCoordinateX(l, direction);
        emergencyGrouping(k, l, y, x, direction, highLevelConnectInfo);
    }

    private void emergencyGrouping(int k, int l, int y, int x, int direction, HighLevelConnectInfo highLevelConnectInfo) {
        /* 探索先が存在して、かつ節ならば、仮想の枝をグループ化する */
        if (y >= 0 && y < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight() && x >= 0 && x < frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth()) {
            if (circuitUnit.getCircuitBlock().getMatrix().get(y).get(x).isExist()) {
                /* 隣が節なら枝の緊急生成 */
                if (!circuitUnit.getCircuitBlock().getMatrix().get(y).get(x).getElecomInfo().isBranch()) {
                    /* 隣接する節が仮想枝と接続されていない時は仮想枝を生成 */
                    if (!circuitUnit.getCircuitBlock().getMatrix().get(y).get(x).getCircuitInfo().getGroupedTerminal()[(direction + 2) % 4]) {
                        HighLevelConnectInfo emergencyInfo = new HighLevelConnectInfo(new IntegerDimension(k, l), new IntegerDimension(y, x));
                        emergencyInfo.setGroupVarieties(WIRE);
                        emergencyInfo.setRole(BRANCH);
                        emergencyInfo.getNextGroups().getDirections().add(direction);
                        emergencyInfo.getNextGroups().getDirections().add((direction + 2) % 4);
                        circuitUnit.getHighLevelConnectList().getBranch().add(emergencyInfo);
                        /* 自身の絶対座標と向きを登録する */
                        highLevelConnectInfo.getNextGroups().getDirections().add((direction + 2) % 4);
                        highLevelConnectInfo.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                        /* 節同士の端子をtrueにし、重複を防ぐ */
                        circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getGroupedTerminal()[direction] = true;
                        circuitUnit.getCircuitBlock().getMatrix().get(y).get(x).getCircuitInfo().getGroupedTerminal()[(direction + 2) % 4] = true;
                    }
                    /* 接続されている時は自身の絶対座標と向きだけを登録する */
                    else {
                        highLevelConnectInfo.getNextGroups().getDirections().add((direction + 2) % 4);
                        highLevelConnectInfo.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(k).get(l).getCircuitInfo().getAbco());
                    }
                }
                /* そうでなければ、隣の枝の絶対座標と向きを登録する */
                else {
                    highLevelConnectInfo.getNextGroups().getDirections().add((direction + 2) % 4);
                    highLevelConnectInfo.getNextGroups().getAbcos().add(circuitUnit.getCircuitBlock().getMatrix().get(y).get(x).getCircuitInfo().getAbco());
                }
            }
        }
    }

    /**
     * 向きを元に隣接するブロックのY座標を相対的に導出する。
     * 共通化させても大丈夫なのでstaticメソッド。
     */
    public static int nextCoordinateY(int abcoY, int direction) {
        switch (direction) {
            case 0:
                abcoY--;
                break;
            case 2:
                abcoY++;
                break;
        }
        return abcoY;
    }

    /**
     * 向きを元に隣接するブロックのX座標を相対的に導出する。
     * 共通化させても大丈夫なのでstaticメソッド。
     */
    public static int nextCoordinateX(int abcoX, int direction) {
        switch (direction) {
            case 1:
                abcoX++;
                break;
            case 3:
                abcoX--;
                break;
        }
        return abcoX;
    }

    /**
     * 枝候補を再帰的にグループ化する。
     *
     * @param abcoY 呼び出し元のY軸の絶対座標
     * @param abcoX 呼び出し元のX軸の絶対座標
     * @param direction 呼び出し元の端子の向き
     * @param highLevelConnectInfo 格納するグループ情報
     */
    private void branchRecursiveSearch(int abcoY, int abcoX, int direction, HighLevelConnectInfo highLevelConnectInfo) {
        abcoY = nextCoordinateY(abcoY, direction);
        abcoX = nextCoordinateX(abcoX, direction);
        /* 指定座標が行列の範囲外の時はパスする */
        if (abcoY < 0 || abcoY >= frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getHeight() || abcoX < 0 || abcoX >= frame.getBasePanel().getEditCircuitPanel().getCircuitSize().getWidth()) {
            return;
        }
        CircuitInfo c = circuitUnit.getCircuitBlock().getMatrix().get(abcoY).get(abcoX).getCircuitInfo();
        ElecomInfo e = circuitUnit.getCircuitBlock().getMatrix().get(abcoY).get(abcoX).getElecomInfo();

        if (e.isBranch()) {
            /* グループ化済みでなく、対応する端子がある場合のみ探索 */
            if (!c.getGroupedTerminal()[(direction + 2) % 4] && c.getTerminalCorrespond()[(direction + 2) % 4] != -1) {
                /* 対応する端子をグループ化したとみなす */
                c.getGroupedTerminal()[(direction + 2) % 4] = true;
                /* 次のブロックの隣接方向を指定 */
                direction = c.getTerminalCorrespond()[(direction + 2) % 4];
                /* まだグループ済みでない場合のみ */
                if (!c.getGroupedTerminal()[direction]) {
                    /* 隣接方向の対応する端子もグループ化したとみなす */
                    c.getGroupedTerminal()[direction] = true;
                    /* グループ化個数を増分する */
                    c.setHighLevelConnectIndex(c.getHighLevelConnectIndex() + 1);
                    /* このブロックをグループ登録 */
                    highLevelConnectInfo.getAbcos().add(c.getAbco());
                    /* 隣接方向の対応する端子が存在する場合のみ再帰呼び出しする */
                    if (c.getTerminal()[direction]) {
                        branchRecursiveSearch(abcoY, abcoX, direction, highLevelConnectInfo);
                    }
                }
            }
        }
        /* 節だった場合はその絶対座標を登録し、再起呼び出しを打ち止めする */
        else {
            highLevelConnectInfo.getNextGroups().getDirections().add((direction + 2) % 4);
            highLevelConnectInfo.getNextGroups().getAbcos().add(new IntegerDimension(abcoY, abcoX));
        }
    }

    /**
     * nextCoordinateAbcosからnextGroupsを取得する。
     */
    public void getNextGroups() {
        /* 節のインデックスを取得する */
        for (HighLevelConnectInfo node : circuitUnit.getHighLevelConnectList().getNode()) {
            node.setIndex(circuitUnit.getHighLevelConnectList().getNode().indexOf(node));
        }
        /* 枝のインデックスを取得する */
        for (HighLevelConnectInfo branch : circuitUnit.getHighLevelConnectList().getBranch()) {
            branch.setIndex(circuitUnit.getHighLevelConnectList().getBranch().indexOf(branch));
        }
        /* 節から枝へ隣接グループを取得する */
        searchNextGroups(circuitUnit.getHighLevelConnectList().getNode(), circuitUnit.getHighLevelConnectList().getBranch());
        /* 枝から節へ隣接グループを取得する */
        searchNextGroups(circuitUnit.getHighLevelConnectList().getBranch(), circuitUnit.getHighLevelConnectList().getNode());
    }

    /**
     * 隣接するグループを探索して対応させる。
     *
     * @param base 隣接グループを対応させるグループリスト
     * @param next 対応させられるグループリスト
     *
     */
    private void searchNextGroups(ArrayList<HighLevelConnectInfo> base, ArrayList<HighLevelConnectInfo> next) {
        /* 対応させるグループリストを探索する */
        for (HighLevelConnectInfo baseList : base) {
            /* 隣接するグループがなければパスする */
            if (baseList.getNextGroups().getAbcos() == null) {
                continue;
            }
            /* 対応させるグループの隣接するグループの絶対座標を探索する */
            for (int i = 0; i < baseList.getNextGroups().getAbcos().size(); i++) {
                IntegerDimension baseNextGroupAbco = baseList.getNextGroups().getAbcos().get(i);
                boolean flg = false;
                /* 対応させられるグループリストを探索する */
                for (HighLevelConnectInfo nextList : next) {
                    /* 対応させられるグループの構成ブロックの絶対座標を探索する */
                    for (IntegerDimension groupAbco : nextList.getAbcos()) {
                        /* 対応させられるグループを構成する座標に、対応させるグループの隣接グループの座標が含まれているか判断する */
                        if (baseNextGroupAbco.equals(groupAbco)) {
                            /* 今度は、対応させられるグループの隣接座標が、対応させるグループの座標に含まれているかをチェックする */
                            for (int j = 0; j < nextList.getNextGroups().getAbcos().size(); j++) {
                                IntegerDimension nextListNextGroupAbco = nextList.getNextGroups().getAbcos().get(j);
                                for (IntegerDimension baseListGroupAbco : baseList.getAbcos()) {
                                    /* 互いに真に隣接していることが判断できたら隣接グループとして登録する */
                                    if (nextListNextGroupAbco.equals(baseListGroupAbco)) {
                                        boolean existFlg = false;
                                        for (int index : baseList.getNextGroups().getIndexs()) {
                                            /* すでに登録済みであれば登録しない */
                                            if (nextList.getIndex() == index) {
                                                existFlg = true;
                                                break;
                                            }
                                        }
                                        if (!existFlg) {
                                            boolean connectFlg = false;
                                            switch (baseList.getRole()) {
                                                case BRANCH:
                                                    if (nextList.getRole() == NODE) {
                                                        connectFlg = true;
                                                    }
                                                    else if (nextList.getRole() == TERMINAL_NODE) {
                                                        if (baseList.getNextGroups().getDirections().get(i) == nextList.getConnectDirection()) {
                                                            connectFlg = true;
                                                        }
                                                    }
                                                    break;
                                                case TERMINAL_BRANCH:
                                                case IN_BRANCH:
                                                case OUT_BRANCH:
                                                    if (baseList.getRole() == TERMINAL_BRANCH && nextList.getRole() == CENTER_NODE || baseList.getRole() == IN_BRANCH && nextList.getRole() == IN_NODE || baseList.getRole() == OUT_BRANCH && nextList.getRole() == OUT_NODE) {
                                                        connectFlg = true;
                                                    }
                                                    else if (nextList.getRole() == TERMINAL_NODE) {
                                                        if (baseList.getConnectDirection() == nextList.getConnectDirection()) {
                                                            connectFlg = true;
                                                        }
                                                    }
                                                    break;
                                                case NODE:
                                                    if (nextList.getRole() == BRANCH) {
                                                        connectFlg = true;
                                                    }
                                                    break;
                                                case CENTER_NODE:
                                                    if (nextList.getRole() == TERMINAL_BRANCH) {
                                                        connectFlg = true;
                                                    }
                                                    break;
                                                case IN_NODE:
                                                    if (nextList.getRole() == IN_BRANCH) {
                                                        connectFlg = true;
                                                    }
                                                    break;
                                                case OUT_NODE:
                                                    if (nextList.getRole() == OUT_BRANCH) {
                                                        connectFlg = true;
                                                    }
                                                    break;
                                                case TERMINAL_NODE:
                                                    if (nextList.getRole() == BRANCH) {
                                                        if (baseList.getConnectDirection() == nextList.getNextGroups().getDirections().get(j)) {
                                                            connectFlg = true;
                                                        }
                                                    }
                                                    else if (nextList.getRole() == TERMINAL_BRANCH || nextList.getRole() == IN_BRANCH || nextList.getRole() == OUT_BRANCH) {
                                                        if (baseList.getConnectDirection() == nextList.getConnectDirection()) {
                                                            connectFlg = true;
                                                        }
                                                    }
                                                    break;
                                            }
                                            /* すべての条件を満たしてきた時だけ登録する */
                                            if (connectFlg) {
                                                baseList.getNextGroups().getIndexs().add(nextList.getIndex());
                                                baseList.getNextGroups().getInfos().add(nextList);
                                                flg = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (flg) break;
                            }
                        }
                        if (flg) break;
                    }
                    if (flg) break;
                }
            }
        }
    }

    /**
     * ある節を始点にして再帰的に木を導出する。
     *
     * @param baseInfo 始点の節のグループ情報
     */
    private void treeRecursiveSearch(HighLevelConnectInfo baseInfo) {
        baseInfo.setTree(true);
        for (HighLevelConnectInfo nextBranch : baseInfo.getNextGroups().getInfos()) {
            /* すでに向きが定義された枝は探索しない */
            if (nextBranch.getDirection() == null) {
                for (HighLevelConnectInfo nextNode : nextBranch.getNextGroups().getInfos()) {
                    /* 隣接する枝の、自身ではない方の節を探索していく */
                    if (baseInfo != nextNode) {
                        /* 枝の向きの始点としてbaseInfoを登録する */
                        nextBranch.setDirection(baseInfo);
                        /* 隣接する節が、まだ木として巡回されていなければ、間の枝を木の枝として登録する */
                        if (!nextNode.isTree()) {
                            nextBranch.setTree(true);
                            treeRecursiveSearch(nextNode);
                        }
                    }
                }
            }
        }
    }
}
