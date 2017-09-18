package Sho.Matrix;

import java.util.ArrayList;

/**
 * 行列を表現するクラス。ジェネリクス版。
 *
 * @author 翔
 * @version 1.2
 */
public class Matrix<E> extends OriginMatrix{
    private ArrayList<ArrayList<E>> matrix;

    public Matrix() {
        super();
        matrix = new ArrayList<>();
    }

    public ArrayList<ArrayList<E>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<E>> matrix) {
        this.matrix = matrix;
    }

    @Override
    public void clear() {
        matrix.clear();
        getRowRelatedIndex().clear();
        getColumnRelatedIndex().clear();
    }
}
