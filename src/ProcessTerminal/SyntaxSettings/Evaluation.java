package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;

public class Evaluation extends Factor {
    public enum TYPE {
        SINGLE, CALC, COMPARES
    }

    public enum CALCMODE {
        PLUS, MINUS, MULTIPLE, DIVIDE, REMIND
    }

    private TYPE type;
    private Factor base, adder;
    private CALCMODE calcmode;
    private Syntax.COMPARES compares;
    private Command.C_TYPE cType;
    private Syntax.S_TYPE sType;


    // Single Element
    public Evaluation(Command.C_TYPE cType, Factor base) {
        super();
        type = TYPE.SINGLE;
        this.cType = cType;
        this.base = base;
    }

    // Calc Element
    public Evaluation(Command.C_TYPE cType, Factor base, CALCMODE calcmode, Factor adder) {
        super();
        type = TYPE.CALC;
        this.cType = cType;
        this.base = base;
        this.adder = adder;
        this.calcmode = calcmode;
    }

    // Decision Element
    public Evaluation(Syntax.S_TYPE sType, Factor base, Syntax.COMPARES compares, Factor adder) {
        super();
        type = TYPE.COMPARES;
        this.sType = sType;
        this.base = base;
        this.adder = adder;
        this.compares = compares;
    }

    public TYPE getType() {
        return type;
    }

    public Command.C_TYPE getcType() {
        return cType;
    }

    public Factor getBase() {
        return base;
    }

    public Factor getAdder() {
        return adder;
    }

    public Syntax.S_TYPE getsType() {
        return sType;
    }

    @Override
    public int getValue(DelegateData data) {
        switch (type) {
            case SINGLE:
                return base.getValue(data);
            case CALC:
                return calcReturn(base.getValue(data), adder.getValue(data));
            case COMPARES:
                return checkRequire(data);
            default:
                return 0;
        }
    }

    @Override
    public boolean updateFactor(DelegateData data) {
        if (base.updateFactor(data)) return true;
        if (type != TYPE.SINGLE) {
            if (adder.updateFactor(data)) return true;
        }
        return false;
    }

    private int calcReturn(int numberA, int numberB) {
        int result;
        switch (calcmode) {
            case PLUS:
                result = numberA + numberB;
                break;
            case MINUS:
                result = numberA - numberB;
                break;
            case MULTIPLE:
                result = numberA * numberB;
                break;
            case DIVIDE:
                result = numberA / numberB;
                break;
            case REMIND:
                result = numberA % numberB;
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    public int checkRequire(DelegateData data) {
        if (analysisFactorType(base) && analysisFactorType(adder))
            return checkMoreThan(base.getValue(data), adder.getValue(data));
        else if (analysisFactorType(base))
            return checkMoreThan(base.getValue(data), adder.getAlmightyVariable(data).getIntValue());
        else if (analysisFactorType(adder))
            return checkMoreThan(base.getAlmightyVariable(data).getIntValue(), adder.getValue(data));
        else
            return checkMoreThan(base.getAlmightyVariable(data).getIntValue(), adder.getAlmightyVariable(data).getIntValue());
    }

    private boolean analysisFactorType(Factor factor) {
        return (factor.getFactor() == Factor.Factor_Type.LITERAL || factor.getFactor() == Factor.Factor_Type.BOOL);
    }

    public int checkMoreThan(int left, int right) {
        boolean bool = false;
        switch (compares) {
            case EQUAL:
                bool = (left == right);
                break;
            case LARGE:
                bool = (left > right);
                break;
            case LARGE_EQUAL:
                bool = (left >= right);
                break;
            case SMALL:
                bool = (left < right);
                break;
            case SMALL_EQUAL:
                bool = (left <= right);
                break;
            case NOT:
                bool = (left != right);
                break;
            default:
                break;
        }
        if (bool) return 1;
        else return 0;
    }

    public String getSavingData() {
        switch (type) {
            case SINGLE:
                return base.getSavingData();
            case CALC:
                return base.getSavingData() + "," + calcmode.toString() + "," + adder.getSavingData();
            case COMPARES:
                return base.getSavingData() + "," + compares.toString() + "," + adder.getSavingData();
        }
        return null;
    }

    @Override
    public void refactorProgram(String before, String after) {
        base.refactorProgram(before, after);
        if (type != TYPE.SINGLE)
            adder.refactorProgram(before, after);
    }

    @Override
    public boolean refactorProgram(String name) {
        boolean refact = base.refactorProgram(name);
        if (type != TYPE.SINGLE) {
            if (adder.refactorProgram(name))
                refact = true;
        }
        return refact;
    }

    @Override
    public String getListString() {
        switch (type) {
            case SINGLE:
                return base.getListString();
            case CALC:
                return base.getListString() + " " + convertToCalcSymbol() + " " + adder.getListString();
            case COMPARES:
                return base.getListString() + " " + convertToComparesSymbol() + " " + adder.getListString();
            default:
                return null;
        }
    }

    public String convertToCalcSymbol() {
        switch (calcmode) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLE:
                return "*";
            case DIVIDE:
                return "/";
            case REMIND:
                return "%";
            default:
                return null;
        }
    }

    public String convertToComparesSymbol() {
        switch (compares){
            case EQUAL:
                return "==";
            case LARGE:
                return ">";
            case LARGE_EQUAL:
                return ">=";
            case SMALL:
                return "<";
            case SMALL_EQUAL:
                return "<=";
            case NOT:
                return "!=";
            default:
                return null;
        }
    }
}
