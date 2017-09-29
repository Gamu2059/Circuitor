package ProcessTerminal.VariableSettings;

import java.util.ArrayList;

public class VariableList {
    private String name;
    private ArrayList<Variable> variableList = new ArrayList<Variable>();

    public VariableList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** 変数検索
     その名前の変数があった場合：その変数(ProcessTerminal.VariableSettings.Variable)
     なかった場合              ：null
     */
    public Variable searchVariable(String name) {
        for (Variable variable : variableList) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }
        return null;
    }

    public String generateNewVariable(Variable variable) {
        if (searchVariable(variable.getName()) == null) {
            variableList.add(variable);
            return "作成に成功しました。";
        }
        return "同名の変数が既に作成されています。";
    }

    public Variable getElement(int number){
        return variableList.get(number);
    }

    public String deleteVariable(String name) {
        Variable variable;
        if ((variable = searchVariable(name)) != null) {
            variableList.remove(variable);
            return name + " の削除が完了しました。";
        }
        return name + " は存在していません。";
    }

    public String renameVariable(String before, String after) {
        if (searchVariable(after) == null) {
            searchVariable(before).setName(after);
            return before + " の名前を " + after + " に変更しました。";
        }
        return after + " はすでに変数名として使用されています。";
    }

    public ArrayList<Variable> getVariableList() {
        return variableList;
    }

    public ArrayList<String> getMenuList(){
        ArrayList<String> buffer=new ArrayList<>();
        for(Variable variable:variableList){
            buffer.add(variable.getOutPutName());
        }
        return buffer;
    }

    public ArrayList<String> getStringList(){
        ArrayList<String> buffer=new ArrayList<>();
        for(Variable variable:variableList){
            buffer.add(variable.getName());
        }
        return buffer;
    }

    public void resetValue(){
        for(Variable variable:variableList)
            variable.bootInitialize();
    }

    public static void main(String[] args) {
        VariableList list=new VariableList("test");
        list.generateNewVariable(new Variable(Variable.Type.INT,"変数"));
        list.renameVariable("変数","variable");
        list.deleteVariable("variable");
    }
}
