package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;

public class Command extends ListElement{
    public enum C_TYPE {
        RET, CALC, WAIT
    }

    private C_TYPE type;
    private Factor target;
    private Evaluation evaluation;

//    代入+計算 命令
    public Command(Factor target, Evaluation evaluation) {
        super(ElementType.COMMAND);
        switch (evaluation.getType()){
            case SINGLE:
            case CALC:
                type=evaluation.getcType();
                break;
        }
        this.target = target;
        this.evaluation=evaluation;
    }

    public Command(Evaluation evaluation){
        super(ElementType.COMMAND);
        this.type=evaluation.getcType();
        this.evaluation=evaluation;
    }

//    Getter&Setter
    public Factor getTarget() {
        return target;
    }

    public void setTarget(Factor target) {
        this.target = target;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public C_TYPE getType() {
        return type;
    }

    @Override
    public boolean boot(DelegateData data) {
        switch (type) {
            case RET:
            case CALC:
                if (target.isPinVariable())
                    data.getVariableGroup().searchVariableList("出力ピン").searchVariable(target.getName()).setIntValue(evaluation.getValue(data));
                else
                    target.getAlmightyVariable(data).setIntValue(evaluation.getValue(data));
                break;
            case WAIT:
                return true;
        }
        return false;
    }

    @Override
    public boolean updateFactor(DelegateData data) {
        return (target.updateFactor(data)) || (evaluation.updateFactor(data));
    }

    @Override
    public void refactorProgram(boolean isFunction,String before,String after){
        if(type!=C_TYPE.WAIT) target.refactorProgram(before, after);
        evaluation.refactorProgram(before, after);
    }

    @Override
    public boolean refactorProgram(boolean isFunction, String name){
        if(type!=C_TYPE.WAIT) {
            if(target.refactorProgram(name))
                return true;
        }
        return evaluation.refactorProgram(name);
    }

    @Override
    public String getListString(){
        switch (type){
            case RET:
            case CALC:
                return target.getListString()+" = "+evaluation.getListString();
            case WAIT:
                return "wait ("+evaluation.getListString()+" times)";
            default:
                return null;
        }
    }
}
