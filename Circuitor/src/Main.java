import Sho.Matrix.DoubleMatrix;

public class Main {

    public static void main(String[] args) {
        double[][] mat;
        double[] vec;
        int num = 3;
        int dim = 150;
        long start, end;

//        while(true) {
//            start = System.currentTimeMillis();
//            for (int i = 0; i < num; i++) {
//                mat = DoubleMatrix.initDoubleMatrix(dim, dim);
//                vec = DoubleMatrix.initDoubleVector(dim);
//                DoubleMatrix.getGaussEquation(mat, vec);
//            }
//            end = System.currentTimeMillis();
//
//            System.out.println(1000f / (end - start) + "[Hz]");
//        }
        mat = new double[][]{
            {1,3,-1},
            {2,0,-1},
            {3,10,-4},
        };
        vec = new double[]{1,7,-2};
        DoubleMatrix d = new DoubleMatrix();
        d.setMatrix(DoubleMatrix.getGaussEquation(mat, vec));
        DoubleMatrix.out(d);
    }
}
