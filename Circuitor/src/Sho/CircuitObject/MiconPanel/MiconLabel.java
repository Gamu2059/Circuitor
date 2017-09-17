package Sho.CircuitObject.MiconPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.BaseComponent.BasePanel;
import KUU.NewComponent.NewJLabel;
import Master.BorderMaster.BorderMaster;
import Master.ColorMaster.ColorMaster;
import Sho.CircuitObject.Circuit.TerminalDirection;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * マイコンのピン設定を行うためのラベルクラス。
 */
public class MiconLabel extends NewJLabel implements MouseListener {
    /**
     * ピン番号
     */
    private int index;

    public MiconLabel(BaseFrame frame, int index, String role) {
        super(frame);
        this.index = index;
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);
        setText(role);
        setBorder(BorderMaster.getRegularBorder());
        switch (role) {
            case "出力":
                addMouseListener(this);
                setBackground(ColorMaster.getOutColor());
                break;
            case "入力":
                addMouseListener(this);
                setBackground(ColorMaster.getInColor());
                break;
            default:
                addMouseListener(new MiconNotPinListener(frame));
                setBackground(ColorMaster.getNotSelectedColor());
                break;
        }
    }

    /**
     * クリックしたラベルに対応するマイコンのピンの入出力設定を変更する。
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        /* 出力だったなら入力に変更する */
        if (getFrame().getBasePanel().getMiconPanel().getMiconPin()[index] == TerminalDirection.OUT) {
            getFrame().getBasePanel().getMiconPanel().getMiconPin()[index] = TerminalDirection.IN;
            setBackground(ColorMaster.getInColor());
        }
        /* 入力だったなら出力に変更する */
        else if (getFrame().getBasePanel().getMiconPanel().getMiconPin()[index] == TerminalDirection.IN) {
            getFrame().getBasePanel().getMiconPanel().getMiconPin()[index] = TerminalDirection.OUT;
            setBackground(ColorMaster.getOutColor());
        }
        setText(getCorrespondText(getFrame().getBasePanel().getMiconPanel().getMiconPin()[index]));
        /* 対応の変更を通達する */
        getFrame().getBasePanel().getEditCircuitPanel().updateLink();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (getFrame().getBasePanel().getOverAllMode() != BasePanel.OverAllMode.EXECUTE) {
            getFrame().getHelpLabel().setText("このピンはクリックすることで入力用か出力用かを設定することが出来ます。");
        } else {
            getFrame().getHelpLabel().setText("現在、この画面は編集出来ません。");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        getFrame().getHelpLabel().setText("");
    }

    @Override
    public void handResize(int w, int h) {

    }

    /**
     * miconPinに保持された値と対応する文字列を返す。
     */
    private String getCorrespondText(TerminalDirection t) {
        switch (t) {
            case OUT:
                return "出力";
            case IN:
                return "入力";
            case POWER:
                return "電源";
            case GND:
                return "接地";
        }
        return null;
    }
}