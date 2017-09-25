package Sho.CircuitObject.Execute;

import Master.ImageMaster.ImageMaster;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.CircuitUnit;
import Sho.CircuitObject.Circuit.DrawCood;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.Graphed.BranchNodeConnect;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteInfo;
import Sho.CircuitObject.Operate.OperateDetection_;
import Sho.CircuitObject.Operate.OperateOperate_;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;
import Sho.Matrix.IntegerMatrix;

import java.util.ArrayList;

import static Sho.Matrix.DoubleMatrix.*;

/**
 * 実行処理を管轄するクラス。
 * このクラスは実行中のウィンドウ１つにつき１インスタンスのみ生成可能。
 */
public class Execute extends Thread {
    /**
     * 電流のしきい値。しかし、他のしきい値としても利用される。
     */
    public final static double THRESHOLD_CURRENT = 1e-9;
    /** 大元のExecuteUnitPanelのアドレスを保持する */
    private ExecuteUnitPanel exePanel;
    /**
     * 実行を中止するための制御用フラグ。
     * trueならば再開、falseならば停止。
     */
    private boolean running;
    /**
     * 実行を完全に停止するための制御用フラグ。
     * trueになった瞬間、シミュレーションを完全停止させる。
     */
    private boolean runStop;

    /**
     * 実行用の電子部品グループ情報
     */
    private ArrayList<HighLevelExecuteGroup> executeGroups;

    /*****************************/
    /** 回路解析に用いる汎用行列 */
    /*****************************/
    /**
     * 閉路抵抗行列
     */
    private DoubleMatrix loopResistanceMatrix;
    /**
     * 閉路静電容量行列
     */
    private DoubleMatrix loopCapacitanceMatrix;
    /**
     * 抵抗行列
     */
    private DoubleMatrix resistance;
    /**
     * 静電容量行列
     */
    private DoubleMatrix capacitance;

    /*********************************/
    /** 回路解析に用いる汎用ベクトル */
    /*********************************/
    /**
     * 閉路電流ベクトル
     */
    private DoubleMatrix loopCurrent;
    /**
     * 閉路電圧ベクトル
     */
    private DoubleMatrix loopVoltage;
    /**
     * 閉路抵抗ベクトル
     */
    private DoubleMatrix loopResistance;
    /**
     * 閉路クーロン力ベクトル
     */
    private DoubleMatrix loopCoulomb;
    /**
     * 起電力ベクトル
     */
    private DoubleMatrix branchPower;
    /**
     * 枝電流ベクトル
     */
    private DoubleMatrix branchCurrent;
    /**
     * 枝電圧ベクトル
     */
    private DoubleMatrix branchVoltage;
    /**
     * 枝抵抗ベクトル
     */
    private DoubleMatrix branchResistance;
    /**
     * 枝クーロン力ベクトル
     */
    private DoubleMatrix branchCoulomb;
    /**
     * 枝最大蓄電圧ベクトル
     */
    private DoubleMatrix branchMaxVoltage;


    public Execute(ExecuteUnitPanel panel) {
        super();
        this.exePanel = panel;
        running = true;
        runStop = false;
    }

