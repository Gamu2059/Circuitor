package ProcessTerminal.BootingBuffer;

public class ForBuffer {
    private String functionName;
    private int lineNumber;

    public ForBuffer(String functionName, int lineNumber) {
        this.functionName = functionName;
        this.lineNumber = lineNumber;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    public String getFunctionName() {
        return functionName;
    }
}
