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
public class CircuitIO extends DataIO {
    public CircuitIO(BaseFrame frame) {
        super(frame, new CCTCFilter(), "cctc");
    }

    protected void outputter(File f) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        CircuitUnit u = getFrame().getBasePanel().getEditCircuitPanel().getCircuitUnit();
        CircuitBlock b;
        CircuitInfo c;
        ElecomInfo e;
        for (int i=0;i<u.getCircuitBlock().getMatrix().size();i++) {
            for (int j=0;j<u.getCircuitBlock().getMatrix().get(0).size();j++) {
                b = u.getCircuitBlock().getMatrix().get(i).get(j);
                if (b.isExist()) {
                    if (b.getCircuitInfo().getReco().equals(0, 0)) {
                        c = b.getCircuitInfo();
                        e = b.getElecomInfo();

                        writer.write(e.getPartsVarieties()+","+e.getPartsStandards()+","+e.getPartsDirections()+","+c.getAbco().getHeight()+","+c.getAbco().getWidth()+","+e.getEtcStatus());
                        writer.newLine();
                    }
                }
            }
        }
        writer.close();
    }

    protected void inputter(File f) throws Exception {
        UnitPanel panel = getFrame().getBasePanel().getEditCircuitPanel();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        try {
            String line;
            String[] data;

            /* 読み込む前に検査を設ける */
            line = reader.readLine();
            while (line != null) {
                data = line.split(",", 0);
                /* 0番：種類　1番：規格　2番：向き　3番：ｙ座標　4番：ｘ座標　5番：ステータス */
                PartsVarieties.valueOf(data[0]);
                PartsStandards.valueOf(data[1]);
                PartsDirections.valueOf(data[2]);
                Integer.parseInt(data[3]);
                Integer.parseInt(data[4]);
                Integer.parseInt(data[5]);
                line = reader.readLine();
            }
            reader.close();

            /* 読み込み開始 */
            /* すでに存在している回路を削除する */
            panel.getOperateOperate().allDelete(panel);
            CircuitUnit u = panel.getCircuitUnit();
            CircuitBlock b;
            int y,x, status;
            reader = new BufferedReader(new FileReader(f));
            line = reader.readLine();
            while (line != null) {
                data = line.split(",");
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
                line = reader.readLine();
            }
            panel.getOperateOperate().update(panel);
            getFrame().getHelpLabel().setText("回路データの入力に成功しました。");
        } catch (IOException e) {
            getFrame().getHelpLabel().setText("回路データの入力に失敗しました。");
        } finally {
            reader.close();
        }
    }
}
