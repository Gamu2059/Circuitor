package Master.ColorMaster;

import java.awt.*;

/**
 * 全体の色情報を統括するクラス。
 */
public class ColorMaster {
    /***********************/
    /** メニューバーカラー */
    /***********************/
    private static Color menuColor = new Color(200, 220, 220);

    /*****************/
    /** バックカラー */
    /*****************/
    private static Color backColor = new Color(80, 80, 80);
    private static Color subBackColor = new Color(80, 110, 110);

    /*******************/
    /** ボーダーカラー */
    /*******************/
    private static Color regularBorderColor = new Color(0, 0, 0);
    private static Color selectableBorderColor = new Color(130, 180, 100);
    private static Color overLapBorderColor = new Color(80, 220, 150);
    private static Color selectedBorderColor = new Color(0, 255, 200);
    private static Color bondableBorderColor = new Color(255, 150, 200);
    private static Color predictionBorderColor = new Color(200, 255, 30);
    private static Color rangingBorderColor = new Color(255, 200, 100);
    private static Color rangedBorderColor = new Color(170, 100, 40);
    private static Color errorBorderColor = new Color(255, 0, 0);
    private static Color offSwitchBorderColor = new Color(60, 150, 180);
    private static Color onSwitchBorderColor = new Color(100, 230, 240);
    private static Color selectableVoltmeterBorderColor = new Color(160, 130, 50);
    private static Color selectableAmmeterBorderColor = new Color(80, 70, 190);
    private static Color selectableVariableResistanceBorderColor = new Color(170, 50, 170);
    private static Color selectedVoltmeterBorderColor = new Color(220, 180, 60);
    private static Color selectedAmmeterBorderColor = new Color(100, 100, 255);
    private static Color selectedVariableResistanceBorderColor = new Color(230, 80, 230);


    /*******************/
    /** セレクトカラー */
    /*******************/
    private static Color selectableColor = new Color(240, 100, 100);
    private static Color selectedColor = new Color(140, 240, 240);
    private static Color notSelectedColor = new Color(180, 180, 180);

    /*******************/
    /** クリックカラー */
    /*******************/
    private static Color clickedColor = new Color(100, 120, 120);

    /***********************/
    /** ヘルプラベルカラー */
    /***********************/
    private static Color helpLabelColor = new Color(200, 220, 220);

    /*****************************/
    /** マイコンピンラベルカラー */
    /*****************************/
    private static Color outColor = new Color(200, 100, 100);
    private static Color inColor = new Color(100, 200, 200);

    /***************/
    /** 基板カラー */
    /***************/
    private static Color substrateColor = new Color(31, 132, 98);

    /***************/
    /** 電流カラー */
    /***************/
    private static Color regularCurrentColor = new Color(255,255,160);
    private static Color irregularCurrentColor = new Color(200,0,0);

    /*****************/
    /** グラフカラー */
    /*****************/
    private static Color graphBackColor = new Color(20, 50, 30);
    private static Color graphPointColor = new Color(190, 190,0);

    /**
     * メイン操作パネル-実行オプション変更フレーム
     */
    private static Color mainExecuteFrameClickedLabelColor = new Color(150, 150, 255);
    private static Color mainExecuteFrameUnselectedLabelColor = new Color(175, 255, 255);
    private static Color mainExecuteFrameEnableLabelColor = new Color(175, 255, 255);
    private static Color mainExecuteFrameNotEnableLabelColor = new Color(200, 200, 200);
    private static Color mainExecuteFrameEnableTextColor = new Color(255, 255, 255);
    private static Color mainExecuteFrameNotEnableTextColor = new Color(200, 200, 200);

    private static Color editorSelectOrderPanelColor = new Color(150, 255, 255);

    /***********************/
    /** メニューバーカラー */
    /***********************/
    public static Color getMenuColor() {
        return menuColor;
    }

    /*****************/
    /** バックカラー */
    /*****************/
    public static Color getBackColor() {
        return backColor;
    }

