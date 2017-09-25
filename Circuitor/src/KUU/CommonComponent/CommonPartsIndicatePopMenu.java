package KUU.CommonComponent;

import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.Circuit.CircuitInfo;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.HighLevelConnect.HighLevelExecuteGroup;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 電子部品の説明を表示するポップアップメニュー。
 * UnitPanelが所有する形でこのインスタンスは再利用する。
 */
public class CommonPartsIndicatePopMenu extends JPopupMenu {
    private JLabel label;
    private IntegerDimension abco, currentCo;

    public CommonPartsIndicatePopMenu() {
        super();
        label = new JLabel();
        add(label);
    }

    /**
     * 部品ポップの表示を制御する。
     *
     * 何らかの形で特定の部品の範囲外から範囲内にマウスが入った時、ポップを表示する。
     * 部品の範囲内で少しでもマウスが動いた時、ポップを非表示する。
     * 部品が存在しない場合でもポップを非表示にする。
     */
    public void controllPop(UnitPanel panel, CircuitBlock b, MouseEvent e) {
        if (!b.isExist()) {
            hidePop();
            return;
        }
        CircuitInfo c = b.getCircuitInfo();
        currentCo = new IntegerDimension(c.getAbco().getHeight()-c.getReco().getHeight(), c.getAbco().getWidth() - c.getReco().getWidth());
        if (isShowable(panel)) {
            setVisible(true);
            showPop(b, e);
        } else {
            setVisible(false);
        }
    }

    public void controllPop(UnitPanel panel, HighLevelExecuteGroup group, MouseEvent e) {
        currentCo = group.getAbco();
        if (isShowable(panel)) {
            setVisible(true);
            showPop(group, e);
        } else {
            setVisible(false);
        }
    }

    private boolean isShowable(UnitPanel panel) {
        if (isVisible()) {
            return !currentCo.equals(abco) || panel.getDeltaCursorCo().equals(0, 0);
        } else {
            return !currentCo.equals(abco);
        }
    }

    private void showPop(CircuitBlock b, MouseEvent e) {
        if (b.getElecomInfo().getPartsVarieties() == PartsVarieties.WIRE) {
            setVisible(false);
            return;
        }
        setLocation(e.getXOnScreen() + UnitPanel.UNIT_PIXEL, e.getYOnScreen() + UnitPanel.UNIT_PIXEL);
        abco = currentCo;
        changeContent(b.getElecomInfo());
    }

    private void showPop(HighLevelExecuteGroup group, MouseEvent e) {
        ElecomInfo ele = group.getBehavior().getElecomInfo();
        if (ele.getPartsVarieties() == PartsVarieties.WIRE) {
            setVisible(false);
            return;
        }
        setLocation(e.getXOnScreen() + UnitPanel.UNIT_PIXEL, e.getYOnScreen() + UnitPanel.UNIT_PIXEL);
        abco = currentCo;
        changeContent(ele);
    }

    public void hidePop() {
        setVisible(false);
        abco = null;
    }

    public void changeContent(ElecomInfo ele) {
        label.setText(getContentString(ele));
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
}
