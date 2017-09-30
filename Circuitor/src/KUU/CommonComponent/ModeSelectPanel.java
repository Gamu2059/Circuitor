package KUU.CommonComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.BaseComponent.BasePanel;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * モード選択を行う画面のパネル。
 * モードによらず共通的に配置される。
 */
public class ModeSelectPanel extends NewJPanel implements MouseListener {
    /**
     * 表示用ラベル
     */
    private JLabel indicateLabel;
    /**
     * 回路切替ボタン
     */
    private JLabel circuitLabel;
    /**
     * 命令切替ボタン
     */
    private JLabel orderLabel;

    public ModeSelectPanel(BaseFrame frame) {
        super(frame);
        /* コンポーネントの登録 */
        setLayout(null);
        add(indicateLabel = new JLabel("モード切替"));
        add(circuitLabel = new JLabel("回路"));
        add(orderLabel = new JLabel("命令"));
        /* コンポーネントの設定 */
        indicateLabel.setBackground(ColorMaster.getNotSelectedColor());
        circuitLabel.setBackground(ColorMaster.getSelectedColor());
        orderLabel.setBackground(ColorMaster.getNotSelectedColor());
        /* 背景色の表示 */
        indicateLabel.setOpaque(true);
        circuitLabel.setOpaque(true);
        orderLabel.setOpaque(true);
        /* ボーダの設定 */
        LineBorder border = new LineBorder(Color.BLACK);
        indicateLabel.setBorder(border);
        circuitLabel.setBorder(border);
        orderLabel.setBorder(border);
        /* 文字の表示位置の設定 */
        indicateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        circuitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        /* リスナの設定 */
        circuitLabel.addMouseListener(this);
        orderLabel.addMouseListener(this);
    }

    /**
     * 実行開始と実行終了時に呼び出すメソッド。
     * @param state trueは開始を意味する。開始した場合はリスナが退避されて何もできなくなる。
     *               falseは終了を意味する。終了した場合はリスナが回復する。
     */
    public void setExecuteState(boolean state) {
        if (state) {
            circuitLabel.removeMouseListener(this);
            orderLabel.removeMouseListener(this);
            circuitLabel.setEnabled(false);
            orderLabel.setEnabled(false);
            indicateLabel.setEnabled(false);
        } else {
            circuitLabel.addMouseListener(this);
            orderLabel.addMouseListener(this);
            circuitLabel.setEnabled(true);
            orderLabel.setEnabled(true);
            indicateLabel.setEnabled(true);
        }
    }

    @Override
    public void handResize(int width, int height) {
        indicateLabel.setBounds(0, 0, width, 20);
        circuitLabel.setBounds(0, 20, width / 2, height - 20);
        orderLabel.setBounds(width / 2, 20, width - width / 2, height - 20);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        circuitLabel.setBackground(ColorMaster.getNotSelectedColor());
        orderLabel.setBackground(ColorMaster.getNotSelectedColor());
        JLabel label;
        if (e.getSource() instanceof JLabel) {
            label = (JLabel) e.getSource();
            /* ラベルの色の変更 */
            label.setBackground(ColorMaster.getSelectedColor());
            if (label == circuitLabel) {
                getFrame().getBasePanel().setDisplay(BasePanel.OverAllMode.CIRCUIT);
            } else if (label == orderLabel) {
                getFrame().getBasePanel().setDisplay(BasePanel.OverAllMode.ORDER);
                getFrame().setOrderPanelCanClick(false, false, true);
                getFrame().updateOrderPanel(true);
                getFrame().getBasePanel().getEditOrderPanel().setLineNumber(0);
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {
        getFrame().getHelpLabel().setText("");
    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public JLabel getCircuitLabel() {
        return circuitLabel;
    }

    public JLabel getOrderLabel() {
        return orderLabel;
    }
}
