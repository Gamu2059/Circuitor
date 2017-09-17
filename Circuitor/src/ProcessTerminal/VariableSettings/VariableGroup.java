package ProcessTerminal.VariableSettings;

import java.util.ArrayList;

public class VariableGroup {
    private ArrayList<VariableList> variableGroup=new ArrayList<>();

    public void generateNewVariableList(String name){
        if (searchVariableList(name) == null)
            variableGroup.add(new VariableList(name));
    }

    public void resetValue(){
        for(VariableList list:variableGroup)
            list.resetValue();
    }

    public VariableList searchVariableList(String name){
        for (VariableList variableList: variableGroup) {
            if (variableList.getName().equals(name)) {
                return variableList;
            }
        }
        return null;
    }

    public ArrayList<VariableList> getVariableGroup() {
        return variableGroup;
    }
}
