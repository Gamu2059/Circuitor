package KUU.BaseComponent;

import KUU.CommonComponent.MiconPanel;
import KUU.CommonComponent.ModeSelectPanel;
import KUU.FrameWorkComponent.CircuitComponent.*;
import KUU.FrameWorkComponent.OrderComponent.*;
import KUU.FrameWorkComponent.ExecuteComponent.*;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;

import java.awt.*;

/**
 * フレーム直下に位置する最上位のパネル。
 */
public class BasePanel extends NewJPanel {
    /** エディタ等の全体的なモードを把握するための列挙型 */
    public enum OverAllMode {
        CIRCUIT,
        ORDER,
        EXECUTE,
    }

    /** 全体的なモードを保持する */
    private OverAllMode overAllMode;
    /** 実行直前のモードを保持する */
    private OverAllMode preMode;

    /** モード選択パネル */
    private ModeSelectPanel modeSelectPanel;
    /** マイコン設定パネル */
    private MiconPanel miconPanel;

    /***********************/
    /** 汎用フレームワーク */
    /***********************/
    /** メイン操作パネル */
    private NewJPanel mainPanel;
    /** サブ操作パネル */
    private NewJPanel subPanel;
    /** エディタパネル */
    private NewJPanel editPanel;

    /***************************/
    /** モード別フレームワーク */
    /***************************/
    /** メイン操作パネル */
    private MainCircuitPanel mainCircuitPanel;
    private MainOrderPanel mainOrderPanel;
    private MainExecutePanel mainExecutePanel;
    /** サブ操作パネル */
    private SubCircuitPanel subCircuitPanel;
    private SubOrderPanel subOrderPanel;
    private SubExecutePanel subExecutePanel;
    /** エディタパネル */
    private EditCircuitPanel editCircuitPanel;
    private EditOrderPanel editOrderPanel;
    private EditExecutePanel editExecutePanel;

    public BasePanel(BaseFrame frame) {
        super(frame);
        /* コンポーネントの生成(追加はしない) */
        setLayout(null);
        setBackground(ColorMaster.getMenuColor());
        add(modeSelectPanel = new ModeSelectPanel(frame));
        add(miconPanel = new MiconPanel(frame));
        add(mainCircuitPanel = new MainCircuitPanel(frame));
        add(mainOrderPanel = new MainOrderPanel(frame));
        add(mainExecutePanel = new MainExecutePanel(frame));
        add(subCircuitPanel = new SubCircuitPanel(frame));
        add(subOrderPanel = new SubOrderPanel(frame));
        add(subExecutePanel = new SubExecutePanel(frame));
        add(editCircuitPanel = new EditCircuitPanel(frame));
        add(editOrderPanel = new EditOrderPanel(frame));
        add(editExecutePanel = new EditExecutePanel(frame));
        preMode = OverAllMode.CIRCUIT;
        setDisplay(OverAllMode.CIRCUIT);
    }

    @Override
    public void handResize(int width, int height) {
        /* 共通配置 */
        modeSelectPanel.setBounds(0, (height / 12) * 8, width / 6, height / 12);
        modeSelectPanel.handResize(width / 6, height / 12);
        miconPanel.setBounds(0, (height / 12) * 9, width / 6, height - (height / 12) * 9);
        miconPanel.handResize(width / 6, height - (height / 12) * 9);
        mainPanel.setBounds(0, 0, width / 6, (height / 12) * 8);
        mainPanel.handResize(width / 6, (height / 12) * 8);
        subPanel.setBounds(width / 6, 0, width / 6, height);
        subPanel.handResize(width / 6, height);
        editPanel.setBounds((width / 6) * 2, 0, width - (width / 6) * 2, height);
        editPanel.handResize(width - (width / 6) * 2, height);
    }

    /**
     * 実行画面に切り替える。
     */
    public void runExecuteMode() {
        preMode = overAllMode;
        /* 実行開始 */
        if (getEditExecutePanel().getOperateOperate().startSimulation(getEditExecutePanel())) {
            setDisplay(OverAllMode.EXECUTE);
            miconPanel.setExecuteState(true);
            modeSelectPanel.setExecuteState(true);

            getFrame().getBaseMenuBar().getMenuLoad().setEnabled(false);
            getFrame().getBaseMenuBar().getMenuSave().setEnabled(false);
            getFrame().getBaseMenuBar().getItemModeCircuit().setEnabled(false);
            getFrame().getBaseMenuBar().getItemModeOrder().setEnabled(false);
            getFrame().getBaseMenuBar().getItemExecution().setEnabled(false);
            /* 念のため実行中断ボタンを初期化する */
            getMainExecutePanel().getExecuteStopLabel().setIcon(ImageMaster.getImageMaster().getExecuteStopIcon());
            getMainExecutePanel().getExecuteStopLabel().setText("実行を中断する");
        } else {
            stopExecuteMode();
        }
    }

