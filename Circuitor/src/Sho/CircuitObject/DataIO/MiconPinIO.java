package Sho.CircuitObject.DataIO;

import KUU.BaseComponent.BaseFrame;
import Master.ColorMaster.ColorMaster;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.MiconPanel.MiconLabel;
import Sho.CircuitObject.MiconPanel.MiconPanel;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * マイコンピンのデータ入出力をまとめたクラス。
 */
public class MiconPinIO {
    private static final String START = "_____START MICON DATA_____";
    private static final String END = "_____END MICON DATA_____";

    private BaseFrame frame;

    public MiconPinIO(BaseFrame frame) {
        this.frame = frame;
    }

    public void outputter(PrintWriter w) throws Exception {
        if (w == null) {
            throw new Exception("PrintWriter is null");
        }

        MiconPanel panel = frame.getBasePanel().getMiconPanel();

        w.println(START);
        for (int i=0;i<panel.getMiconPin().length;i++) {
            w.print(panel.getMiconPin()[i]+",");
        }
        w.println();
        w.println(END);
    }

    public void inputter(BufferedReader r) throws Exception {
        if (r == null) {
            throw new Exception("BufferedReader is null");
        }

        MiconPanel panel = frame.getBasePanel().getMiconPanel();
        TerminalDirection[] pin = panel.getMiconPin();
        MiconLabel[] label = panel.getMiconLabel();

        String line;
        String[] data;

        /* 読み込み開始 */
        // STARTの文字列が出現するまで読み飛ばし
        line = r.readLine();
        while (!line.equals(START)) {
            line = r.readLine();
        }
        line = r.readLine();

        while (!line.isEmpty() && !line.equals(END)) {
            data = line.split(",");
            for (int i=0;i<data.length;i++) {
                switch (data[i]){
                    case "GND":
                        pin[i] = TerminalDirection.GND;
                        label[i].setBackground(ColorMaster.getNotSelectedColor());
                        break;
                    case "POWER":
                        pin[i] = TerminalDirection.POWER;
                        label[i].setBackground(ColorMaster.getNotSelectedColor());
                        break;
                    case "IN":
                        pin[i] = TerminalDirection.IN;
                        label[i].setBackground(ColorMaster.getInColor());
                        break;
                    default:
                        pin[i] = TerminalDirection.OUT;
                        label[i].setBackground(ColorMaster.getOutColor());
                        break;
                }
                label[i].setText(MiconLabel.getCorrespondText(pin[i]));
            }
            line = r.readLine();
        }
        /* 対応の変更を通達する */
        frame.getBasePanel().getEditCircuitPanel().updateLink();
    }
}