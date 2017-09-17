package ProcessTerminal.SyntaxSettings;

import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.ErrorStatus;

public class ListElement {
    public enum ElementType {
        COMMAND, SYNTAX, VOID
    }

    private ElementType elementType;

    public ListElement(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public boolean updateFactor(DelegateData data) {
        return false;
    }

    public boolean boot(DelegateData data) {
        return (elementType == ElementType.SYNTAX);
    }

    /**
     * 関数:true
     * 変数:false
     */
    public void refactorProgram(boolean isFunction,String before, String after) {
    }

    public boolean refactorProgram(boolean flag, String name) {
        return false;
    }

    public String getListString() {
        return "...";
    }
}
