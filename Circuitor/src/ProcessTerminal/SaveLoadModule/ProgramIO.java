package ProcessTerminal.SaveLoadModule;

import DataIO.DataIO;
import KUU.BaseComponent.BaseFrame;
import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.FunctionSettings.Function;
import ProcessTerminal.SyntaxSettings.*;
import ProcessTerminal.VariableSettings.DoubleArray;
import ProcessTerminal.VariableSettings.SingleArray;
import ProcessTerminal.VariableSettings.Variable;

import javax.swing.*;
import java.io.*;

public class ProgramIO {
    public static final String START = "_____START PROGRAM DATA_____";
    public static final String END = "_____END PROGRAM DATA_____";

    private DelegateData data;
    private String nowLoadingFunction;

    public ProgramIO(DelegateData data) {
        this.data=data;
    }

    public void outputter(PrintWriter w) throws Exception {
        if (w == null) {
            throw new Exception("PrintWriter is null");
        }
        data.getFunctionGroup().updateFactor(data);

        w.println(START);
        // 変数リスト書き出し
        for (Variable variable : data.getVariableGroup().searchVariableList("変数").getVariableList()) {
            w.println("VARIABLE,VARIABLE," + variable.getType() + "," + variable.getName() + ",");
        }
        for (Variable variable : data.getVariableGroup().searchVariableList("配列").getVariableList()) {
            if (variable instanceof SingleArray) {
                w.println("VARIABLE,ARRAY," + variable.getType() + "," + variable.getName() + "," + ((SingleArray) variable).getArrays().length + ",");
            }
        }
        for (Variable variable : data.getVariableGroup().searchVariableList("2次元配列").getVariableList()) {
            if (variable instanceof DoubleArray) {
                w.println("VARIABLE,SQUARE," + variable.getType() + "," + variable.getName() + "," + ((DoubleArray) variable).getArrays().length + "," + ((DoubleArray) variable).getArrays()[0].length + ",");
            }
        }

        for (Function function : data.getFunctionGroup().getFunctionGroup()) {
            w.println("FUNCTION," + function.getName() + ",");
            for (ListElement element : function.getProgramList()) {
                if (element instanceof Command) {
                    switch (((Command) element).getType()) {
                        case RET:
                            w.println("ELEMENT,Command,RET," + ((Command) element).getTarget().getSavingData() + "," + ((Command) element).getEvaluation().getSavingData() + ",");
                            break;
                        case CALC:
                            w.println("ELEMENT,Command,CALC," + ((Command) element).getTarget().getSavingData() + "," + ((Command) element).getEvaluation().getSavingData() + ",");
                            break;
                        case WAIT:
                            w.println("ELEMENT,Command,WAIT" + ((Command) element).getEvaluation().getValue(data) + ",");
                            break;
                    }
                } else if (element instanceof Syntax) {
                    switch (((Syntax) element).getsType()) {
                        case IF:
                        case WHILE:
                            w.println("ELEMENT,Syntax," + ((Syntax) element).getsType().toString() + "," + ((Syntax) element).getEvaluation().getSavingData() + ",");
                            break;
                        case FOR:
                            w.println("ELEMENT,Syntax,FOR," + ((Syntax) element).getTarget().getSavingData() + "," + ((Syntax) element).getInitValue().getSavingData() + "," + ((Syntax) element).getEvaluation().getSavingData() + "," +
                                    ((Syntax) element).getInclementValue().getSavingData() + ",");
                            break;
                        case TRUE:
                        case FALSE:
                        case END:
                        case BREAK:
                        case CONTINUE:
                            w.println("ELEMENT,Syntax," + ((Syntax) element).getsType().toString() + ",");
                            break;
                        case FUNCTION:
                            w.println("ELEMENT,Syntax,FUNCTION," + ((Syntax) element).getFunctionName() + ",");
                            break;
                        default:
                            break;
                    }
                } else {
                    w.println("ELEMENT,VOID,");
                }
            }
        }
        w.println(END);
    }

    public void inputter(BufferedReader r) throws Exception {
        if (r == null) {
            throw new Exception("BufferedReader is null");
        }

        /** 現在情報の初期化 */
        data.getFunctionGroup().getFunctionGroup().clear();
        data.getVariableGroup().searchVariableList("変数").getVariableList().clear();
        data.getVariableGroup().searchVariableList("配列").getVariableList().clear();
        data.getVariableGroup().searchVariableList("2次元配列").getVariableList().clear();

        String text;

        // STARTの文字列が出現するまで読み飛ばし
        text = r.readLine();
        while (!text.equals(START)) {
            text = r.readLine();
        }
        text = r.readLine();

        while (!text.isEmpty() && !text.equals(END)) {
            analysisText(text);
            text = r.readLine();
        }

        for(Function function:data.getFunctionGroup().getFunctionGroup()){
            function.removeOrder(function.getProgramList().size()-1);
        }
        data.getFunctionGroup().updateFactor(data);
    }

