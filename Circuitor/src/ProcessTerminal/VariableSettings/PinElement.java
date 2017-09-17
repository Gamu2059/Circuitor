package ProcessTerminal.VariableSettings;

public class PinElement extends Variable {
    private int systemIndex;

    public PinElement(Type type, String name,int index) {
        super(type, name);
        systemIndex = index;
    }

    public int getSystemIndex() {
        return systemIndex;
    }
}
