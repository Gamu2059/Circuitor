package Sho.CircuitObject.Circuit;

/**
 * 様々な端子の役割を定義する列挙型。
 *
 * @author 翔
 * @version 1.3
 */
public enum TerminalDirection {
    /* コンデンサ、ダイオード、ＬＥＤなど */
    /** アノード */
    ANODE,
    /** カソード */
    CATHODE,

    /* 論理ＩＣ、ＰＩＣなど */
    /** 入力しか行わない端子 */
    IN,
    /** 出力しか行わい端子 */
    OUT,

    /* 論理ＩＣ、ＰＩＣ、電源など */
    /** 電源側(入出力問わず、相対的に電位の高い方) */
    POWER,
    /** 接地側(入出力問わず、相対的に電位の低い方) */
    GND,

    /* トランジスタ */
    /** エミッタ */
    EMITTER,
    /** コレクタ */
    COLLECTOR,
    /** ベース */
    BASE,

    /* その他 */
    /** とりあえず、あることを示す */
    EXIST,
    ;
}
