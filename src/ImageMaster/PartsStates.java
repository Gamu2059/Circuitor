package Master.ImageMaster;

/**
 * 電子部品の状態を管理する列挙型。
 * @author 翔
 * @version 1.1
 */
public enum PartsStates {
    /** 部品パレットに表示する画像 */
    MODEL,
    /** 状態の変化が外見に現れない電子部品や、状態が変化し機能している電子部品 */
    ON,
    /** 状態が変化し機能していない電子部品 */
    OFF,
    ;
}
