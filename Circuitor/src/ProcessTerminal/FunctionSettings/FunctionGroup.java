package ProcessTerminal.FunctionSettings;

import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.ErrorStatus;

import java.util.ArrayList;

public class FunctionGroup {
    private ArrayList<Function> functionGroup = new ArrayList<>();

    public ArrayList<Function> getFunctionGroup() {
        return functionGroup;
    }

    public Function searchFunction(String name) {
        for (Function function : functionGroup) {
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    public String generateNewFunction(String name) {
        return generateNewFunction(false,name);
    }

    public String generateNewFunction(boolean isLoading,String name) {
        if (searchFunction(name) == null) {
            functionGroup.add(new Function(isLoading,name));
            return name + " の作成に成功しました。";
        } else {
            return name + " は既に関数名に使われています。";
        }
    }

    public String deleteFunction(String name) {
        Function function;
        if ((function = searchFunction(name)) != null) {
            functionGroup.remove(function);
            return name + " の削除が完了しました。";
        } else return name + " という名の関数は存在しません。";
    }

    public String renameFunction(String before, String after) {
        if (searchFunction(after) == null) {
            searchFunction(before).setName(after);
            return "関数名を " + before + " から " + after + " に変更しました。";
        } else
            return after + " は既に関数名に使われています。";
    }

    public boolean updateFactor(DelegateData delegateData) {
        boolean flag = false;
        for (Function function : functionGroup) {
            if (function.updateFactor(delegateData))
                if (!flag) flag = true;
        }
        return flag;
    }

    public void refactorProgram(boolean isFunction, String before, String after) {
        for (Function function : functionGroup) {
            function.refactorProgram(isFunction, before, after);
        }
    }

    public ArrayList<ErrorStatus> refactorProgram(boolean isFunction, String name) {
        ArrayList<ErrorStatus> buffer=new ArrayList<>();
        for (Function function : functionGroup) {
            function.refactorProgram(isFunction, name,buffer);
        }
        return buffer;
    }

    public ArrayList<String> getFunctionString(){
        ArrayList<String> buffer=new ArrayList<>();
        for(Function function:functionGroup)
            buffer.add(function.getName());
        return buffer;
    }
}