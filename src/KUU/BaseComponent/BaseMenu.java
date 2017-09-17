package KUU.BaseComponent;

import Master.ColorMaster.ColorMaster;

import javax.swing.*;

/**
 * メニューバーの大別項目。
 */
public class BaseMenu extends JMenu {
    public BaseMenu(String s) {
        super(s);
        setBorder(null);
        setOpaque(true);
        setBackground(ColorMaster.getMenuColor());
    }
}
