package KUU.CommonComponent;

import Master.FontMaster.FontMaster;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.Circuit.CircuitInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 電子部品の説明を描画する処理クラス。
 * UnitPanelが所有する形でこのインスタンスは再利用する。
 */
public class CommonPartsIndicatePopMenu {
    private boolean isShown;
    private String text;
    private IntegerDimension preCo, currentCo, mouseCo;

    // リアルタイムでステートを反映させるために、部品のElecomInfoを保持する
    private ElecomInfo stateBuffer;

    public CommonPartsIndicatePopMenu() {
        super();
        mouseCo = new IntegerDimension();
    }

    /**
     * 部品ポップの表示を制御する。
     *
     * 何らかの形で特定の部品の範囲外から範囲内にマウスが入った時、ポップを表示する。
     * 部品の範囲内で少しでもマウスが動いた時、ポップを非表示する。
     * 部品が存在しない場合でもポップを非表示にする。
     */
    public void controlPop(UnitPanel panel, CircuitBlock b) {
        if (!b.isExist()) {
            hidePop();
            return;
        }
        CircuitInfo c = b.getCircuitInfo();
        currentCo = new IntegerDimension(c.getAbco().getHeight()-c.getReco().getHeight(), c.getAbco().getWidth() - c.getReco().getWidth());
        int cont = controlShowing(panel);
        if (cont > 0) {
            isShown = true;
            showPop(b);
        } else if (cont < 0) {
            isShown = false;
        }
    }

    public void controlPop(UnitPanel panel, HighLevelExecuteGroup group) {
        currentCo = group.getAbco();
        int cont = controlShowing(panel);
        if (cont > 0) {
            isShown = true;
            showPop(group);
        } else if (cont < 0) {
            isShown = false;
        }
    }

    /**
     * 表示するかどうかを数値で返す。
     * 0より大きければ表示、0未満ならば非表示、0ならば何もしない。
     */
    private int controlShowing(UnitPanel panel) {
        if (isShown) {
            if (!currentCo.equals(preCo)) {
                return 1;
            } else if (!panel.getDeltaCursorCo().equals(0, 0)) {
                return -1;
            }
        } else {
            if (preCo == null || !currentCo.equals(preCo)) {
                return 1;
            }
        }
        return 0;
    }

    private void showPop(CircuitBlock b) {
        changeContent(b.getElecomInfo());
    }

    private void showPop(HighLevelExecuteGroup group) {
        changeContent(group.getBehavior().getElecomInfo());
    }

    public void hidePop() {
        isShown = false;
        preCo = null;
        currentCo = null;
    }

    /**
     * 表示するコンテンツを切り替える。
     * この時点で文章は決定しない。
     */
    public void changeContent(ElecomInfo ele) {
        if (ele.getPartsVarieties() == PartsVarieties.WIRE) {
            isShown = false;
            return;
        }
        preCo = currentCo;
        stateBuffer = ele;
    }

    private String getContentString() {
        PartsVarieties v = stateBuffer.getPartsVarieties();
        PartsStandards sd = stateBuffer.getPartsStandards();
        PartsStates st = stateBuffer.getPartsStates();
        double par = stateBuffer.getEtcStatus();
        switch (v) {
            case DIODE:
                return "整流ダイオード";
            case LED:
                if (sd == PartsStandards.RED) {
                    return "赤色LED " + st;
                } else if (sd == PartsStandards.GREEN) {
                    return "緑色LED " + st;
                } else {
                    return "青色LED " + st;
                }
            case MEASURE:
                if (sd == PartsStandards.VOLTMETER) {
                    return "電圧計";
                } else {
                    return "電流計";
                }
            case LOGIC_IC:
                switch (sd) {
                    case AND_CHIP:
                        return "論理積回路";
                    case AND_IC:
                        return "論理積IC";
                    case OR_CHIP:
                        return "論理和回路";
                    case OR_IC:
                        return "論理和IC";
                    case XOR_CHIP:
                        return "排他的論理和回路";
                    case XOR_IC:
                        return "排他的論理和IC";
                    case NOT_CHIP:
                        return "否定論理回路";
                    default:
                        return "否定論理IC";
                }
            case PIC:
                return "18ピン PICマイコン";
            case POWER:
                if (sd == PartsStandards.DC) {
                    return "直流電源 " + par + "[V]";
                }
            case PULSE:
                return "パルス出力器 " + par + "[Hz]";
            case TRANSISTOR:
                return "NPN型バイポーラトランジスタ";
            case SWITCH:
                return "タクトスイッチ " + st;
            case RESISTANCE:
                switch (sd) {
                    case _10:
                        return "抵抗器 10[Ω]";
                    case _100:
                        return "抵抗器 100[Ω]";
                    case _1000:
                        return "抵抗器 1000[Ω]";
                    case _10000:
                        return "抵抗器 10000[Ω]";
                    default:
                        return "抵抗器 " + par + "[Ω]";
                }
        }
        return null;
    }

    public void drawIndicate(Graphics g2, UnitPanel panel) {
        if (!isShown) {
            return;
        }

        // 文章決定
        text = getContentString();

        int baseSize = panel.getBaseSize();
        int y, x;

        if (panel.getCursorCo() == null) {
            return;
        }
        mouseCo.setHeight((panel.getCursorCo().getHeight() + 2) * baseSize + panel.getPaintBaseCo().getHeight());
        mouseCo.setWidth((panel.getCursorCo().getWidth() + 2) * baseSize + panel.getPaintBaseCo().getWidth());

        g2.setFont(FontMaster.getRegularFont());
        y = g2.getFontMetrics().getHeight() + 4;
        x = g2.getFontMetrics().stringWidth(text) + 4;

        g2.setColor(Color.WHITE);
        g2.fillRect(mouseCo.getWidth(), mouseCo.getHeight(), x, y);
        if (text != null) {
            g2.setColor(Color.BLACK);
            g2.drawString(text, mouseCo.getWidth() + 2, mouseCo.getHeight() + g2.getFontMetrics().getAscent() + 2);
        }
    }
}
