package KUU.BaseComponent;

import DataIO.CctIO;
import KUU.Mode.MainOrderVariableMode;
import Master.ImageMaster.ImageMaster;
import ProcessTerminal.MasterTerminal;
import Sho.CircuitObject.DataIO.CircuitIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Circuitorのウィンドウを構成するクラス。
 * 複製可能な構造をしている。
 */
public class BaseFrame extends JFrame implements ComponentListener,WindowStateListener,WindowListener {
    /**
     * メニューバーとヘルプ以外のすべてのコンポーネントを管理
     */
    private BasePanel basePanel;
    /**
     * ヘルプ表示
     */
    private HelpLabel helpLabel;
    /**
     * メニューバー
     */
    private BaseMenuBar baseMenuBar;
    /**
     * データの入出力オブジェクト
     */
    private CctIO cctIO;
    /**
     * プログラム部
     */
    private MasterTerminal masterTerminal;

    public BaseFrame() {
        super();

        basePanel = new BasePanel(this);
        helpLabel = new HelpLabel(this);
        baseMenuBar = new BaseMenuBar(this);

        /** プログラム制御オブジェクト */
        masterTerminal = new MasterTerminal(this);

        /** IOオブジェクト(MasterTerminalを内部で参照しているため、MasterTerminalの生成前にCctIOを生成してはいけない) */
        cctIO = new CctIO(this);

        /** CircuitUnitオブジェクトの生成 */
        basePanel.getEditCircuitPanel().createCircuitUnit();
        basePanel.getSubOrderPanel().getOrderUnitPanel().createCircuitUnit();
        basePanel.getEditExecutePanel().createCircuitUnit();

        /** レイアウトの設定 */
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        /* ベースパネルのレイアウト設定 */
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weighty = 0.9;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        layout.setConstraints(basePanel, gbc);
        /* ヘルプラベルのレイアウト設定 */
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        layout.setConstraints(helpLabel, gbc);

        /** コンポーネントの追加 */
        setJMenuBar(baseMenuBar);
        getContentPane().add(basePanel);
        getContentPane().add(helpLabel);

        /** その他の設定 */
        /* ウィンドウを最大化して表示 */
        setBounds(new Rectangle(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()));
        /* リスナの設定 */
        addComponentListener(this);
        addWindowStateListener(this);
        addWindowListener(this);
        setTitle("Circuitor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(ImageMaster.getImageMaster().getCircuitorIcon().getImage());
        setVisible(true);
    }

    /**
     * ウィンドウ単位でアプリケーションを終了する時に呼び出す。
     */
    public void disposeCircuitor() {
        getBasePanel().stopExecuteMode();
        dispose();
    }

    /**
     * サブ操作パネル、エディタパネルの更新を行う。
     * 関数リスト表示中は選択中が抜けないようにサブ操作パネルは更新しないが、
     * 関数リスト表示中でも強制的に更新を行う場合は、flgにtrueが入る。
     */
    public void updateOrderPanel(boolean flg){
        if ((basePanel.getMainOrderPanel().getVariableMode() != MainOrderVariableMode.FUNCTION) || flg) {
            basePanel.getSubOrderPanel().updateVariableList();
        }
        basePanel.getEditOrderPanel().setNowFunctionName();
        basePanel.getEditOrderPanel().updateProgramList();
        basePanel.getSubOrderPanel().setLineNumber(-1);
    }

    /** 編集パネル、削除パネル、命令挿入パネルがクリックできるようにするかを設定する */
    public void setOrderPanelCanClick(boolean variableFlg, boolean programFlg, boolean addFlg){
        basePanel.getSubOrderPanel().setVariableEditDeleteCanClick(variableFlg);
        basePanel.getEditOrderPanel().setProgramEditDeleteCanClick(programFlg);
        basePanel.getEditOrderPanel().getSelectOrderPanel().setAddCanClick(addFlg);
    }

    /**
     * getter
     */
    public BasePanel getBasePanel() {
        return basePanel;
    }

    public HelpLabel getHelpLabel() {
        return helpLabel;
    }

    public BaseMenuBar getBaseMenuBar() {
        return baseMenuBar;
    }

    public CctIO getCctIO() {
        return cctIO;
    }

    public MasterTerminal getMasterTerminal() {
        return masterTerminal;
    }

    /************************/
    /** WindowStateListener */
    /************************/
    /**
     * ウィンドウを最大化した時にリサイズする。
     */
    @Override
    public void windowStateChanged(WindowEvent e) {
        /* 最大化ボタンを押したときのみtrueになる */
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            setBounds(new Rectangle(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()));
        }
        basePanel.handResize();
    }

    /**********************/
    /** ComponentListener */
    /**********************/
    /**
     * ウィンドウのサイズを変更した時にリサイズする。
     */
    @Override
    public void componentResized(ComponentEvent e) {
        basePanel.handResize();
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

    /*******************/
    /** WindowListener */
    /*******************/
    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * クローズボタンを押した時の処理。
     * また、クローズされるのは自分自身だけ。
     */
    @Override
    public void windowClosing(WindowEvent e) {
        disposeCircuitor();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
