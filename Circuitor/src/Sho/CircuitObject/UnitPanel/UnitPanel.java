package Sho.CircuitObject.UnitPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.PartsDirections;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.Circuit.CircuitBorder;
import Sho.CircuitObject.Circuit.CircuitUnit;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectGroup;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.Operate.ElecomInfoSelector;
import Sho.CircuitObject.Operate.OperateBorder_;
import Sho.CircuitObject.Operate.OperateDetection_;
import Sho.CircuitObject.Operate.OperateOperate_;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * CircuitUnitを内包した回路を直接描画するための抽象パネルクラス。
 */
public abstract class UnitPanel extends NewJPanel implements MouseInputListener,MouseWheelListener {
    /*****************************/
    /** 画面に関する基本的な情報 */
    /*****************************/
    /**
     * 回路ブロックのピクセル単位(固定)
     */
    public static final int UNIT_PIXEL = 12;
    /**
     * 回路全体の情報を統括する
     */
    private CircuitUnit circuitUnit;
    /**
     * 基板のサイズ
     */
    private IntegerDimension circuitSize;

    /*************************/
    /** 画面描画に関する情報 */
    /*************************/
    /**
     * 基板の基準座標(描画の基準点)
     */
    private IntegerDimension paintBaseCo;
    /**
     * 描画の拡大率
     */
    private int paintRatio;
    /**
     * 一つ前の描画の拡大率
     */
    private int prePaintRatio;
    /**
     * 描画に用いる汎用矩形変数
     */
    private Rectangle2D paintRect;

    /***************************/
    /** マウス操作に関する情報 */
    /***************************/
    /**
     * マウス押下地点の基板上の座標(ピクセル単位)
     */
    private IntegerDimension pressedCo;
    /**
     * マウスの基板上の座標(ブロック単位)
     */
    private IntegerDimension cursorCo;

    /*****************************/
    /** 処理上で使う汎用的な変数 */
    /*****************************/
    /**
     * ブロックの汎用変数
     */
    private CircuitBlock tmp;
    private ArrayList<CircuitBlock> tmps;
    /**
     * 座標の汎用変数
     */
    private ArrayList<IntegerDimension> idTmps;
    /**
     * グループの汎用変数
     */
    private ArrayList<HighLevelConnectInfo> hcTmps;

    /***********************************************/
    /** 様々な処理を行うための処理専用オブジェクト */
    /***********************************************/
    /**
     * 電子部品データ構築用インスタンス
     */
    private ElecomInfoSelector elecomInfoSelector;
    /**
     * ボーダ描画用インスタンス
     */
    private OperateBorder_ operateBorder;
    /**
     * ボーダ描画部分判定処理用インスタンス
     */
    private OperateDetection_ operateDetection;
    /**
     * 多様な処理を行うためのインスタンス
     */
    private OperateOperate_ operateOperate;

    /**
     * 回路情報を空状態で生成し、基板色等を初期化する。
     *
     * @param frame ウィンドウ単位でデータを統括するためのフレームコンポーネント。
     */
    protected UnitPanel(BaseFrame frame) {
        super(frame);
        /** レイアウトの設定 */
        setLayout(null);
        /** 回路の基本データの生成 */
        circuitSize = new IntegerDimension(50, 80);
        /* このタイミングで生成しようとするとbasePanelのオブジェクトが完成していない状態なのでNullPointerExceptionが発生します */
//        circuitUnit = new CircuitUnit(frame);
        /** 画像描画用変数の初期化 */
        paintBaseCo = new IntegerDimension();
        paintRatio = 1;
        prePaintRatio = 1;
        paintRect = new Rectangle2D.Double();
        /** マウス情報用変数の初期化 */
        pressedCo = new IntegerDimension();
        /** 一時保存用変数の初期化 */
        tmps = new ArrayList<>();
        idTmps = new ArrayList<>();
        hcTmps = new ArrayList<>();
        /** 処理専用オブジェクトの生成 */
        elecomInfoSelector = new ElecomInfoSelector(frame);
        operateBorder = new OperateBorder_(frame);
        operateDetection = new OperateDetection_(frame);
        operateOperate = new OperateOperate_(frame);
        setBackground(ColorMaster.getBackColor());
    }

    /**
     * CircuitUnitオブジェクトはコンストラクタの後で生成するようにします。
     */
    protected void createCircuitUnit() {
        circuitUnit = new CircuitUnit(getFrame());
    }

    /**
     * circuitUnit
     */
    public CircuitUnit getCircuitUnit() {
        return circuitUnit;
    }

    public void setCircuitUnit(CircuitUnit circuitUnit) {
        this.circuitUnit = circuitUnit;
    }

    /**
     * circuitSize
     */
    public IntegerDimension getCircuitSize() {
        return circuitSize;
    }

    /**
     * paintBaseCo
     */
    public IntegerDimension getPaintBaseCo() {
        return paintBaseCo;
    }

