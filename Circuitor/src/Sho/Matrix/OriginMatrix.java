package Sho.Matrix;

import java.util.ArrayList;

/**
 * 行列を表現するクラス。
 * ただし、大元であるため行列の型を保持できない。
 *
 * @author 翔
 * @version 1.1
 */
public abstract class OriginMatrix {
    private ArrayList<Integer> rowRelatedIndex;
    private ArrayList<Integer> columnRelatedIndex;

    public OriginMatrix() {
        rowRelatedIndex = new ArrayList<>();
        columnRelatedIndex = new ArrayList<>();
    }

    public ArrayList<Integer> getRowRelatedIndex() {
        return rowRelatedIndex;
    }

    public void setRowRelatedIndex(ArrayList<Integer> rowRelatedIndex) {
        this.rowRelatedIndex = rowRelatedIndex;
    }

    public ArrayList<Integer> getColumnRelatedIndex() {
        return columnRelatedIndex;
    }

    public void setColumnRelatedIndex(ArrayList<Integer> columnRelatedIndex) {
        this.columnRelatedIndex = columnRelatedIndex;
    }

    public abstract void clear();
}