    /**
     * 実行専用マルチスレッドを開始する。
     */
    public void run() {
        try {
//            /* 閉路接続されているグループ情報を出力 */
//            for (HighLevelConnectInfo highLevelConnectInfo : exePanel.getCircuitUnit().getHighLevelConnectList().getNode()) {
//                System.out.println(highLevelConnectInfo);
//            }
//            for (HighLevelConnectInfo highLevelConnectInfo : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
//                System.out.println(highLevelConnectInfo);
//            }
//            System.out.println("LoopMatrix :\n");
//            System.out.print("  ");
//            for (int x : exePanel.getCircuitUnit().getLoopMatrix().getColumnRelatedIndex()) {
//                System.out.print(x + ",");
//            }
//            System.out.println();
//            for (ArrayList<Integer> y : exePanel.getCircuitUnit().getLoopMatrix().getMatrix()) {
//                System.out.print(exePanel.getCircuitUnit().getLoopMatrix().getRowRelatedIndex().get(exePanel.getCircuitUnit().getLoopMatrix().getMatrix().indexOf(y)));
//                System.out.println(y + "\n");
//            }
            /* フラグの初期設定 */
            running = true;
            runStop = false;
            exePanel.setVoltmeter(null);
            exePanel.setAmmeter(null);
            exePanel.getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getValueIndicateLabel().initFormattedValue();
            exePanel.getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getValueIndicateLabel().initFormattedValue();
            exePanel.getFrame().getBasePanel().getSubExecutePanel().getVoltagePanel().getGraphPanel().initValueAndGraph();
            exePanel.getFrame().getBasePanel().getSubExecutePanel().getCurrentPanel().getGraphPanel().initValueAndGraph();
            /* 実行用グループの生成 */
            createExecuteGroups();
            createDrawCood();
            /* 実行に必要な行列の生成 */
            createExecuteMatrices();
            /* プログラム制御の初期化 */
            exePanel.getFrame().getMasterTerminal().initSimulator();

            /* 初期解析 */
            analysis();
            while (!runStop) {
                try {
                    while (!running && !runStop) {
                        sleep(10);
                    }
                    sleep(exePanel.getExeSpeed());
                    analysis();
                    /* 点の移動 */
                    for (HighLevelConnectInfo info : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
                        if (info.getRole() == HighLevelConnectGroup.BRANCH) {
                            if (info.getHighLevelExecuteInfo().getDrawCood().getPointCount() >= info.getHighLevelExecuteInfo().getDrawCood().getPointMaxCount()) {
                                info.getHighLevelExecuteInfo().moveDrawCood();
                                info.getHighLevelExecuteInfo().getDrawCood().setPointCount(0);
                            } else {
                                info.getHighLevelExecuteInfo().getDrawCood().setPointCount(info.getHighLevelExecuteInfo().getDrawCood().getPointCount() + 1);
                            }
                        }
                    }
                    exePanel.goOnInterrupt();
                    exePanel.reDirection();
                } catch (InterruptedException e) {}
            }
        } finally {
            /* 終了処理 */
            for (HighLevelExecuteGroup group : executeGroups) {
                group.getBehavior().disposeBehavior();
            }
            exePanel.getFrame().getBasePanel().autoCallStopExecute();
            exePanel.setExecutor(null);
            System.out.println("実行専用スレッドが終了しました。");
        }
    }

