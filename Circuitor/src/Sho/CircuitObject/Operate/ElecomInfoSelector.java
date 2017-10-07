package Sho.CircuitObject.Operate;

import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.PartsDirections;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.CircuitLinkInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.IntegerDimension.IntegerDimension;

import static Sho.CircuitObject.Circuit.TerminalDirection.*;

/**
 * 電子部品の追加時に必要となる、電子部品の接続関係やサイズを定義した超低レベルクラス。
 */
public class ElecomInfoSelector {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    private boolean link[];
    private int correspond[];
    private TerminalDirection direction[];

    public ElecomInfoSelector(BaseFrame frame) {
        this.frame = frame;
    }

    public void elecomInfoSelector(ElecomInfo elecomInfo) {
        elecomInfo.setPartsDirections(PartsDirections.UP);
        switch (elecomInfo.getPartsVarieties()) {
            case LED:
            case CAPACITANCE:
            case DIODE:
                /*[1][0]
                * link : 左
                * direction : 左はアノード
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = ANODE;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));

                /*[1][2]
                * link : 右
                * direction : 右はカソード
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = CATHODE;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case LOGIC_IC:
                switch (elecomInfo.getPartsStandards()) {
                    case AND_CHIP:
                    case OR_CHIP:
                    case XOR_CHIP:
                    case NOT_CHIP:
                        /*[0][1]
                        * 電源
                        * link : 上
                        * direction : 上は電源
                        * */
                        link = new boolean[4];
                        direction = new TerminalDirection[4];
                        link[0] = true;
                        direction[0] = POWER;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 1, link, direction));

