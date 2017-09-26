import Sho.Matrix.DoubleMatrix;

public class Main {

    public static void main(String[] args) {
        double[][] mat;
        double[] vec;
        int num = 4;
        int dim = 100;
        long start, end;

        while(true) {
            start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                mat = DoubleMatrix.initDoubleMatrix(dim, dim);
                vec = DoubleMatrix.initDoubleVector(dim);
                DoubleMatrix.getGaussEquation(mat, vec);
            }
            end = System.currentTimeMillis();

            System.out.println(1f / (end - start) + "[Hz]");
        }
    }
}
