package Sho.CircuitObject.HighLevelConnect;

import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * HighLevelConnectInfoクラスで必要な隣接グループの情報を一元的に管理するクラス。
 *
 * @author 翔
 * @version 1.1
 */
public class HighLevelNextInfo implements CopyTo,Cloneable {
    /** 隣接グループの隣接部の絶対座標 */
    private ArrayList<IntegerDimension> abcos;

    /** 隣接グループのリスト内でのインデックス */
    private ArrayList<Integer> indexs;

    /** 隣接グループが隣接しているブロックに対する隣接方向 */
    private ArrayList<Integer> directions;

    /** 隣接グループの情報そのもの */
    private ArrayList<HighLevelConnectInfo> infos;

    public HighLevelNextInfo() {
        abcos = new ArrayList<>();
        indexs = new ArrayList<>();
        directions = new ArrayList<>();
        infos = new ArrayList<>();
    }

    /**
     * 情報を空にする。
     */
    public void clear() {
        indexs.clear();
        infos.clear();
    }

    public ArrayList<IntegerDimension> getAbcos() {
        return abcos;
    }

    public void setAbcos(ArrayList<IntegerDimension> abcos) {
        this.abcos = abcos;
    }

    public ArrayList<Integer> getIndexs() {
        return indexs;
    }

    public void setIndexs(ArrayList<Integer> indexs) {
        this.indexs = indexs;
    }

    public ArrayList<Integer> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<Integer> directions) {
        this.directions = directions;
    }

    public ArrayList<HighLevelConnectInfo> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<HighLevelConnectInfo> infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("   HighLevelNextInfo :\n");
        stringBuffer.append("       abcos : " + abcos + "\n");
        stringBuffer.append("       indexs : " + indexs + "\n");
        stringBuffer.append("       directions : " + directions + "\n");
        return stringBuffer.toString();
    }

    @Override
    public HighLevelNextInfo clone() {
        HighLevelNextInfo clone = new HighLevelNextInfo();
        for (IntegerDimension abco : this.abcos) {
            clone.getAbcos().add(abco.clone());
        }
        for (int index : this.indexs) {
            clone.getIndexs().add(index);
        }
        for (int direction : this.directions) {
            clone.getDirections().add(direction);
        }
        for (HighLevelConnectInfo info : this.infos) {
            clone.getInfos().add(info);
        }
        return clone;
    }

    @Override
    public void copyTo(Object o) {
        if (o instanceof HighLevelNextInfo) {
            HighLevelNextInfo highLevelNextInfo = (HighLevelNextInfo)o;
            highLevelNextInfo.abcos.clear();
            for (IntegerDimension abco : this.abcos) {
                highLevelNextInfo.getAbcos().add(abco);
            }
            highLevelNextInfo.indexs.clear();
            for (int index : this.indexs) {
                highLevelNextInfo.getIndexs().add(index);
            }
            highLevelNextInfo.directions.clear();
            for (int direction : this.directions) {
                highLevelNextInfo.getDirections().add(direction);
            }
            highLevelNextInfo.infos.clear();
            for (HighLevelConnectInfo info : this.infos) {
                highLevelNextInfo.getInfos().add(info);
            }
        }
        else {
            new CannotCopyToException("HighLevelNextInfo");
        }
    }
}