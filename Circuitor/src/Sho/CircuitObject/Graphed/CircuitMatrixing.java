package Sho.CircuitObject.Graphed;

import KUU.BaseComponent.BaseFrame;
import Sho.CircuitObject.Circuit.CircuitUnit;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.Matrix.IntegerMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * 接続行列、閉路行列、カットセット行列の作成を行うクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class CircuitMatrixing {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;

    private CircuitUnit circuitUnit;

    public CircuitMatrixing(BaseFrame frame) {
        this.frame = frame;
    }

    public boolean circuitMatrixing(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();

        /* 基本的な行列の生成 */
//        circuitUnit.setIncidenceMatrix(new IntegerMatrix());
        circuitUnit.setLoopMatrix(new IntegerMatrix());
//        circuitUnit.setCutsetMatrix(new IntegerMatrix());

//        createIncidenceMatrix();
        createLoopMatrix(panel);
//        createCutsetMatrix();

        if (circuitUnit.getLoopMatrix().getMatrix().isEmpty()) {
            System.out.println("電源となる電子部品が閉路に接続されていないため解析を進めることができません！");
            return false;
        }
        return true;
    }

    /**
     * 接続行列を作成する。
     */
//    private static void createIncidenceMatrix() {
//        boolean columnFlg = false;
//        for (HighLevelConnectInfo node : circuitUnit.getHighLevelConnectList().getNode()) {
//            /* 行の生成と、その行と対応する節のインデックスを登録する */
//            circuitUnit.getIncidenceMatrix().getMatrix().add(new ArrayList<>());
//            circuitUnit.getIncidenceMatrix().getRowRelatedIndex().add(node.getIndex());
//            for (int i = 0; i < circuitUnit.getHighLevelConnectList().getBranch().size(); i++) {
//                circuitUnit.getIncidenceMatrix().getMatrix().get(circuitUnit.getIncidenceMatrix().getRowRelatedIndex().indexOf(node.getIndex())).add(0);
//            }
//            for (HighLevelConnectInfo branch : circuitUnit.getHighLevelConnectList().getBranch()) {
//                /* 最初の一回だけ列と対応する枝のインデックスを登録する */
//                if (!columnFlg) {
//                    circuitUnit.getIncidenceMatrix().getColumnRelatedIndex().add(branch.getIndex());
//                }
//                /* 接続点が断線していたらパスする */
//                if (branch.getDirection() != null) {
//                    for (int index : branch.getNextGroups().getIndexs()) {
//                        if (node.getIndex() == index) {
//                            /* 自身が枝の始点であれば、1を登録する */
//                            if (node.getIndex() == branch.getDirection().getIndex()) {
//                                circuitUnit.getIncidenceMatrix().getMatrix().get(node.getIndex()).set(branch.getIndex(), 1);
//                            }
//                            /* 自身が枝の終点であれば、-1を登録する */
//                            else {
//                                circuitUnit.getIncidenceMatrix().getMatrix().get(node.getIndex()).set(branch.getIndex(), -1);
//                            }
//                        }
//                    }
//                }
//            }
//            columnFlg = true;
//        }
//    }

    /**
     * 基本閉路行列を作成する。
     */
    private void createLoopMatrix(UnitPanel panel) {
        circuitUnit = panel.getCircuitUnit();
        /* 基本閉路行列を作成する */
        for (HighLevelConnectInfo link : circuitUnit.getHighLevelConnectList().getBranch()) {
            /* 先に列の対応インデックスを登録する */
            circuitUnit.getLoopMatrix().getColumnRelatedIndex().add(link.getIndex());
            if (!link.isTree()) {
                /* 接続点が断線していたらパスする */
                if (link.getDirection() != null) {
                    /* 行列の行を生成すると共に列を同時に生成する */
                    circuitUnit.getLoopMatrix().getMatrix().add(new ArrayList<>());
                    circuitUnit.getLoopMatrix().getRowRelatedIndex().add(link.getIndex());
                    for (int i = 0; i < circuitUnit.getHighLevelConnectList().getBranch().size(); i++) {
                        circuitUnit.getLoopMatrix().getMatrix().get(circuitUnit.getLoopMatrix().getRowRelatedIndex().indexOf(link.getIndex())).add(0);
                    }
                    /* 閉路を構成する枝のインデックスリストを生成する */
                    ArrayList<Integer> loopList = new ArrayList<>();
                    loopList.add(link.getIndex());
                    if (loopMatrixRecursiveSearch(link, link.getDirection().getIndex(), loopList)) {
                        /* 閉路構成リストをたどり、それらの閉路の向きとの対応を取得する */
                        int direction = link.getDirection().getIndex();
                        HighLevelConnectInfo branch;
                        for (int index : loopList) {
                            branch = circuitUnit.getHighLevelConnectList().getBranch().get(index);
                            /* 始点と向きが一致したら1、そうでなければ-1を登録する */
                            if (direction == branch.getDirection().getIndex()) {
                                circuitUnit.getLoopMatrix().getMatrix().get(circuitUnit.getLoopMatrix().getRowRelatedIndex().indexOf(link.getIndex())).set(index, 1);
                            } else {
                                circuitUnit.getLoopMatrix().getMatrix().get(circuitUnit.getLoopMatrix().getRowRelatedIndex().indexOf(link.getIndex())).set(index, -1);
                            }
                            /* 始点じゃない方の節を始点に設定する */
                            for (HighLevelConnectInfo nextNode : branch.getNextGroups().getInfos()) {
                                if (nextNode.getIndex() != direction) {
                                    direction = nextNode.getIndex();
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("閉路行列の行生成に失敗しました。: " + link.getIndex() + "番のリンク\n");
                    }
                }
            }
        }

        /* 基本閉路の列をリストの昇順、枝の昇順に並べ替える */
        IntegerMatrix tmp = new IntegerMatrix();
        for (int y : circuitUnit.getLoopMatrix().getRowRelatedIndex()) {
            tmp.getMatrix().add(new ArrayList<>());
            tmp.getRowRelatedIndex().add(y);
        }
        /* リンクの情報を一時退避 */
        for (int x : circuitUnit.getLoopMatrix().getColumnRelatedIndex()) {
            if (!circuitUnit.getHighLevelConnectList().getBranch().get(x).isTree()) {
                for (int i = 0; i < circuitUnit.getLoopMatrix().getMatrix().size(); i++) {
                    tmp.getMatrix().get(i).add(circuitUnit.getLoopMatrix().getMatrix().get(i).get(x));
                }
                tmp.getColumnRelatedIndex().add(x);
            }
        }
        /* リンクの情報を削除 */
        for (int x : tmp.getColumnRelatedIndex()) {
            for (int i = 0; i < circuitUnit.getLoopMatrix().getMatrix().size(); i++) {
                circuitUnit.getLoopMatrix().getMatrix().get(i).remove(circuitUnit.getLoopMatrix().getColumnRelatedIndex().indexOf(x));
            }
            circuitUnit.getLoopMatrix().getColumnRelatedIndex().remove(circuitUnit.getLoopMatrix().getColumnRelatedIndex().indexOf(x));
        }
        /* 一時保存の方に本データを追加していき、一時保存を本データとして置き換える */
        for (int x = 0; x < circuitUnit.getLoopMatrix().getColumnRelatedIndex().size(); x++) {
            for (int y = 0; y < circuitUnit.getLoopMatrix().getRowRelatedIndex().size(); y++) {
                tmp.getMatrix().get(y).add(circuitUnit.getLoopMatrix().getMatrix().get(y).get(x));
            }
            tmp.getColumnRelatedIndex().add(circuitUnit.getLoopMatrix().getColumnRelatedIndex().get(x));
        }
        circuitUnit.setLoopMatrix(tmp);
    }

    /**
     * 探索元の枝を基準に、閉路構成枝を再帰的に探索する。
     *
     * @param baseInfo 探索元の枝の情報
     * @param direction 探索元の節のインデックス
     * @param loopList 閉路構成枝のインデックスリスト
     *
     */
    private boolean loopMatrixRecursiveSearch(HighLevelConnectInfo baseInfo, int direction, ArrayList<Integer> loopList) {
        for (HighLevelConnectInfo nextNode : baseInfo.getNextGroups().getInfos()) {
            /* 枝のもつ節のうち、探索元でない節を選択 */
            if (nextNode.getIndex() != direction) {
                for (HighLevelConnectInfo nextBranch : nextNode.getNextGroups().getInfos()) {
                    /* 探索元は除外する */
                    if (nextBranch != baseInfo) {
                        if (nextBranch.isTree()) {
                            /* 探索先の枝が閉路構成枝に既に登録されていたら探索しない */
                            boolean exist = false;
                            for (int index : loopList) {
                                if (nextBranch.getIndex() == index) {
                                    exist = true;
                                    break;
                                }
                            }
                            /* 登録されていなかったら枝を構成枝として登録して再帰的に探索する */
                            if (!exist) {
                                loopList.add(nextBranch.getIndex());
                                /* trueが帰ってきたらそのままtrueを伝播させる */
                                if (loopMatrixRecursiveSearch(nextBranch, nextNode.getIndex(), loopList)) {
                                    return true;
                                }
                                /* falseが帰ってきたら自身の登録を解除する */
                                else {
                                    loopList.remove(loopList.indexOf(nextBranch.getIndex()));
                                }
                            }
                        } else {
                            /* リストの最初(閉路の基準となるリンク)に戻ったら、trueを返して再帰処理を終了する */
                            if (nextBranch.getIndex() == loopList.get(0)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        /* 探索先で行き詰ったらfalseを返す */
        return false;
    }

    /**
     * 基本カットセット行列を作成する。
     */
//    private static void createCutsetMatrix() {
//        /* 本データで検査すると例外が発生するため、ダミーとして用意するリスト */
//        ArrayList<HighLevelConnectInfo> dummyList = new ArrayList<>();
//        for (HighLevelConnectInfo dummy : circuitUnit.getHighLevelConnectList().getBranch()) {
//            dummyList.add(dummy.clone());
//        }
//        /* 削除した枝などを一時退避させるためのリスト */
//        ArrayList<HighLevelConnectInfo> escapeList = new ArrayList<>();
//        /* 削除する枝の組み合わせを保持するリスト */
//        ArrayList<Boolean> deletePattern = new ArrayList<>();
//        /* 節と対応させるフラグリスト */
//        ArrayList<Boolean> flgList = new ArrayList<>();
//
//        /* 木の枝の数だけカットセットを求める */
//        for (HighLevelConnectInfo branch : circuitUnit.getHighLevelConnectList().getBranch()) {
//            /* 先に列の対応インデックスを登録する */
//            circuitUnit.getCutsetMatrix().getColumnRelatedIndex().add(branch.getIndex());
//            if (branch.isTree()) {
//                /* 行列の行を生成すると共に列を同時に生成する */
//                circuitUnit.getCutsetMatrix().getMatrix().add(new ArrayList<>());
//                circuitUnit.getCutsetMatrix().getRowRelatedIndex().add(branch.getIndex());
//                for (int i = 0; i < circuitUnit.getHighLevelConnectList().getBranch().size(); i++) {
//                    circuitUnit.getCutsetMatrix().getMatrix().get(circuitUnit.getCutsetMatrix().getRowRelatedIndex().indexOf(branch.getIndex())).add(0);
//                }
//                /* 組み合わせの初期化(最初だけtrueにしてisAllFalseにいきなり引っ掛からないようにしておく) */
//                deletePattern.clear();
//                for (int i = 0; i < circuitUnit.getLoopMatrix().getRowRelatedIndex().size(); i++) {
//                    if (i == 0) {
//                        deletePattern.add(true);
//                    }
//                    else {
//                        deletePattern.add(false);
//                    }
//                }
//                /* 組み合わせを変えて、全てのパターンが終わるか基本カットセットが見つかるまで探索する */
//                while (!isAllFalse(deletePattern)) {
//                    /* フラグの初期化 */
//                    flgList.clear();
//                    for (int i = 0; i < circuitUnit.getHighLevelConnectList().getNode().size(); i++) {
//                        flgList.add(false);
//                    }
//                    /* 退避リストを初期化 */
//                    escapeList.clear();
//                    /* 退避リストに現在の枝と削除パターンに該当するリンクを移してダミーリストから削除する */
//                    int branchIndex = -1;
//                    for (HighLevelConnectInfo dummy : dummyList) {
//                        if (dummy.getIndex() == branch.getIndex()) {
//                            branchIndex = dummyList.indexOf(dummy);
//                        }
//                    }
//                    /* 削除と同時に退避リストに登録する */
//                    escapeList.add(dummyList.remove(branchIndex));
//                    for (int i = 0; i < deletePattern.size(); i++) {
//                        if (deletePattern.get(i)) {
//                            /* 該当リンクのインデックスを取得 */
//                            int index = circuitUnit.getLoopMatrix().getRowRelatedIndex().get(i);
//                            for (HighLevelConnectInfo dummy : dummyList) {
//                                if (!dummy.isTree()) {
//                                    if (dummy.getIndex() == index) {
//                                        branchIndex = dummyList.indexOf(dummy);
//                                        break;
//                                    }
//                                }
//                            }
//                            escapeList.add(dummyList.remove(branchIndex));
//                        }
//                    }
//                    /* 再帰的に節を探索する */
//                    for (HighLevelConnectInfo node : circuitUnit.getHighLevelConnectList().getNode()) {
//                        cutsetMatrixRecursiveSearch(node, dummyList, flgList);
//                        break;
//                    }
//                    /* 退避リストの中身をダミーリストに戻す */
//                    for (HighLevelConnectInfo escape : escapeList) {
//                        dummyList.add(escape);
//                    }
//                    /* カットセットの条件を満たしているか判定する */
//                    if (!isAllTrue(flgList)) {
//                        boolean direction = flgList.get(branch.getDirection().getIndex());
//                        /* カットセット構成リストの向きと枝それぞれの向きとの対応を取得する */
//                        for (HighLevelConnectInfo list : escapeList) {
//                            /* 構成枝の向きが基準枝の向きと一致すれば1、そうでなければ-1を登録する */
//                            if (flgList.get(list.getDirection().getIndex()) == direction) {
//                                circuitUnit.getCutsetMatrix().getMatrix().get(circuitUnit.getCutsetMatrix().getRowRelatedIndex().indexOf(branch.getIndex())).set(list.getIndex(), 1);
//                            } else {
//                                circuitUnit.getCutsetMatrix().getMatrix().get(circuitUnit.getCutsetMatrix().getRowRelatedIndex().indexOf(branch.getIndex())).set(list.getIndex(), -1);
//                            }
//                        }
//                        break;
//                    }
//                    /* 削除パターンを更新する */
//                    boolean carry = true;
//                    for (int i = 0; i < deletePattern.size(); i++) {
//                        if (carry) {
//                            if (deletePattern.get(i)) {
//                                deletePattern.set(i, false);
//                                carry = true;
//                            } else {
//                                deletePattern.set(i, true);
//                                carry = false;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        /* 基本カットセットの列をリストの昇順、枝の昇順に並べ替える */
//        Matrix<Integer> tmp = new Matrix<>();
//        for (int y : circuitUnit.getCutsetMatrix().getRowRelatedIndex()) {
//            tmp.getMatrix().add(new ArrayList<>());
//            tmp.getRowRelatedIndex().add(y);
//        }
//        /* 枝の情報を一時退避 */
//        for (int x : circuitUnit.getCutsetMatrix().getColumnRelatedIndex()) {
//            if (circuitUnit.getHighLevelConnectList().getBranch().get(x).isTree()) {
//                for (int i = 0; i < circuitUnit.getCutsetMatrix().getMatrix().size(); i++) {
//                    tmp.getMatrix().get(i).add(circuitUnit.getCutsetMatrix().getMatrix().get(i).get(x));
//                }
//                tmp.getColumnRelatedIndex().add(x);
//            }
//        }
//        /* 枝の情報を削除 */
//        for (int x : tmp.getColumnRelatedIndex()) {
//            for (int i = 0; i < circuitUnit.getCutsetMatrix().getMatrix().size(); i++) {
//                circuitUnit.getCutsetMatrix().getMatrix().get(i).remove(circuitUnit.getCutsetMatrix().getColumnRelatedIndex().indexOf(x));
//            }
//            circuitUnit.getCutsetMatrix().getColumnRelatedIndex().remove(circuitUnit.getCutsetMatrix().getColumnRelatedIndex().indexOf(x));
//        }
//        /* 本データに一時保存データを追加していく */
//        for (int x = 0; x < tmp.getColumnRelatedIndex().size(); x++) {
//            for (int y = 0; y < tmp.getRowRelatedIndex().size(); y++) {
//                circuitUnit.getCutsetMatrix().getMatrix().get(y).add(tmp.getMatrix().get(y).get(x));
//            }
//            circuitUnit.getCutsetMatrix().getColumnRelatedIndex().add(tmp.getColumnRelatedIndex().get(x));
//        }
//    }

    /**
     * 渡されたBoolean型リストの中身がすべてfalseならばtrueを返す。
     *
     * @param lists Boolean型のリスト
     */
    private boolean isAllFalse(List<Boolean> lists) {
        boolean flg = true;
        for (boolean list : lists) {
            if (list) {
                flg = false;
                break;
            }
        }
        return flg;
    }

    /**
     * 渡されたBoolean型リストの中身がすべてtrueならばtrueを返す。
     *
     * @param lists Boolean型のリスト
     */
    private boolean isAllTrue(List<Boolean> lists) {
        boolean flg = true;
        for (boolean list : lists) {
            if (!list) {
                flg = false;
                break;
            }
        }
        return flg;
    }

    /**
     * 渡されたダミーの枝リストを再帰的に辿る。
     *
     * @param baseInfo 探索元の節の情報
     * @param branchList ダミーの枝リスト
     * @param flgList フラグリスト
     */
    private void cutsetMatrixRecursiveSearch(HighLevelConnectInfo baseInfo, ArrayList<HighLevelConnectInfo> branchList, ArrayList<Boolean> flgList) {
        flgList.set(baseInfo.getIndex(), true);
        for (HighLevelConnectInfo nextBranch : baseInfo.getNextGroups().getInfos()) {
            /* 隣接する枝がダミーリストにも存在する場合は探索する */
            for (HighLevelConnectInfo dummyBranch : branchList) {
                if (nextBranch.getIndex() == dummyBranch.getIndex()) {
                    /* 隣接する節がまだ巡回されていなければ再帰的に辿る */
                    for (HighLevelConnectInfo nextNode : nextBranch.getNextGroups().getInfos()) {
                        if (nextNode.getIndex() != baseInfo.getIndex()) {
                            if (!flgList.get(nextNode.getIndex())) {
                                cutsetMatrixRecursiveSearch(nextNode, branchList, flgList);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
