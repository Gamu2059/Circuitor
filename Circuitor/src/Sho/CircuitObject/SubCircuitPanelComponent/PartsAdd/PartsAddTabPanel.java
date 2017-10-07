package Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd;

import KUU.BaseComponent.BaseFrame;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.ElecomInfo;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 部品追加タブの中に入れるパネルクラス。
 * 固定サイズのボタンを複数個内包します。
 */
public class PartsAddTabPanel extends NewJPanel implements ComponentListener,MouseWheelListener {
    /**
     * タブに表示する文字列。
     */
    private String title;
    /**
     * ボタンの描画基準となる座標。
     */
    private int paintBase;

    public PartsAddTabPanel(BaseFrame frame, PartsVarieties varieties) {
        super(frame);
        switch (varieties) {
            case CAPACITANCE:
                title = "コンデンサ";
                break;
            case DIODE:
                title = "ダイオード";
                break;
            case LED:
                title = "ＬＥＤ";
                break;
            case LOGIC_IC:
                title = "論理素子";
                break;
            case PIC:
                title = "マイコン";
                break;
            case POWER:
                title = "電源";
                break;
            case RESISTANCE:
                title = "抵抗";
                break;
            case SWITCH:
                title = "スイッチ";
                break;
            case TRANSISTOR:
                title = "トランジスタ";
                break;
            case MEASURE:
                title = "計測器";
                break;
            case PULSE:
                title = "パルス出力器";
                break;
            case WIRE:
                title = "導線";
                break;
        }
        paintBase = 0;
        setBackground(ColorMaster.getClickedColor());
        addComponentListener(this);
        addMouseWheelListener(this);
        setLayout(null);
        ElecomInfo elecomInfo;
        PartsPanelKeyWords.getPartsPanelKeyWords();
        for (int index : PartsPanelKeyWords.getIntegerElecomInfoHashMap().keySet()) {
            elecomInfo = PartsPanelKeyWords.getIntegerElecomInfoHashMap().get(index);
            if (elecomInfo.getPartsVarieties() == varieties) {
                this.add(new PartsButton(frame, elecomInfo));
            }
        }
    }

    public String getTitle() {
        return title;
    }

    private void setPaintBase(int paintBase) {
        if (paintBase < 0 || getComponents().length * getWidth() <= getHeight()) {
            paintBase = 0;
        } else if (paintBase > getComponents().length * getWidth() - getHeight()) {
            paintBase = getComponents().length * getWidth() - getHeight();
        }
        this.paintBase = paintBase;
    }

    /**
     * ホイールを動かしたらボタンの描画基準点を変える。
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /* 描画位置が下に行く */
        if (e.getWheelRotation() < 0) {
            setPaintBase(paintBase - 60);
        }
        /* 描画位置が上に行く */
        else if (e.getWheelRotation() > 0) {
            setPaintBase(paintBase + 60);
        }
        handResize(getWidth(), getHeight());
    }

    /**
     * リサイズを検出して下位のコンポーネントに通知する。
     */
    @Override
    public void componentResized(ComponentEvent e) {
        /* リサイズされたらpaintBaseはリセットされる */
        paintBase = 0;
        handResize(getWidth(), getHeight());
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

    @Override
    public void handResize(int w, int h) {
        for (int i = 0; i < getComponents().length; i++) {
            getComponents()[i].setBounds(0, w * i - paintBase, w, w);
        }
    }
}
