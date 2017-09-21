package DataIO;

import KUU.BaseComponent.BaseFrame;
import ProcessTerminal.SaveLoadModule.ProgramIO;
import Sho.CircuitObject.DataIO.CircuitIO;
import Sho.CircuitObject.DataIO.MiconPinIO;

import java.io.*;

/**
 * Circuitorのセーブデータ入出力を管理するクラス。
 * 回路データ、命令データ、ピン設定データを入出力する。
 */
public class CctIO extends DataIO {
    private ProgramIO programIO;
    private CircuitIO circuitIO;
    private MiconPinIO miconPinIO;

    public CctIO(BaseFrame frame) {
        super(frame, new CctFilter(), "cct");
        circuitIO = new CircuitIO(frame);
        programIO = frame.getMasterTerminal().getProgramIO();
        miconPinIO = new MiconPinIO(frame);
    }

    protected void outputter(File f) throws Exception {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(f)));

        try {
            circuitIO.outputter(printWriter);
            programIO.outputter(printWriter);
            miconPinIO.outputter(printWriter);
            getFrame().getHelpLabel().setText("データの保存に成功しました。");
        } catch (Exception e) {
            e.printStackTrace();
            getFrame().getHelpLabel().setText("データの保存に失敗しました。");
        } finally {
            printWriter.close();
        }

    }

    protected void inputter(File f) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(f));

        try {
            circuitIO.inputter(reader);
            programIO.inputter(reader);
            miconPinIO.inputter(reader);
            getFrame().getHelpLabel().setText("データの読込に成功しました。");
        } catch (Exception e) {
            e.printStackTrace();
            getFrame().getHelpLabel().setText("データの読込に失敗しました。");
        } finally {
            reader.close();
        }
    }
}
