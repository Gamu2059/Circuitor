package ProcessTerminal;

import KUU.BaseComponent.BaseFrame;
import ProcessTerminal.BootingBuffer.BrakingData;
import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.BootingBuffer.ForBuffer;
import ProcessTerminal.FunctionSettings.Function;
import ProcessTerminal.FunctionSettings.FunctionGroup;
import ProcessTerminal.SaveLoadModule.ProgramIO;
import ProcessTerminal.SyntaxSettings.ListElement;
import ProcessTerminal.SyntaxSettings.Syntax;
import ProcessTerminal.VariableSettings.PinElement;
import ProcessTerminal.VariableSettings.Variable;
import ProcessTerminal.VariableSettings.VariableGroup;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MasterTerminal {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    /**
     * 変数リストの種類のリスト
     */
    private VariableGroup variableGroup = new VariableGroup();
    /**
     * 実行中の関数待機リスト
     */
    private ArrayList<BrakingData> brakeList = new ArrayList<BrakingData>();
    /**
     * 関数のリスト
     */
    private FunctionGroup functionGroup = new FunctionGroup();
    /**
     * 実行中のfor文の実行回数保持用リスト
     */
    private ArrayList<ForBuffer> forBootingList = new ArrayList<ForBuffer>();

    private DelegateData delegateData;
    /**
     * セーブ・ロード用クラス
     */
    private ProgramIO programIO;
    /**
     * エラー発生時に保持する情報
     */
    private String consoleMessage = null;
    private ArrayList<ErrorStatus> errorList = new ArrayList<>();
    private boolean isError;

    private BaseFrame baseFrame;

    private boolean[] outputPinList=new boolean[18];

    public MasterTerminal(BaseFrame baseFrame) {
        prepareTerminal();
        this.baseFrame = baseFrame;
        delegateData = new DelegateData(variableGroup, brakeList, functionGroup,forBootingList);
        programIO=new ProgramIO(delegateData);
    }

    private MasterTerminal() {
        prepareTerminal();
        delegateData = new DelegateData(variableGroup, brakeList, functionGroup, forBootingList);
        programIO=new ProgramIO(delegateData);
    }

    private void prepareTerminal(){
        /** ピンリスト生成 */
        variableGroup.generateNewVariableList("ピン");
        variableGroup.generateNewVariableList("出力ピン");
        for (int i = 0; i < 18; i++) {
            if (i == 4 || i == 13) continue;
            if (i < 4) {
                variableGroup.searchVariableList("ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i + 2));
                variableGroup.searchVariableList("出力ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i + 2));
            } else if (i < 13) {
                variableGroup.searchVariableList("ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i + 1));
                variableGroup.searchVariableList("出力ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i + 1));
            } else {
                variableGroup.searchVariableList("ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i));
                variableGroup.searchVariableList("出力ピン").generateNewVariable(new PinElement(Variable.Type.BOOL, "ピン" + String.valueOf(i + 1), i));
            }
        }
        variableGroup.generateNewVariableList("変数");
        variableGroup.generateNewVariableList("配列");
        variableGroup.generateNewVariableList("2次元配列");
        functionGroup.generateNewFunction("SETUP");
        functionGroup.generateNewFunction("MAIN");
    }

    public void initAllProgram(){
        delegateData.getFunctionGroup().getFunctionGroup().clear();
        delegateData.getVariableGroup().getVariableGroup().clear();
        prepareTerminal();
    }

    public VariableGroup getVariableGroup() {
        return variableGroup;
    }

    public FunctionGroup getFunctionGroup() {
        return functionGroup;
    }

    public boolean[] getOutputPinList() {
        boolean[] buffer = new boolean[18];
        for(int i=0;i<outputPinList.length;i++){
            buffer[i]=outputPinList[i];
        }
        return buffer;
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

    public ArrayList<String> getListString(String name) {
        return functionGroup.searchFunction(name).getListString();
    }

    public ProgramIO getProgramIO() {
        return programIO;
    }

    public boolean isError() {
        return isError;
    }

    public ArrayList<String> getErrorListToString() {
        ArrayList<String> buffer = new ArrayList<>();
        for (ErrorStatus status : errorList) {
            buffer.add(status.getFunctionName() + " , " + status.getLineNumber());
        }
        return buffer;
    }

    public ArrayList<String> getVariableMenuList(String name) {
        return variableGroup.searchVariableList(name).getMenuList();
    }

    public ArrayList<String> getVariableStringList(String name) {
        return variableGroup.searchVariableList(name).getStringList();
    }

    public boolean updateFactor() {
        return functionGroup.updateFactor(delegateData);
    }

    private void refactorProgram(String before, String after) {
        functionGroup.refactorProgram(false, before, after);
        updateFactor();
    }

    private void refactorProgram(String name) {
        errorList = functionGroup.refactorProgram(false, name);
        updateFactor();
    }

    private void refactorFunction(String before, String after) {
        functionGroup.refactorProgram(true, before, after);
    }

    private void refactorFunction(String name) {
        errorList = functionGroup.refactorProgram(true, name);
    }

    /**
     * 変数の操作
     * コンソール文を返す
     */

    public Variable searchVariable(String listName,String variableName){
        return delegateData.getVariableGroup().searchVariableList(listName).searchVariable(variableName);
    }

    public void generateNewVariable(String groupName, Variable variable) {
        consoleMessage = variableGroup.searchVariableList(groupName).generateNewVariable(variable);
    }

    public void deleteVariable(String groupName, String name) {
        consoleMessage = variableGroup.searchVariableList(groupName).deleteVariable(name);
        refactorProgram(name);
    }

    public void renameVariable(String groupName, String before, String after) {
        consoleMessage = variableGroup.searchVariableList(groupName).renameVariable(before, after);
        refactorProgram(before, after);
    }

    /**
     * 関数の操作
     * コンソール文を返す
     */
    public Function searchFunction(String name){
        return delegateData.getFunctionGroup().searchFunction(name);
    }

    public void generateNewFunction(String name) {
        consoleMessage = functionGroup.generateNewFunction(name);
    }

    public void deleteFunction(String name) {
        consoleMessage = functionGroup.deleteFunction(name);
        refactorFunction(name);
    }

    public void renameFunction(String before, String after) {
        consoleMessage = functionGroup.renameFunction(before, after);
        refactorFunction(before, after);
    }

    /**
     * 命令の操作
     */

    public ListElement searchOrder(String functionName,int lineNumber){
        return delegateData.getFunctionGroup().searchFunction(functionName).getProgramList().get(lineNumber);
    }

    public void addOrder(String functionName, int lineNumber, ListElement element) {
        delegateData.getFunctionGroup().searchFunction(functionName).addOrder(false, lineNumber, element);
    }

    public void setOrder(String functionName, int lineNumber, ListElement element) {
        delegateData.getFunctionGroup().searchFunction(functionName).setOrder(lineNumber, element);
    }

    public void removeOrder(String functionName, int lineNumber) {
        delegateData.getFunctionGroup().searchFunction(functionName).removeOrder(lineNumber);
    }

    public boolean moveOrder(String functionName, int beforeNumber, int afterNumber) {
        return delegateData.getFunctionGroup().searchFunction(functionName).moveOrder(beforeNumber, afterNumber);
    }

    public String getVariableArray(String groupName, String variableName) {
        return variableName + " = " + variableGroup.searchVariableList(groupName).searchVariable(variableName).getValue();
    }

    public String getVariableArray(String groupName, String variableName, int index1) {
        return variableName + " = " + variableGroup.searchVariableList(groupName).searchVariable(variableName).get(index1).getValue();
    }

    public String getVariableArray(String groupName, String variableName, int index1, int index2) {
        return variableName + " = " + variableGroup.searchVariableList(groupName).searchVariable(variableName).get(index1, index2).getValue();
    }

    /**
     * initSimulator
     * 実行画面に入る直前に 1回だけ 呼び出すこと。
     */
    public void initSimulator() {
        updateFactor();
        for(int i=0;i<outputPinList.length;i++)
            outputPinList[i]=false;
        delegateData.getVariableGroup().resetValue();
        delegateData.setNowFunction("SETUP");
        delegateData.setNowLineNumber(0);
        functionGroup.searchFunction("SETUP").boot(delegateData);
        delegateData.setNowFunction("MAIN");
        delegateData.setNowLineNumber(0);
        delegateData.setWaitTurn(0);
    }


    public boolean[] bootProgram(boolean[] pinList) {
        isError = false;
        /** 現在の入出力ピンをintに変換 */
        for (int i = 0; i < pinList.length - 2; i++) {
            if (pinList[i + 2])
                variableGroup.searchVariableList("ピン").getElement(i).setIntValue(TRUE);
            else
                variableGroup.searchVariableList("ピン").getElement(i).setIntValue(FALSE);
        }

        /** WAIT命令の効力がある場合スキップ */
        if (!(delegateData.getWaitTurn() > 0)) {
            if (functionGroup.searchFunction(delegateData.getNowFunction()).boot(delegateData) == -1) {
                consoleMessage = delegateData.getErrorFunctionName() + " の " + String.valueOf(delegateData.getErrorLineNumber() + 1) + "行目 でエラーが発生しました。";
                JOptionPane.showMessageDialog(baseFrame, consoleMessage);
                delegateData.setErrorFunctionName("void");
                delegateData.setErrorLineNumber(-1);
                isError = true;
            }
        } else
            delegateData.setWaitTurn(delegateData.getWaitTurn() - 1);

        for (int i = 0; i < pinList.length - 2; i++)
            outputPinList[i + 2] = !(variableGroup.searchVariableList("出力ピン").getElement(i).getIntValue() == FALSE);

        boolean[] buffer = new boolean[18];
        for(int i=0;i<outputPinList.length;i++){
            buffer[i]=outputPinList[i];
        }

        return buffer;
    }

    public static void main(String[] args) {
        MasterTerminal masterTerminal = new MasterTerminal();

        boolean[] pin = new boolean[18];
        for (int i = 0; i < pin.length; i++) pin[i] = false;
        pin[17]=true;
        pin[10]=true;

        for (String text : masterTerminal.getListString("MAIN")) {
            System.out.println(text);
        }

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println(masterTerminal.moveOrder("MAIN",33,28));

        for (String text : masterTerminal.getListString("MAIN")) {
            System.out.println(text);
        }

        masterTerminal.initSimulator();
        for (int i = 0; i < 10; i++) {
            masterTerminal.bootProgram(pin);
        }

//
//        stringBuilder = new StringBuilder();
//        for (Variable variable : masterTerminal.getVariableGroup().searchVariableList("ピン").getVariableList()) {
//            stringBuilder.append(variable.getName()).append(":").append(variable.getIntValue()).append(", ");
//        }
//        System.out.println(stringBuilder.toString());
//
//        stringBuilder = new StringBuilder();
//        for (Variable variable : masterTerminal.getVariableGroup().searchVariableList("出力ピン").getVariableList()) {
//            stringBuilder.append(variable.getName()).append(":").append(variable.getIntValue()).append(", ");
//        }
//
//        System.out.println(stringBuilder.toString());

        System.out.println(Arrays.toString(pin));
        System.out.println(Arrays.toString(masterTerminal.outputPinList));
    }
}
