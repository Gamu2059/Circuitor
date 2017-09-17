package ProcessTerminal.VariableSettings;

public class Variable {
    public enum Type {
        INT, BOOL
    }

    protected String name;
    protected Type type;
    protected int intValue;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
        intValue = 0;
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
}
