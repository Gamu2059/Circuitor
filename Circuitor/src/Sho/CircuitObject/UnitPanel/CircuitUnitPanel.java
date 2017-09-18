package Sho.CircuitObject.UnitPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Master.ImageMaster.PartsDirections;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.Graphed.BranchNodeConnect;
import Sho.CircuitObject.Graphed.CircuitMatrixing;
import Sho.CircuitObject.Circuit.CircuitBorder.*;
import Sho.CircuitObject.Circuit.CircuitOperateMode.*;
import Sho.CircuitObject.Circuit.CircuitOperateCommand.*;
import Sho.CircuitObject.Circuit.CircuitOperateBehavior.*;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd.PartsButton;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.VariablePulseDialog;
import Sho.CircuitObject.SubCircuitPanelComponent.PartsEdit.VariableResistanceDialog;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * CircuitUnitを内包した回路エディタ用パネルクラス。
 */
public class CircuitUnitPanel extends UnitPanel {
    /*****************************/
    /** 回路解析専用オブジェクト */
    /*****************************/
    /**
     * 回路解析用オブジェクト
     */
    private BranchNodeConnect branchNodeConnect;
    /**
     * 閉路行列生成用オブジェクト
     */
    private CircuitMatrixing circuitMatrixing;

    /*************************************/
    /** 回路エディタの操作を管理する情報 */
    /*************************************/
    /**
     * 基板上に１つでも部品や結合点があればtrueになる。
     */
    private boolean nodeExist;
    /**
     * マウスの右ボタンで押下したかを保持する。
     */
    private boolean pressedRight;
    /**
     * 範囲指定の始点座標。
     */
    private IntegerDimension rangeStart;
    /**
     * 範囲指定の終点座標。
     */
    private IntegerDimension rangeEnd;
    /**
     * 移動、複製専用のドラッグ基準座標。
     */
    private IntegerDimension moveCursorCo;

    public CircuitUnitPanel(BaseFrame frame) {
        super(frame);
        /** リスナの設定 */
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        /** 回路解析用オブジェクトの生成 */
        branchNodeConnect = new BranchNodeConnect(frame);
        circuitMatrixing = new CircuitMatrixing(frame);
        /** その他初期化 */
        nodeExist = false;
        pressedRight = false;
        rangeStart = new IntegerDimension();
        rangeEnd = new IntegerDimension();
        moveCursorCo = new IntegerDimension();
    }

    /**
     * circuitUnitオブジェクトを生成する。
     * 特に、回路エディタでは空の状態で接続解析を行う。
     */
    @Override
    public void createCircuitUnit() {
        super.createCircuitUnit();
        getOperateOperate().update(this);
    }

    /**
     * branchNodeConnect
     */
    public BranchNodeConnect getBranchNodeConnect() {
        return branchNodeConnect;
    }

    public void setBranchNodeConnect(BranchNodeConnect branchNodeConnect) {
        this.branchNodeConnect = branchNodeConnect;
    }

    /**
     * circuitMatrixing
     */
    public CircuitMatrixing getCircuitMatrixing() {
        return circuitMatrixing;
    }

    public void setCircuitMatrixing(CircuitMatrixing circuitMatrixing) {
        this.circuitMatrixing = circuitMatrixing;
    }

    /**
     * nodeExist
     */
    public boolean isNodeExist() {
        return nodeExist;
    }

    public void setNodeExist(boolean nodeExist) {
        this.nodeExist = nodeExist;
    }

    /**
     * rangeStart
     */
    public IntegerDimension getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(IntegerDimension rangeStart) {
        this.rangeStart = rangeStart;
    }

