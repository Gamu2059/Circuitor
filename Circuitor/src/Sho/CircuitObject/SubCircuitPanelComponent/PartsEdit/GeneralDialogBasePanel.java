package Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit;

import KUU.GeneralComponent.GeneralItemPanel;
import Master.BorderMaster.BorderMaster;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * VariableDialogPanelに組み込むパネルの汎用クラス。
 */
public abstract class GeneralDialogBasePanel extends JPanel implements ComponentListener {
    protected double min, max;
    private JLabel label;
    private JPanel panel;
    protected JTextField textField;
    protected GeneralItemPanel confirmLabel;

    public GeneralDialogBasePanel(double status, double min, double max) {
        super();
        this.min = min;
        this.max = max;
        setLayout(null);
        addComponentListener(this);
        add(label = new JLabel(getLabelTitle()));
        add(panel = new JPanel());
        add(confirmLabel = new GeneralItemPanel(true, null, "確定"));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderMaster.getRegularBorder());
        panel.setBorder(BorderMaster.getRegularBorder());
        if (status < min) {
            status = min;
        } else if (status > max) {
            status = max;
        }
        panel.add(textField = new JTextField(String.valueOf(status), 80));
    }

    protected abstract String getLabelTitle();

    @Override
    public void componentResized(ComponentEvent e) {
        label.setBounds(0,0, getWidth(), 20);
        panel.setBounds(0, 20, getWidth(), getHeight() - 80);
        confirmLabel.setBounds(0,getHeight() - 60, getWidth(), 60);
        textField.setBounds((panel.getWidth() - 80) / 2,(panel.getHeight() - 20) / 2,80 ,20);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
