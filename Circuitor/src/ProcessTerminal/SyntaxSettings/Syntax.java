package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.ErrorStatus;

public class Syntax extends ListElement {
    public enum S_TYPE {
        IF, WHILE, FOR, END, FUNCTION, TRUE, FALSE, BREAK, CONTINUE
    }

    public enum COMPARES {
        EQUAL, SMALL, SMALL_EQUAL, LARGE, LARGE_EQUAL, NOT
    }

    private S_TYPE sType;
    private Evaluation evaluation;
    private String functionName;
    private Factor target;
    private Evaluation inclementValue;
    private Evaluation initValue;

    // IF,WHILE
    public Syntax(Evaluation evaluation) {
        super(ElementType.SYNTAX);
        this.sType = evaluation.getsType();
        this.evaluation=evaluation;
    }

    // FOR
    public Syntax(Factor target,Evaluation initValue,Evaluation evaluation,Evaluation inclementValue) {
        super(ElementType.SYNTAX);
        sType=S_TYPE.FOR;
        this.target = target;
        this.initValue = initValue;
        this.evaluation = evaluation;
        this.inclementValue = inclementValue;
    }

    // FUNCTION
    public Syntax(String functionName) {
        super(ElementType.SYNTAX);
        sType = S_TYPE.FUNCTION;
        this.functionName = functionName;
    }

    // それ以外
    public Syntax(S_TYPE sType) {
        super(ElementType.SYNTAX);
        this.sType = sType;
    }

    public S_TYPE getsType() {
        return sType;
    }

    public void setsType(S_TYPE sType) {
        this.sType = sType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public Factor getTarget() {
        return target;
    }

    public Evaluation getInclementValue() {
        return inclementValue;
    }

    public Evaluation getInitValue() {
        return initValue;
    }

    public int checkRequire(DelegateData data){
        return evaluation.checkRequire(data);
    }

    @Override
    public boolean updateFactor(DelegateData data) {
        if(sType==S_TYPE.IF||sType==S_TYPE.WHILE)
            return (evaluation.updateFactor(data));
        else if(sType==S_TYPE.FOR)
            return !(!target.updateFactor(data)&&!initValue.updateFactor(data)&&!evaluation.updateFactor(data)&&!inclementValue.updateFactor(data));
        return false;
    }

    @Override
    public void refactorProgram(boolean isFunction, String before, String after) {
        if (isFunction) {
            if (sType == S_TYPE.FUNCTION) {
                if (functionName.equals(before)) functionName = after;
            }
        } else {
            switch (sType) {
                case FOR:
                    target.refactorProgram(before, after);
                    initValue.refactorProgram(before, after);
                    inclementValue.refactorProgram(before, after);
                case IF:
                case WHILE:
                    evaluation.refactorProgram(before, after);
                    break;
            }
        }
    }

    @Override
    public boolean refactorProgram(boolean isFunction, String name) {
        if (isFunction) {
            if (sType == S_TYPE.FUNCTION) {
                if (functionName.equals(name)) {
                    functionName = null;
                    return true;
                }
            }
        } else {
            switch (sType) {
                case FOR:
                    return (target.refactorProgram(name)|| initValue.refactorProgram(name)|| inclementValue.refactorProgram(name));
                case IF:
                case WHILE:
                    return evaluation.refactorProgram(name);
            }
        }
        return false;
    }

    @Override
    public String getListString(){
        switch (sType){
            case IF:
                return "if ("+evaluation.getListString()+") {";
            case WHILE:
                return "while ("+evaluation.getListString()+") {";
            case FOR:
                return "for ( "+target.getListString()+" = "+initValue.getListString()+" ; "+ evaluation.getListString()+" ; "+target.getListString()+" = "+inclementValue.getListString()+" ) {";
            case FUNCTION:
                return functionName+" start";
            case END:
                return "}";
            case TRUE:
                return "TRUE:";
            case FALSE:
                return "FALSE:";
            case BREAK:
                return "break";
            case CONTINUE:
                return "continue";
            default:
                return null;
        }
    }
}
