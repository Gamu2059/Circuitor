package ProcessTerminal;

public class ErrorStatus {
    private String functionName;
    private int lineNumber;

    public ErrorStatus(String functionName, int lineNumber) {
        this.functionName = functionName;
        this.lineNumber = lineNumber;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