    /**
     * rangeEnd
     */
    public IntegerDimension getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(IntegerDimension rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    /**
     * moveCursorCo
     */
    public IntegerDimension getMoveCursorCo() {
        return moveCursorCo;
    }

    public void setMoveCursorCo(IntegerDimension moveCursorCo) {
        this.moveCursorCo = moveCursorCo;
    }

    /**
     * 基板、ボーダ、部品の順番に描画する。
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;

        /* 基板の描画 */
        g2.setColor(ColorMaster.getSubstrateColor());
        getPaintRect().setRect(getPaintBaseCo().getWidth(), getPaintBaseCo().getHeight(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getWidth(), UNIT_PIXEL * getPaintRatio() * getCircuitSize().getHeight());
        g2.fill(getPaintRect());
        /* ボーダの描画 */
        for (int i = 0; i < getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < getCircuitSize().getWidth(); j++) {
                b = getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                if (b.getBorder() != null) {
                    g2.setColor(CircuitBorder.getColor(b.getBorder()));
                    getPaintRect().setRect(
                            UNIT_PIXEL * getPaintRatio() * j + getPaintBaseCo().getWidth() + 1,
                            UNIT_PIXEL * getPaintRatio() * i + getPaintBaseCo().getHeight() + 1,
                            UNIT_PIXEL * getPaintRatio() - 2,
                            UNIT_PIXEL * getPaintRatio() - 2
                    );
                    g2.fill(getPaintRect());
                }
            }
        }
        /* 部品の描画 */
        for (int i = 0; i < getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < getCircuitSize().getWidth(); j++) {
                b = getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                e = b.getElecomInfo();
                c = b.getCircuitInfo();
                if (b.isExist()) {
                    g2.drawImage(
                            ImageMaster.getImageMaster().getImage(e.getPartsVarieties(), e.getPartsStandards(), e.getPartsStates(), e.getPartsDirections(), c.getReco().getHeight(), c.getReco().getWidth()).getImage(),
                            UNIT_PIXEL * getPaintRatio() * j + getPaintBaseCo().getWidth(),
                            UNIT_PIXEL * getPaintRatio() * i + getPaintBaseCo().getHeight(),
                            UNIT_PIXEL * getPaintRatio(),
                            UNIT_PIXEL * getPaintRatio(),
                            this
                    );
                }
            }
        }
        /* 予測部品の描画 */
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_ADD:
                        if (getTmp() != null && getCursorCo() != null) {
                            IntegerDimension size = getTmp().getElecomInfo().getSize();
                            e = getTmp().getElecomInfo();
                            g2.drawImage(ImageMaster.getImageMaster().getTempImage(e.getPartsVarieties(), e.getPartsStandards(), e.getPartsDirections()).getImage(),
                                    UNIT_PIXEL * getPaintRatio() * (getCursorCo().getWidth() - size.getWidth() / 2) + getPaintBaseCo().getWidth(),
                                    UNIT_PIXEL * getPaintRatio() * (getCursorCo().getHeight() - size.getHeight() / 2) + getPaintBaseCo().getHeight(),
                                    UNIT_PIXEL * getPaintRatio() * size.getWidth(),
                                    UNIT_PIXEL * getPaintRatio() * size.getHeight(),
                                    this
                            );
                        }
                        break;
                }
                break;
            case WIRE_BOND:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case WIRE_BOND:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case RANGING:
                                ArrayList<CircuitBlock> blocks = getOperateOperate().predictionMatchWire(this);
                                if (blocks != null) {
                                    for (CircuitBlock b1 : blocks) {
                                        c = b1.getCircuitInfo();
                                        e = b1.getElecomInfo();
                                        g2.drawImage(ImageMaster.getImageMaster().getTempImage(e.getPartsVarieties(), e.getPartsStandards(), e.getPartsDirections()).getImage(),
                                                UNIT_PIXEL * getPaintRatio() * c.getAbco().getWidth() + getPaintBaseCo().getWidth(),
                                                UNIT_PIXEL * getPaintRatio() * c.getAbco().getHeight() + getPaintBaseCo().getHeight(),
                                                UNIT_PIXEL * getPaintRatio(),
                                                UNIT_PIXEL * getPaintRatio(),
                                                this
                                        );
                                    }
                                }
                                break;
                        }
                        break;
                }
                break;
            case PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case MOVE:
                                if (getCursorCo() != null && moveCursorCo != null && getTmps() != null) {
                                    int recoY, recoX, abcoY, abcoX;
                                    ElecomInfo e1;
                                    recoY = getCursorCo().getHeight() - moveCursorCo.getHeight();
                                    recoX = getCursorCo().getWidth() - moveCursorCo.getWidth();
                                    for (CircuitBlock b1 : getTmps()) {
                                        if (b1.getCircuitInfo().getReco().equals(0, 0)) {
                                            e1 = b1.getElecomInfo();
                                            abcoY = b1.getCircuitInfo().getAbco().getHeight() + recoY;
                                            abcoX = b1.getCircuitInfo().getAbco().getWidth() + recoX;
                                            g2.drawImage(ImageMaster.getImageMaster().getTempImage(e1.getPartsVarieties(), e1.getPartsStandards(), e1.getPartsDirections()).getImage(),
                                                    UNIT_PIXEL * getPaintRatio() * abcoX + getPaintBaseCo().getWidth(),
                                                    UNIT_PIXEL * getPaintRatio() * abcoY + getPaintBaseCo().getHeight(),
                                                    UNIT_PIXEL * getPaintRatio() * e1.getSize().getWidth(),
                                                    UNIT_PIXEL * getPaintRatio() * e1.getSize().getHeight(),
                                                    this
                                            );
                                        }
                                    }
                                }
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /**
     * マイコンのリンク情報を更新する。
     */
    public void updateLink() {
        CircuitBlock b;
        int abcoY, abcoX, sizeY, sizeX;
        PartsDirections dir;
        boolean flg = false;
        /* 回路上にマイコンが存在する場合、左上を複製し、再配置する */
        for (int i = 0; i < getCircuitUnit().getCircuitBlock().getMatrix().size(); i++) {
            for (int j = 0; j < getCircuitUnit().getCircuitBlock().getMatrix().get(0).size(); j++) {
                b = getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                if (b.getElecomInfo().getPartsVarieties() == PartsVarieties.PIC) {
                    abcoY = i;
                    abcoX = j;
                    sizeY = b.getElecomInfo().getSize().getHeight();
                    sizeX = b.getElecomInfo().getSize().getWidth();
                    dir = b.getElecomInfo().getPartsDirections();
                    /* 元の情報を消す */
                    for (int y = abcoY; y < abcoY + sizeY; y++) {
                        for (int x = abcoX; x < abcoX + sizeX; x++) {
                            getOperateOperate().clear(getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x));
                        }
                    }
                    /* その座標に新しいマイコンの情報を生成する */
                    CircuitBlock micon = new CircuitBlock(abcoY, abcoX);
                    micon.getCircuitInfo().setReco(new IntegerDimension());
                    micon.getElecomInfo().setPartsVarieties(PartsVarieties.PIC);
                    micon.getElecomInfo().setPartsStandards(PartsStandards._18PINS);
                    getOperateOperate().specifiedAdd(this, micon, abcoY, abcoX, dir);
                    getOperateOperate().update(this);
                    flg = true;
                    break;
                }
            }
            if (flg) {
                break;
            }
        }
    }

    /**
     * 部品の追加前にするべきことをここで行う。
     * 部品ボタンは追加する時に必ずこれを呼び出さねばならない。
     */
    public void addPrepare(CircuitBlock b) {
        getElecomInfoSelector().elecomInfoSelector(b.getElecomInfo());
        b.getCircuitInfo().setReco(new IntegerDimension());
        setTmp(b);
    }

    /**
     * 拡大率と基準座標がリセットされる。
     */
    public void resetPaintPotion() {
        setPaintRatio(1);
        getPaintBaseCo().setHeight(0);
        getPaintBaseCo().setWidth(0);
        repaint();
    }

    /***************************************/
    /** マウス操作による回路エディタの操作 */
    /***************************************/
    /**
     * 押されたボタンに対応するモードに切り替える。
     * また、モードが直前のモードと同じではない場合は、コマンドがデフォルト値に戻される。
     * その上で、パネルが保持する一時情報をすべてリセットする。
     */
    public void changeMode(JPanel panel) {
        if (panel == getFrame().getBasePanel().getMainCircuitPanel().getPartsAddLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.PARTS_ADD) {
                getCircuitUnit().getMode().setMode(Mode.PARTS_ADD);
                getCircuitUnit().getCommand().setCommand(Command.PARTS_ADD);
                getFrame().getHelpLabel().setText("部品を追加するモード：サブ操作パネルから追加したい部品を選んでください。");
            }
        } else if (panel == getFrame().getBasePanel().getMainCircuitPanel().getWireExpansionLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.WIRE_BOND) {
                getCircuitUnit().getMode().setMode(Mode.WIRE_BOND);
                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getWireBondPanel().resetCommand();
                getFrame().getHelpLabel().setText("導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
            }
        } else if (panel == getFrame().getBasePanel().getMainCircuitPanel().getPartsMoveLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.PARTS_MOVE) {
                getCircuitUnit().getMode().setMode(Mode.PARTS_MOVE);
                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsMovePanel().resetCommand();
                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスカーソルを右側の基板に移動させて下さい。");
            }
        } else if (panel == getFrame().getBasePanel().getMainCircuitPanel().getPartsDeleteLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.PARTS_DELETE) {
                getCircuitUnit().getMode().setMode(Mode.PARTS_DELETE);
                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().resetCommand();
                getFrame().getHelpLabel().setText("部品や導線を削除するモード：こちらは導線をマス単位で削除することが出来ます。マウスカーソルを右側の基板に移動させて下さい。");
            }
        } else if (panel == getFrame().getBasePanel().getMainCircuitPanel().getOperateEditorLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.EDIT_MOVE) {
                getCircuitUnit().getMode().setMode(Mode.EDIT_MOVE);
                getCircuitUnit().getCommand().setCommand(Command.EDIT_MOVE);
                getFrame().getHelpLabel().setText("基板を移動させるモード：基板をドラッグアンドドロップすることで移動させることが出来ます。更に、マウスホイールで拡大縮小も行えます。");
            }
        } else if (panel == getFrame().getBasePanel().getMainCircuitPanel().getPartsEditLabel()) {
            if (getCircuitUnit().getMode().getMode() != Mode.PARTS_EDIT) {
                getCircuitUnit().getMode().setMode(Mode.PARTS_EDIT);
                getCircuitUnit().getCommand().setCommand(Command.PARTS_EDIT);
                getFrame().getHelpLabel().setText("部品の設定を行うモード：マウスカーソルを右側の基板に移動させて下さい。");
            }
        }
        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
        resetTmp();
        resetTmps();
        resetIdTmps();
        resetHcTmps();
        /* 各種の無効化 */
        getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
        /* 再描画 */
        reDetection();
    }

    /**
     * 押されたボタンに対応するモードに切り替える。
     * その上で、パネルが保持する一時情報をすべてリセットする。
     */
    public void changeCommnad(JPanel panel) {
        switch (getCircuitUnit().getMode().getMode()) {
            case WIRE_BOND:
                if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getWireBondPanel().getWireBondLabel()) {
                    getCircuitUnit().getCommand().setCommand(Command.WIRE_BOND);
                    getFrame().getHelpLabel().setText("導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
                } else if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getWireBondPanel().getWireCrossLabel()) {
                    getCircuitUnit().getCommand().setCommand(Command.WIRE_CROSS);
                    getFrame().getHelpLabel().setText("導線の交点を変更するモード：マウスカーソルを右側の基板に移動させて下さい。");
                } else if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getWireBondPanel().getHandBondLabel()) {
                    getCircuitUnit().getCommand().setCommand(Command.HAND_BOND);
                    getFrame().getHelpLabel().setText("部品の端子と導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
                }
                break;
            case PARTS_MOVE:
                if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsMovePanel().getPartsMovePanel()) {
                    getCircuitUnit().getCommand().setCommand(Command.PARTS_MOVE);
                    getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                } else if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsMovePanel().getPartsCopyPanel()) {
                    getCircuitUnit().getCommand().setCommand(Command.PARTS_COPY);
                    getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                } else if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsMovePanel().getPartsRotatePanel()) {
                    getCircuitUnit().getCommand().setCommand(Command.PARTS_ROTATE);
                    getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                }
                break;
            case PARTS_DELETE:
                if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteDetail()) {
                    getCircuitUnit().getCommand().setCommand(Command.DELETE_DETAIL);
                    getFrame().getHelpLabel().setText("部品や導線を削除するモード：こちらは導線をマス単位で削除することが出来ます。マウスカーソルを右側の基板に移動させて下さい。");
                } else if (panel == getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteCollect()) {
                    getCircuitUnit().getCommand().setCommand(Command.DELETE_COLLECT);
                    getFrame().getHelpLabel().setText("部品や導線を削除するモード：こちらは導線を１本単位で削除することが出来ます。マウスカーソルを右側の基板に移動させて下さい。");
                }
                break;
        }
        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
        resetTmp();
        resetTmps();
        resetIdTmps();
        resetHcTmps();
        /* 各種の無効化 */
        getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
        /* 再描画 */
        reDetection();
    }

    /**
     * モードと一時情報の組み合わせに対応する検知パターンに切り替える。
     * ボーダ情報はすべてリセットされるが、パネルが保持する一時情報はリセットされない。
     * 分かりやすくするために、あえて場合分けしている。
     */
    public void reDetection() {
        Mode mode = getCircuitUnit().getMode().getMode();
        Command command = getCircuitUnit().getCommand().getCommand();
        Behavior behavior = getCircuitUnit().getBehavior().getBehavior();
        getOperateDetection().noDetection(this);
        switch (mode) {
            case PARTS_ADD:
                switch (command) {
                    case PARTS_ADD:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().partsAdd_partsAdd_noAction(this);
                                break;
                        }
                        break;
                }
                break;
            case WIRE_BOND:
                switch (command) {
                    case WIRE_BOND:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().wireBond_wireBond_noAction(this);
                                break;
                            case RANGING:
                                getOperateDetection().wireBond_wireBond_ranging(this);
                                break;
                        }
                        break;
                    case WIRE_CROSS:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().wireBond_wireCross_noAction(this);
                                break;
                        }
                        break;
                    case HAND_BOND:
                    case AUTO_BOND:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().wireBond_handBond_noAction(this);
                                break;
                        }
                        break;
                }
                break;
            case PARTS_MOVE:
                switch (command) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                    case PARTS_ROTATE:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().partsMove_commonDetail_noAction(this);
                                break;
                            case RANGING:
                                getOperateDetection().partsMove_common_ranging(this);
                                break;
                            case RANGED:
                                getOperateDetection().partsMove_common_ranged(this);
                                break;
                            case MOVE:
                                if (command != Command.PARTS_ROTATE) {
                                    getOperateDetection().partsMove_common_move(this, command == Command.PARTS_MOVE);
                                } else {
                                    getOperateDetection().partsMove_common_ranged(this);
                                }
                                break;
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (command) {
                    case DELETE_DETAIL:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().partsDelete_noAction(this, false);
                                break;
                            case RANGING:
                                getOperateDetection().partsDelete_ranging(this, false);
                                break;
                        }
                        break;
                    case DELETE_COLLECT:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().partsDelete_noAction(this, true);
                                break;
                            case RANGING:
                                getOperateDetection().partsDelete_ranging(this, true);
                                break;
                        }
                        break;
                }
                break;
            case PARTS_EDIT:
                switch (command) {
                    case PARTS_EDIT:
                        switch (behavior) {
                            case NO_ACTION:
                                getOperateDetection().partsEdit_partsEdit_noAction(this);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /**
     * キャンセルボタンを押した時の処理を行う。
     */
    public void mouseCansel() {
        getFrame().getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().resetMessage();
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_ADD:
                        resetTmp();
                        reDetection();
                        getFrame().getHelpLabel().setText("部品を追加するモード：サブ操作パネルから追加したい部品を選んでください。");
                        break;
                }
                break;
            case WIRE_BOND:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case WIRE_BOND:
                        resetTmp();
                        resetIdTmps();
                        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                        reDetection();
                        getFrame().getHelpLabel().setText("導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
                        break;
                }
                break;
            case PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                    case PARTS_ROTATE:
                        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                        reDetection();
                        if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                            getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                        } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                            getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                        } else {
                            getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        resetIdTmps();
                        resetHcTmps();
                        getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
                        reDetection();
                        getFrame().getHelpLabel().setText("部品や導線を削除するモード：マウスカーソルを右側の基板に移動させて下さい。");
                        break;
                }
                break;
        }
    }

    /**
     * モードとコマンドによって処理が変わる。
     * また、可読性を重視して、あえてモードによる場合分けを行っています。
     * PARTS_MOVE:
     * 　PARTS_MOVE:
     * 　　左プレスかつボーダが無い場合は範囲指定の始点として扱います。
     * 　　左プレスかつ範囲ボーダの場合はドラッグアンドドロップの基準位置として扱います。
     * 　PARTS_COPY:
     * 　　左プレスかつボーダが無い場合は範囲指定の始点として扱います。
     * 　　左プレスかつ範囲ボーダの場合はドラッグアンドドロップの基準位置として扱います。
     * 　PARTS_ROTATE:
     * 　　左プレスかつボーダが無い場合は範囲指定の始点として扱います。
     * PARTS_DELETE:
     * 　DELETE_DETAIL:
     * 　　左プレスかつボーダが無い場合は範囲指定の始点として扱います。
     * 　DELETE_COLLECT:
     * 　　左プレスかつボーダが無い場合は範囲指定の始点として扱います。
     * EDIT_MOVE:
     * 　EDIT_MOVE:
     * 　　左プレスの場合のみ基板の描画に関する移動基準位置として扱います。
     */
    @Override
    public void mousePressed(MouseEvent e) {
        /** 右プレスの場合は、全てのモードにおいて基板の移動開始として扱う */
        if (e.getButton() == MouseEvent.BUTTON3) {
            pressedRight = true;
            super.mousePressed(e);
            reDetection();
            return;
        }
        getFrame().getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().resetMessage();
        CircuitBlock b;
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                    case PARTS_ROTATE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    if (getCursorCo() != null) {
                                        b = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth());
                                        if (b.getBorder() == null || b.getBorder() == Borders.OVERLAP) {
                                            /* ボーダ情報が無い、もしくはマウスカーソルと重なっている領域ならば、範囲指定開始として扱う */
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.RANGING);
                                            rangeStart.setHeight(getCursorCo().getHeight());
                                            rangeStart.setWidth(getCursorCo().getWidth());
                                            rangeEnd.setHeight(getCursorCo().getHeight());
                                            rangeEnd.setWidth(getCursorCo().getWidth());
                                            reDetection();
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            } else {
                                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            }
                                        }
                                    }
                                }
                                break;
                            case RANGED:
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    if (getCursorCo() != null) {
                                        b = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth());
                                        if (b.getBorder() == null || b.getBorder() == Borders.OVERLAP) {
                                            /* ボーダ情報が無い、もしくはマウスカーソルと重なっている領域ならば、範囲指定開始として扱う */
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.RANGING);
                                            rangeStart.setHeight(getCursorCo().getHeight());
                                            rangeStart.setWidth(getCursorCo().getWidth());
                                            rangeEnd.setHeight(getCursorCo().getHeight());
                                            rangeEnd.setWidth(getCursorCo().getWidth());
                                            reDetection();
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            } else {
                                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                            }
                                        } else if (b.getBorder() == Borders.RANGED || b.getBorder() == Borders.SELECTED) {
                                            /* ボーダ情報が指定範囲、もしくは選択された領域ならば、操作開始として扱う */
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.MOVE);
                                            /* 一時情報に追加する */
                                            getOperateOperate().addPartsCommonCandidate(this);
                                            if (getCircuitUnit().getCommand().getCommand() != Command.PARTS_ROTATE) {
                                                /* 回転操作でなければ基準座標を登録 */
                                                moveCursorCo.setHeight(getCursorCo().getHeight());
                                                moveCursorCo.setWidth(getCursorCo().getWidth());
                                                /* キャンセルボタンを無効化 */
                                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                            }
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                /* 移動操作ならば元あった情報を削除する */
                                                getOperateOperate().partsMoveBeforeInfoDelete(this);
                                            }
                                            reDetection();
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：指定した領域を移動させましょう。指定した領域をドラッグアンドドロップすることで移動させることが出来ます。");
                                            } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：指定した領域を複製しましょう。指定した領域をドラッグアンドドロップすることで複製することが出来ます。");
                                            } else {
                                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：指定した領域を回転させましょう。指定した領域をクリックすることで反時計回りに回転させることが出来ます。");
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    if (getCursorCo() != null) {
                                        b = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth());
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.RANGING);
                                        rangeStart.setHeight(getCursorCo().getHeight());
                                        rangeStart.setWidth(getCursorCo().getWidth());
                                        rangeEnd.setHeight(getCursorCo().getHeight());
                                        rangeEnd.setWidth(getCursorCo().getWidth());
                                        reDetection();
                                        getFrame().getHelpLabel().setText("部品や導線を削除するモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                    }
                                }
                                break;
                        }
                        break;
                }
                break;
            case EDIT_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case EDIT_MOVE:
                        super.mousePressed(e);
                        reDetection();
                        break;
                }
                break;
        }
    }

    /**
     * モードとコマンドによって処理が変わる。
     * また、可読性を重視して、あえてモードによる場合分けを行っています。
     * PARTS_ADD:
     * 　PARTS_ADD:
     * 　　真の呼び出し元がボタンで左クリックの場合のみ追加操作として扱います。
     * 　　tmpがnull以外で左クリックの場合のみ追加します。
     * 　　ただし、真の呼び出し元がボタンの場合は追加情報を更新するだけになります。
     * WIRE_BOND:
     * 　WIRE_BOND:
     * 　　左クリックの場合のみ結合始点操作として扱います。
     * 　　tmpがnull以外で左クリックの場合のみ結合終点操作として扱います。
     * 　WIRE_CROSS:
     * 　　左クリックで変わります。
     * 　HAND_BOND:
     * 　　結合可能な部品と自動的に結合します。
     * 　AUTO_BOND:
     * 　　真の呼び出し元がボタンなら警告ダイアログを表示し、同意を得られた場合は全ての結合可能な末端導線が結合されます。
     * PARTS_DELETE:
     * 　DELETE_DETAIL:
     * 　　真の呼び出し元がボタンの場合は削除を実行します。
     * 　DELETE_COLLECT:
     * 　　真の呼び出し元がボタンの場合は削除を実行します。
     * 　DELETE_ALL:
     * 　　真の呼び出し元がボタンなら警告ダイアログを表示し、同意を得られた場合は全ての部品を削除します。
     * EDIT_MOVE:
     * 　EDIT_RESET:
     * 　　真の呼び出し元がボタンの場合は、基板の描画位置と拡大率をリセットします。
     * PARTS_EDIT:
     * 　PARTS_EDIT:
     * 　　真の呼び出し元がパネルの場合は、その位置にある部品の設定を行います。
     * 　　真の呼び出し元がボタンの場合は、全ての部品の設定を初期化します。
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_ADD:
                        if (e.getSource() instanceof PartsButton) {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                /* ボタンを左クリックしたら部品情報追加 */
                                CircuitBlock tmp = new CircuitBlock(0, 0);
                                ((PartsButton)e.getSource()).getCircuitBlock().copyTo(tmp);
                                getFrame().getBasePanel().getEditCircuitPanel().addPrepare(tmp);
                                /* キャンセルボタンを押せるようにする */
                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                reDetection();
                                getFrame().getHelpLabel().setText("部品を追加するモード：マウスカーソルを右側の基板に移動させて下さい。");
                            }
                        } else if (e.getSource() instanceof CircuitUnitPanel) {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                /* パネルを左クリックしたら部品を追加 */
                                if (getCursorCo() != null && getTmp() != null) {
                                    getOperateOperate().add(this, getCursorCo());
                                    reDetection();
                                    getFrame().getHelpLabel().setText("部品を追加するモード：部品は連続して追加することが出来ます。ただし、電源とマイコンは追加できる数に上限があるので注意して下さい。");
                                }
                            }
                        }
                        break;
                }
                break;
            case WIRE_BOND:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case WIRE_BOND:
                        if (getTmp() == null) {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (getCursorCo() != null) {
                                    if (getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth()).getBorder() == Borders.OVERLAP) {
                                        /* 始点指定操作として扱う */
                                        setTmp(getCursorCo());
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.RANGING);
                                        /* キャンセルボタンを押せるようにする */
                                        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                        reDetection();
                                        getFrame().getHelpLabel().setText("導線を結合するモード：導線を伸ばす終点をクリックして下さい。何もない場所を選択すると導線を伸ばすだけになります。導線や部品の端子を選択すると、その箇所と自動結合します。");
                                    }
                                }
                            }
                        } else {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                /* 終点確定操作兼導線の結合として扱う */
                                getOperateOperate().bond(this);
                                resetTmp();
                                getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                /* 結合操作が完了してもキャンセル無効化 */
                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                reDetection();
                                getFrame().getHelpLabel().setText("導線を結合するモード：再び導線を伸ばしたり結合したりしたい場合は始点をクリックして選択して下さい。");
                            }
                        }
                        break;
                    case WIRE_CROSS:
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            /* 左クリックの場合は操作として扱う */
                            if (getCursorCo() != null) {
                                getOperateOperate().crossChange(this, getCursorCo());
                                reDetection();
                                getFrame().getHelpLabel().setText("導線の交点を変更するモード：連続してクリックすることで他のパターンに変更することが出来ます。");
                            }
                        }
                        break;
                    case HAND_BOND:
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            /* 左クリックの場合は操作として扱う */
                            if (getCursorCo() != null) {
                                getOperateOperate().partsBond(this, getCursorCo());
                                reDetection();
                            }
                        }
                        break;
                    case AUTO_BOND:
                        if (e.getSource() instanceof GeneralItemPanel) {
                            reDetection();
                            /* 確認ダイアログを表示し、「はい」なら全部を結合する */
                            String[] message = new String[] {
                                    "隣接する部品の端子と接続されていない導線を全て接続します。",
                                    "あえて接続していない箇所がある場合は「いいえ」を押してください。",
                                    "本当に全て接続してもよろしいですか？"
                            };
                            if (JOptionPane.showConfirmDialog(getFrame(), message, "確認", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                getOperateOperate().allPartsBond(this);
                            }
                            getCircuitUnit().getCommand().setCommand(getCircuitUnit().getCommand().getPreCommand());
                            reDetection();
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        if (e.getSource() instanceof GeneralItemPanel) {
                            getOperateOperate().delete(this, getCircuitUnit().getCommand().getCommand() == Command.DELETE_COLLECT);
                            /* キャンセルを無効化 */
                            getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                            /* 一時保存情報も消去する */
                            resetIdTmps();
                            resetHcTmps();
                            reDetection();
                            getFrame().getHelpLabel().setText("部品や導線を削除するモード：他に削除したい部品や導線がある場合は、再び選択して下さい。");
                        }
                        break;
                    case DELETE_ALL:
                        if (e.getSource() instanceof GeneralItemPanel) {
                            /* 警告ダイアログを表示し、「はい」なら全部削除する */
                            String[] message = new String[] {
                                    "基板上の部品および導線を全て削除します。",
                                    "本当に全て削除してもよろしいですか？"
                            };
                            if (JOptionPane.showConfirmDialog(getFrame(), message, "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                getOperateOperate().allDelete(this);
                                /* 一時保存情報も消去する */
                                resetIdTmps();
                                resetHcTmps();
                                /* 念のため削除ボタンとキャンセルボタンを無効化する */
                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
                            }
                            getCircuitUnit().getCommand().setCommand(getCircuitUnit().getCommand().getPreCommand());
                            reDetection();
                        }
                        break;
                }
                break;
            case EDIT_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case EDIT_MOVE:
                        if (e.getSource() instanceof GeneralItemPanel) {
                            /* 基板の位置と拡縮を初期値に戻す */
                            resetPaintPotion();
                        }
                        break;
                }
                break;
            case PARTS_EDIT:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_EDIT:
                        if (e.getSource() instanceof UnitPanel) {
                            /* 設定を行う */
                            if (getCursorCo() != null) {
                                if (getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth()).getBorder() == Borders.OVERLAP) {
                                    CircuitInfo c = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth()).getCircuitInfo();
                                    ElecomInfo ele = getCircuitUnit().getCircuitBlock().getMatrix().get(getCursorCo().getHeight()).get(getCursorCo().getWidth()).getElecomInfo();
                                    int y = c.getAbco().getHeight() - c.getReco().getHeight();
                                    int x = c.getAbco().getWidth() - c.getReco().getWidth();
                                    if (ele.getPartsVarieties() == PartsVarieties.RESISTANCE && ele.getPartsStandards() == PartsStandards._variable) {
                                        new VariableResistanceDialog(this, getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x));
                                    } else if (ele.getPartsVarieties() == PartsVarieties.PULSE && ele.getPartsStandards() == PartsStandards.PULSE) {
                                        new VariablePulseDialog(this, getCircuitUnit().getCircuitBlock().getMatrix().get(y).get(x));
                                    }
                                }
                            }
                        } else if (e.getSource() instanceof GeneralItemPanel) {
                            /* 設定の初期化 */
                            reDetection();
                            /* 確認ダイアログを表示し、「はい」なら全部を結合する */
                            String[] message = new String[] {
                                "部品に記録された全ての設定を初期値に戻します。",
                                "初期値に戻したくない場合は「いいえ」を押してください。",
                                "本当に全て初期値に戻してもよろしいですか？"
                            };
                            if (JOptionPane.showConfirmDialog(getFrame(), message, "確認", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                getOperateOperate().partsEditInit(this);
                            }
                            reDetection();
                        }
                        break;
                }
                break;
        }
    }

    /**
     * モードとコマンドによって処理が変わる。
     * また、可読性を重視して、あえてモードによる場合分けを行っています。
     * PARTS_MOVE:
     * 　PARTS_DETAIL_MOVE:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　PARTS_COLLECT_MOVE:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　PARTS_DETAIL_COPY:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　PARTS_COLLECT_COPY:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　PARTS_DETAIL_ROTATE:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　PARTS_COLLECT_ROTATE:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * PARTS_DELETE:
     * 　DELETE_DETAIL:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * 　DELETE_COLLECT:
     * 　　左ドラッグの場合のみ範囲指定の終点位置をこの地点に変更します。
     * EDIT_MOVE:
     * 　EDIT_MOVE:
     * 　　左ドラッグの場合のみ描画位置をずらします。
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        /** 直前に右プレスしていた場合にドラッグしている場合、つまり右ボタン押下状態でのドラッグでは、全てのモードで基板の移動として扱う */
        if (pressedRight) {
            super.mouseDragged(e);
            reDetection();
            return;
        }
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                    case PARTS_ROTATE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case RANGING:
                                getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
                                if (getCursorCo() != null) {
                                    rangeEnd.setHeight(getCursorCo().getHeight());
                                    rangeEnd.setWidth(getCursorCo().getWidth());
                                }
                                reDetection();
                                break;
                            case MOVE:
                                getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
                                reDetection();
                                break;
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case RANGING:
                                getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
                                if (getCursorCo() != null) {
                                    rangeEnd.setHeight(getCursorCo().getHeight());
                                    rangeEnd.setWidth(getCursorCo().getWidth());
                                }
                                reDetection();
                                break;
                        }
                        break;
                }
                break;
            case EDIT_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case EDIT_MOVE:
                        super.mouseDragged(e);
                        reDetection();
                        break;
                }
                break;
        }
    }

    /**
     * モードとコマンドによって処理が変わる。
     * また、可読性を重視して、あえてモードによる場合分けを行っています。
     * PARTS_MOVE:
     * 　PARTS_MOVE:
     * 　　始点がボーダ無しで左リリースの場合は、この地点を終点として扱います。
     * 　　始点がボーダ有りで左リリースの場合は、この地点を変更基準地点として扱います。
     * 　PARTS_COPY:
     * 　　始点がボーダ無しで左リリースの場合は、この地点を終点として扱います。
     * 　　始点がボーダ有りで左リリースの場合は、この地点を変更基準地点として扱います。
     * 　PARTS_ROTATE:
     * 　　始点がボーダ無しで左リリースの場合は、この地点を終点として扱います。
     * 　　始点がボーダ有りで左リリースの場合は、右回転します。
     * PARTS_DELETE:
     * 　　始点が左リリースの場合は、この地点を終点として扱います。
     * 　DELETE_COLLECT:
     * 　　始点が左リリースの場合は、この地点を終点として扱います。
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            pressedRight = false;
        }
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                    case PARTS_COPY:
                    case PARTS_ROTATE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case RANGING:
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    /* 範囲指定終了操作として扱う */
                                    getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
                                    if (getCursorCo() != null) {
                                        rangeEnd.setHeight(getCursorCo().getHeight());
                                        rangeEnd.setWidth(getCursorCo().getWidth());
                                        /* 範囲指定終了時に、その範囲内に選択された部品や導線が一つもなければキャンセルとして扱う */
                                        boolean flg = false;
                                        for (int i = Math.min(rangeStart.getHeight(), rangeEnd.getHeight()); i <= Math.max(rangeStart.getHeight(), rangeEnd.getHeight()); i++) {
                                            for (int j = Math.min(rangeStart.getWidth(), rangeEnd.getWidth()); j <= Math.max(rangeStart.getWidth(), rangeEnd.getWidth()); j++) {
                                                if (getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j).isExist()) {
                                                    flg = true;
                                                    break;
                                                }
                                            }
                                            if (flg) {
                                                break;
                                            }
                                        }
                                        if (flg) {
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.RANGED);
                                            /* キャンセルボタンを有効化 */
                                            getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：指定した領域を移動させましょう。指定した領域をドラッグアンドドロップすることで移動させることが出来ます。");
                                            } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：指定した領域を複製しましょう。指定した領域をドラッグアンドドロップすることで複製することが出来ます。");
                                            } else {
                                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：指定した領域を回転させましょう指定した領域をクリックすることで反時計回りに回転させることが出来ます。");
                                            }
                                        } else {
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                            /* キャンセルボタンを無効化 */
                                            getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                            if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：移動させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                            } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：複製させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                            } else {
                                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：回転させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                            }

                                        }
                                    } else {
                                        /* 範囲外で離した場合は、キャンセルとして扱う */
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                        /* キャンセルボタンを無効化 */
                                        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                        if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                            getFrame().getHelpLabel().setText("部品や導線を移動させるモード：移動させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                        } else if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_COPY) {
                                            getFrame().getHelpLabel().setText("部品や導線を複製させるモード：複製させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                        } else {
                                            getFrame().getHelpLabel().setText("部品や導線を回転させるモード：回転させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                        }
                                    }
                                    reDetection();
                                }
                                break;
                            case MOVE:
                                if (getCircuitUnit().getCommand().getCommand() != Command.PARTS_ROTATE) {
                                    /* 指定場所に一時情報を展開する */
                                    if (getOperateOperate().expand(this)) {
                                        /* 展開に成功した場合 */
                                        resetTmps();
                                        if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                            /* キャンセルボタンを無効化 */
                                            getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                            getFrame().getHelpLabel().setText("部品や導線を移動させるモード：移動させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                        } else {
                                            getCircuitUnit().getBehavior().setBehavior(Behavior.RANGED);
                                            /* キャンセルボタンを有効化 */
                                            getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                            getFrame().getHelpLabel().setText("部品や導線を複製させるモード：指定した領域を再びドラッグアンドドラッグすることで更に複製することが出来ます。");
                                        }
                                    } else {
                                        /* 展開に失敗した場合 */
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.RANGED);
                                        /* キャンセルボタンを有効化 */
                                        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                        if (getCircuitUnit().getCommand().getCommand() == Command.PARTS_MOVE) {
                                            /* 展開元の座標に一時情報を戻す */
                                            getOperateOperate().partsMoveAfterInfoReturn(this);
                                            getFrame().getHelpLabel().setText("部品や導線を移動させるモード：指定した領域を移動させましょう。指定した領域をドラッグアンドドロップすることで移動させることが出来ます。");
                                        } else {
                                            getFrame().getHelpLabel().setText("部品や導線を複製させるモード：指定した領域を複製しましょう。指定した領域をドラッグアンドドロップすることで複製することが出来ます。");
                                        }
                                        resetTmps();
                                    }
                                } else {
                                    /* 指定範囲の始点と終点を参照し、その中点を基準として右回転する */
                                    if (getOperateOperate().rotate(this, false)) {
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.RANGED);
                                        getFrame().getHelpLabel().setText("部品や導線を回転させるモード：領域内をクリックすることで連続して回転させることが出来ます。");
                                    } else {
                                        getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                        /* キャンセルボタンを無効化 */
                                        getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                        getFrame().getHelpLabel().setText("部品や導線を回転させるモード：回転させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                    }
                                    resetTmps();
                                }
                                reDetection();
                        }
                        break;
                }
                break;
            case PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case RANGING:
                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    /* 範囲指定終了操作として扱う */
                                    getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
                                    if (getCursorCo() != null) {
                                        rangeEnd.setHeight(getCursorCo().getHeight());
                                        rangeEnd.setWidth(getCursorCo().getWidth());
                                        getOperateOperate().addDeleteCandidate(this, rangeStart, rangeEnd, getCircuitUnit().getCommand().getCommand() == Command.DELETE_COLLECT);
                                        if (getCircuitUnit().getCommand().getCommand() == Command.DELETE_COLLECT) {
                                            if (!getHcTmps().isEmpty()) {
                                                /* 削除ボタンとキャンセルボタンを有効化 */
                                                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().enablePanel();
                                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：このモードでは複数ヶ所を同時に選択できます。他に削除したい部分があれば選択しましょう。また、選択中の部分を再度選択するとキャンセルとなります。");
                                            } else {
                                                /* 削除ボタンとキャンセルボタンを無効化 */
                                                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
                                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：全ての選択された箇所がキャンセルされました。");
                                            }
                                        } else {
                                            if (!getIdTmps().isEmpty()) {
                                                /* 削除ボタンとキャンセルボタンを有効化 */
                                                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().enablePanel();
                                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().enablePanel();
                                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：このモードでは複数ヶ所を同時に選択できます。他に削除したい部分があれば選択しましょう。また、選択中の部分を再度選択するとキャンセルとなります。");
                                            } else {
                                                /* 削除ボタンとキャンセルボタンを無効化 */
                                                getFrame().getBasePanel().getSubCircuitPanel().getUpperPanel().getPartsDeletePanel().getDeleteLabel().disablePanel();
                                                getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().disablePanel();
                                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：全ての選択された箇所がキャンセルされました。");
                                            }
                                        }
                                    }
                                    /* 範囲外で離した場合は、キャンセルとして扱う */
                                    getCircuitUnit().getBehavior().setBehavior(Behavior.NO_ACTION);
                                    reDetection();
                                }
                                break;
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_ADD:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                if (getTmp() != null) {
                                    getFrame().getHelpLabel().setText("部品を追加するモード：左クリックすると追加できます。ただし、赤枠が表示されている時は追加できません。");
                                } else {
                                    getFrame().getHelpLabel().setText("部品を追加するモード：部品を追加するためにサブ操作パネルから部品を選択しましょう。");
                                }
                                break;
                        }
                        break;
                }
                break;
            case  WIRE_BOND:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case WIRE_BOND:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("導線を結合するモード：引き延ばす導線の始点をクリックして下さい。導線そのものや部品の端子が選択できます。");
                                break;
                            case RANGING:
                                getFrame().getHelpLabel().setText("導線を結合するモード：導線を伸ばす終点をクリックして下さい。何もない場所を選択すると導線を伸ばすだけになります。導線や部品の端子を選択すると、その箇所と自動結合します。");
                                break;
                        }
                        break;
                    case WIRE_CROSS:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("導線の交点を変更するモード：導線の交点をクリックすることで交点のパターンを変更することができます。");
                                break;
                        }
                        break;
                    case HAND_BOND:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品の端子と導線を結合するモード：部品の端子をクリックすると、隣接する導線と自動結合します。導線をクリックすると、隣接する全ての部品の端子と自動結合します。");
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：移動させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                break;
                            case RANGING:
                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                break;
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：指定した領域を移動させましょう。指定した領域をドラッグアンドドロップすることで移動させることが出来ます。");
                                break;
                        }
                        break;
                    case PARTS_COPY:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：複製させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                break;
                            case RANGING:
                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                break;
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：指定した領域を複製しましょう。指定した領域をドラッグアンドドロップすることで複製することが出来ます。");
                                break;
                        }
                        break;
                    case PARTS_ROTATE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：回転させる範囲の始点を選択して下さい。部品や導線をクリックするとその部分だけが選択されます。領域指定の場合はマウスを押したままにして下さい。");
                                break;
                            case RANGING:
                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                break;
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：指定した領域を回転させましょう。指定した領域をクリックすることで反時計回りに回転させることが出来ます。");
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：削除したい部品や導線をクリックして選択して下さい。まとめて選択したい場合は、領域の始点でマウスを押したままにして下さい。");
                                break;
                            case RANGING:
                                getFrame().getHelpLabel().setText("部品や導線を削除するモード：マウスボタンを押したままマウスを動かすことで範囲を変更することが出来ます。範囲を確定したい場合は、マウスボタンを離して下さい。");
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_EDIT:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_EDIT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品の設定を行うモード：可変抵抗とパルス出力器をクリックして選択することで、それぞれの設定を変更することができます。");
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /**
     * 回路エディタの回路作成領域からマウスカーソルが外れた時の処理です。
     */
    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        getOperateOperate().getMouseInIndex(this, e.getY(), e.getX());
        reDetection();
        switch (getCircuitUnit().getMode().getMode()) {
            case PARTS_ADD:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_ADD:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                if (getTmp() != null) {
                                    getFrame().getHelpLabel().setText("部品を追加するモード：他の部品に変えて追加したい時は、そのまま他の部品を選択して下さい。");
                                } else {
                                    getFrame().getHelpLabel().setText("部品を追加するモード：サブ操作パネルから追加したい部品を選んでください。");
                                }
                                break;
                        }
                        break;
                }
                break;
            case  WIRE_BOND:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case WIRE_BOND:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                            case RANGING:
                                getFrame().getHelpLabel().setText("導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                    case WIRE_CROSS:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("導線の交点を変更するモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                    case HAND_BOND:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品の端子と導線を結合するモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_MOVE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_MOVE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                            case RANGING:
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を移動させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                    case PARTS_COPY:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                            case RANGING:
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を複製させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                    case PARTS_ROTATE:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                            case RANGING:
                            case RANGED:
                            case MOVE:
                                getFrame().getHelpLabel().setText("部品や導線を回転させるモード：マウスカーソルを右側の基板に移動させて下さい。");
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_DELETE:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case DELETE_DETAIL:
                    case DELETE_COLLECT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                            case RANGING:
                                if (getFrame().getBasePanel().getSubCircuitPanel().getCancelLabel().getBackground() == ColorMaster.getSelectableColor()) {
                                    getFrame().getHelpLabel().setText("部品や導線を削除するモード：削除ボタンを押すことで選択した部品や導線を削除出来ます。");
                                } else {
                                    getFrame().getHelpLabel().setText("部品や導線を削除するモード：マウスカーソルを右側の基板に移動させて下さい。");
                                }
                                break;
                        }
                        break;
                }
                break;
            case  PARTS_EDIT:
                switch (getCircuitUnit().getCommand().getCommand()) {
                    case PARTS_EDIT:
                        switch (getCircuitUnit().getBehavior().getBehavior()) {
                            case NO_ACTION:
                                getFrame().getHelpLabel().setText("部品の設定を行うモード：設定の初期化を行いたい場合は、サブ操作パネルの初期化ボタンを押して下さい。");
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /**
     * マウスの位置から対象ボーダを付与します。
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        reDetection();
    }

    /**
     * 基板の拡大縮小を行う
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        reDetection();
    }
}
