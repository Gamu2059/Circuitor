package Sho.Matrix;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 行列を表現するクラス。実数版。
 * 
 * @author 翔 
 * @version 1.1
 */
public class DoubleMatrix extends OriginMatrix {
    /**
     * 一度に扱う数値の最大絶対値
     * オフスイッチ、ダイオードの逆バイアスなどで扱われる
     */
    public final static double MAXVALUE = 1e15;

    /**
     * 一度に扱う非ゼロの最小絶対値
     * 抵抗のない導線などで扱われる
     */
    public final static double MINVALUE = 1e-15;

    /**
     * 最小絶対値以下の時などに補正に使う値
     */
    public final static double CORRECTION = 1e-6;

    private ArrayList<ArrayList<Double>> matrix;

    public DoubleMatrix() {
        super();
        matrix = new ArrayList<>();
    }

    public ArrayList<ArrayList<Double>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Double>> matrix) {
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
    public DoubleMatrix getAdd(DoubleMatrix add) {
        if (this.getRowRelatedIndex().size() != add.getRowRelatedIndex().size() || this.getColumnRelatedIndex().size() != add.getColumnRelatedIndex().size()) {
            return null;
        }

        DoubleMatrix matrix = new DoubleMatrix();

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
    public DoubleMatrix getSub(DoubleMatrix sub) {
        if (this.getRowRelatedIndex().size() != sub.getRowRelatedIndex().size() || this.getColumnRelatedIndex().size() != sub.getColumnRelatedIndex().size()) {
            return null;
        }

        DoubleMatrix matrix = new DoubleMatrix();

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

    public DoubleMatrix getMul(IntegerMatrix mul) {
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
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < mul.getColumnRelatedIndex().size(); j++) {
                tmp = 0;
                for (int k = 0; k < this.getColumnRelatedIndex().size(); k++) {
                    tmp += this.matrix.get(i).get(k) * mul.getMatrix().get(k).get(j);
                }
                matrix.matrix.get(i).add(tmp);
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
            matrix.matrix.add(new ArrayList<>());
            for (int j = 0; j < mul.getColumnRelatedIndex().size(); j++) {
                tmp = 0;
                for (int k = 0; k < this.getColumnRelatedIndex().size(); k++) {
                    if(this.matrix.get(i).get(k) * mul.matrix.get(k).get(j) > 0) {
                        tmp += 1 / this.matrix.get(i).get(k) * mul.matrix.get(k).get(j);
                    }
                }
                matrix.matrix.get(i).add(tmp);
            }
        }
        return matrix;
    }

    /**
     * 自身の転置変換を行った行列を返す。
     * rowとcolumnは自身のものがコピーされる。
     */
    public DoubleMatrix getTurn() {
        DoubleMatrix matrix = new DoubleMatrix();

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

    /**
     * 自身の中身を通常の配列にコピーして返す。
     * ただし、横１列であるのが前提。
     */
    public double[] getArrayVector() {
        double ret[] = new double[getColumnRelatedIndex().size()];
        for (int i = 0; i < getColumnRelatedIndex().size(); i++) {
            ret[i] = matrix.get(0).get(i);
        }
        return ret;
    }

    /**
     * 自身の中身を通常の２次元配列にコピーして返す。
     */
    public double[][] getArrayMatrix() {
        double ret[][] = new double[getRowRelatedIndex().size()][getColumnRelatedIndex().size()];
        for (int i = 0; i < getRowRelatedIndex().size(); i++) {
            for (int j = 0; j < getColumnRelatedIndex().size(); j++) {
                ret[i][j] = matrix.get(i).get(j);
            }
        }
        return ret;
    }

    /**
     * 指定した係数行列の第index列に、vectorを代入して返す。
     */
    public static double[][] getVectorAssignment(double[][] matrix, double[] vector, int index) {
        if (index < 0 || index >= matrix[0].length) {
            return null;
        }
        double ret[][] = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (j == index) {
                    ret[i][j] = vector[i];
                } else {
                    ret[i][j] = matrix[i][j];
                }
            }
        }
        return ret;
    }

    /**
     * 余因子展開を用いて行列式を導出し、それを返す。
     * 10次あたりから極端に速度が落ちるため、使用はお勧めしない。
     */
    public static double getDeterminant(double[][] matrix) {
        int n = matrix.length;
        double d = 0.0, B[][], s = 1.0;
        int i1, i2, i3, k, m;

        if (n == 1) {
            d = matrix[0][0];
        } else {
            m = n - 1;
            B = new double[m][m];
            for (i1 = 0; i1 < n; i1++) {
                for (i2 = 0; i2 < m; i2++) {
                    k = 0;
                    for (i3 = 0; i3 < n; i3++) {
                        if (i3 != i1) {
                            B[i2][k] = matrix[i2 + 1][i3];
                            k++;
                        }
                    }
                }
                d += s * matrix[0][i1] * getDeterminant(B);
                s = -s;
            }
        }
        return d;
    }

    /**
     * 係数行列と右辺ベクトルを指定して、連立方程式の解ベクトルを返す。
     */
    public static ArrayList<ArrayList<Double>> getEquation(double[][] matrix, double[] vector) {
        ArrayList<ArrayList<Double>> ret = new ArrayList<>();
        ret.add(new ArrayList<>());
        double det = getDeterminant(matrix);
        for (int i = 0; i < matrix[0].length; i++) {
            ret.get(0).add(getDeterminant(getVectorAssignment(matrix, vector, i)) / det);
        }
        return ret;
    }

    /**
     * ガウスの消去法を用いて連立方程式を解き、その解をベクトルとして抽出して返す。
     * 完全ピボット選択を実装しているが、それでも対角成分が限りなく小さくなるのであれば、補正値を加算し崩壊を防ぐ。
     * ただし、補正値を加算すると大きな誤差が生じるため不具合の温床となる。
     * matrixは正方行列、vectorはmatrixと同次であることが前提条件。
     */
    public static ArrayList<ArrayList<Double>> getGaussEquation(double[][] matrix, double[] vector) {
        int N = matrix.length;
        double cor[][] = new double[N][N];
        DoubleMatrix equation = new DoubleMatrix();
        /* matrixとvectorをequationに代入する */
        for (int i = 0; i < N; i++) {
            // 縦の処理だが、面倒なのでここで横側のリストインデックスを初期化する
            equation.getColumnRelatedIndex().add(i, i);
            equation.getMatrix().add(i, new ArrayList<>());
            for (int j = 0; j < N; j++) {
                cor[i][j] = 0;
                equation.getMatrix().get(i).add(j, matrix[i][j]);
            }
            equation.getMatrix().get(i).add(N, vector[i]);
        }
        /* ガウスの消去法 */
        for (int f = 0; f < N; f++) {
            pivot(N, f, equation);
            forward(N, f, equation, cor);
        }
        /* 補正行列を係数行列に加算する */
        for (int i=0;i<N;i++) {
            for (int j=0;j<N;j++) {
                matrix[i][j] += cor[i][j];
            }
        }
        /* 後退代入を行い、そのまま返す */
        return backward(N, equation);
    }

    /**
     * 完全ピボット選択を行う
     *
     * @param f 現在注目している対角要素のインデックス
     */
    public static void pivot(int N, int f, DoubleMatrix equation) {
        /* 選択した座標を管理する */
        int pInX, pInY;
        double max;
        double tmp;
        max = Math.abs(equation.getMatrix().get(f).get(f));
        pInX = pInY = f;
        /* 現在注目している(f, f)以降の行列から絶対値最大のものを選択する */
        if (f != N - 1) {
            for (int i = f; i < N; i++) {
                for (int j = f; j < N; j++) {
                    if (i != f || j != f) {
                        if (Math.abs(equation.getMatrix().get(i).get(j)) > max) {
                            max = Math.abs(equation.getMatrix().get(i).get(j));
                            pInY = i;
                            pInX = j;
                        }
                    }
                }

            }
        }
        /* 行を入れ替える */
        if(pInY != f) {
            for (int j = 0; j <= N; j++) {
                tmp = equation.getMatrix().get(f).get(j);
                equation.getMatrix().get(f).set(j, equation.getMatrix().get(pInY).get(j));
                equation.getMatrix().get(pInY).set(j, tmp);
            }
        }
        /* 列を入れ替える */
        if(pInX != f) {
            for (int i = 0; i < N; i++) {
                tmp = equation.getMatrix().get(i).get(f);
                equation.getMatrix().get(i).set(f, equation.getMatrix().get(i).get(pInX));
                equation.getMatrix().get(i).set(pInX, tmp);
            }
            /* 列のインデックスも入れ替える */
            int InTmp = equation.getColumnRelatedIndex().get(f);
            equation.getColumnRelatedIndex().set(f, equation.getColumnRelatedIndex().get(pInX));
            equation.getColumnRelatedIndex().set(pInX, InTmp);
        }
    }

    /**
     * 前進消去
     *
     * @param cor 補正値行列
     * @param f 現在注目している対角要素のインデックス
     */
    private static void forward(int N, int f, DoubleMatrix equation, double[][] cor) {
        double p, q;
        /* 対角成分が限りなく小さい時、補正値を加える */
        if (Math.abs(equation.getMatrix().get(f).get(f)) < MINVALUE) {
            if (equation.getMatrix().get(f).get(f) >= 0) {
                equation.getMatrix().get(f).set(f, equation.getMatrix().get(f).get(f) + CORRECTION);
                cor[f][equation.getColumnRelatedIndex().indexOf(f)] += CORRECTION;
            } else {
                equation.getMatrix().get(f).set(f, equation.getMatrix().get(f).get(f) - CORRECTION);
                cor[f][equation.getColumnRelatedIndex().indexOf(f)] -= CORRECTION;
            }
        }
        p = equation.getMatrix().get(f).get(f);
        for (int j = f; j <= N; j++) {
            equation.getMatrix().get(f).set(j, equation.getMatrix().get(f).get(j) / p);
        }
        for (int i = f + 1; i < N; i++) {
            q = equation.getMatrix().get(i).get(f);
            for (int l = f; l <= N; l++) {
                equation.getMatrix().get(i).set(l, equation.getMatrix().get(i).get(l) - q * equation.getMatrix().get(f).get(l));
            }
        }
    }

    /**
     * 後退代入
     */
    private static ArrayList<ArrayList<Double>> backward(int N, DoubleMatrix equation) {
        double x[] = new double[N];
        x[N - 1] = equation.getMatrix().get(N - 1).get(N) / equation.getMatrix().get(N - 1).get(N - 1);
        for (int k = N - 2; k >= 0; k--) {
            double sum = 0.0;
            for (int j = k + 1; j < N; j++) {
                sum += equation.getMatrix().get(k).get(j) * x[j];
            }
            x[k] = equation.getMatrix().get(k).get(N) - sum;
        }
        /* 列の入れ替えを考慮しながら答えを返す */
        ArrayList<ArrayList<Double>> ans = new ArrayList<>();
        ans.add(new ArrayList<>());
        for (int i = 0; i < N; i++) {
            ans.get(0).add(i, x[equation.getColumnRelatedIndex().indexOf(i)]);
        }
        return ans;
    }

    private static void out(DoubleMatrix d) {
        int N = d.getMatrix().size();
        for (int i=0;i<N;i++) {
            for (int j=0;j<=N;j++) {
                System.out.print(d.getMatrix().get(i).get(j)+", ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
