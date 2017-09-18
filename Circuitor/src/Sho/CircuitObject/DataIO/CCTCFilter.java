package Sho.CircuitObject.DataIO;

import DataIO.DataFilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Circuitorの回路データのフィルタクラス。
 */
public class CCTCFilter extends DataFilter {
    @Override
    public String getDescription() {
        return "Circuitor 回路データ";
    }
}