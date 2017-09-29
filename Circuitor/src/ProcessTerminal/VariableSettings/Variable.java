package ProcessTerminal.VariableSettings;

public class Variable {
    public enum Type {
        INT, BOOL
    }
    // 0=FALSE

    protected String name;
    protected Type type;
    protected int intValue;
    protected int startingValue;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
        intValue = 0;
        startingValue = 0;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getValue() {
        if (type == Type.BOOL) {
            if (intValue == 0) return "false";
            else return "true";
        } else
            return String.valueOf(intValue);
    }

    public void setIntValue(int intValue) {
        if (type == Type.BOOL && intValue != 0) {
            this.intValue = 1;
        }
        this.intValue = intValue;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOutPutName() {
        return type.name() + " : " + name;
    }

    public void resetValue() {
        intValue = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Variable get(int index) {
        return this;
    }

    public Variable get(int index1, int index2) {
        return this;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public int getStartingValue(int index) {
        return getStartingValue();
    }

    public int getStartingValue(int index1, int index2) {
        return getStartingValue();
    }

    public void setStartingValue(int value) {
        startingValue = value;
    }

    public void setStartingValue(int index1, int value) {
        setStartingValue(value);
    }

    public void setStartingValue(int index1, int index2, int value) {
        setStartingValue(value);
    }

    public int getArraySize() {
        return 1;
    }

    public int getArraySizeHorizontal() {
        return 0;
    }

    public void bootInitialize() {
        intValue = startingValue;
    }

    public void setAllArrays(int[] setArrays) {
        return 0;
    }
    public void setAllArrays(int[][] setArrays) {
        return 0;
    }
}
