package Sho.CircuitObject.HighLevelConnect;

/**
 * 便宜的にグループがどういう構成のグループなのかを示すための列挙型。
 *
 * @author 翔
 * @version 1.1
 */
public enum HighLevelConnectGroup {
    /** 純粋な導線のグループ */
    BRANCH,
    /** 純粋な分岐点のグループ */
    NODE,
    /** 電子部品を構成する端子枝 */
    TERMINAL_BRANCH,
    /** 電子部品を構成する端子節 */
    TERMINAL_NODE,
    /** 電子部品の端子節をまとめる中心節 */
    CENTER_NODE,
    /** IN_NODEにまとめられる端子枝　入力端子だけでなく接地端子も含まれる */
    IN_BRANCH,
    /** 入力端子と接地端子をまとめる中心節 */
    IN_NODE,
    /** OUT_NODEにまとめられる端子枝　出力端子だけでなく供給端子も含まれる */
    OUT_BRANCH,
    /** 供給端子と出力端子をまとめる中心節 */
    OUT_NODE,
    ;
}