    public void setPaintBaseCo(IntegerDimension paintBaseCo) {
        this.paintBaseCo = paintBaseCo;
    }

    /**
     * paintRatio
     */
    public int getPaintRatio() {
        return paintRatio;
    }

    /**
     * 拡大率を変更する。
     *
     * @param paintRatio 拡大率。１から５までの整数値のみ受け付ける。
     */
    public void setPaintRatio(int paintRatio) {
        if (paintRatio >= 1 && paintRatio <= 5) {
            this.paintRatio = paintRatio;
        }
    }

    /**
     * prePaintRatio
     */
    public int getPrePaintRatio() {
        return prePaintRatio;
    }

    public void setPrePaintRatio(int prePaintRatio) {
        this.prePaintRatio = prePaintRatio;
    }

    /**
     * paintRect
     */
    public Rectangle2D getPaintRect() {
        return paintRect;
    }

    /**
     * pressedCo
     */
    public IntegerDimension getPressedCo() {
        return pressedCo;
    }

    public void setPressedCo(IntegerDimension pressedCo) {
        this.pressedCo = pressedCo;
    }

    /**
     * curcorCo
     */
    public IntegerDimension getCursorCo() {
        return cursorCo;
    }

    public void setCursorCo(IntegerDimension cursorCo) {
        this.cursorCo = cursorCo;
    }

    /**
     * tmp
     */
    public CircuitBlock getTmp() {
        return tmp;
    }

    /**
     * CircuitBlockを直接格納する。
     */
    public void setTmp(CircuitBlock tmp) {
        this.tmp = tmp;
    }

