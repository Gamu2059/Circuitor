package KUU.CommonComponent;

import KUU.BaseComponent.BaseFrame;
import Master.ColorMaster.ColorMaster;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.MiconPanel.MiconNotPinListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * マイコンのピン設定を行う画面のパネル。
 * モードによらず共通的に配置される。
 */
public class MiconPanel extends Sho.CircuitObject.MiconPanel.MiconPanel {
    public MiconPanel(BaseFrame frame) {
        super(frame);
    }


    /**
     * 実行開始と実行終了時に呼び出すメソッド。
     * @param state trueは開始を意味する。開始した場合はリスナが退避されて何もできなくなる。
     *               falseは終了を意味する。終了した場合はリスナが回復する。
     */

    public void setExecuteState(boolean state) {
        if (state) {
            for (int i=0;i<getMiconLabel().length;i++) {
                getMiconLabel()[i].removeMouseListener(getMiconLabel()[i]);
            }
        } else {
            for (int i=0;i<getMiconLabel().length;i++) {
                if (i==4 || i==13) {
                    getMiconLabel()[i].addMouseListener(new MiconNotPinListener(getFrame()));
                } else {
                    getMiconLabel()[i].addMouseListener(getMiconLabel()[i]);
                }
            }
        }
    }

    /**
     * ファイルの読み込み時に入力を設定するメソッド。
     * 初期生成時は全て出力のため、入力に変更する処理のみでよい。
     */
    public void setPinIO(String[] str){
        for (int i = 0; i < 18; i++) {
            if(Objects.equals(str[i], "IN")) {
                getFrame().getBasePanel().getMiconPanel().getMiconPin()[i] = TerminalDirection.IN;
                JLabel label = getFrame().getBasePanel().getMiconPanel().getMiconLabel()[i];
                label.setBackground(ColorMaster.getInColor());
                label.setText("入力");
            }
        }
        getFrame().getBasePanel().getEditCircuitPanel().updateLink();
    }
}
