package Master.ImageMaster;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;

import static Master.ImageMaster.PartsDirections.*;
import static Master.ImageMaster.PartsStandards.*;
import static Master.ImageMaster.PartsVarieties.*;

/**
 * 電子部品の画像のリソースを一括管理するクラス。
 * @author 翔
 * @version 1.2
 */
public class ImageMaster {
    /**
     * ImageMasterのインスタンスを保有する。
     */
    private static ImageMaster onlyOne;

    /**
     * リソースをパスと関連付けて登録する。
     */
    private static HashMap<String,URL> urlMaster = new HashMap<>();
    private static HashMap<String,ImageIcon> imageMaster = new HashMap<>();

    /**
     * イメージマスタを呼び出す。
     *
     * @since 1.2
     */
    public static ImageMaster getImageMaster() {
        if (onlyOne == null) {
            onlyOne = new ImageMaster();
        }
        return onlyOne;
    }

    private ImageMaster() {}

    /**
     * リソースをストアする。
     *
     * @since 1.3
     */
    private boolean store(String pass) {
        /* マスタに登録されていなければ、生成して登録 */
        if (!urlMaster.containsKey(pass)) {
            try {
                urlMaster.put(pass, getClass().getClassLoader().getResource(pass));
                imageMaster.put(pass, new ImageIcon(urlMaster.get(pass)));
            } catch (NullPointerException e) {
                System.out.println("ImageMasterで例外が発生しました。");
                System.out.println("例外が発生したパス：" + pass);
                return false;
            }
        }
        return true;
    }

    /**
     * 部品パネル専用のパスを作成する。
     *
     * @since 1.2
     */
    private String createModelPass(PartsVarieties partsVarieties, PartsStandards partsStandards) {
        return "Resource/" + partsVarieties + "/" + partsStandards + "/MODEL/UP.png";
    }

    /**
     * 部品のパスを作成する。
     *
     * @since 1.2
     */
    private String createPass(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsStates partsStates, PartsDirections partsDirections, int y, int x) {
        partsDirections = getDirection(partsVarieties, partsStandards, partsDirections);
        /* パス形成 */
        return "Resource/" + partsVarieties + "/" + partsStandards + "/" + partsStates + "/" + partsDirections + "/_" + y + "_" + x + ".png";
    }

    /**
     * 仮配置専用のパスを作成する。
     *
     * @since 1.4
     */
    private String createTempPass(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsDirections partsDirections) {
        partsDirections = getDirection(partsVarieties, partsStandards, partsDirections);
        /* パス形成 */
        return "Resource/" + partsVarieties + "/" + partsStandards + "/TEMPLACE/" + partsDirections + ".png";
    }

    /**
     * 指定された部品と向きから、存在しない向きを修正して向きを返す。
     */
    private PartsDirections getDirection(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsDirections partsDirections) {
        if (partsVarieties == RESISTANCE || partsVarieties == SWITCH || partsVarieties == PartsVarieties.PULSE) {
            if (partsDirections == DOWN) {
                return UP;
            }
            else if (partsDirections == LEFT) {
                return RIGHT;
            }
        }
        else if (partsVarieties == WIRE) {
            if (partsStandards == _1 || partsStandards == _4) {
                if (partsDirections == DOWN) {
                    return UP;
                }
                else if (partsDirections == LEFT) {
                    return RIGHT;
                }
            }
            else if (partsStandards == _5 || partsStandards == _6) {
                return UP;
            }
        }
        return partsDirections;
    }

    /**
     * イメージマスタから部品パネル専用のURLリソースを取得する。
     *
     * @since 1.2
     */
    public URL getModelURL(PartsVarieties partsVarieties, PartsStandards partsStandards) {
        if (partsVarieties == null || partsStandards == null) {
            return null;
        }
        String pass = createModelPass(partsVarieties, partsStandards);
        if (store(pass)) {
            return urlMaster.get(pass);
        }
        return null;
    }

