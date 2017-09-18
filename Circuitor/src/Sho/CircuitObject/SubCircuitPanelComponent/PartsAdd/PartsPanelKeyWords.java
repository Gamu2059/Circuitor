package Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd;


import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.ElecomInfo;

import java.util.HashMap;

import static Master.ImageMaster.PartsStandards.*;
import static Master.ImageMaster.PartsVarieties.*;

/**
 * 整数値と電子部品情報を対応つけるマスタ。
 * 全ウィンドウ共通の情報なのでstatic生成される。
 */
public class PartsPanelKeyWords {
    private static PartsPanelKeyWords onlyOne;

    private static HashMap<Integer, ElecomInfo> integerElecomInfoHashMap = new HashMap<>();

    public static PartsPanelKeyWords getPartsPanelKeyWords() {
        if (onlyOne == null) {
            onlyOne = new PartsPanelKeyWords();
        }
        return onlyOne;
    }

    private PartsPanelKeyWords() {
        integerElecomInfoHashMap.put(0, new ElecomInfo(LED, BLUE));
        integerElecomInfoHashMap.put(1, new ElecomInfo(LED, GREEN));
        integerElecomInfoHashMap.put(2, new ElecomInfo(LED, RED));
        integerElecomInfoHashMap.put(3, new ElecomInfo(POWER, DC));
        integerElecomInfoHashMap.put(4, new ElecomInfo(RESISTANCE, _10));
        integerElecomInfoHashMap.put(5, new ElecomInfo(RESISTANCE, _100));
        integerElecomInfoHashMap.put(6, new ElecomInfo(RESISTANCE, _1000));
        integerElecomInfoHashMap.put(7, new ElecomInfo(RESISTANCE, _10000));
        integerElecomInfoHashMap.put(8, new ElecomInfo(WIRE, _0));
        integerElecomInfoHashMap.put(9, new ElecomInfo(WIRE, _1));
        integerElecomInfoHashMap.put(10, new ElecomInfo(WIRE, _2));
        integerElecomInfoHashMap.put(11, new ElecomInfo(WIRE, _3));
        integerElecomInfoHashMap.put(12, new ElecomInfo(WIRE, _4));
        integerElecomInfoHashMap.put(13, new ElecomInfo(WIRE, _5));
        integerElecomInfoHashMap.put(14, new ElecomInfo(WIRE, _6));
        integerElecomInfoHashMap.put(15, new ElecomInfo(SWITCH, TACT));
        integerElecomInfoHashMap.put(16, new ElecomInfo(DIODE, RECT));
        integerElecomInfoHashMap.put(17, new ElecomInfo(TRANSISTOR, BIPOLAR_NPN));
        integerElecomInfoHashMap.put(18, new ElecomInfo(LOGIC_IC, AND_CHIP));
        integerElecomInfoHashMap.put(19, new ElecomInfo(LOGIC_IC, NOT_CHIP));
        integerElecomInfoHashMap.put(20, new ElecomInfo(LOGIC_IC, OR_CHIP));
        integerElecomInfoHashMap.put(21, new ElecomInfo(LOGIC_IC, XOR_CHIP));
        integerElecomInfoHashMap.put(22, new ElecomInfo(LOGIC_IC, AND_IC));
        integerElecomInfoHashMap.put(23, new ElecomInfo(LOGIC_IC, NOT_IC));
        integerElecomInfoHashMap.put(24, new ElecomInfo(LOGIC_IC, OR_IC));
        integerElecomInfoHashMap.put(25, new ElecomInfo(LOGIC_IC, XOR_IC));
        integerElecomInfoHashMap.put(26, new ElecomInfo(PIC, _18PINS));
        integerElecomInfoHashMap.put(27, new ElecomInfo(MEASURE, VOLTMETER));
        integerElecomInfoHashMap.put(28, new ElecomInfo(MEASURE, AMMETER));
        integerElecomInfoHashMap.put(29, new ElecomInfo(RESISTANCE, _variable));
        integerElecomInfoHashMap.put(30, new ElecomInfo(PartsVarieties.PULSE, PartsStandards.PULSE));
    }

    public static HashMap<Integer, ElecomInfo> getIntegerElecomInfoHashMap() {
        return integerElecomInfoHashMap;
    }
}
