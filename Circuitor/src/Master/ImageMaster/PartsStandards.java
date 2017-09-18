package Master.ImageMaster;

/**
 * 電子部品の規格を管理する列挙型。
 * @author 翔
 * @version 1.1
 */
public enum PartsStandards {
    /* DIODE */
    /** 整流用ダイオード */
    RECT,

    /* LED */
    /** 青色ＬＥＤ */
    BLUE,
    /** 緑色ＬＥＤ */
    GREEN,
    /** 赤色ＬＥＤ */
    RED,

    /* LOGIC_IC */
    /** 論理積ＩＣ */
    AND_CHIP,
    AND_IC,
    /** 否定論理ＩＣ */
    NOT_CHIP,
    NOT_IC,
    /** 論理和ＩＣ */
    OR_CHIP,
    OR_IC,
    /** 排他的論理和ＩＣ */
    XOR_CHIP,
    XOR_IC,

    /* PIC */
    /** 18ピン */
    _18PINS,
    MODEL,

    /* POWER */
    /** 直流電源 */
    DC,

    /* RESISTANCE */
    /** 10Ω */
    _10,
    /** 100Ω */
    _100,
    /** 1000Ω */
    _1000,
    /** 10000Ω */
    _10000,
    /** 可変抵抗 */
    _variable,

    /* SWITCH */
    /** タクトスイッチ */
    TACT,
    /** トグルスイッチ */
    TOGGLE,

    /* TRANSISTOR */
    /** npn型 */
    BIPOLAR_NPN,

    /* MEASURE */
    /** 電流計 */
    AMMETER,
    /** 電圧系 */
    VOLTMETER,

    /* PULSE */
    /** パルス */
    PULSE,

    /* WIRE */
    /** 未接続線 */
    _0,
    /** 単直線 */
    _1,
    /** 直角カーブ */
    _2,
    /** Ｔ字接続 */
    _3,
    /** ２直線カーブ */
    _4,
    /** 十字交差 */
    _5,
    /** 十字接続 */
    _6,
    ;
}
