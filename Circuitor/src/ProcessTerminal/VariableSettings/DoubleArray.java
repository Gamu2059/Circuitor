package ProcessTerminal.VariableSettings;

public class DoubleArray extends Variable {
    private Variable[][] arrays;

    public DoubleArray(Type type, String name, int index1, int index2) {
        super(type, name);
        arrays = new Variable[index1][index2];
        for (int i = 0; i < arrays.length; i++) {
            for (int j = 0; j < arrays[0].length; j++) {
                arrays[i][j] = new Variable(type, String.valueOf(i) + ":" + String.valueOf(j));
            }
        }
    }

    @Override
    public Variable get(int index1, int index2) {
        if (index1 >= arrays.length || index2 >= arrays[0].length) return null;
        else return arrays[index1][index2];
    }

    public Variable[][] getArrays() {
        return arrays;
    }

    @Override
    public String getOutPutName() {
        return super.getOutPutName() + "[" + arrays.length + "][" + arrays[0].length + "]";
    }

    @Override
    public void resetValue() {
        for (Variable[] list : arrays)
            for (Variable variable : list)
                variable.resetValue();
    }
}
