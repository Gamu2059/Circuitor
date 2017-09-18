package KUU.BaseComponent;

import Master.ColorMaster.ColorMaster;

import javax.swing.*;

/**
 * メニューバーの項目。
 */
public class BaseMenuItem extends JMenuItem {
    public BaseMenuItem(String s) {
        super(s);
        setBorder(null);
        setOpaque(true);
        setBackground(ColorMaster.getMenuColor());
    }
}
