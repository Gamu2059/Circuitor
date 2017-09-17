package Sho.CircuitObject.Circuit;

import static Sho.CircuitObject.Circuit.CircuitOperateMode.Mode.*;

/**
 * 回路エディタのメイン操作パネルでのモード切替を保持する。
 */
public class CircuitOperateMode {
    public enum Mode{
        /** 部品・導線の追加 */
        PARTS_ADD,
        /** 導線の結合 */
        WIRE_BOND,
        /** 部品・導線の移動 */
        PARTS_MOVE,
        /** 部品・導線の削除 */
        PARTS_DELETE,
        /** 部品の実行前設定 */
        PARTS_EDIT,
        /** エディタの移動 */
        EDIT_MOVE,
        /** 実行専用 */
        EXECUTE,
    }
    private Mode mode;
    private Mode preMode;

    public CircuitOperateMode() {
        setMode(PARTS_ADD);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode != mode) {
            this.preMode = this.mode;
            this.mode = mode;
        }
    }

    public Mode getPreMode() {
        return preMode;
    }
}