    /* 実行用のグループリストを作成する */
    private void createExecuteGroups() {
        /* 枝の描画情報の生成 */
        for (HighLevelConnectInfo branch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
            branch.setHighLevelExecuteInfo(new HighLevelExecuteInfo());
        }
        /* 電子部品のグループ生成 */
        executeGroups = new ArrayList<>();
        HighLevelExecuteGroup executeGroup;
        for (HighLevelConnectInfo center : exePanel.getCircuitUnit().getHighLevelConnectList().getNode()) {
            if (center.getRole() == HighLevelConnectGroup.CENTER_NODE) {
                executeGroup = new HighLevelExecuteGroup(exePanel, exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(center.getAbcos().get(0).getHeight()).get(center.getAbcos().get(0).getWidth()).getElecomInfo(), center.getAbcos().get(0));
                for (HighLevelConnectInfo branch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
                    if (branch.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                        if (center.getNextGroups().getInfos().contains(branch)) {
                            executeGroup.getExecuteInfos().add(branch);
                            /* 座標向き共に同じ入力枝、出力枝が存在するならば、それもグループに含める */
                            for (HighLevelConnectInfo altBranch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
                                if (altBranch.getRole() == HighLevelConnectGroup.IN_BRANCH || altBranch.getRole() == HighLevelConnectGroup.OUT_BRANCH) {
                                    if (altBranch.getConnectDirection() == branch.getConnectDirection() && altBranch.getAbcos().get(0).equals(branch.getAbcos().get(0))) {
                                        executeGroup.getExecuteInfos().add(altBranch);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                executeGroup.setCorrespondGroup();
                executeGroups.add(executeGroup);
            }
        }
    }

    /* グループ情報を基に描画座標を決定する */
    private void createDrawCood() {
        CircuitUnit u = exePanel.getCircuitUnit();
        DrawCood drawCood;
        ArrayList<IntegerDimension> coods;
        int startY,startX;
        ElecomInfo e;

        for (HighLevelConnectInfo branch : u.getHighLevelConnectList().getBranch()) {
            if (branch.getRole() == HighLevelConnectGroup.BRANCH) {
                /* 後の処理が楽になるように用意する */
                int terminal[] = new int[2];
                /* 整列する */
                coods = new ArrayList<>();
                exePanel.getOperateOperate().getAlignmentList(exePanel, branch, exePanel.getOperateOperate().getRemoveTerminal(exePanel, branch, branch.getAbcos().get(0)), coods);
                if (branch.isVirtual()) {
                    /* 仮想枝の場合、始点と枝の根元が同じ座標でなければ逆向きに整列する */
                    if (!coods.get(0).equals(branch.getNextGroups().getAbcos().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())))) {
                        coods = getReverseList(coods);
                    }
                } else {
                    /* 通常枝の場合、枝の根元から見た方向に始点がなければ逆向きに整列する */
                    startY = BranchNodeConnect.nextCoordinateY(branch.getNextGroups().getAbcos().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())).getHeight(),
                            branch.getNextGroups().getDirections().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())));
                    startX = BranchNodeConnect.nextCoordinateX(branch.getNextGroups().getAbcos().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())).getWidth(),
                            branch.getNextGroups().getDirections().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())));
                    if (!coods.get(0).equals(startY, startX)) {
                        coods = getReverseList(coods);
                    }
                    /* 隣接する節がNODEであれば、リストに追加する */
                    startY = branch.getNextGroups().getAbcos().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())).getHeight();
                    startX = branch.getNextGroups().getAbcos().get(branch.getNextGroups().getIndexs().indexOf(branch.getDirection().getIndex())).getWidth();
                    for (int i = 0; i < branch.getNextGroups().getAbcos().size(); i++) {
                        if (branch.getNextGroups().getAbcos().get(i).equals(startY, startX)) {
                            coods.add(0, branch.getNextGroups().getAbcos().get(i));
                        } else {
                            coods.add(branch.getNextGroups().getAbcos().get(i));
                        }
                    }
                }
                /* 描画点の決定 */
                int direction[][] = new int[coods.size()][2];
                for (int i = 0; i < coods.size(); i++) {
                    if (i > 0) {
                        direction[i][0] = OperateDetection_.getNearY(coods.get(i).getHeight(), coods.get(i - 1).getHeight());
                        if (direction[i][0] == -1) {
                            direction[i][0] = OperateDetection_.getNearX(coods.get(i).getWidth(), coods.get(i - 1).getWidth());
                        }
                    } else {
                        /* 始端の時はNODEかどうかチェックし、NODEなら、-1とする */
                        if (!u.getCircuitBlock().getMatrix().get(coods.get(i).getHeight()).get(coods.get(i).getWidth()).getElecomInfo().isBranch()) {
                            direction[i][0] = -1;
                        } else {
                            direction[i][0] = terminal[0];
                        }
                    }
                    if (i < coods.size() - 1) {
                        direction[i][1] = OperateDetection_.getNearY(coods.get(i).getHeight(), coods.get(i + 1).getHeight());
                        if (direction[i][1] == -1) {
                            direction[i][1] = OperateDetection_.getNearX(coods.get(i).getWidth(), coods.get(i + 1).getWidth());
                        }
                    } else {
                        /* 終端の時はNODEかどうかチェックし、NODEなら、-1とする */
                        if (!u.getCircuitBlock().getMatrix().get(coods.get(i).getHeight()).get(coods.get(i).getWidth()).getElecomInfo().isBranch()) {
                            direction[i][1] = -1;
                        } else {
                            direction[i][1] = terminal[1];
                        }
                    }
                }
                /* 描画点格納 */
                drawCood = new DrawCood();
                for (int i = 0; i < coods.size(); i++) {
                    /* WIRE_4は例外で、３点しか描画位置を設けない */
                    e = u.getCircuitBlock().getMatrix().get(coods.get(i).getHeight()).get(coods.get(i).getWidth()).getElecomInfo();
                    if (e.getPartsVarieties() == PartsVarieties.WIRE && e.getPartsStandards() == PartsStandards._4) {
                        if (i==0) {
                            drawCood.getPoints().add(getTerminalPoint(direction[i][0]));
                            drawCood.getCoods().add(coods.get(i));
                        }
                        drawCood.getPoints().add(getTerminalPoint(direction[i][1]));
                        drawCood.getCoods().add(coods.get(i));
                        continue;
                    }
                    /* 入力方向が-1の場合はパス */
                    if (direction[i][0] != -1) {
                        if (i==0) {
                            drawCood.getPoints().add(getTerminalPoint(direction[i][0]));
                            drawCood.getCoods().add(coods.get(i));
                        }
                    }
                    /* CENTER */
                    drawCood.getPoints().add(DrawCood.DrawPoint.CENTER);
                    drawCood.getCoods().add(coods.get(i));
                    /* 出力方向が-1の場合はパス */
                    if (direction[i][1] != -1) {
                        drawCood.getPoints().add(getTerminalPoint(direction[i][1]));
                        drawCood.getCoods().add(coods.get(i));
                    }
                }
                drawCood.setBasePoint(drawCood.getCoods().size() % 3);
                branch.getHighLevelExecuteInfo().setDrawCood(drawCood);
            }
        }
    }

    /* その位置の描画点を返す。 */
    private DrawCood.DrawPoint getTerminalPoint(int terminal) {
        switch (terminal) {
            case 0:
                return DrawCood.DrawPoint.UP;
            case 1:
                return DrawCood.DrawPoint.RIGHT;
            case 2:
                return DrawCood.DrawPoint.DOWN;
            case 3:
                return DrawCood.DrawPoint.LEFT;
            default:
                return DrawCood.DrawPoint.CENTER;
        }
    }

    /* 座標リストの中身を逆方向から整列したものを返す */
    private ArrayList<IntegerDimension> getReverseList(ArrayList<IntegerDimension> list) {
        if (list.size() < 2) {
            return list;
        }
        ArrayList<IntegerDimension> reverse = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            reverse.add(list.get(i));
        }
        return reverse;
    }

    /**
     * 実行に必要な行列を生成する。
     * 中身は補完されないが、とりあえずサイズだけ生成する。
     */
    private void createExecuteMatrices() {
        DoubleMatrix mat;
        IntegerMatrix loop;
        loop = exePanel.getCircuitUnit().getLoopMatrix();
        /** 抵抗行列と静電容量行列 */
        for (int k = 0; k < 2; k++) {
            mat = new DoubleMatrix();
            for (int i = 0; i < loop.getColumnRelatedIndex().size(); i++) {
                mat.getColumnRelatedIndex().add(loop.getColumnRelatedIndex().get(i));
                mat.getRowRelatedIndex().add(loop.getColumnRelatedIndex().get(i));
                mat.getMatrix().add(new ArrayList<>());
                for (int j = 0; j < loop.getColumnRelatedIndex().size(); j++) {
                    mat.getMatrix().get(i).add(j, 0.0);
                }
            }
            if (k == 0) {
                resistance = mat;
            } else {
                capacitance = mat;
            }
        }
        /** 閉路電流ベクトルと閉路クーロンベクトルと閉路抵抗ベクトル */
        /* 閉路電圧ベクトルは行列の乗算によって生成されるため生成しない */
        for (int k=0;k<3;k++) {
            mat = new DoubleMatrix();
            mat.getRowRelatedIndex().add(-1);
            mat.getMatrix().add(new ArrayList<>());
            for (int i = 0; i < loop.getRowRelatedIndex().size(); i++) {
                mat.getColumnRelatedIndex().add(loop.getRowRelatedIndex().get(i));
                mat.getMatrix().get(0).add(i, 0.0);
            }
            if (k == 0) {
                loopCurrent = mat;
            } else if (k==1) {
                loopCoulomb = mat;
            }else {
                loopResistance = mat;
            }
        }
        /** 起電力ベクトル、枝電流ベクトル、枝電圧ベクトル、枝抵抗ベクトル、枝クーロンベクトル、枝最大蓄電圧ベクトル */
        for (int k=0;k<6;k++) {
            mat = new DoubleMatrix();
            mat.getRowRelatedIndex().add(-1);
            mat.getMatrix().add(new ArrayList<>());
            for (int i = 0; i < loop.getColumnRelatedIndex().size(); i++) {
                mat.getColumnRelatedIndex().add(loop.getColumnRelatedIndex().get(i));
                mat.getMatrix().get(0).add(i, 0.0);
            }
            if (k==0) {
                branchPower = mat;
            }else if (k==1) {
                branchCurrent = mat;
            }else if (k==2){
                branchVoltage = mat;
            }else if (k==3){
                branchResistance = mat;
            }else if (k==4){
                branchCoulomb = mat;
            }else {
                branchMaxVoltage = mat;
            }
        }
    }

    /**
     * 与えられた回路情報から連立方程式を数段構えで解き、各々の電子部品の振る舞いを抵抗値という形に反映させる。
     * １．抵抗値を考慮し連立方程式を解く。
     * ２．電子部品各々に流れる電流の向きを保持し、その電流に対する振る舞いを行う。
     * ３．抵抗値を考慮し連立方程式を解く。
     * ４．電子部品各々に流れる電流の向きを保持し、２の時の向きと比較し、一致しなければ極大抵抗とみなす。
     * ５．抵抗値を考慮し連立方程式を解く。
     * ６．閉路電流に閾値を設け、各ベクトルを求める。
     * ７．電子部品各々に流れる電流に対する振る舞いを行う。
     */
    private void analysis() {
        getPowerVector();
        getBranchCurrentWithEquation();
        for (HighLevelExecuteGroup group : executeGroups) {
            group.getBehavior().preBehavior(true);
        }
        getBranchCurrentWithEquation();
        for (HighLevelExecuteGroup group : executeGroups) {
            group.getBehavior().preBehavior(false);
        }
        getBranchCurrentWithEquation();
        getVectors();
        for (HighLevelExecuteGroup group : executeGroups) {
            group.getBehavior().behavior();
        }
    }

    /**
     * 起電力ベクトルを求める。
     * 最初の一度しか呼び出さない。
     */
    private void getPowerVector() {
        int index;
        /* 全ての枝に存在する現在蓄電圧を起電力ベクトルに格納する */
        /* 入力枝、出力枝を有していても、それは起電力を有するものではないので、ここではパスする */
        for (HighLevelConnectInfo branch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
            if (branch.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                if (/*branch.getGroupVarieties() == PartsVarieties.CAPACITANCE || */branch.getGroupVarieties() == PartsVarieties.POWER) {
                    index = branchPower.getColumnRelatedIndex().indexOf(branch.getIndex());
                    /* その枝の根元が中心節なら電圧はそのまま、そうでなければ電圧は-1倍とする */
                    if (branch.getDirection().getRole() == HighLevelConnectGroup.CENTER_NODE) {
                        branchPower.getMatrix().get(0).set(index, branch.getHighLevelExecuteInfo().getPotential());
                    } else {
                        branchPower.getMatrix().get(0).set(index, -branch.getHighLevelExecuteInfo().getPotential());
                    }
                }
            }
        }
    }

    /**
     * 抵抗値を考慮し連立方程式を使って閉路方程式を解き、線形結合を行って枝電流を求める。
     */
    private void getBranchCurrentWithEquation() {
        int index;
        double tmp;
        /* 全ての枝に存在する抵抗値を抵抗行列に格納する */
        for (HighLevelConnectInfo branch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
            index = resistance.getRowRelatedIndex().indexOf(branch.getIndex());
            /* 何もない枝も極小抵抗として扱う */
            if (branch.getHighLevelExecuteInfo().getResistance() < MINVALUE) {
                resistance.getMatrix().get(index).set(index, MINVALUE);
            } else {
                resistance.getMatrix().get(index).set(index, branch.getHighLevelExecuteInfo().getResistance());
            }
        }
        /* 閉路方程式の解を求め、各ベクトルを求める */
        loopResistanceMatrix = exePanel.getCircuitUnit().getLoopMatrix().getMul(resistance.getMul(exePanel.getCircuitUnit().getLoopMatrix().getTurn()));
        loopVoltage = exePanel.getCircuitUnit().getLoopMatrix().getMul(branchPower.getTurn()).getTurn();
        loopCurrent.setMatrix(DoubleMatrix.getGaussEquation(loopResistanceMatrix.getArrayMatrix(), loopVoltage.getArrayVector()));
        /* 枝電流：閉路行列の１の部分の閉路電流を線形結合した値 */
        for (int i = 0; i < branchCurrent.getColumnRelatedIndex().size(); i++) {
            tmp = 0;
            for (int j = 0; j < exePanel.getCircuitUnit().getLoopMatrix().getRowRelatedIndex().size(); j++) {
                if (exePanel.getCircuitUnit().getLoopMatrix().getMatrix().get(j).get(i) == 1) {
                    tmp += loopCurrent.getMatrix().get(0).get(j);
                }
            }
            branchCurrent.getMatrix().get(0).set(i, tmp);
        }
    }

    /**
     * 各ベクトルを求める。
     * この時、閉路電流に閾値を設ける。
     * これにより、電流が孤立したように見える現象を防ぐ。
     */
    private void getVectors() {
        double tmp;
        int index;
        ElecomInfo capaE;
        /* 計測器のみ、生の電流値を別個に格納しておく */
        for (HighLevelConnectInfo info : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
            index = branchCurrent.getColumnRelatedIndex().indexOf(info.getIndex());
            if (info.getGroupVarieties() == PartsVarieties.MEASURE) {
                capaE = exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getElecomInfo();
                if (capaE.getLinkedTerminal().get(0).getReco().equals(exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getCircuitInfo().getReco())) {
                    if (capaE.getLinkedTerminal().get(0).getTerminalDirection()[(ImageMaster.getIntFromPartsDirection(capaE.getPartsDirections()) + 3) % 4] == TerminalDirection.ANODE) {
                        /* 枝電流 */
                        info.getHighLevelExecuteInfo().setRealCurrent(branchCurrent.getMatrix().get(0).get(index));
                    }
                }
            }
        }
        /* 閉路電流：しきい値補正を行う */
        for (int i=0;i<loopCurrent.getColumnRelatedIndex().size(); i++) {
            if (Math.abs(loopCurrent.getMatrix().get(0).get(i)) < THRESHOLD_CURRENT) {
                loopCurrent.getMatrix().get(0).set(i, 0.0);
            }
        }
        /* 閉路抵抗：閉路起電力を閉路電流で割った値 */
        for (int i = 0; i <loopResistance.getColumnRelatedIndex().size(); i++) {
            if (Math.abs(loopCurrent.getMatrix().get(0).get(i)) < MINVALUE) {
                loopResistance.getMatrix().get(0).set(i, MAXVALUE);
            } else {
                loopResistance.getMatrix().get(0).set(i, Math.abs(loopVoltage.getMatrix().get(0).get(i)) / Math.abs(loopCurrent.getMatrix().get(0).get(i)));
            }
        }
        /* 枝電流：閉路行列の１の部分の閉路電流を線形結合した値 */
        for (int i = 0; i < branchCurrent.getColumnRelatedIndex().size(); i++) {
            tmp = 0;
            for (int j = 0; j < exePanel.getCircuitUnit().getLoopMatrix().getRowRelatedIndex().size(); j++) {
                if (exePanel.getCircuitUnit().getLoopMatrix().getMatrix().get(j).get(i) == 1) {
                    tmp += loopCurrent.getMatrix().get(0).get(j);
                }
            }
            if (Math.abs(tmp) < THRESHOLD_CURRENT) {
                branchCurrent.getMatrix().get(0).set(i, 0.0);
            } else {
                branchCurrent.getMatrix().get(0).set(i, tmp);
            }
        }
        /* 枝抵抗：閉路行列の１の部分と対応する閉路抵抗の逆数の合計の逆数 */
        for (int i = 0; i < branchResistance.getColumnRelatedIndex().size(); i++) {
            tmp = 0;
            for (int j = 0; j < exePanel.getCircuitUnit().getLoopMatrix().getRowRelatedIndex().size(); j++) {
                if (exePanel.getCircuitUnit().getLoopMatrix().getMatrix().get(j).get(i) == 1) {
                    if (loopResistance.getMatrix().get(0).get(j) < MINVALUE) {
                        loopResistance.getMatrix().get(0).set(j, MINVALUE);
                    }
                    tmp += 1 / loopResistance.getMatrix().get(0).get(j);
                }
            }
            if (tmp < MINVALUE) {
                tmp = MINVALUE;
            }
            branchResistance.getMatrix().get(0).set(i, 1 / tmp);
        }
        /* 枝電圧：枝電流と枝抵抗の積 */
        for (int i = 0; i < branchVoltage.getColumnRelatedIndex().size(); i++) {
            branchVoltage.getMatrix().get(0).set(i, branchCurrent.getMatrix().get(0).get(i) * branchResistance.getMatrix().get(0).get(i));
        }
//        /* キャパシタの静電容量を静電容量行列に格納する。しかし、これは定数のため閉路静電容量行列まで作成する。なお、この操作は一度しか行わない。 */
//        if (loopCapacitanceMatrix == null) {
//            for (HighLevelConnectInfo branch : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
//                index = capacitance.getRowRelatedIndex().indexOf(branch.getIndex());
//                if (branch.getGroupVarieties() == PartsVarieties.CAPACITANCE) {
//                    /* その枝がアノード側の場合のみキャパシタンスを登録する */
//                    capaE = exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(branch.getAbcos().get(0).getHeight()).get(branch.getAbcos().get(0).getWidth()).getElecomInfo();
//                    if (capaE.getLinkedTerminal().get(0).getReco().equals(exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(branch.getAbcos().get(0).getHeight()).get(branch.getAbcos().get(0).getWidth()).getCircuitInfo().getReco())) {
//                        if (capaE.getLinkedTerminal().get(0).getTerminalDirection()[(exePanel.getOperateOperate().getIntForDirection(capaE.getPartsDirections()) + 3) % 4] == TerminalDirection.ANODE) {
//                            capacitance.getMatrix().get(index).set(index, branch.getHighLevelExecuteInfo().getCapacitance());
//                        }
//                    }
//                }
//            }
//            /* 連立方程式を解くための閉路静電容量の係数行列を作成する */
//            loopCapacitanceMatrix = exePanel.getCircuitUnit().getLoopMatrix().getReciprocalMul(capacitance.getMul(exePanel.getCircuitUnit().getLoopMatrix().getTurn()));
//            /* 閉路クーロン力：閉路静電容量行列と起電力ベクトルの連立方程式を解いて求める */
//            loopCoulomb.setMatrix(DoubleMatrix.getGaussEquation(loopCapacitanceMatrix.getArrayMatrix(), loopVoltage.getArrayVector()));
//            /* 枝クーロン力：閉路行列の１の部分と対応する閉路クーロン力の線形結合を行う */
//            for (int i = 0; i < branchCoulomb.getColumnRelatedIndex().size(); i++) {
//                tmp = 0;
//                for (int j = 0; j < exePanel.getCircuitUnit().getLoopMatrix().getRowRelatedIndex().size(); j++) {
//                    if (exePanel.getCircuitUnit().getLoopMatrix().getMatrix().get(j).get(i) == 1) {
//                        tmp += loopCoulomb.getMatrix().get(0).get(j);
//                    }
//                }
//                branchCoulomb.getMatrix().get(0).set(i, tmp);
//            }
//            /* 最大蓄電圧：枝クーロン力を枝静電容量で割ったもの。ただし、キャパシタ以外は何もしない */
//            for (HighLevelConnectInfo info : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
//                if (info.getGroupVarieties() == PartsVarieties.CAPACITANCE) {
//                    index = branchCurrent.getColumnRelatedIndex().indexOf(info.getIndex());
//                    capaE = exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getElecomInfo();
//                    if (capaE.getLinkedTerminal().get(0).getReco().equals(exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getCircuitInfo().getReco())) {
//                        if (capaE.getLinkedTerminal().get(0).getTerminalDirection()[(exePanel.getOperateOperate().getIntForDirection(capaE.getPartsDirections()) + 3) % 4] == TerminalDirection.ANODE) {
//                            info.getHighLevelExecuteInfo().setMaxPotential(Math.abs(branchCoulomb.getMatrix().get(0).get(index) / info.getHighLevelExecuteInfo().getCapacitance()));
//                        }
//                    }
//                }
//            }
//        }
        /* それぞれの枝に、電流、電圧、抵抗ベクトルの値を格納する */
        for (HighLevelConnectInfo info : exePanel.getCircuitUnit().getHighLevelConnectList().getBranch()) {
            index = branchCurrent.getColumnRelatedIndex().indexOf(info.getIndex());
            /* 枝電流 */
            info.getHighLevelExecuteInfo().setCurrent(branchCurrent.getMatrix().get(0).get(index));
//            if (info.getGroupVarieties() == PartsVarieties.CAPACITANCE) {
//                capaE = exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getElecomInfo();
//                if (capaE.getLinkedTerminal().get(0).getReco().equals(exePanel.getCircuitUnit().getCircuitBlock().getMatrix().get(info.getAbcos().get(0).getHeight()).get(info.getAbcos().get(0).getWidth()).getCircuitInfo().getReco())) {
//                    if (capaE.getLinkedTerminal().get(0).getTerminalDirection()[(exePanel.getOperateOperate().getIntForDirection(capaE.getPartsDirections()) + 3) % 4] == TerminalDirection.ANODE) {
//                        /* 枝起電力 */
//                        info.getHighLevelExecuteInfo().setVoltage(branchVoltage.getMatrix().get(0).get(index));
//                        /* 枝抵抗 */
//                        info.getHighLevelExecuteInfo().setResistance(branchResistance.getMatrix().get(0).get(index));
//                    }
//                }
//            }
        }
    }

    /**
     * running
     */
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * runStop
     */
    public boolean isRunStop() {
        return runStop;
    }

    public void setRunStop(boolean runStop) {
        this.runStop = runStop;
    }

    public ArrayList<HighLevelExecuteGroup> getExecuteGroups() {
        return executeGroups;
    }

    public DoubleMatrix getLoopResistanceMatrix() {
        return loopResistanceMatrix;
    }

    public DoubleMatrix getLoopCapacitanceMatrix() {
        return loopCapacitanceMatrix;
    }

    public DoubleMatrix getResistance() {
        return resistance;
    }

    public DoubleMatrix getCapacitance() {
        return capacitance;
    }

    public DoubleMatrix getLoopCurrent() {
        return loopCurrent;
    }

    public DoubleMatrix getLoopVoltage() {
        return loopVoltage;
    }

    public DoubleMatrix getLoopResistance() {
        return loopResistance;
    }

    public DoubleMatrix getLoopCoulomb() {
        return loopCoulomb;
    }

    public DoubleMatrix getBranchPower() {
        return branchPower;
    }

    public DoubleMatrix getBranchCurrent() {
        return branchCurrent;
    }

    public DoubleMatrix getBranchVoltage() {
        return branchVoltage;
    }

    public DoubleMatrix getBranchResistance() {
        return branchResistance;
    }

    public DoubleMatrix getBranchCoulomb() {
        return branchCoulomb;
    }

    public DoubleMatrix getBranchMaxVoltage() {
        return branchMaxVoltage;
    }
}
