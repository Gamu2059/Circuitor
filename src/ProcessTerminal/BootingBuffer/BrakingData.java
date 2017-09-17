package ProcessTerminal.BootingBuffer;

public class BrakingData {
    private int lineNumber;
    private String functionName;

    public BrakingData(int lineNumber, String functionName) {
        this.lineNumber = lineNumber;
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