    public static Color getSubBackColor() {
        return subBackColor;
    }

    /*******************/
    /** ボーダーカラー */
    /*******************/
    public static Color getRegularBorderColor() {
        return regularBorderColor;
    }

    public static Color getSelectableBorderColor() {
        return selectableBorderColor;
    }

    public static Color getOverLapBorderColor() {
        return overLapBorderColor;
    }

    public static Color getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public static Color getBondableBorderColor() {
        return bondableBorderColor;
    }

    public static Color getPredictionBorderColor() {
        return predictionBorderColor;
    }

    public static Color getRangingBorderColor() {
        return rangingBorderColor;
    }

    public static Color getRangedBorderColor() {
        return rangedBorderColor;
    }

    public static Color getErrorBorderColor() {
        return errorBorderColor;
    }

    public static Color getOffSwitchBorderColor() {
        return offSwitchBorderColor;
    }

    public static Color getOnSwitchBorderColor() {
        return onSwitchBorderColor;
    }

    public static Color getSelectableVoltmeterBorderColor() {
        return selectableVoltmeterBorderColor;
    }

    public static Color getSelectableAmmeterBorderColor() {
        return selectableAmmeterBorderColor;
    }

    public static Color getSelectableVariableResistanceBorderColor() {
        return selectableVariableResistanceBorderColor;
    }

    public static Color getSelectedVoltmeterBorderColor() {
        return selectedVoltmeterBorderColor;
    }

    public static Color getSelectedAmmeterBorderColor() {
        return selectedAmmeterBorderColor;
    }

    public static Color getSelectedVariableResistanceBorderColor() {
        return selectedVariableResistanceBorderColor;
    }

    /*******************/
    /** セレクトカラー */
    /*******************/
    public static Color getSelectableColor() {
        return selectableColor;
    }

    public static Color getSelectedColor() {
        return selectedColor;
    }

    public static Color getNotSelectedColor() {
        return notSelectedColor;
    }

    /*******************/
    /** クリックカラー */
    /*******************/
    public static Color getClickedColor() {
        return clickedColor;
    }

    /***********************/
    /** ヘルプラベルカラー */
    /***********************/
    public static Color getHelpLabelColor() {
        return helpLabelColor;
    }

    /*****************************/
    /** マイコンピンラベルカラー */
    /*****************************/
    public static Color getOutColor() {
        return outColor;
    }

    public static Color getInColor() {
        return inColor;
    }

    /***************/
    /** 基板カラー */
    /***************/
    public static Color getSubstrateColor() {
        return substrateColor;
    }

    /***************/
    /** 電流カラー */
    /***************/
    public static Color getRegularCurrentColor() {
        return regularCurrentColor;
    }

    public static Color getIrregularCurrentColor() {
        return irregularCurrentColor;
    }

    /*****************/
    /** グラフカラー */
    /*****************/
    public static Color getGraphBackColor() {
        return graphBackColor;
    }

    public static Color getGraphPointColor() {
        return graphPointColor;
    }



    public static Color getMainExecuteFrameClickedLabelColor() {
        return mainExecuteFrameClickedLabelColor;
    }

    public static Color getMainExecuteFrameEnableLabelColor() {
        return mainExecuteFrameEnableLabelColor;
    }

    public static Color getMainExecuteFrameNotEnableLabelColor() {
        return mainExecuteFrameNotEnableLabelColor;
    }

    public static Color getMainExecuteFrameEnableTextColor() {
        return mainExecuteFrameEnableTextColor;
    }

    public static Color getMainExecuteFrameNotEnableTextColor() {
        return mainExecuteFrameNotEnableTextColor;
    }

    public static Color getMainExecuteFrameUnselectedLabelColor() {
        return mainExecuteFrameUnselectedLabelColor;
    }

    public static Color getEditorSelectOrderPanelColor() {
        return editorSelectOrderPanelColor;
    }


}
