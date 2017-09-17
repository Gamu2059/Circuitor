package Sho.CircuitObject.Circuit;

import Master.ColorMaster.ColorMaster;

import java.awt.*;

/**
 * ボーダ描画に関する情報をまとめたマスタクラス。
 */
public class CircuitBorder {
    public enum Borders {
        /** 選択可能な領域 */
        SELECTABLE,
        /** マウスカーソルが重なっている選択可能な領域 */
        OVERLAP,
        /** 選択した領域 */
        SELECTED,
        /** 導線の結合が可能な領域 */
        BONDABLE,
        /** 追加、移動、複製、回転、結合経路の予測領域 */
        PREDICTION,
        /** 範囲選択中の領域 */
        RANGING,
        /** 範囲選択された領域 */
        RANGED,
        /** 操作不能となる領域 */
        ERROR,
        /** オン状態のスイッチの領域 */
        ON_SWITCH,
        /** オフ状態のスイッチの領域 */
        OFF_SWITCH,
        /** 選択可能な電圧計の領域 */
        SELECTABLE_VOL,
        /** 選択可能な電流計の領域 */
        SELECTABLE_AMM,
        /** 選択可能な可変抵抗の領域 */
        SELECTABLE_VAR,
        /** 選択された電圧計の領域 */
        SELECTED_VOL,
        /** 選択された電流計の領域 */
        SELECTED_AMM,
        /** 選択された可変抵抗の領域 */
        SELECTED_VAR,
    }

    public static Color getColor(Borders borders) {
        switch (borders) {
            case SELECTABLE:
                return ColorMaster.getSelectableBorderColor();
            case OVERLAP:
                return ColorMaster.getOverLapBorderColor();
            case SELECTED:
                return ColorMaster.getSelectedBorderColor();
            case BONDABLE:
                return ColorMaster.getBondableBorderColor();
            case PREDICTION:
                return ColorMaster.getPredictionBorderColor();
            case RANGING:
                return ColorMaster.getRangingBorderColor();
            case RANGED:
                return ColorMaster.getRangedBorderColor();
            case ERROR:
                return ColorMaster.getErrorBorderColor();
            case ON_SWITCH:
                return ColorMaster.getOnSwitchBorderColor();
            case OFF_SWITCH:
                return ColorMaster.getOffSwitchBorderColor();
            case SELECTABLE_VOL:
                return ColorMaster.getSelectableVoltmeterBorderColor();
            case SELECTABLE_AMM:
                return ColorMaster.getSelectableAmmeterBorderColor();
            case SELECTABLE_VAR:
                return ColorMaster.getSelectableVariableResistanceBorderColor();
            case SELECTED_VOL:
                return ColorMaster.getSelectedVoltmeterBorderColor();
            case SELECTED_AMM:
                return ColorMaster.getSelectedAmmeterBorderColor();
            case SELECTED_VAR:
                return ColorMaster.getSelectedVariableResistanceBorderColor();
            default:
                return null;
        }
    }
}
