package KUU.GeneralComponent;

import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GeneralBoolPanel extends JLabel {
    public GeneralBoolPanel() {
        setBackground(ColorMaster.getSelectedColor());
        setText("true");
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                turn();
            }
        });
    }

    public void turn(){
        if (getBackground() == ColorMaster.getSelectedColor()){
            setBackground(ColorMaster.getSelectableColor());
            setText("false");
        } else {
            setBackground(ColorMaster.getSelectedColor());
            setText("true");
        }
    }

    public int getNum() {
        if (getBackground() == ColorMaster.getSelectedColor()) {
            return 1;
        } else {
            return 0;
        }
    }
}
