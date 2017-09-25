package KUU.CommonComponent;

import Master.FontMaster.FontMaster;
import Master.ImageMaster.PartsStandards;
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
    public void controlPop(UnitPanel panel, CircuitBlock b, MouseEvent e) {
        if (!b.isExist()) {
            hidePop();
            return;
        }
        CircuitInfo c = b.getCircuitInfo();
        currentCo = new IntegerDimension(c.getAbco().getHeight()-c.getReco().getHeight(), c.getAbco().getWidth() - c.getReco().getWidth());
        int cont = controlShowing(panel);
        if (cont > 0) {
            isShown = true;
            showPop(b, e);
        } else if (cont < 0) {
            isShown = false;
        }
    }

    public void controlPop(UnitPanel panel, HighLevelExecuteGroup group, MouseEvent e) {
        currentCo = group.getAbco();
        int cont = controlShowing(panel);
        if (cont > 0) {
            isShown = true;
            showPop(group, e);
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

    private void showPop(CircuitBlock b, MouseEvent e) {
        changeContent(b.getElecomInfo(), e);
    }

    private void showPop(HighLevelExecuteGroup group, MouseEvent e) {
        ElecomInfo ele = group.getBehavior().getElecomInfo();
        changeContent(ele, e);
    }

    public void hidePop() {
        isShown = false;
        preCo = null;
        currentCo = null;
    }

    public void changeContent(ElecomInfo ele, MouseEvent e) {
        if (ele.getPartsVarieties() == PartsVarieties.WIRE) {
            isShown = false;
            return;
        }
        preCo = currentCo;
        text = getContentString(ele);
    }

    private String getContentString(ElecomInfo ele) {
        PartsVarieties v = ele.getPartsVarieties();
        PartsStandards s = ele.getPartsStandards();
        switch (v) {
            case DIODE:
                return "整流ダイオード";
            case LED:
                if (s == PartsStandards.RED) {
                    return "赤色LED";
                } else if (s == PartsStandards.GREEN) {
                    return "緑色LED";
                } else {
                    return "青色LED";
                }
            case MEASURE:
                if (s == PartsStandards.VOLTMETER) {
                    return "電圧計";
                } else {
                    return "電流計";
                }
            case LOGIC_IC:
                switch (s) {
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
                if (s == PartsStandards.DC) {
                    return "直流電源 " + ele.getEtcStatus() + "[V]";
                }
            case PULSE:
                return "パルス出力器 " + ele.getEtcStatus() + "[Hz]";
            case TRANSISTOR:
                return "NPN型バイポーラトランジスタ";
            case SWITCH:
                return "タクトスイッチ";
            case RESISTANCE:
                switch (s) {
                    case _10:
                        return "抵抗器 10[Ω]";
                    case _100:
                        return "抵抗器 100[Ω]";
                    case _1000:
                        return "抵抗器 1000[Ω]";
                    case _10000:
                        return "抵抗器 10000[Ω]";
                    default:
                        return "抵抗器 " + ele.getEtcStatus() + "[Ω]";
                }
        }
        return null;
    }

    public void drawIndicate(Graphics g2, UnitPanel panel) {
        if (!isShown) {
            return;
        }

        mouseCo.setHeight((panel.getCursorCo().getHeight() + 2) * UnitPanel.UNIT_PIXEL);
        mouseCo.setWidth((panel.getCursorCo().getWidth() + 2) * UnitPanel.UNIT_PIXEL);

        g2.setFont(FontMaster.getRegularFont());

        int y, x;
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
