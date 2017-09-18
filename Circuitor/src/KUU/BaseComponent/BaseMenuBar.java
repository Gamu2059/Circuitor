package KUU.BaseComponent;

import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * メニューバー。
 */
public class BaseMenuBar extends JMenuBar implements ActionListener {
    /** 大元のフレーム */
    private BaseFrame frame;

    /*****************/
    /** 大別メニュー */
    /*****************/
    /** ファイル */
    private BaseMenu menuFile;
    /** モード */
    private BaseMenu menuMode;
    /** 実行 */
    private BaseMenu menuExecute;
    /** ヘルプ */
    private BaseMenu menuHelp;

    /*********************/
    /** メニュー項目 */
    /*********************/
    /** ファイル > 新規 */
    private BaseMenuItem itemNewWindow;

    /** ファイル > ロード */
    private BaseMenuItem itemLoad;

    /** ファイル > セーブ */
    private BaseMenuItem itemSave;

    /** ファイル > 終了 */
    private BaseMenuItem itemProgramEnd;

    /** モード > 回路 */
    private BaseMenuItem itemModeCircuit;
    /** モード > 命令 */
    private BaseMenuItem itemModeOrder;

    /** 実行 > 実行 */
    private BaseMenuItem itemExecution;

    /** ヘルプ > ヘルプ */
    private BaseMenuItem itemHelp;

    public BaseMenuBar(BaseFrame frame) {
        super();
        this.frame = frame;
        /* 背景設定 */
        setBackground(ColorMaster.getMenuColor());
        setOpaque(true);
        /* 大別項目の追加 */
        add(menuFile = new BaseMenu("ファイル"));
        add(menuMode = new BaseMenu("モード"));
        add(menuExecute = new BaseMenu("実行"));
        add(menuHelp = new BaseMenu("ヘルプ"));

        /* ファイルメニューの登録 */
        menuFile.add(itemNewWindow = new BaseMenuItem("新規"));
        menuFile.add(itemLoad = new BaseMenuItem("開く"));
        menuFile.add(itemSave = new BaseMenuItem("保存"));
        menuFile.add(itemProgramEnd = new BaseMenuItem("終了"));
        /* モードメニューの登録 */
        menuMode.add(itemModeCircuit = new BaseMenuItem("回路モード"));
        menuMode.add(itemModeOrder = new BaseMenuItem("命令モード"));
        /* 実行メニューの登録 */
        menuExecute.add(itemExecution = new BaseMenuItem("実行する"));
        /* ヘルプメニューの登録 */
        menuHelp.add(itemHelp = new BaseMenuItem("説明書"));

        /* リスナの登録 */
        itemNewWindow.addActionListener(this);
        itemNewWindow.setActionCommand("NewWindow");
        itemLoad.addActionListener(this);
        itemLoad.setActionCommand("Load");
        itemSave.addActionListener(this);
        itemSave.setActionCommand("Save");
        itemProgramEnd.addActionListener(this);
        itemProgramEnd.setActionCommand("ProgramEnd");
        itemModeCircuit.addActionListener(this);
        itemModeCircuit.setActionCommand("ModeCircuit");
        itemModeOrder.addActionListener(this);
        itemModeOrder.setActionCommand("ModeOrder");
        itemExecution.addActionListener(this);
        itemExecution.setActionCommand("Execute");
        itemHelp.addActionListener(this);
        itemHelp.setActionCommand("Help");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            /** ファイル */
            case "NewWindow":
                new BaseFrame();
                break;
            case "Load":
                frame.getCctIO().inputData();
                frame.updateOrderPanel(true);
                break;
            case "Save":
                frame.getCctIO().outputData();
                break;
            case "ProgramEnd":
                frame.disposeCircuitor();
                break;
            /** モード */
            case "ModeCircuit":
                frame.getBasePanel().getModeSelectPanel().getCircuitLabel().setBackground(ColorMaster.getSelectedColor());
                frame.getBasePanel().getModeSelectPanel().getOrderLabel().setBackground(ColorMaster.getNotSelectedColor());
                frame.getBasePanel().setDisplay(BasePanel.OverAllMode.CIRCUIT);
                break;
            case "ModeOrder":
                frame.getBasePanel().getModeSelectPanel().getCircuitLabel().setBackground(ColorMaster.getNotSelectedColor());
                frame.getBasePanel().getModeSelectPanel().getOrderLabel().setBackground(ColorMaster.getSelectedColor());
                frame.getBasePanel().setDisplay(BasePanel.OverAllMode.ORDER);
                break;
            /** 実行 */
            case "Execute":
                frame.getBasePanel().runExecuteMode();
                break;
            /** ヘルプ */
            case "Help":
                new HelpBrowser(frame);
                break;
        }
    }

    public BaseMenuItem getItemLoad() {
        return itemLoad;
    }

    public BaseMenuItem getItemSave() {
        return itemSave;
    }

    public BaseMenuItem getItemModeCircuit() {
        return itemModeCircuit;
    }

    public BaseMenuItem getItemModeOrder() {
        return itemModeOrder;
    }

    public BaseMenuItem getItemExecution() {
        return itemExecution;
    }
}
