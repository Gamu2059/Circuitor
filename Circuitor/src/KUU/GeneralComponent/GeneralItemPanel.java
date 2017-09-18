package KUU.GeneralComponent;

import Master.ColorMaster.ColorMaster;
import Master.FontMaster.FontMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 操作パネルの汎用的なパネルクラス。
 */

public class GeneralItemPanel extends JPanel implements MouseListener {
    /**
     * パネルに表示するアイコン。
     */
    private ImageIcon icon;
    /**
     * パネルに表示するテキスト。
     */
    private String text;
    /**
     * クリックされた後に再選択可能かどうかを示す。
     * そもそもこのフィールドが初期化されないようであればクリックできない。
     */
    private Boolean reselectable;
    /**
     * リスナが呼び出された時に、対応する処理を行うかどうかを判断する。
     * リスナを直接的に剥奪すると不具合が生じ、最悪の場合はエラーが発生するので剥奪はしない。
     */
    private boolean canBehave;
    /**
     * マウスリスナの重要な処理があるメソッドが呼び出されたことをカウントする変数。
     * 注意：前提として、各ボタン固有の処理を持つメソッドが、一つのリスナにつき一つまでであること。
     * 　　　そうしないと正確な呼び出し回数が記録できない。
     * 　　　もしもClickedとPressedのような別々の動作に別々の処理を持たせたければ、それぞれを別のリスナで記述すること。
     */
    private int processNum;

    public GeneralItemPanel(String text) {
        this.text = text;
        setBackground(ColorMaster.getNotSelectedColor());
        setFont(FontMaster.getRegularFont());
        canBehave = false;
        processNum = 0;
    }

    public GeneralItemPanel(Boolean reselectable, ImageIcon icon, String text) {
        this(text);
        this.icon = icon;
        this.reselectable = reselectable;
        if (reselectable != null) {
            addMouseListener(this);
            if(reselectable){
                setBackground(ColorMaster.getSelectableColor());
            }else {
                setBackground(ColorMaster.getBackColor());
            }
        }
    }

    /**
     * icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * text
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * canBehave
     */
    public boolean isCanBehave() {
        return canBehave;
    }

    /**
     * 処理の完了したリスナの数をカウントし、合計数がリスナの総数と一致したらパネルのマウス処理を無効化する。
     * このパネルでの各リスナの各メソッドで処理の最後に呼び出す必要がある。
     */
    public void processFinished() {
        if (reselectable != null) {
            if (!reselectable && canBehave) {
                processNum++;
                if (processNum == getMouseListeners().length) {
                    disablePanel();
                }
            }
        }
    }

    /**
     * パネルを再選択可能にします。
     * ただし、reselectableがfalseのパネルしか処理されません。
     */
    public void enablePanel() {
        if (reselectable != null) {
            if (!reselectable && !canBehave) {
                setBackground(ColorMaster.getSelectableColor());
                canBehave = true;
            }
        }
    }

    /**
     * パネルを再選択不可能にします。
     * ただし、reselectableがfalseのパネルしか処理されません。
     * 注意：パネルのマウス動作に関わる処理から直接的に呼び出さないで下さい。競合問題が発生する可能性があります。
     */
    public void disablePanel() {
        if (reselectable != null) {
            if (!reselectable && canBehave) {
                setBackground(ColorMaster.getBackColor());
                canBehave = false;
                processNum = 0;
            }
        }
    }

    /**
     * 文字列をパネル中心に描画します。
     * アイコンは左詰めして描画します。
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (icon != null) {
            g.drawImage(icon.getImage(), 0, 0, getHeight(), getHeight(), this);
        }
        if (!text.isEmpty()) {
            g.drawString(text, (getWidth() - g.getFontMetrics().stringWidth(text)) / 2, (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent());
        }
        g.setColor(ColorMaster.getRegularBorderColor());
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * 押された瞬間に背景色を変えます。
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (reselectable != null) {
            if (!reselectable && !canBehave) {
                return;
            }
        }
        setBackground(ColorMaster.getClickedColor());
    }

    /**
     * 離された瞬間に背景色を変えます。
     * reselectableがfalseの場合は一時的にリスナが削除されます。
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (reselectable != null) {
            if (reselectable) {
                setBackground(ColorMaster.getSelectableColor());
            } else {
                if (canBehave) {
                    processFinished();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
