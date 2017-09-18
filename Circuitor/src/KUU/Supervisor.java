package KUU;

import DataIO.DataFilter;
import KUU.BaseComponent.BaseFrame;

import java.io.File;

/**
 * Circuitorのウィンドウ等問わず、ソフトウェア全体を管理する最上位のクラス。
 */
public class Supervisor {
    public static void main(String[] args) {
        new Supervisor(args);
    }

    private Supervisor(String[] args) {
        BaseFrame baseFrame = new BaseFrame();
        /* CCTCファイルもしくはCCTPファイルをクリックして起動した場合の専用処理 */
        if (args.length > 0) {
            /* 拡張子の取得 */
            String ext = DataFilter.getExtension(new File(args[0]));
            if (ext.equals("cct")) {
                baseFrame.getCctIO().inputData(args[0]);
            }
        }
    }
}