                        /*[2][1]
                        * 接地
                        * link : 下
                        * direction : 下はＧＮＤ
                        * */
                        link = new boolean[4];
                        direction = new TerminalDirection[4];
                        link[2] = true;
                        direction[2] = GND;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 1, link, direction));

                        /*[1][2]
                        * 出力
                        * link : 右
                        * direction : 右はOUT
                        * */
                        link = new boolean[4];
                        direction = new TerminalDirection[4];
                        link[1] = true;
                        direction[1] = OUT;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 2, link, direction));

                        if (elecomInfo.getPartsStandards() != PartsStandards.NOT_CHIP) {
                            /*[0][0]
                            * 入力
                            * link : 左
                            * direction : 左はIN
                            * */
                            link = new boolean[4];
                            direction = new TerminalDirection[4];
                            link[3] = true;
                            direction[3] = IN;
                            elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, direction));

                            /*[2][0]
                            * 入力
                            * link : 左
                            * direction : 左はIN
                            * */
                            link = new boolean[4];
                            direction = new TerminalDirection[4];
                            link[3] = true;
                            direction[3] = IN;
                            elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 0, link, direction));
                        } else {
                            /*[1][0]
                            * 入力
                            * link : 左
                            * direction : 左はIN
                            * */
                            link = new boolean[4];
                            direction = new TerminalDirection[4];
                            link[3] = true;
                            direction[3] = IN;
                            elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));
                        }

                        elecomInfo.setPartsStates(PartsStates.OFF);
                        elecomInfo.setSize(new IntegerDimension(3, 3));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(false);
                        break;
                    case AND_IC:
                    case OR_IC:
                    case XOR_IC:
                    case NOT_IC:
                        /*[0][0]
                        * 電源
                        * link : 上
                        * direction : 上は電源
                        * */
                        link = new boolean[4];
                        direction = new TerminalDirection[4];
                        link[0] = true;
                        direction[0] = POWER;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, direction));

                        /*[2][6]
                        * 接地
                        * link : 下
                        * direction : 下はＧＮＤ
                        * */
                        link = new boolean[4];
                        direction = new TerminalDirection[4];
                        link[2] = true;
                        direction[2] = GND;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 6, link, direction));

                        int y=0,x=0;
                        if (elecomInfo.getPartsStandards() != PartsStandards.NOT_IC) {
                            /* 入力 */
                            for (int i=0;i<8;i++) {
                                switch (i) {
                                    case 0:y=0;x=1;break;
                                    case 1:y=0;x=2;break;
                                    case 2:y=0;x=4;break;
                                    case 3:y=0;x=5;break;
                                    case 4:y=2;x=0;break;
                                    case 5:y=2;x=1;break;
                                    case 6:y=2;x=3;break;
                                    case 7:y=2;x=4;break;
                                }
                                link = new boolean[4];
                                direction = new TerminalDirection[4];
                                if (i < 4) {
                                    link[0] = true;
                                    direction[0] = IN;
                                } else {
                                    link[2] = true;
                                    direction[2] = IN;
                                }
                                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(y, x, link, direction));
                            }
                            /* 出力 */
                            for (int i=0;i<4;i++) {
                                switch (i) {
                                    case 0:y=0;x=3;break;
                                    case 1:y=0;x=6;break;
                                    case 2:y=2;x=2;break;
                                    case 3:y=2;x=5;break;
                                }
                                link = new boolean[4];
                                direction = new TerminalDirection[4];
                                if (i < 2) {
                                    link[0] = true;
                                    direction[0] = OUT;
                                } else {
                                    link[2] = true;
                                    direction[2] = OUT;
                                }
                                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(y, x, link, direction));
                            }
                        } else {
                            /* 入力 */
                            for (int i=0;i<6;i++) {
                                switch (i) {
                                    case 0:y=0;x=1;break;
                                    case 1:y=0;x=3;break;
                                    case 2:y=0;x=5;break;
                                    case 3:y=2;x=0;break;
                                    case 4:y=2;x=2;break;
                                    case 5:y=2;x=4;break;
                                }
                                link = new boolean[4];
                                direction = new TerminalDirection[4];
                                if (i < 3) {
                                    link[0] = true;
                                    direction[0] = IN;
                                } else {
                                    link[2] = true;
                                    direction[2] = IN;
                                }
                                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(y, x, link, direction));
                            }
                            /* 出力 */
                            for (int i=0;i<6;i++) {
                                switch (i) {
                                    case 0:y=0;x=2;break;
                                    case 1:y=0;x=4;break;
                                    case 2:y=0;x=6;break;
                                    case 3:y=2;x=1;break;
                                    case 4:y=2;x=3;break;
                                    case 5:y=2;x=5;break;
                                }
                                link = new boolean[4];
                                direction = new TerminalDirection[4];
                                if (i < 3) {
                                    link[0] = true;
                                    direction[0] = OUT;
                                } else {
                                    link[2] = true;
                                    direction[2] = OUT;
                                }
                                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(y, x, link, direction));
                            }
                        }

                        elecomInfo.setPartsStates(PartsStates.OFF);
                        elecomInfo.setSize(new IntegerDimension(3, 7));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(false);
                        break;
                }
                break;
            case PIC:
                int y=0,x=0;
                for (int i=0;i<18;i++) {
                    switch (i) {
                        case 0:y=2;x=0;break;
                        case 1:y=2;x=1;break;
                        case 2:y=2;x=2;break;
                        case 3:y=2;x=3;break;
                        case 4:y=2;x=4;break;
                        case 5:y=2;x=5;break;
                        case 6:y=2;x=6;break;
                        case 7:y=2;x=7;break;
                        case 8:y=2;x=8;break;
                        case 9:y=0;x=8;break;
                        case 10:y=0;x=7;break;
                        case 11:y=0;x=6;break;
                        case 12:y=0;x=5;break;
                        case 13:y=0;x=4;break;
                        case 14:y=0;x=3;break;
                        case 15:y=0;x=2;break;
                        case 16:y=0;x=1;break;
                        case 17:y=0;x=0;break;
                    }
                    link = new boolean[4];
                    direction = new TerminalDirection[4];
                    if (i == 4) {
                        link[2] = true;
                        direction[2] = GND;
                    } else if (i == 13) {
                        link[0] = true;
                        direction[0] = POWER;
                    }else if (i < 9) {
                        link[2] = true;
                        direction[2] = frame.getBasePanel().getMiconPanel().getMiconPin()[i];
                    } else if (i < 18){
                        link[0] = true;
                        direction[0] = frame.getBasePanel().getMiconPanel().getMiconPin()[i];
                    }
                    elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(y, x, link, direction));
                }

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 9));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case POWER:
                /*[1][0]
                * link : 左
                * direction : 左は電源
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = POWER;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));

                /*[1][8]
                * link : 右
                * direction : 右は接地
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = GND;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 8, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 9));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case RESISTANCE:
                /*[0][0]
                * link : 左
                * direction : 左
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, direction));

                /*[0][2]
                * link : 右
                * direction : 右
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(1, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case MEASURE:
                /*[1][0]
                * link : 左
                * direction : 左
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = ANODE;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));

                /*[1][2]
                * link : 右
                * direction : 右
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = CATHODE;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case PULSE:
                /*[1][0]
                * link : 左
                * direction : 左
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));

                /*[1][2]
                * link : 右
                * direction : 右
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case SWITCH:
                /*[1][0]
                * link : 左
                * direction : 左
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[3] = true;
                direction[3] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 0, link, direction));

                /*[1][2]
                * link : 右
                * direction : 右
                * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[1] = true;
                direction[1] = EXIST;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(1, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case TRANSISTOR:
                /*[2][0]
                 * エミッタ
                 * link : 下
                 * direction : 下はエミッタ
                 * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[2] = true;
                direction[2] = EMITTER;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 0, link, direction));

                /*[2][1]
                 * コレクタ
                 * link : 下
                 * direction : 下はコレクタ
                 * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[2] = true;
                direction[2] = COLLECTOR;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 1, link, direction));

                /*[2][2]
                 * エミッタ
                 * link : 下
                 * direction : 下はベース
                 * */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                link[2] = true;
                direction[2] = BASE;
                elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(2, 2, link, direction));

                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(3, 3));
                elecomInfo.setHighLevelConnectSize(1);
                elecomInfo.setBranch(false);
                break;
            case WIRE:
                /* 全てに共通 */
                link = new boolean[4];
                direction = new TerminalDirection[4];
                correspond = new int[4];
                /* 対応しない場所には-1を格納 */
                for (int i = 0; i < correspond.length; i++) {
                    correspond[i] = -1;
                }
                elecomInfo.setPartsStates(PartsStates.OFF);
                elecomInfo.setSize(new IntegerDimension(1, 1));
                switch (elecomInfo.getPartsStandards()) {
                    case _0:
                        /*[0][0]
                        * link : 左
                        * correspond : すべてなし
                        * direction : すべてなし
                        * */
                        link[3] = true;
                        correspond[1] = 3;
                        correspond[3] = 1;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, correspond, direction));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(true);
                        break;
                    case _1:
                        /*[0][0]
                        * link : 左右
                        * correspond : 左右
                        * */
                        link[1] = link[3] = true;
                        correspond[1] = 3;
                        correspond[3] = 1;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, correspond, direction));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(true);
                        break;
                    case _2:
                        /*[0][0]
                        * link : 左下
                        * correspond : 左下
                        * */
                        link[2] = link[3] = true;
                        correspond[2] = 3;
                        correspond[3] = 2;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, correspond, direction));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(true);
                        break;
                    case _3:
                        /*[0][0]
                        * link : 上以外
                        * */
                        link[1] = link[2] = link[3] = true;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, direction));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(false);
                        break;
                    case _4:
                        /*[0][0]
                        * link : すべて
                        * correspond : 右上、左下
                        * */
                        link[0] = link[1] = link[2] = link[3] = true;
                        correspond[0] = 1;
                        correspond[1] = 0;
                        correspond[2] = 3;
                        correspond[3] = 2;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, correspond, direction));
                        elecomInfo.setHighLevelConnectSize(2);
                        elecomInfo.setBranch(true);
                        break;
                    case _5:
                        /*[0][0]
                        * link : すべて
                        * correspond : 上下、左右
                        * */
                        link[0] = link[1] = link[2] = link[3] = true;
                        correspond[0] = 2;
                        correspond[1] = 3;
                        correspond[2] = 0;
                        correspond[3] = 1;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, correspond, direction));
                        elecomInfo.setHighLevelConnectSize(2);
                        elecomInfo.setBranch(true);
                        break;
                    case _6:
                        /*[0][0]
                        * link : すべて
                        * */
                        link[0] = link[1] = link[2] = link[3] = true;
                        elecomInfo.getLinkedTerminal().add(new CircuitLinkInfo(0, 0, link, direction));
                        elecomInfo.setHighLevelConnectSize(1);
                        elecomInfo.setBranch(false);
                        break;
                }
                break;
        }
    }
}
