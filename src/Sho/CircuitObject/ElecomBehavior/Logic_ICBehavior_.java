package Sho.CircuitObject.ElecomBehavior;

import Master.ImageMaster.PartsStates;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.IntegerDimension.IntegerDimension;
import Sho.Matrix.DoubleMatrix;

import java.util.ArrayList;

import static Master.ImageMaster.PartsStandards.*;
import static Sho.Matrix.DoubleMatrix.MAXVALUE;

/**
 * 論理ＩＣの振る舞いを定義するクラス。
 */
public class Logic_ICBehavior_ extends ICBehavior_ {
    public Logic_ICBehavior_(ExecuteUnitPanel exePanel, ElecomInfo info) {
        super(exePanel, info);
    }

    @Override
    public void setCorrespondGroup(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        boolean flg;
        /* 0番は電源、1番は接地とする */
        /*
        * CHIP系：
        * NOT以外：2番を出力、3番、4番を入力とする
        * NOT　　：2番を出力、3番を入力とする
        *
        * IC系：
        * NOT以外：3n+2、3n+3を入力、3n+4を出力とする(0<=n<=3)
        * NOT　　：2n+2を入力、2n+3を出力とする(0<=n<=5)
        */

        /* LOGIC_ICにおいては、TERMINAL_BRANCHはすべて使わないので、極大抵抗を埋め込んでおく */
        for (HighLevelConnectInfo terminal : groups) {
            if (terminal.getRole() == HighLevelConnectGroup.TERMINAL_BRANCH) {
                terminal.getHighLevelExecuteInfo().setResistance(MAXVALUE);
            }
        }
        setInfos(new ArrayList<>());
        flg = false;
        getInfos().add(null);
        for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
            for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                if (linkInfo.getTerminalDirection()[j] == TerminalDirection.POWER) {
                    for (HighLevelConnectInfo connectInfo : groups) {
                        if (connectInfo.getRole() == HighLevelConnectGroup.OUT_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(0, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                flg = true;
                                break;
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
                        if (connectInfo.getRole() == HighLevelConnectGroup.IN_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                            if (connectInfo.getConnectDirection() == j) {
                                getInfos().set(1, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                flg = true;
                                break;
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

        if (getElecomInfo().getPartsStandards() == AND_CHIP || getElecomInfo().getPartsStandards() == NOT_CHIP || getElecomInfo().getPartsStandards() == OR_CHIP || getElecomInfo().getPartsStandards() == XOR_CHIP) {
            /* 出力端子 */
            flg = false;
            getInfos().add(2, null);
            for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
                for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                    if (linkInfo.getTerminalDirection()[j] == TerminalDirection.OUT) {
                        for (HighLevelConnectInfo connectInfo : groups) {
                            if (connectInfo.getRole() == HighLevelConnectGroup.OUT_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                                if (connectInfo.getConnectDirection() == j) {
                                    getInfos().set(2, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                    flg = true;
                                    break;
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
            /* 入力端子 */
            int cnt = 3;
            if (getElecomInfo().getPartsStandards() != NOT_CHIP) {
                for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
                    for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                        flg = false;
                        if (linkInfo.getTerminalDirection()[j] == TerminalDirection.IN) {
                            getInfos().add(cnt, null);
                            for (HighLevelConnectInfo connectInfo : groups) {
                                if (connectInfo.getRole() == HighLevelConnectGroup.IN_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                                    if (connectInfo.getConnectDirection() == j) {
                                        getInfos().set(cnt, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                        connectInfo.getHighLevelExecuteInfo().setResistance(0);
                                        cnt++;
                                        flg = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (flg) {
                            break;
                        }
                    }
                }
            } else {
                flg = false;
                getInfos().add(null);
                for (CircuitLinkInfo linkInfo : getElecomInfo().getLinkedTerminal()) {
                    for (int j = 0; j < linkInfo.getTerminalDirection().length; j++) {
                        if (linkInfo.getTerminalDirection()[j] == TerminalDirection.IN) {
                            for (HighLevelConnectInfo connectInfo : groups) {
                                if (connectInfo.getRole() == HighLevelConnectGroup.IN_BRANCH && connectInfo.getAbcos().get(0).equals(abco.getHeight() + linkInfo.getReco().getHeight(), abco.getWidth() + linkInfo.getReco().getWidth())) {
                                    if (connectInfo.getConnectDirection() == j) {
                                        getInfos().set(3, new CorrespondInfo(connectInfo, linkInfo.getTerminalDirection()[j]));
                                        connectInfo.getHighLevelExecuteInfo().setResistance(0);
                                        flg = true;
                                        break;
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
            }
        } else {
            /* 向き情報を使って登録を行う。ただし、電源がちゃんと登録されていないと登録できない */
            addWithDirection(groups, abco);
        }
        setInputList(new boolean[getElecomInfo().getLinkedTerminal().size()]);
        setOutputList(new boolean[getElecomInfo().getLinkedTerminal().size()]);
    }

    @Override
    public void preBehavior(boolean isFirst) {
        /* 0番と1番がnullなら全て極大抵抗とする */
        if (getInfos().get(0) == null || getInfos().get(1) == null) {
            for (int i = 2; i < getInfos().size(); i++) {
                if (getInfos().get(i) != null) {
                    getInfos().get(i).getInfo().getHighLevelExecuteInfo().setResistance(MAXVALUE);
                }
            }
        } else {
            super.preBehavior(isFirst);
        }
    }

    @Override
    public void behavior() {
        /* いずれの場合も0番と1番がnullならばOFFとする */
        if (getInfos().get(0) == null || getInfos().get(1) == null) {
            setState(PartsStates.OFF);
        } else {
            /* 0番に流れる電流は第２解析で行っているため、入力リストで判断する */
            if (getInputList()[0]) {
                setState(PartsStates.ON);
            } else {
                setState(PartsStates.OFF);
            }
        }
        /* OFFの場合はすべてのOUT部分の出力リストをfalseにする */
        if (getState() == PartsStates.OFF) {
            super.behavior();
        } else {
            /* 入力を受け付ける */
            super.setInput();
            /* それぞれの振る舞いを考慮して出力リストを作成する */
            switch (getElecomInfo().getPartsStandards()) {
                case AND_CHIP:
                    getOutputList()[2] = getInputList()[3] && getInputList()[4];
                    break;
                case OR_CHIP:
                    getOutputList()[2] = getInputList()[3] || getInputList()[4];
                    break;
                case XOR_CHIP:
                    getOutputList()[2] = getInputList()[3] != getInputList()[4];
                    break;
                case NOT_CHIP:
                    getOutputList()[2] = !getInputList()[3];
                    break;
                case AND_IC:
                    for (int i = 0; i < getInfos().size(); i++) {
                        if ((i - 2) % 3 == 0) {
                            getOutputList()[i+2] = getInputList()[i] && getInputList()[i+1];
                        }
                    }
                    break;
                case OR_IC:
                    for (int i = 0; i < getInfos().size(); i++) {
                        if ((i - 2) % 3 == 0) {
                            getOutputList()[i+2] = getInputList()[i] || getInputList()[i+1];
                        }
                    }
                    break;
                case XOR_IC:
                    for (int i = 0; i < getInfos().size(); i++) {
                        if ((i - 2) % 3 == 0) {
                            getOutputList()[i+2] = getInputList()[i] != getInputList()[i+1];
                        }
                    }
                    break;
                case NOT_IC:
                    for (int i = 0; i < getInfos().size(); i++) {
                        if ((i - 2) % 2 == 0) {
                            getOutputList()[i+1] = !getInputList()[i];
                        }
                    }
                    break;
            }
        }
        /* 出力 */
        super.setOutput();
    }

    @Override
    public void disposeBehavior() {

    }

    /**
     * 与えられた向きに応じて登録を行う
     * 電源の座標を基準にしているため、電源が端子接続されていない場合は、こちらも登録できない
     */
    private void addWithDirection(ArrayList<HighLevelConnectInfo> groups, IntegerDimension abco) {
        int y,x,cnt=2;
        /* 電源が断線している場合は、適当に登録して極大抵抗として扱う */
        if (getInfos().get(0) == null || getInfos().get(1) == null) {
            for (int i=0;i<groups.size();i++) {
                if (groups.get(i).getRole() != HighLevelConnectGroup.TERMINAL_BRANCH) {
                    groups.get(i).getHighLevelExecuteInfo().setResistance(DoubleMatrix.MAXVALUE);
                }
            }
            return;
        }
        switch (getElecomInfo().getPartsDirections()) {
            case UP:
                /* 電源の右側からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 1;
                for (; cnt < 8; cnt++,x++) {
                    add(y, x, cnt, groups, abco, 0);
                }
                /* 電源の下２マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 14; cnt++,x++) {
                    add(y, x, cnt, groups, abco, 2);
                }
                break;
            case RIGHT:
                /* 電源の下側からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() + 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 8; cnt++,y++) {
                    add(y, x, cnt, groups, abco, 1);
                }
                /* 電源の左２マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 2;
                for (; cnt < 14; cnt++,y++) {
                    add(y, x, cnt, groups, abco, 3);
                }
                break;
            case DOWN:
                /* 電源の左側からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() - 1;
                for (; cnt < 8; cnt++,x--) {
                    add(y, x, cnt, groups, abco, 2);
                }
                /* 電源の上２マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 2;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 14; cnt++,x--) {
                    add(y, x, cnt, groups, abco, 0);
                }
                break;
            case LEFT:
                /* 電源の上側からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight() - 1;
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth();
                for (; cnt < 8; cnt++,y--) {
                    add(y, x, cnt, groups, abco, 3);
                }
                /* 電源の右２マス目からスタート */
                y = getInfos().get(0).getInfo().getAbcos().get(0).getHeight();
                x = getInfos().get(0).getInfo().getAbcos().get(0).getWidth() + 2;
                for (; cnt < 14; cnt++,y--) {
                    add(y, x, cnt, groups, abco, 1);
                }
                break;
        }
    }
}
