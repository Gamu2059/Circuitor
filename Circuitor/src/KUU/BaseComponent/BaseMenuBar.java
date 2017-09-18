package KUU.BaseComponent;

import Master.ColorMaster.ColorMaster;

import javax.swing.*;
import java.awt.*;
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
    private BaseMenu menuLoad;
    /** ファイル > ロード > 回路ファイルを開く */
    private BaseMenuItem itemLoadCircuit;
    /** ファイル > ロード > 命令ファイルを開く */
    private BaseMenuItem itemLoadOrder;

    /** ファイル > モード > セーブ */
    private BaseMenu menuSave;
    /** ファイル > モード > 回路ファイルに保存 */
    private BaseMenuItem itemSaveCircuit;
    /** ファイル > モード > 命令ファイルに保存 */
    private BaseMenuItem itemSaveOrder;

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
        menuFile.add(menuLoad = new BaseMenu("開く"));
        menuLoad.add(itemLoadCircuit = new BaseMenuItem("回路ファイルを開く"));
        menuLoad.add(itemLoadOrder = new BaseMenuItem("命令ファイルを開く"));
        menuFile.add(menuSave = new BaseMenu("保存"));
        menuSave.add(itemSaveCircuit = new BaseMenuItem("回路ファイルを保存"));
        menuSave.add(itemSaveOrder = new BaseMenuItem("命令ファイルを保存"));
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
        itemLoadCircuit.addActionListener(this);
        itemLoadCircuit.setActionCommand("LoadCircuit");
        itemLoadOrder.addActionListener(this);
        itemLoadOrder.setActionCommand("LoadOrder");
        itemSaveCircuit.addActionListener(this);
        itemSaveCircuit.setActionCommand("SaveCircuit");
        itemSaveOrder.addActionListener(this);
        itemSaveOrder.setActionCommand("SaveOrder");
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
            case "LoadCircuit":
                frame.getCircuitIO().inputData();
                break;
            case "LoadOrder":
                frame.getMasterTerminal().getProgramIO().inputData();
                frame.updateOrderPanel(true);
                break;
            case "SaveCircuit":
                frame.getCircuitIO().outputData();
                break;
            case "SaveOrder":
                frame.getMasterTerminal().getProgramIO().outputData();
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

    public BaseMenu getMenuLoad() {
        return menuLoad;
    }

    public BaseMenu getMenuSave() {
        return menuSave;
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
