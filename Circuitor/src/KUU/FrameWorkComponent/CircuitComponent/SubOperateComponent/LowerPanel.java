package KUU.FrameWorkComponent.CircuitComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.*;

/**
 * キャンセルラベルの下側のパネル。
 * メッセージログとして使用する。
 */
public class LowerPanel extends NewJPanel{
    private MessageLog log;
    private JScrollPane scrollPane;

    public LowerPanel(BaseFrame frame){
        super(frame);
        setLayout(null);
        setOpaque(true);
        scrollPane = new JScrollPane();
        scrollPane.getViewport().setView(log = new MessageLog(frame));
        add(scrollPane);
    }

    public MessageLog getLog() {
        return log;
    }

    @Override
    public void handResize(int width, int height) {
        scrollPane.setBounds(0, 0, width, height);
    }
}
