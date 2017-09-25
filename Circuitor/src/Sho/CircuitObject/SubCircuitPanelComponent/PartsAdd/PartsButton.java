package Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd;

import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.ImageMaster;
import Sho.CircuitObject.Circuit.CircuitBlock;
import Sho.CircuitObject.Circuit.ElecomInfo;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 部品追加パネルに組み込まれるボタンクラス。
 */
public class PartsButton extends JButton implements MouseListener {
    /** 大元のJFrameのアドレスを保持する */
    private BaseFrame frame;
    /**
     * このボタンが押された瞬間に生成される最低限の電子部品情報を格納するための汎用変数。
     */
    private CircuitBlock circuitBlock;
    /**
     * 背景色は全ウィンドウ共通なのでstatic。
     */
    private static Color color;

    public PartsButton(BaseFrame frame, ElecomInfo elecomInfo) {
        super(ImageMaster.getImageMaster().getModelImage(elecomInfo));
        this.frame = frame;
        if (color == null) {
            color = new Color(180, 200, 200);
        }
        circuitBlock = new CircuitBlock(0, 0);
        circuitBlock.setElecomInfo(elecomInfo);
        /* サイズを生成しておかないと部品追加時に不具合をきたす */
        circuitBlock.getElecomInfo().setSize(new IntegerDimension());
        setFocusPainted(false);
        setBackground(color);
        String title;
        switch (elecomInfo.getPartsStandards()) {
//            case _10UF:
//                title = "コンデンサ";
//                break;
            case RECT:
                title = "ダイオード";
                break;
            case BLUE:
                title = "青色ＬＥＤ";
                break;
            case GREEN:
                title = "緑色ＬＥＤ";
                break;
            case RED:
                title = "赤色ＬＥＤ";
                break;
            case AND_CHIP:
                title = "ＡＮＤ チップ";
                break;
            case AND_IC:
                title = "ＡＮＤ ＩＣ";
                break;
            case NOT_CHIP:
                title = "ＮＯＴ チップ";
                break;
            case NOT_IC:
                title = "ＮＯＴ ＩＣ";
                break;
            case OR_CHIP:
                title = "ＯＲ チップ";
                break;
            case OR_IC:
                title = "ＯＲ ＩＣ";
                break;
            case XOR_CHIP:
                title = "ＸＯＲ チップ";
                break;
            case XOR_IC:
                title = "ＸＯＲ ＩＣ";
                break;
            case _18PINS:
                title = "マイコン";
                break;
            case DC:
                title = "直流電源";
                break;
            case _10:
                title = "10Ω";
                break;
            case _100:
                title = "100Ω";
                break;
            case _1000:
                title = "1000Ω";
                break;
            case _10000:
                title = "10000Ω";
                break;
            case _variable:
                title = "可変抵抗";
                break;
            case TACT:
                title = "スイッチ";
                break;
            case BIPOLAR_NPN:
                title = "トランジスタ";
                break;
            case VOLTMETER:
                title = "電圧計";
                break;
            case AMMETER:
                title = "電流計";
                break;
            case PULSE:
                title = "可変パルス出力器";
                break;
            default:
                title = "導線";
                break;
        }
        setText(title);
        setVerticalTextPosition(JButton.BOTTOM);
        setHorizontalTextPosition(JButton.CENTER);
        addMouseListener(this);
    }

    public CircuitBlock getCircuitBlock() {
        return circuitBlock;
    }

    /**
     * 回路エディタパネルに押されたボタンの情報を通知する。
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        frame.getBasePanel().getEditCircuitPanel().mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
