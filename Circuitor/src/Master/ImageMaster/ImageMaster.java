package Master.ImageMaster;

import Sho.CircuitObject.Circuit.ElecomInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.net.URL;
import java.util.HashMap;

import static Master.ImageMaster.PartsDirections.*;
import static Master.ImageMaster.PartsStandards.*;
import static Master.ImageMaster.PartsVarieties.*;

/**
 * 電子部品の画像のリソースを一括管理するクラス。
 * @author 翔
 * @version 1.0.3
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

    private TransparentFilter filter = new TransparentFilter();
    private StringBuilder builder = new StringBuilder();

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
     * StringBuilderの文字列を全て削除する。
     */
    private void stringReset() {
        builder.delete(0, builder.length());
    }

    private int getIntFromPartsStates(ElecomInfo e) {
        switch (e.getPartsStates()) {
            case ON:
                return 1;
            case OFF:
                return 0;
            default:
                return -1;
        }
    }

    /**
     * 与えられた向きに応じた数値が返される。
     */
    public static int getIntFromPartsDirection(ElecomInfo e) {
        boolean f = isOnlyTwoDirections(e);
        return getIntFromPartsDirection(e.getPartsDirections()) % (f ? 2 : 4);
    }

    public static int getIntFromPartsDirection(PartsDirections d) {
        switch (d) {
            case UP:
                return 0;
            case RIGHT:
                return 1;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * 状態のみで情報を特定できる場合はtrueを返す。
     * そうでない場合はfalseを返す。
     */
    public static boolean isOnlyStatesParts(ElecomInfo e) {
        switch (e.getPartsVarieties()) {
            case LOGIC_IC:
            case WIRE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 向きが二つしか存在しない場合はtrueを返す。
     * そうでない場合はfalseを返す。
     */
    public static boolean isOnlyTwoDirections(ElecomInfo e) {
        switch (e.getPartsVarieties()) {
            case PULSE:
            case RESISTANCE:
            case SWITCH:
                return true;
            default:
                return false;
        }
    }

    /**
     * 部品の種類によって向きごとの画像を参照するか否かを判断してファイル名をObjectクラスで返す。
     */
    private Object getStates(ElecomInfo e) {
        if (isOnlyStatesParts(e)) {
            return getIntFromPartsStates(e);
        } else {
            return getIntFromPartsStates(e) + "_" + getIntFromPartsDirection(e);
        }
    }

    /**
     * リソースをストアする。
     * 引数がtrueの場合は本設置、falseの場合は仮設置の画像を参照する。
     */
    private boolean store(String path) {
        /* マスタに登録されていなければ、生成して登録 */
        if (!urlMaster.containsKey(path)) {
            try {
                urlMaster.put(path, getClass().getClassLoader().getResource(path));
                // 透過した画像と普通の画像を保存する
                Toolkit tk = Toolkit.getDefaultToolkit();
                Image image = tk.createImage(urlMaster.get(path));
                image = tk.createImage(new FilteredImageSource(image.getSource(), filter));
                imageMaster.put(path + false, new ImageIcon(image));
                imageMaster.put(path + true, new ImageIcon(urlMaster.get(path)));
            }catch (NullPointerException e) {
                System.out.println("ImageMasterで例外が発生しました。");
                System.out.println("例外が発生したパス：" + path);
                return false;
            }
        }
        return true;
    }

    /**
     * ElecomInfoで部品の特徴を把握できない場合はtrueを返す。
     */
    private boolean isNull(ElecomInfo e) {
        return e.getPartsVarieties() == null || e.getPartsStandards() == null || e.getPartsDirections() == null || e.getPartsStates() == null;
    }

    /**
     * 部品のパスを作成する。
     * 画像統括バージョン。
     */
    private String createPass(ElecomInfo e) {
        stringReset();
        return  builder.append("Resource/").append(e.getPartsVarieties()).append('/').append(e.getPartsStandards()).append('/').append(getStates(e)).append(".png").toString();
    }

    /**
     * 部品パネル専用のパスを作成する。
     * 画像統括バージョン。
     */
    private String createModelPass(ElecomInfo e) {
        stringReset();
        return builder.append("Resource/").append(e.getPartsVarieties()).append('/').append(e.getPartsStandards()).append("/MODEL.png").toString();
    }

    /**
     * イメージマスタから部品パネル専用のアイコンリソースを取得する。
     * 画像統括バージョン。
     */
    public ImageIcon getModelImage(ElecomInfo e) {
        if (e.getPartsVarieties() == null || e.getPartsStandards() == null) {
            return null;
        }
        String path = createModelPass(e);
        if (store(path)) {
            return imageMaster.get(path + true);
        }
        return null;
    }

    /**
     * イメージマスタからアイコンリソースを取得する。
     * 画像統括バージョン。
     */
    public ImageIcon getImage(ElecomInfo e) {
        if (isNull(e)) {
            return null;
        }
        String path = createPass(e);
        if (store(path)) {
            return imageMaster.get(path + true);
        }
        return null;
    }

    /**
     * イメージマスタから仮配置専用のアイコンリソースを取得する。
     * 画像統括バージョン。
     */
    public ImageIcon getTempImage(ElecomInfo e) {
        if (isNull(e)) {
            return null;
        }
        String path = createPass(e);
        if (store(path)) {
            return imageMaster.get(path + false);
        }
        return null;
    }

    private ImageIcon getGeneralIcon(String path) {
        stringReset();
        path = builder.append("Resource/_ICON/").append(path).append(".png").toString();
        if (store(path)) {
            return imageMaster.get(path + true);
        }
        return null;
    }

    /**
     * Circuitorのメインアイコンを取得する。
     */
    public ImageIcon getCircuitorIcon() {
        return getGeneralIcon("circuitor");
    }

    /**
     * キャンセルアイコンを取得する。
     */
    public ImageIcon getCancelIcon() {
        return getGeneralIcon("cancel");
    }

    /**
     * 追加アイコンを取得する。
     */
    public ImageIcon getAddIcon() {
        return getGeneralIcon("add");
    }

    /**
     * 結合アイコンを取得する。
     */
    public ImageIcon getBondIcon() {
        return getGeneralIcon("bond");
    }

    /**
     * 部品移動アイコンを取得する。
     */
    public ImageIcon getPartsMoveIcon() {
        return getGeneralIcon("parts_move");
    }

    /**
     * 削除アイコンを取得する。
     */
    public ImageIcon getDeleteIcon() {
        return getGeneralIcon("delete");
    }

    /**
     * 移動アイコンを取得する。
     */
    public ImageIcon getMoveIcon() {
        return getGeneralIcon("move");
    }

    /**
     * 編集アイコンを取得する。
     */
    public ImageIcon getEditIcon() {
        return getGeneralIcon("edit");
    }

    /**
     * 実行開始アイコンを取得する。
     */
    public ImageIcon getExecuteStartIcon() {
        return getGeneralIcon("execute_start");
    }

    /**
     * 実行停止アイコンを取得する。
     */
    public ImageIcon getExecuteStopIcon() {
        return getGeneralIcon("execute_stop");
    }

    /**
     * 関数アイコンを取得する。
     */
    public ImageIcon getFunctionIcon() {
        return getGeneralIcon("function");
    }

    /**
     * 変数アイコンを取得する。
     */
    public ImageIcon getVariableIcon() {
        return getGeneralIcon("variable");
    }

    /**
     * 一次元配列アイコンを取得する。
     */
    public ImageIcon getOneDimensionArrayIcon() {
        return getGeneralIcon("oneDarray");
    }

    /**
     * 二次元配列アイコンを取得する。
     */
    public ImageIcon getTwoDimensionArrayIcon() {
        return getGeneralIcon("twoDarray");
    }

    /**
     * 部品複製アイコンを取得する。
     */
    public ImageIcon getPartsCopyIcon() {
        return getGeneralIcon("parts_copy");
    }

    /**
     * 部品回転アイコンを取得する。
     */
    public ImageIcon getPartsRotateIcon() {
        return getGeneralIcon("parts_rotate");
    }

    /**
     * 部品削除アイコンを取得する。
     */
    public ImageIcon getPartsDeleteIcon() {
        return getGeneralIcon("parts_delete");
    }

    /**
     * 交点変更アイコンを取得する。
     */
    public ImageIcon getCrossChangeIcon() {
        return getGeneralIcon("crossChange");
    }

    /**
     * 端子結合アイコンを取得する。
     */
    public ImageIcon getHandBondIcon() {
        return getGeneralIcon("handBond");
    }

    /**
     * 基板位置初期化アイコンを取得する。
     */
    public ImageIcon getPositionResetIcon() {
        return getGeneralIcon("position_reset");
    }
}
