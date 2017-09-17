package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.VariableSettings.Variable;

public class Factor {
    public enum Factor_Type {
        BOOL, VARIABLE, LITERAL, ARRAY, SQUARE, EVALUATION
        /** フラグ,変数,定数,配列,2次元配列,評価式 */
    }

    private Factor_Type factor;
    private Variable variable;
    private String name;
    private int value;
    private boolean flag;
    private Index fact1, fact2;

    public Factor(String name) {
        factor = Factor_Type.VARIABLE;
        this.name = name;
    }

    public Factor(int value) {
        factor = Factor_Type.LITERAL;
        this.value = value;
    }

    public Factor(boolean flag) {
        factor = Factor_Type.BOOL;
        this.flag = flag;
    }

    public Factor(String name, Index fact1) {
        factor = Factor_Type.ARRAY;
        this.name = name;
        this.fact1 = fact1;
    }

    public Factor(String name, Index fact1, Index fact2) {
        factor = Factor_Type.SQUARE;
        this.name = name;
        this.fact1 = fact1;
        this.fact2 = fact2;
    }

    protected Factor() {
        factor = Factor_Type.EVALUATION;
    }

    public Factor_Type getFactor() {
        return factor;
    }

    public String getName() {
        return name;
    }

    public Index getFact1() {
        return fact1;
    }

    public Index getFact2() {
        return fact2;
    }

    public boolean isPinVariable(){
        try {
            return name.substring(0,2).equals("ピン");
        }catch(Exception e){
            return false;
        }
    }

    public int getValue(){
        switch (factor) {
            case BOOL:
                if (flag) return 1;
                else return 0;
            case LITERAL:
                return value;
        }
        return -128;
    }

    public int getValue(DelegateData data) {
        switch (factor) {
            case BOOL:
                if (flag) return 1;
                else return 0;
            case LITERAL:
                return value;
            default:
                return getAlmightyVariable(data).getIntValue();
        }
    }

    public Variable getAlmightyVariable(DelegateData data) {
        switch (factor) {
            case VARIABLE:
                if (isPinVariable()) return data.getVariableGroup().searchVariableList("ピン").searchVariable(name);
                else return data.getVariableGroup().searchVariableList("変数").searchVariable(name);
            case ARRAY:
                return data.getVariableGroup().searchVariableList("配列").searchVariable(name).get(fact1.getIndexValue(data));
            case SQUARE:
                return data.getVariableGroup().searchVariableList("2次元配列").searchVariable(name).get(fact1.getIndexValue(data), fact2.getIndexValue(data));
            default:
                return null;
        }
    }

    /**
     * 変数アドレスがリストに存在するかどうかのチェック。リファクタ時に呼び出す。
     */
    public boolean updateFactor(DelegateData data) {
        switch (factor) {
            case VARIABLE:
                if (isPinVariable())
                    return (variable = data.getVariableGroup().searchVariableList("ピン").searchVariable(name)) == null;
                else
                    return (variable = data.getVariableGroup().searchVariableList("変数").searchVariable(name)) == null;
            case ARRAY:
                return (variable = data.getVariableGroup().searchVariableList("配列").searchVariable(name)) == null;
            case SQUARE:
                return (variable = data.getVariableGroup().searchVariableList("2次元配列").searchVariable(name)) == null;
        }
        return false;
    }

    public String getSavingData() {
        switch (factor) {
            case VARIABLE:
                return variable.getName();
            case BOOL:
                return String.valueOf(flag);
            case LITERAL:
                return String.valueOf(value);
            case ARRAY:
                return variable.getName() + ":" + fact1.getSavingData();
            case SQUARE:
                return variable.getName() + ":" + fact1.getSavingData() + ":" + fact2.getSavingData();
            default:
                return null;
        }
    }

    public void refactorProgram(String before, String after) {
        if (factor == Factor_Type.VARIABLE || factor == Factor_Type.ARRAY || factor == Factor_Type.SQUARE)
            if (name.equals(before))
                name = after;
    }

    public boolean refactorProgram(String name) {
        if (factor == Factor_Type.VARIABLE || factor == Factor_Type.ARRAY || factor == Factor_Type.SQUARE)
            if (this.name.equals(name)) {
                this.name = null;
                return true;
            }
        return false;
    }

    public String getListString() {
        switch (factor) {
            case BOOL:
                return String.valueOf(flag);
            case LITERAL:
                return String.valueOf(value);
            case VARIABLE:
                return name;
            case ARRAY:
                return name + "[" + fact1.getSavingData() + "]";
            case SQUARE:
                return name + "[" + fact1.getSavingData() + "][" + fact2.getSavingData() + "]";
            default:
                return null;
        }
    }
}