    private ListElement analysisText(String text) {
        String[] splitData = text.split(",");
        switch (splitData[0]) {
            case "ELEMENT":
                addElement(text);
                break;
            case "VARIABLE":
                addVariable(text);
                break;
            case "FUNCTION":
                /** 関数を作成する */
                data.getFunctionGroup().generateNewFunction(splitData[1]);
                nowLoadingFunction = splitData[1];
                break;
            default:
                break;
        }
        return null;
    }

    private void addVariable(String text) {
        String[] splitData = text.split(",");
        switch (splitData[1]) {
            case "VARIABLE":
                data.getVariableGroup().searchVariableList("変数").generateNewVariable(new Variable(Variable.Type.valueOf(splitData[2]), splitData[3]));
                break;
            case "ARRAY":
                data.getVariableGroup().searchVariableList("配列").generateNewVariable(new SingleArray(Variable.Type.valueOf(splitData[2]), splitData[3], Integer.parseInt(splitData[4])));
                break;
            case "SQUARE":
                data.getVariableGroup().searchVariableList("2次元配列").generateNewVariable(new DoubleArray(Variable.Type.valueOf(splitData[2]), splitData[3], Integer.parseInt(splitData[4]), Integer.parseInt(splitData[5])));
                break;
            default:
                break;
        }
    }

    private void addElement(String text) {
        /** 記録用バッファ */
        Evaluation.CALCMODE calcmode;

        String[] splitData = text.split(",");

        switch (splitData[1]) {
            case "Command":
                switch (splitData[2]) {
                    case "RET":
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Command(createVariableFactor(splitData[3]), new Evaluation(Command.C_TYPE.RET, convertToMultiFactor(splitData[4]))));
                        break;
                    case "CALC":
                        calcmode = Evaluation.CALCMODE.valueOf(splitData[5]);
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Command(createVariableFactor(splitData[3]), new Evaluation(Command.C_TYPE.CALC, convertToMultiFactor(splitData[4]), calcmode, convertToMultiFactor(splitData[6]))));
                        break;
                    case "WAIT":
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Command(new Evaluation(Command.C_TYPE.WAIT, convertToMultiFactor(splitData[3]))));
                        break;
                    default:
                        break;
                }
                break;
            case "Syntax":
                switch (splitData[2]) {
                    case "IF":
                    case "WHILE":
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Syntax(new Evaluation(Syntax.S_TYPE.valueOf(splitData[2]), convertToMultiFactor(splitData[3]), Syntax.COMPARES.valueOf(splitData[4]), convertToMultiFactor(splitData[5]))));
                        break;
                    case "FOR":
                        /** ELEMENT,Syntax,FOR,初期値変数,初期値,条件式(左,演算子,右),増分値 */
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Syntax(createVariableFactor(splitData[3]), new Evaluation(Command.C_TYPE.RET, convertToMultiFactor(splitData[4])),
                                        new Evaluation(Syntax.S_TYPE.WHILE, convertToMultiFactor(splitData[5]), Syntax.COMPARES.valueOf(splitData[6]), convertToMultiFactor(splitData[7])),
                                        new Evaluation(Command.C_TYPE.CALC, createVariableFactor(splitData[3]), Evaluation.CALCMODE.valueOf(splitData[9]), convertToMultiFactor(splitData[10]))));
                        break;
                    case "FUNCTION":
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Syntax(splitData[3]));
                        break;
                    case "TRUE":
                    case "FALSE":
                    case "END":
                    case "BREAK":
                    case "CONTINUE":
                        data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1,
                                new Syntax(Syntax.S_TYPE.valueOf(splitData[2])));
                        break;
                }
                break;
            default:
                data.getFunctionGroup().searchFunction(nowLoadingFunction).addOrder(true, data.getFunctionGroup().searchFunction(nowLoadingFunction).getProgramList().size() - 1, new ListElement(ListElement.ElementType.VOID));
                break;
        }
    }

    /**
     * int に変換できれば true
     */
    private boolean isChangeInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isChangeConst(String value) {
        if (value.equals("true") || value.equals("false"))
            return true;
        else {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    private Factor convertToMultiFactor(String text){
        if(isChangeConst(text))
            return convertToFactor(text);
        else
            return createVariableFactor(text);
    }

    private Factor convertToFactor(String value) {
        if (value.equals("true") || value.equals("false"))
            return new Factor(Boolean.parseBoolean(value));
        if (isChangeInt(value))
            return new Factor(Integer.parseInt(value));
        return null;
    }

    private Factor createVariableFactor(String text) {
        String[] splitData = text.split(":");
        switch (splitData.length) {
            case 1:
                return new Factor(splitData[0]);
            case 2:
                return new Factor(splitData[0], createIndex(splitData[1]));
            case 3:
                return new Factor(splitData[0], createIndex(splitData[1]), createIndex(splitData[2]));
            default:
                return null;
        }
    }

    private Index createIndex(String text){
        if(isChangeInt(text))
            return new Index(Integer.parseInt(text));
        else
            return new Index(text);
    }
}
