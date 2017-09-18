package KUU.BaseComponent;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

/**
 * ヘルプ画面を表示する専用のダイアログ。
 * 生成した瞬間にモーダル状態で表示される。
 */
public class HelpBrowser extends JDialog {
    public HelpBrowser(BaseFrame frame) {
        super(frame, true);
        try {
            /* 表示するHTMLファイルの取得 */
            JEditorPane help = new JEditorPane(getClass().getClassLoader().getResource("Circuitor_help/Circuitor_main.html"));
            help.setContentType("text/html");
            help.setEditable(false);
            help.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            help.setPage(e.getURL());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "リンクの取得に失敗しました。");
                        }
                    }
                }
            });
            getContentPane().add(new JScrollPane(help));
            /* ダイアログの設定 */
            setTitle("ヘルプ");
            setBounds(new Rectangle(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()));
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
