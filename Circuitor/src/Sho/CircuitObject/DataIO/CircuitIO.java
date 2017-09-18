package Sho.CircuitObject.DataIO;

import DataIO.DataIO;
import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.PartsDirections;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import java.io.*;


/**
 * 回路側のデータ入出力をまとめたクラス。
 */
public class CircuitIO {
    public static final String START = "_____START CIRCUIT DATA_____";
    public static final String END = "_____END CIRCUIT DATA_____";

    private BaseFrame frame;

    public CircuitIO(BaseFrame frame) {
        this.frame = frame;
    }

    public void outputter(PrintWriter w) throws Exception {
        if (w == null) {
            throw new Exception("PrintWriter is null");
        }

        CircuitUnit u = frame.getBasePanel().getEditCircuitPanel().getCircuitUnit();
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;

        w.println(START);
        for (int i=0;i<u.getCircuitBlock().getMatrix().size();i++) {
            for (int j=0;j<u.getCircuitBlock().getMatrix().get(0).size();j++) {
                b = u.getCircuitBlock().getMatrix().get(i).get(j);
                if (b.isExist()) {
                    if (b.getCircuitInfo().getReco().equals(0, 0)) {
                        c = b.getCircuitInfo();
                        e = b.getElecomInfo();

                        w.println(e.getPartsVarieties()+","+e.getPartsStandards()+","+e.getPartsDirections()+","+c.getAbco().getHeight()+","+c.getAbco().getWidth()+","+e.getEtcStatus());
                    }
                }
            }
        }
        w.println(END);
    }

    public void inputter(BufferedReader r) throws Exception {
        if (r == null) {
            throw new Exception("BufferedReader is null");
        }

        UnitPanel panel = frame.getBasePanel().getEditCircuitPanel();

        String line;
        String[] data;

        /* 読み込み開始 */
        /* すでに存在している回路を削除する */
        panel.getOperateOperate().allDelete(panel);
        CircuitBlock b;
        int y,x, status;

        // STARTの文字列が出現するまで読み飛ばし
        line = r.readLine();
        while (!line.equals(START)) {
            line = r.readLine();
        }
        line = r.readLine();

        while (!line.isEmpty() && !line.equals(END)) {
            data = line.split(",");
            /* 0番：種類　1番：規格　2番：向き　3番：ｙ座標　4番：ｘ座標　5番：ステータス */
            /* 読み込んだ情報を追加していく */
            y = Integer.parseInt(data[3]);
            x = Integer.parseInt(data[4]);
            status = Integer.parseInt(data[5]);
            /* ブロックを生成し、情報を書き込む */
            b = new CircuitBlock(y, x);
            b.getCircuitInfo().setReco(new IntegerDimension());
            b.getElecomInfo().setPartsVarieties(PartsVarieties.valueOf(data[0]));
            b.getElecomInfo().setPartsStandards(PartsStandards.valueOf(data[1]));
            b.getElecomInfo().setEtcStatus(status);
            panel.getOperateOperate().specifiedAdd(panel, b, y, x, PartsDirections.valueOf(data[2]));
            line = r.readLine();
        }
        panel.getOperateOperate().update(panel);
    }
}
