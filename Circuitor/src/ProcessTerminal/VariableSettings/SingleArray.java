package ProcessTerminal.VariableSettings;

public class SingleArray extends Variable {
    private Variable[] arrays;

    public SingleArray(Type type, String name, int index) {
        super(type, name);
        arrays = new Variable[index];
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = new Variable(type, String.valueOf(i));
        }
    }

    @Override
    public Variable get(int index) {
        if (index >= arrays.length) return null;
        else return arrays[index];
    }

    public Variable[] getArrays() {
        return arrays;
    }

    @Override
    public String getOutPutName() {
        return super.getOutPutName() + "[" + arrays.length + "]";
    }

    @Override
    public void resetValue() {
        for (Variable variable : arrays)
            variable.resetValue();
    }

    @Override
    public int getStartingValue(int index) {
        return arrays[index].getStartingValue();
    }

    @Override
    public int getArraySize() {
        return arrays.length;
    }

    @Override
    public void bootInitialize() {
        for (Variable variable : arrays) {
            variable.bootInitialize();
        }
    }

    @Override
    public void setAllStartingArrays(int[] setArrays) {
        for (int i = 0; i < arrays.length; i++)
            arrays[i].setStartingValue(setArrays[i]);
    }
}
