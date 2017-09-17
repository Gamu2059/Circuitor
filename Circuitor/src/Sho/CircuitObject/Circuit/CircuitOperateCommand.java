package Sho.CircuitObject.Circuit;

import static Sho.CircuitObject.Circuit.CircuitOperateCommand.Command.*;

/**
 * 回路エディタのサブ操作パネルでのコマンド切替を保持する。
 */
public class CircuitOperateCommand {
    public enum Command {
        /*******************/
        /** PARTS_ADD MODE */
        /*******************/
        /** 部品・導線の追加 */
        PARTS_ADD,

        /*******************/
        /** WIRE_BOND MODE */
        /*******************/
        /** 導線の延長・結合 */
        WIRE_BOND,
        /** 交点の操作 */
        WIRE_CROSS,
        /** 末端の部品との結合(手動) */
        HAND_BOND,
        /** 末端の部品との結合(自動) */
        AUTO_BOND,

        /********************/
        /** PARTS_MOVE MODE */
        /********************/
        /** 部品・導線の移動(マス単位) */
        PARTS_MOVE,
        /** 部品・導線の複製(マス単位) */
        PARTS_COPY,
        /** 部品・導線の回転(マス単位) */
        PARTS_ROTATE,

        /**********************/
        /** PARTS_DELETE MODE */
        /**********************/
        /** 部品・導線の削除(マス単位) */
        DELETE_DETAIL,
        /** 部品・導線の削除(導線単位) */
        DELETE_COLLECT,
        /** 全削除 */
        DELETE_ALL,

        /********************/
        /** PARTS_EDIT MODE */
        /********************/
        /** 編集 */
        PARTS_EDIT,

        /*******************/
        /** EDIT_MOVE MODE */
        /*******************/
        /** エディタの移動・拡縮 */
        EDIT_MOVE,
        /** 位置リセット */
        EDIT_RESET,

        /*********************/
        /** DATA_INSERT MODE */
        /*********************/
        /** ファイルからのデータ入力 */
        DATA_INSERT,

        /*****************/
        /** EXECUTE MODE */
        /*****************/
        /** 実行専用 */
        EXECUTE,
    }
    private Command command;
    private Command preCommand;

    public CircuitOperateCommand() {
        setCommand(PARTS_ADD);
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        if (this.command != command) {
            this.preCommand = this.command;
            this.command = command;
        }
    }

    public Command getPreCommand() {
        return preCommand;
    }
}