    /**
     * 実行を終了する。
     * 実行専用スレッド以外は、必ずこのメソッドを呼び出して下さい。
     */
    public void stopExecuteMode() {
        /* 実行終了 */
        getEditExecutePanel().getOperateOperate().stopSimulation(getEditExecutePanel());
    }

    /**
     * 実行終了時の処理。
     * 実行専用メソッド以外からは絶対に呼び出さないで下さい。
     */
    public void autoCallStopExecute() {
        setDisplay(preMode);
        miconPanel.setExecuteState(false);
        modeSelectPanel.setExecuteState(false);
        getFrame().getBaseMenuBar().getMenuLoad().setEnabled(true);
        getFrame().getBaseMenuBar().getMenuSave().setEnabled(true);
        getFrame().getBaseMenuBar().getItemModeCircuit().setEnabled(true);
        getFrame().getBaseMenuBar().getItemModeOrder().setEnabled(true);
        getFrame().getBaseMenuBar().getItemExecution().setEnabled(true);
        /* 命令モードへの切り替えで変数を選択状態にする */
        if (overAllMode == OverAllMode.ORDER){
            switch (getFrame().getBasePanel().getMainOrderPanel().getVariableMode()){
                case FUNCTION:
                    getFrame().getBasePanel().getMainOrderPanel().getFunctionLabel().setBackground(ColorMaster.getSelectedColor());
                    break;
                case VARIABLE:
                    getFrame().getBasePanel().getMainOrderPanel().getVariableLabel().setBackground(ColorMaster.getSelectedColor());
                    break;
                case ARRAY:
                    getFrame().getBasePanel().getMainOrderPanel().getOneDimensionArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    break;
                case SQUARE:
                    getFrame().getBasePanel().getMainOrderPanel().getTwoDimensionArrayLabel().setBackground(ColorMaster.getSelectedColor());
                    break;
            }
        }
    }

    /**
     * 指定したモードに適した画面構成に変更する。
     * また、構成変更後、メソッド内で自動的にリサイズ検証が行われる。
     */
    public void setDisplay(OverAllMode mode) {
        overAllMode = mode;
        for (Component c : getComponents()) {
            c.setVisible(false);
        }
        /* コモンコンポーネントの追加 */
        modeSelectPanel.setVisible(true);
        miconPanel.setVisible(true);
        /* フレームワークコンポーネントの追加 */
        switch (overAllMode) {
            case CIRCUIT:
                mainPanel = mainCircuitPanel;
                subPanel = subCircuitPanel;
                editPanel = editCircuitPanel;
                break;
            case ORDER:
                mainPanel = mainOrderPanel;
                subPanel = subOrderPanel;
                editPanel = editOrderPanel;
                break;
            case EXECUTE:
                mainPanel = mainExecutePanel;
                subPanel = subExecutePanel;
                editPanel = editExecutePanel;
                break;
        }
        mainPanel.setVisible(true);
        subPanel.setVisible(true);
        editPanel.setVisible(true);
        handResize();
    }

    /**
     * リサイズ検証用メソッド。
     * これが呼び出された時、このベースパネルとその階層下にある全てのコンポーネントのサイズが検証されなければならない。
     * サイズ検証が全て終了した段階で階層の検証を行ってから再描画を行う。
     */
    public void handResize() {
        handResize(getWidth(), getHeight());
        repaint();
    }

    public OverAllMode getOverAllMode() {
        return overAllMode;
    }

    public ModeSelectPanel getModeSelectPanel() {
        return modeSelectPanel;
    }

    public MiconPanel getMiconPanel() {
        return miconPanel;
    }

    public MainCircuitPanel getMainCircuitPanel() {
        return mainCircuitPanel;
    }

    public MainOrderPanel getMainOrderPanel() {
        return mainOrderPanel;
    }

    public MainExecutePanel getMainExecutePanel() {
        return mainExecutePanel;
    }

    public SubCircuitPanel getSubCircuitPanel() {
        return subCircuitPanel;
    }

    public SubOrderPanel getSubOrderPanel() {
        return subOrderPanel;
    }

    public SubExecutePanel getSubExecutePanel() {
        return subExecutePanel;
    }

    public EditCircuitPanel getEditCircuitPanel() {
        return editCircuitPanel;
    }

    public EditOrderPanel getEditOrderPanel() {
        return editOrderPanel;
    }

    public EditExecutePanel getEditExecutePanel() {
        return editExecutePanel;
    }
}
