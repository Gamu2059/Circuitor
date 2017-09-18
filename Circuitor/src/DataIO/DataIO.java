package DataIO;

import KUU.BaseComponent.BaseFrame;
import DataIO.DataFilter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * データ入出力の形式を定める抽象クラス。
 */
public abstract class DataIO {
    /** 大元のフレーム。 */
    private BaseFrame frame;
    /** ファイルを開くためのオブジェクト */
    private JFileChooser fileChooser;
    /** 拡張子を制限するためのフィルタ。 */
    private DataFilter filter;
    /** 拡張子。 */
    private String ext;

    protected DataIO(BaseFrame frame, DataFilter filter, String ext) {
        this.frame = frame;
        this.filter = filter;
        this.ext = ext;
        this.filter.setExt(ext);
    }

    /** ファイルチューザを初めて開いた時に、実行ファイルの場所を指定する */
    private String getCurrentpath() {
        String cp = System.getProperty("java.class.path");
        String fs = System.getProperty("file.separator");
        String acp = (new File(cp)).getAbsolutePath();
        int p, q;
        for (p = 0; (q = acp.indexOf(fs, p)) >= 0; p = q + 1) ;
        return acp.substring(0, p);
    }

    /**
     * 保存用メソッド。
     * プログラムから保存する場合に用いる。
     */
    public final void outputData() {
        /* ファイルを選択した場合のみ処理を実行する */
        if (fileChooser == null) {
            fileChooser = new JFileChooser(getCurrentpath());
        }
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File("無題." + ext));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String name = file.getName();
            /* 後ろに拡張子がついていなければ追加する */
            if (!filter.isContain(file)) {
                name += "." + ext;
            }
            /* 保存場所を指定してファイルを更新する */
            file = new File(fileChooser.getCurrentDirectory().getPath()+"\\"+name);
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("ファイル生成に失敗しました。");
            }
            /* ファイルに回路データを出力する */
            if (file.canWrite()) {
                try {
                    outputter(file);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "データの保存に失敗しました。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "選択したファイルに保存することができません。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 具体的な保存アルゴリズムを記述する。
     * 保存に失敗した場合は、Exceptionオブジェクトを生成して、その場でthrowすること。
     */
    protected abstract void outputter(File file) throws Exception;

    /**
     * データファイルから起動した際の書き込み用メソッド。
     * ファイル起動の場合に用いる。
     */
    public final void inputData(String path) {
        File file = new File(path);
        if (file.canRead()) {
            try {
                inputter(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "ファイルが壊れています。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "選択したファイルを開くことができません。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 読み込み用メソッド。
     * プログラムから読み込み場合に用いる。
     */
    public final void inputData() {
        /* ファイルを選択した場合のみ処理を実行する */
        if (fileChooser == null) {
            fileChooser = new JFileChooser(getCurrentpath());
        }
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            /* ファイルから回路データを入力する */
            if (fileChooser.getSelectedFile().canRead()) {
                try {
                    inputter(fileChooser.getSelectedFile());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "ファイルが壊れています。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "選択したファイルを開くことができません。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 具体的な読み込みアルゴリズムを記述する。
     * 読み込みに失敗した場合は、Exceptionオブジェクトを生成して、その場でthrowすること。
     */
    protected abstract void inputter(File file) throws Exception;

    public BaseFrame getFrame() {
        return frame;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public DataFilter getFilter() {
        return filter;
    }

    public String getExt() {
        return ext;
    }
}
