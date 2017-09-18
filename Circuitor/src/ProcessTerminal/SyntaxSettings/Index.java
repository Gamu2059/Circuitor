package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;

public class Index {
    public enum Type{
        VARIABLE,CONSTANT
    }

    private Type type;
    private String variableName;
    private int constantIndex;

    public Index(String variableName) {
        type=Type.VARIABLE;
        this.variableName = variableName;
    }

    public Index(int constantIndex) {
        type=Type.CONSTANT;
        this.constantIndex = constantIndex;
    }

    public Type getType() {
        return type;
    }

    public String getVariableName() {
        return variableName;
    }

    public int getConstantIndex() {
        return constantIndex;
    }

    public int getIndexValue(DelegateData data){
        switch (type){
            case VARIABLE:
                return data.getVariableGroup().searchVariableList("変数").searchVariable(variableName).getIntValue();
            case CONSTANT:
                return constantIndex;
        }
        return -1;
    }

    public String getSavingData(){
        switch (type){
            case VARIABLE:
                return variableName;
            case CONSTANT:
                return String.valueOf(constantIndex);
        }
        return null;
    }
}
