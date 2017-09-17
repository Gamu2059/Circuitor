package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
//import ProcessTerminal.MasterTerminal;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;

import java.util.ArrayList;
import java.util.Arrays;

import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * マイコンの振る舞いを定義するクラス。
 */
public class PICBehavior_ extends ICBehavior_ {
    public PICBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        /* PICにおいては、TERMINAL_BRANCHはすべて使わないので、極大抵抗を埋め込んでおく */
        for (HighLevelConnectInfo terminal : groups) {
            if (terminal.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                terminal.getHighLevelExecuteInfo().setResistance(MAXVALUE);
            }
        }
        boolean flg = false;
        boolean powerFlg, gndFlg;
        /* ピンが全て入力の場合、電源ピンのTERMINAL_BRANCHだけ抵抗を開放する */
        for (int i=0;i<getExePanel().getFrame().getBasePanel().getMiconPanel().getMiconPin().length;i++) {
            if (i != 4 && i != 13) {
                if (getExePanel().getFrame().getBasePanel().getMiconPanel().getMiconPin()[i] == TerminalDirection.OUT) {
                    flg = true;
                    break;
                }
            }
        }
        powerFlg = flg;
        flg = false;
        /* ピンが全て出力の場合、接地ピンのTERMINAL_BRANCHだけ抵抗を開放する */
        for (int i=0;i<getExePanel().getFrame().getBasePanel().getMiconPanel().getMiconPin().length;i++) {
            if (i != 4 && i != 13) {
                if (getExePanel().getFrame().getBasePanel().getMiconPanel().getMiconPin()[i] == TerminalDirection.IN) {
                    flg = true;
                    break;
                }
            }
        }
        gndFlg = flg;
        /* 0番は電源、1番は接地とし、それ以外の1番から18番までの端子は3番から17番までに順に定義する */
        setInfos(new ArrayList<>());
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.POWER) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (powerFlg && connectInfo.getRole() == HighLevelConnectGroup.OUT_BRANCH || !powerFlg && connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                                if (connectInfo.getConnectDirection() == j) {
                                    getInfos().set(0, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                    if (!powerFlg) {
                                        connectInfo.getHighLevelExecuteInfo().setResistance(0);
                                    }
                                    flg = true;
                                    break;
                                }
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
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.GND) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (gndFlg && connectInfo.getRole() == HighLevelConnectGroup.IN_BRANCH || !gndFlg && connectInfo.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                                if (connectInfo.getConnectDirection() == j) {
                                    getInfos().set(1, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                    if (!gndFlg) {
                                        connectInfo.getHighLevelExecuteInfo().setResistance(0);
                                    }
                                    flg = true;
                                    break;
                                }
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
        addWithDirection(groups, abco);
        setInputList(new boolean[getElecomInfo().getLinkedTerminal().size()]);
        setOutputList(new boolean[getElecomInfo().getLinkedTerminal().size()]);
    }

    @Override
    public void preBehavior(boolean isFirst) {
        super.preBehavior(isFirst);
    }

    @Override
    public void behavior() {
        super.behavior();
        /* 入力を受け付ける */
        super.setInput();
        /* それぞれの振る舞いを考慮して出力リストを作成する */
        setOutputList(getExePanel().getFrame().getMasterTerminal().bootProgram(getInputList()));
        super.setOutput();
        /* もしエラーがあれば、実行を中断する */
        if (getExePanel().getFrame().getMasterTerminal().isError()) {
            getExePanel().getExecutor().setRunStop(true);
        }
    }

    @Override
    public void disposeBehavior() {

    }

    /**
     * 与えられた向きに応じて登録を行う
     * 電源の座標を基準にしているため、電源が端子接続されていない場合は、こちらも登録できない
     */
    private void addWithDirection(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        int y, x, cnt = 2;
//        /* 電源が断線している場合は、適当に登録して極大抵抗として扱う */
//        if (getInfos().get(0) == null || getInfos().get(1) == null) {
//            for (int i=0;i<groups.size();i++) {
//                if (groups.get(i).getRole() != HighLevelConnectGroup.TERMINAL_BRANCH) {
//                    groups.get(i).getHighLevelExecuteInfo().setResistance(DoubleMatrix.MAXVALUE);
//                }
//            }
//            return;
//        }
        switch (getElecomInfo().getPartsDirections()) {
            case UP:
                /* 電源の左４マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 4;
                for (; cnt < 6; cnt++, x++) {
                    add(y, x, cnt, groups, abco, 2);
                }
                /* 電源の右１マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 1;
                for (; cnt < 10; cnt++, x++) {
                    add(y, x, cnt, groups, abco, 2);
                }
                /* 電源の上側、右４マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 4;
                for (; cnt < 14; cnt++, x--) {
                    add(y, x, cnt, groups, abco, 0);
                }
                /* 電源の上側、左１マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 1;
                for (; cnt < 18; cnt++, x--) {
                    add(y, x, cnt, groups, abco, 0);
                }
                break;
            case RIGHT:
                /* 電源の上４マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 4;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 2;
                for (; cnt < 6; cnt++, y++) {
                    add(y, x, cnt, groups, abco, 3);
                }
                /* 電源の下１マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 2;
                for (; cnt < 10; cnt++, y++) {
                    add(y, x, cnt, groups, abco, 3);
                }
                /* 電源の右側、下４マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 4;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 14; cnt++, y--) {
                    add(y, x, cnt, groups, abco, 1);
                }
                /* 電源の右側、上１マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 18; cnt++, y--) {
                    add(y, x, cnt, groups, abco, 1);
                }
                break;
            case DOWN:
                /* 電源の右４マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 4;
                for (; cnt < 6; cnt++, x--) {
                    add(y, x, cnt, groups, abco, 0);
                }
                /* 電源の左１マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 1;
                for (; cnt < 10; cnt++, x--) {
                    add(y, x, cnt, groups, abco, 0);
                }
                /* 電源の下側、左４マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 4;
                for (; cnt < 14; cnt++, x++) {
                    add(y, x, cnt, groups, abco, 2);
                }
                /* 電源の下側、右１マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 1;
                for (; cnt < 18; cnt++, x++) {
                    add(y, x, cnt, groups, abco, 2);
                }
                break;
            case LEFT:
                /* 電源の下４マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 4;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 2;
                for (; cnt < 6; cnt++, y--) {
                    add(y, x, cnt, groups, abco, 1);
                }
                /* 電源の上１マスからスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 2;
                for (; cnt < 10; cnt++, y--) {
                    add(y, x, cnt, groups, abco, 1);
                }
                /* 電源の左側、上４マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 4;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 14; cnt++, y++) {
                    add(y, x, cnt, groups, abco, 3);
                }
                /* 電源の左側、下１マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 18; cnt++, y++) {
                    add(y, x, cnt, groups, abco, 3);
                }
                break;
        }
    }
}
