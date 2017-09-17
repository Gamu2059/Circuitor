package ProcessTerminal.BootingBuffer;

import ProcessTerminal.FunctionSettings.FunctionGroup;
import ProcessTerminal.SyntaxSettings.Syntax;
import ProcessTerminal.VariableSettings.VariableGroup;

import java.util.ArrayList;

public class DelegateData {
    private VariableGroup variableGroup;
    private ArrayList<BrakingData> breakList;
    private FunctionGroup functionGroup;
    private String nowFunction;
    private int nowLineNumber;
    private int waitTurn=0;
    private ArrayList<ForBuffer> forBootingList;
    private String errorFunctionName="MAIN";
    private int errorLineNumber=-1;

    public DelegateData(VariableGroup variableGroup, ArrayList<BrakingData> breakList, FunctionGroup functionGroup, ArrayList<ForBuffer> forBootingList) {
        this.variableGroup = variableGroup;
        this.breakList = breakList;
        this.functionGroup = functionGroup;
        this.forBootingList = forBootingList;
    }

    public VariableGroup getVariableGroup() {
        return variableGroup;
    }

    public ArrayList<BrakingData> getBreakList() {
        return breakList;
    }

    public FunctionGroup getFunctionGroup() {
        return functionGroup;
    }

    public String getNowFunction() {
        return nowFunction;
    }

    public void setNowFunction(String nowFunction) {
        this.nowFunction = nowFunction;
    }

    public int getNowLineNumber() {
        return nowLineNumber;
    }

    public void setNowLineNumber(int nowLineNumber) {
        this.nowLineNumber = nowLineNumber;
    }

    public ArrayList<ForBuffer> getForBootingList() {
        return forBootingList;
    }

    public Integer getWaitTurn() {
        return waitTurn;
    }

    public void setWaitTurn(Integer waitTurn) {
        this.waitTurn = waitTurn;
    }

    public String getErrorFunctionName() {
        return errorFunctionName;
    }

    public void setErrorFunctionName(String errorFunctionName) {
        this.errorFunctionName = errorFunctionName;
    }

    public Integer getErrorLineNumber() {
        return errorLineNumber;
    }

    public void setErrorLineNumber(Integer errorLineNumber) {
        this.errorLineNumber = errorLineNumber;
    }
}
