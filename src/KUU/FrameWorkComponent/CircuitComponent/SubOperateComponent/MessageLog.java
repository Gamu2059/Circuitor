package KUU.FrameWorkComponent.CircuitComponent.SubOperateComponent;

import KUU.BaseComponent.BaseFrame;

import javax.swing.*;

/**
 * メッセージログに表示するリスト。
 */
public class MessageLog extends JList<String> {
    private BaseFrame frame;
    private DefaultListModel<String> model;

    public MessageLog(BaseFrame frame) {
        super();
        this.frame = frame;
        model = new DefaultListModel<>();
        setModel(model);
    }

    public void addMessage(String[] message) {
        resetMessage();
        for (String s : message) {
            model.addElement(s);
        }
    }

    public void resetMessage() {
        model.clear();
    }
}
