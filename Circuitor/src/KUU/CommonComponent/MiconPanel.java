package KUU.CommonComponent;

import KUU.BaseComponent.BaseFrame;
import Sho.CircuitObject.Circuit.TerminalDirection;
import Sho.CircuitObject.MiconPanel.MiconNotPinListener;

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
                if (i==4||i==13) {
                    getMiconLabel()[i].addMouseListener(new MiconNotPinListener(getFrame()));
                } else {
                    getMiconLabel()[i].addMouseListener(getMiconLabel()[i]);
                }
            }
        }
    }
}