    /**
     * イメージマスタからURLリソースを取得する。
     *
     * @since 1.2
     */
    public URL getURL(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsStates partsStates, PartsDirections partsDirections, int y, int x) {
        if (partsVarieties == null || partsStandards == null || partsStates == null || partsDirections == null) {
            return null;
        }
        String pass = createPass(partsVarieties, partsStandards, partsStates, partsDirections, y, x);
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return urlMaster.get(pass);
        }
        return null;
    }

    /**
     * イメージマスタから仮配置専用のURLリソースを取得する。
     *
     * @since 1.4
     */
    public URL getTempURL(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsDirections partsDirections) {
        if (partsVarieties == null || partsStandards == null || partsDirections == null) {
            return null;
        }
        String pass = createTempPass(partsVarieties, partsStandards, partsDirections);
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return urlMaster.get(pass);
        }
        return null;
    }

    /**
     * イメージマスタから部品パネル専用のアイコンリソースを取得する。
     *
     * @since 1.3
     */
    public ImageIcon getModelImage(PartsVarieties partsVarieties, PartsStandards partsStandards) {
        if (partsVarieties == null || partsStandards == null) {
            return null;
        }
        String pass = createModelPass(partsVarieties, partsStandards);
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * イメージマスタからアイコンリソースを取得する。
     *
     * @since 1.3
     */
    public ImageIcon getImage(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsStates partsStates, PartsDirections partsDirections, int y, int x) {
        if (partsVarieties == null || partsStandards == null || partsStates == null || partsDirections == null) {
            return null;
        }
        String pass = createPass(partsVarieties, partsStandards, partsStates, partsDirections, y, x);
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * イメージマスタから仮配置専用のアイコンリソースを取得する。
     *
     * @since 1.4
     */
    public ImageIcon getTempImage(PartsVarieties partsVarieties, PartsStandards partsStandards, PartsDirections partsDirections) {
        if (partsVarieties == null || partsStandards == null || partsDirections == null) {
            return null;
        }
        String pass = createTempPass(partsVarieties, partsStandards, partsDirections);
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * Circuitorのメインアイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getCircuitorIcon() {
        String pass = "Resource/_ICON/circuitor.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * キャンセルアイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getCancelIcon() {
        String pass = "Resource/_ICON/cancel.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 追加アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getAddIcon() {
        String pass = "Resource/_ICON/add.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 結合アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getBondIcon() {
        String pass = "Resource/_ICON/bond.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 部品移動アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getPartsMoveIcon() {
        String pass = "Resource/_ICON/parts_move.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 削除アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getDeleteIcon() {
        String pass = "Resource/_ICON/delete.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 移動アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getMoveIcon() {
        String pass = "Resource/_ICON/move.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 編集アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getEditIcon() {
        String pass = "Resource/_ICON/edit.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 実行開始アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getExecuteStartIcon() {
        String pass = "Resource/_ICON/execute_start.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 実行停止アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getExecuteStopIcon() {
        String pass = "Resource/_ICON/execute_stop.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 関数アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getFunctionIcon() {
        String pass = "Resource/_ICON/function.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 変数アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getVariableIcon() {
        String pass = "Resource/_ICON/variable.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 一次元配列アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getOneDimensionArrayIcon() {
        String pass = "Resource/_ICON/oneDarray.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 二次元配列アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getTwoDimensionArrayIcon() {
        String pass = "Resource/_ICON/twoDarray.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 部品複製アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getPartsCopyIcon() {
        String pass = "Resource/_ICON/parts_copy.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 部品回転アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getPartsRotateIcon() {
        String pass = "Resource/_ICON/parts_rotate.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 部品削除アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getPartsDeleteIcon() {
        String pass = "Resource/_ICON/parts_delete.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 交点変更アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getCrossChangeIcon() {
        String pass = "Resource/_ICON/crossChange.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 端子結合アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getHandBondIcon() {
        String pass = "Resource/_ICON/handBond.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }

    /**
     * 基板位置初期化アイコンを取得する。
     *
     * @since 1.5
     */
    public ImageIcon getPositionResetIcon() {
        String pass = "Resource/_ICON/position_reset.png";
        /* マスタに登録されていなければ、生成して登録 */
        if (store(pass)) {
            return imageMaster.get(pass);
        }
        return null;
    }
}