    /**
     * CircuitUnit上のCircuitBlockを座標を基に格納する。
     *
     * @param abco CircuitUnit上の絶対座標。
     */
    public void setTmp(IntegerDimension abco) {
        if (tmp == null) {
            tmp = new CircuitBlock(0, 0);
        }
        this.tmp = circuitUnit.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth());
    }

    /**
     * tmpのインスタンスを破棄する。
     */
    public void resetTmp() {
        if (this.tmp != null) {
            this.tmp = null;
        }
    }

    /**
     * tmps
     */
    public ArrayList<CircuitBlock> getTmps() {
        return tmps;
    }

    /**
     * tmpsにCircuitUnit上のCircuitBlockを追加する。
     *
     * @param abco CircuitUnit上の絶対座標。
     */
    public void addTmps(IntegerDimension abco) {
        addTmps(abco.getHeight(), abco.getWidth());
    }

    /**
     * tmpsにCircuitUnit上のCircuitBlockを追加する。
     *
     * @param y CircuitUnit上の絶対座標ｙ。
     * @param x CircuitUnit上の絶対座標ｘ。
     */
    public void addTmps(int y, int x) {
        this.tmps.add(circuitUnit.getCircuitBlock().getMatrix().get(y).get(x));
    }

    /**
     * tmpsの中身を破棄する。
     * tmps自身のインスタンスを破棄する訳ではない。
     */
    public void resetTmps() {
        if (this.tmps != null) {
            if (!this.tmps.isEmpty()) {
                this.tmps.clear();
            }
        }
    }

    /**
     * idTmps
     */
    public ArrayList<IntegerDimension> getIdTmps() {
        return idTmps;
    }

    /**
     * idTmpsの中身を破棄する。
     * idTmps自身のインスタンスを破棄する訳ではない。
     */
    public void resetIdTmps() {
        if (this.idTmps != null) {
            if (!this.idTmps.isEmpty()) {
                this.idTmps.clear();
            }
        }
    }

    /**
     * hcTmps
     */
    public ArrayList<HighLevelConnectInfo> getHcTmps() {
        return hcTmps;
    }

    /**
     * 指定座標を含んでいるグループを列挙する。
     * 注意：hcTmpsに登録されているグループが対象ではなく、基板上のすべてのグループが対象。
     *
     * @param abco CircuitUnit上の絶対座標。
     */
    public ArrayList<HighLevelConnectInfo> getHcTmp(IntegerDimension abco) {
        ArrayList<HighLevelConnectInfo> list = new ArrayList<>();
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getNode()) {
            /* 節はNODEとCENTER_NODEを探索する */
            if (info.getRole() == HighLevelConnectGroup.NODE || info.getRole() == HighLevelConnectGroup.CENTER_NODE) {
                for (IntegerDimension abcos : info.getAbcos()) {
                    if (abcos.equals(abco)) {
                        list.add(info);
                        break;
                    }
                }
            }
        }
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getBranch()) {
            /* 導線は仮想部は無視する */
            if (!info.isVirtual()) {
                /* 導線はBRANCHのみ探索する */
                if (info.getRole() == HighLevelConnectGroup.BRANCH) {
                    for (IntegerDimension abcos : info.getAbcos()) {
                        if (abcos.equals(abco)) {
                            list.add(info);
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * hcTmpsにHighLevelConnectInfoを追加する。
     *
     * @param abco CircuitUnit上の絶対座標。
     */
    public void addHcTmp(IntegerDimension abco) {
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getNode()) {
            /* 節はNODEとCENTER_NODEを探索する */
            if (info.getRole() == HighLevelConnectGroup.NODE || info.getRole() == HighLevelConnectGroup.CENTER_NODE) {
                for (IntegerDimension abcos : info.getAbcos()) {
                    if (abcos.equals(abco)) {
                        hcTmps.add(info);
                        break;
                    }
                }
            }
        }
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getBranch()) {
            /* 導線は仮想部は無視する */
            if (!info.isVirtual()) {
                /* 導線はBRANCHのみ探索する */
                if (info.getRole() == HighLevelConnectGroup.BRANCH) {
                    for (IntegerDimension abcos : info.getAbcos()) {
                        if (abcos.equals(abco)) {
                            hcTmps.add(info);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * hcTmpsのHighLevelConnectInfoを削除する。
     *
     * @param abco CircuitUnit上の絶対座標。
     */
    public void removeHcTmp(IntegerDimension abco) {
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getNode()) {
            /* 節はNODEとCENTER_NODEを探索する */
            if (info.getRole() == HighLevelConnectGroup.NODE || info.getRole() == HighLevelConnectGroup.CENTER_NODE) {
                for (IntegerDimension abcos : info.getAbcos()) {
                    if (abcos.equals(abco)) {
                        hcTmps.remove(info);
                        break;
                    }
                }
            }
        }
        for (HighLevelConnectInfo info : getCircuitUnit().getHighLevelConnectList().getBranch()) {
            /* 導線は仮想部は無視する */
            if (!info.isVirtual()) {
                /* 導線はBRANCHのみ探索する */
                if (info.getRole() == HighLevelConnectGroup.BRANCH) {
                    for (IntegerDimension abcos : info.getAbcos()) {
                        if (abcos.equals(abco)) {
                            hcTmps.remove(info);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * hcTmpsの中身を破棄する。
     * hcTmps自身のインスタンスを破棄する訳ではない。
     */
    public void resetHcTmps() {
        if (this.hcTmps != null) {
            if (!this.hcTmps.isEmpty()) {
                this.hcTmps.clear();
            }
        }
    }

    /**
     * elecomInfoSelector
     */
    public ElecomInfoSelector getElecomInfoSelector() {
        return elecomInfoSelector;
    }

    public void setElecomInfoSelector(ElecomInfoSelector elecomInfoSelector) {
        this.elecomInfoSelector = elecomInfoSelector;
    }

    /**
     * operateBorder
     */
    public OperateBorder_ getOperateBorder() {
        return operateBorder;
    }

    public void setOperateBorder(OperateBorder_ operateBorder) {
        this.operateBorder = operateBorder;
    }

    /**
     * operateDetection
     */
    public OperateDetection_ getOperateDetection() {
        return operateDetection;
    }

    public OperateOperate_ getOperateOperate() {
        return operateOperate;
    }

    /**
     * operateOperate
     */
    public void setOperateOperate(OperateOperate_ operateOperate) {
        this.operateOperate = operateOperate;
    }

    public void setOperateDetection(OperateDetection_ operateDetection) {
        this.operateDetection = operateDetection;
    }

    @Override
    public void handResize(int w, int h) {
    }

    /**
     * UnitPanel内でマウスを押下した地点を、基板の基準座標からの相対座標に直してpressedCoに格納する。
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressedCo.setHeight(e.getY() - paintBaseCo.getHeight());
        pressedCo.setWidth(e.getX() - paintBaseCo.getWidth());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * UnitPanel内でドラッグした地点を初期押下地点からの相対座標に直し、基板の描画基準座標を変更する。
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        paintBaseCo.setHeight(e.getY() - pressedCo.getHeight());
        paintBaseCo.setWidth(e.getX() - pressedCo.getWidth());
        operateOperate.getMouseInIndex(this, e.getY(), e.getX());
    }

    /**
     * 拡大率を変更する。
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        prePaintRatio = paintRatio;
        /* 縮小 */
        if (e.getWheelRotation() > 0) {
            setPaintRatio(paintRatio - 1);
        }
        /* 拡大 */
        else if (e.getWheelRotation() < 0) {
            setPaintRatio(paintRatio + 1);
        }
        /* 精密な補正を計算するために、あえて実数型に変換してから計算しています */
        paintBaseCo.setHeight(e.getY() - (int) ((e.getY() - paintBaseCo.getHeight()) * ((double) paintRatio / prePaintRatio)));
        paintBaseCo.setWidth(e.getX() - (int) ((e.getX() - paintBaseCo.getWidth()) * ((double) paintRatio / prePaintRatio)));
        operateOperate.getMouseInIndex(this, e.getY(), e.getX());
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * マウスカーソルの座標をマス単位に変換する。
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        operateOperate.getMouseInIndex(this, e.getY(), e.getX());
    }
}
