package Sho.Matrix;

import java.util.ArrayList;

/**
 * 行列を表現するクラス。整数版。
 *
 * @author 翔
 * @version 1.1
 */
public class IntegerMatrix extends OriginMatrix {
    private ArrayList<ArrayList<Integer>> matrix;

    public IntegerMatrix() {
        super();
        matrix = new ArrayList<>();
    }

    public ArrayList<ArrayList<Integer>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

    @Override
    public void clear() {
        matrix.clear();
        getRowRelatedIndex().clear();
        getColumnRelatedIndex().clear();
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < getColumnRelatedIndex().size(); i++) {
            stringBuffer.append(getColumnRelatedIndex().get(i) + ",");
        }
        stringBuffer.append("\n");
        for (int i = 0; i < matrix.size(); i++) {
            stringBuffer.append(getRowRelatedIndex().get(i) + " : ");
            for (int j = 0; j < matrix.get(0).size(); j++) {
                stringBuffer.append(matrix.get(i).get(j) + ",");
            }
            stringBuffer.append("\n");
        }
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    /**
     * 自身と渡された行列との和を新しい行列として返す。
     * rowとcolumnは自身のものがコピーされる。
     */
    public IntegerMatrix getAdd(IntegerMatrix add) {
        if (this.getRowRelatedIndex().size() != add.getRowRelatedIndex().size() || this.getColumnRelatedIndex().size() != add.getColumnRelatedIndex().size()) {
            return null;
        }

        IntegerMatrix matrix = new IntegerMatrix();

        for (int i : this.getRowRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : this.getColumnRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        for (int i = 0; i < this.getRowRelatedIndex().size(); i++) {
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < this.getColumnRelatedIndex().size(); j++) {
                matrix.matrix.get(i).add(this.matrix.get(i).get(j) + add.matrix.get(i).get(j));
            }
        }
        return matrix;
    }

    /**
     * 自身と渡された行列との差を新しい行列として返す。
     * rowとcolumnは自身のものがコピーされる。
     */
    public IntegerMatrix getSub(IntegerMatrix sub) {
        if (this.getRowRelatedIndex().size() != sub.getRowRelatedIndex().size() || this.getColumnRelatedIndex().size() != sub.getColumnRelatedIndex().size()) {
            return null;
        }

        IntegerMatrix matrix = new IntegerMatrix();

        for (int i : this.getRowRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : this.getColumnRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        for (int i = 0; i < this.getRowRelatedIndex().size(); i++) {
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < this.getColumnRelatedIndex().size(); j++) {
                matrix.matrix.get(i).add(this.matrix.get(i).get(j) - sub.matrix.get(i).get(j));
            }
        }
        return matrix;
    }

    /**
     * 自身と渡された行列との積を新しい行列として返す。
     * rowは自身のものが、columnは渡された行列のものがコピーされる。
     */
    public IntegerMatrix getMul(IntegerMatrix mul) {
        if (this.getColumnRelatedIndex().size() != mul.getRowRelatedIndex().size()) {
            return null;
        }

        IntegerMatrix matrix = new IntegerMatrix();

        for (int i : this.getRowRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : mul.getColumnRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        int tmp;
        for (int i = 0; i < this.getRowRelatedIndex().size(); i++) {
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < mul.getColumnRelatedIndex().size(); j++) {
                tmp = 0;
                for (int k = 0; k < this.getColumnRelatedIndex().size(); k++) {
                    tmp += this.matrix.get(i).get(k) * mul.matrix.get(k).get(j);
                }
                matrix.matrix.get(i).add(tmp);
            }
        }
        return matrix;
    }
    public DoubleMatrix getMul(DoubleMatrix mul) {
        if (this.getColumnRelatedIndex().size() != mul.getRowRelatedIndex().size()) {
            return null;
        }

        DoubleMatrix matrix = new DoubleMatrix();

        for (int i : this.getRowRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : mul.getColumnRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        double tmp;
        for (int i = 0; i < this.getRowRelatedIndex().size(); i++) {
            matrix.getMatrix().add(new ArrayList<>());
            for (int j = 0; j < mul.getColumnRelatedIndex().size(); j++) {
                tmp = 0;
                for (int k = 0; k < this.getColumnRelatedIndex().size(); k++) {
                    tmp += this.matrix.get(i).get(k) * mul.getMatrix().get(k).get(j);
                }
                matrix.getMatrix().get(i).add(tmp);
            }
        }
        return matrix;
    }

    /**
     * 自身と渡された行列との積を新しい行列として返す。
     * しかし、積演算は総和ではなく、逆数の総和になる。
     */
    public DoubleMatrix getReciprocalMul(DoubleMatrix mul) {
        if (this.getColumnRelatedIndex().size() != mul.getRowRelatedIndex().size()) {
            return null;
        }

        DoubleMatrix matrix = new DoubleMatrix();

        for (int i : this.getRowRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : mul.getColumnRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        double tmp;
        for (int i = 0; i < this.getRowRelatedIndex().size(); i++) {
            matrix.getMatrix().add(new ArrayList<>());
            for (int j = 0; j < mul.getColumnRelatedIndex().size(); j++) {
                tmp = 0;
                for (int k = 0; k < this.getColumnRelatedIndex().size(); k++) {
                    if(this.matrix.get(i).get(k) * mul.getMatrix().get(k).get(j) > 0) {
                        tmp += 1 / (this.matrix.get(i).get(k) * mul.getMatrix().get(k).get(j));
                    }
                }
                matrix.getMatrix().get(i).add(tmp);
            }
        }
        return matrix;
    }

    /**
     * 自身の転置変換を行った行列を返す。
     * rowとcolumnは自身のものがコピーされる。
     */
    public IntegerMatrix getTurn() {
        IntegerMatrix matrix = new IntegerMatrix();

        for (int i : this.getColumnRelatedIndex()) {
            matrix.getRowRelatedIndex().add(i);
        }
        for (int i : this.getRowRelatedIndex()) {
            matrix.getColumnRelatedIndex().add(i);
        }
        for (int i = 0; i < this.getColumnRelatedIndex().size(); i++) {
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < this.getRowRelatedIndex().size(); j++) {
                matrix.matrix.get(i).add(this.matrix.get(j).get(i));
            }
        }
        return matrix;
    }
}
