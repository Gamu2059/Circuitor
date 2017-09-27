package Sho.Matrix;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

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

    /**
     * 行列を行数y、列数xで初期化する。
     * 初期化される値は乱数生成される。
     */
    public static double[][] initDoubleMatrix(int y, int x) {
        double[][] mat = new double[y][x];
        int i, j;
        Random rnd = new Random();
        for (i = 0;i<y;i++) {
            for (j=0;j<x;j++) {
                mat[i][j] = rnd.nextDouble() * (-200) + 100;
            }
        }
        return mat;
    }

    /**
     * 行列を行数y、列数xで初期化する。
     * 全ての要素をvalueで初期化する。
     */
    public static double[][] initDoubleMatrix(int y, int x, double value) {
        double[][] mat = new double[y][x];
        int i, j;
        for (i = 0;i<y;i++) {
            for (j=0;j<x;j++) {
                mat[i][j] = value;
            }
        }
        return mat;
    }

    /**
     * ベクトルを次数nで初期化する。
     * 初期化される値は乱数生成される。
     */
    public static double[] initDoubleVector(int n) {
        double[] vec = new double[n];
        int i;
        Random rnd = new Random();
        for (i = 0;i<n;i++) {
                vec[i] = rnd.nextDouble() * (-200) + 100;
            }
        return vec;
    }


    /**
     * ベクトルを次数nで初期化する。
     * 全ての要素をvalueで初期化する。
     */
    public static double[] initDoubleVector(int n, double value) {
        double[] vec = new double[n];
        int i;
        for (i = 0;i<n;i++) {
            vec[i] = value;
        }
        return vec;
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
        int n = matrix.length;

        double[][] ecm = createECM(matrix, vector);

        /* ガウスの消去法 */
        for (int f = 0; f < n; f++) {
            pivot(n, f, ecm);
            forward(n, f, ecm);
        }
        /* 後退代入を行い、そのまま返す */
        return backward(n, ecm);
    }

    /**
     * 拡大係数行列を生成する。
     * 引数のmatrixがn次正則行列だとすると、返される行列はn+1次正則行列となる。
     */
    private static double[][] createECM(double[][] matrix, double[] vector) {
        if (matrix.length != matrix[0].length) {
            return null;
        }

        int i, j, n = matrix.length;
        double[][] ecm = new double[n+1][n+1];
        for (i=0;i<n;i++) {
            for(j=0;j<n;j++) {
                ecm[i][j] = matrix[i][j];
            }
            ecm[i][j] = vector[i];
            ecm[n][i] = i;
        }
        return ecm;
    }

    /**
     * 完全ピボット選択を行う
     */
    private static void pivot(int n, int f, double[][] ecm) {
        /* 選択した座標を管理する */
        int pInX, pInY;
        double max = 0, tmp;
        pInX = pInY = f;
        /* 現在注目している(f, f)以降の行列から絶対値最大のものを選択する */
        for (int i = f; i < n; i++) {
            for (int j = f; j < n; j++) {
                tmp = Math.abs(ecm[i][j]);
                if (tmp > max) {
                    max = tmp;
                    pInY = i;
                    pInX = j;
                }
            }
        }
        /* 行を入れ替える */
        if(pInY != f) {
            for (int j = 0; j <= n; j++) {
                tmp = ecm[f][j];
                ecm[f][j] = ecm[pInY][j];
                ecm[pInY][j] = tmp;
            }
        }
        /* 列を入れ替える */
        if(pInX != f) {
            for (int i = 0; i <= n; i++) {
                tmp = ecm[i][f];
                ecm[i][f] = ecm[i][pInX];
                ecm[i][pInX] = tmp;
            }
        }
    }

    /**
     * 前進消去
     */
    private static void forward(int n, int f, double[][] ecm) {
        double p, q;
        /* 対角成分が限りなく小さい時、補正値を加える */
        p = Math.abs(ecm[f][f]);
        if (p < MINVALUE) {
            if (p >= 0) {
                ecm[f][f] += CORRECTION;
            } else {
                ecm[f][f] -= CORRECTION;
            }
        }
        p = ecm[f][f];
        for (int j = f; j <= n; j++) {
            ecm[f][j] /= p;
        }
        for (int i = f + 1; i < n; i++) {
            q = ecm[i][f];
            for (int l = f; l <= n; l++) {
                ecm[i][l] -= q * ecm[f][l];
            }
        }
    }

    /**
     * 後退代入
     */
    private static ArrayList<ArrayList<Double>> backward(int n, double[][] ecm) {
        double[] x = new double[n];
        x[n - 1] = ecm[n - 1][n] / ecm[n - 1][n - 1];
        for (int k = n - 2; k >= 0; k--) {
            double sum = 0;
            for (int j = k + 1; j < n; j++) {
                sum += ecm[k][j] * x[j];
            }
            x[k] = ecm[k][n] - sum;
        }
        /* 列の入れ替えを考慮しながら答えを返す */
        ArrayList<ArrayList<Double>> ans = new ArrayList<>();
        ans.add(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            int j;
            for (j = 0; j < n; j++) {
                if (ecm[n][j] == i) {
                    break;
                }
            }
            ans.get(0).add(x[j]);
        }
        return ans;
    }

    public static void out(DoubleMatrix d) {
        int n = d.getMatrix().size();
        int m = d.getMatrix().get(0).size();
        for (int i=0;i<n;i++) {
            for (int j=0;j<m;j++) {
                System.out.print(d.getMatrix().get(i).get(j)+", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void out(double[][] d) {
        int y, x;
        y = d.length;
        x = d[0].length;
        for (int i=0;i<y;i++) {
            for (int j=0;j<x;j++) {
                System.out.print(d[i][j]+", ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
