package DataIO;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Circuitorのデータの抽象フィルタクラス。
 * 拡張子の説明メソッドをオーバーライドする必要がある。
 */
public abstract class DataFilter extends FileFilter {
    /** 拡張子。 */
    private String ext;

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return isContain(f);
    }

    /**
     * 拡張子を抽出する。
     * 全ウィンドウで使えるためstatic。
     */
    public static String getExtension(File f){
        String ext = null;
        String filename = f.getName();
        int dotIndex = filename.lastIndexOf('.');

        if ((dotIndex > 0) && (dotIndex < filename.length() - 1)){
            ext = filename.substring(dotIndex + 1).toLowerCase();
        }

        return ext;
    }

    /**
     * 指定したファイルをフィルタにかけた時に、表示させるかどうかを判定する。
     * 適切な拡張子であればtrueを、そうでなければfalseを返す。
     */
    boolean isContain(File f) {
        String ext = getExtension(f);
        return ext != null && ext.equals(this.ext);
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}