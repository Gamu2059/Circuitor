package Sho.CircuitObject.MiconPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.BaseComponent.BasePanel;
import KUU.NewComponent.NewJPanel;
import Master.BorderMaster.BorderMaster;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.TerminalDirection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * マイコンのピン設定を行うためのパネルクラス。
 */
public class MiconPanel extends NewJPanel {
    /**
     * ピン情報を保持する。
     */
    private TerminalDirection miconPin[];
    /**
     * ピン設定表示ラベル。
     */
    private MiconLabel miconLabel[];

    public MiconPanel(BaseFrame frame) {
        super(frame);
        /** パネルの設定 */
        setLayout(null);
        setBackground(ColorMaster.getBackColor());
        setBorder(BorderMaster.getRegularBorder());
        /** ピン設定の初期化 */
        miconPin = new TerminalDirection[18];
        for (int i = 0; i < miconPin.length; i++) {
            miconPin[i] = TerminalDirection.OUT;
        }
        /** コンポーネントの登録 */
        miconLabel = new MiconLabel[18];
        for (int i = 0; i < 18; i++) {
            if (i != 4 && i != 13) {
                if (i < 9) {
                    add(miconLabel[i] = new MiconLabel(frame, i, "出力"));
                } else {
                    /* 逆に詰めないとさかさまになってしまう */
                    add(miconLabel[26 - i] = new MiconLabel(frame, 26 - i, "出力"));
                }
            } else if (i == 4) {
                add(miconLabel[i] = new MiconLabel(frame, i, "接地"));
            } else {
                add(miconLabel[i] = new MiconLabel(frame, i, "電源"));
            }
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                getFrame().getHelpLabel().setText("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (getFrame().getBasePanel().getOverAllMode() != BasePanel.OverAllMode.EXECUTE) {
                    getFrame().getHelpLabel().setText("マイコンのピンを入力用にするか出力用にするかを設定できます。");
                } else {
                    getFrame().getHelpLabel().setText("現在、この画面は編集出来ません。");
                }
            }
        });
    }

    /**
     * miconPin
     */
    public TerminalDirection[] getMiconPin() {
        return miconPin;
    }

    /**
     * miconLabel
     */
    public MiconLabel[] getMiconLabel() {
        return miconLabel;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(
                ImageMaster.getImageMaster().getModelImage(PartsVarieties.PIC, PartsStandards.MODEL).getImage(),
                getWidth() / 3,
                0,
                getWidth() - (getWidth() / 3) * 2,
                getHeight(),
                this
        );
    }

    @Override
    public void handResize(int width, int height) {
        for (int i = 0; i < 18; i++) {
            if (i < 9) {
                if (i != 8) {
                    miconLabel[i].setBounds(0, (height / 9) * i, width / 3, height / 9);
                } else {
                    miconLabel[i].setBounds(0, (height / 9) * i, width / 3, height - (height / 9) * i);
                }
            } else {
                if (i != 9) {
                    miconLabel[i].setBounds(width - width / 3, (height / 9) * (17 - i), width / 3, height / 9);
                } else {
                    miconLabel[i].setBounds(width - width / 3, (height / 9) * (17 - i), width / 3, height - (height / 9) * (17 - i));
                }
            }
        }
    }
}
