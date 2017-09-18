package ProcessTerminal.FunctionSettings;

import ProcessTerminal.BootingBuffer.BrakingData;
import ProcessTerminal.BootingBuffer.DelegateData;
import ProcessTerminal.BootingBuffer.ForBuffer;
import ProcessTerminal.ErrorStatus;
import ProcessTerminal.SyntaxSettings.Command;
import ProcessTerminal.SyntaxSettings.ListElement;
import ProcessTerminal.SyntaxSettings.Syntax;

import java.util.ArrayList;

public class Function {
    private ArrayList<ListElement> programList;
    private String name;

    public Function(boolean isLoading, String name) {
        this.name = name;
        programList = new ArrayList<>();
        if (!isLoading) programList.add(new ListElement(ListElement.ElementType.VOID));
    }

    public Function(String name) {
        this(false, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ListElement> getProgramList() {
        return programList;
    }

    public boolean updateFactor(DelegateData data) {
        boolean flag = false;
        for (ListElement element : programList) {
            if (element.updateFactor(data))
                if (!flag) flag = true;
        }
        return flag;
    }

    public void addOrder(boolean isLoading, int lineNumber, ListElement listElement) {
        if (listElement instanceof Syntax) {
            if (((Syntax) listElement).getsType() == Syntax.S_TYPE.BREAK || ((Syntax) listElement).getsType() == Syntax.S_TYPE.CONTINUE) {
                if (isCanLoopOrder(lineNumber))
                    programList.add(lineNumber, listElement);
            } else {
                programList.add(lineNumber, listElement);
            }
        } else
            programList.add(lineNumber, listElement);
        if (listElement instanceof Syntax) {
            if (!isLoading) {
                switch (((Syntax) listElement).getsType()) {
                    case IF:
                        addOrder(false, lineNumber + 1, new Syntax(Syntax.S_TYPE.TRUE));
                        addOrder(false, lineNumber + 2, new ListElement(ListElement.ElementType.VOID));
                        addOrder(false, lineNumber + 3, new Syntax(Syntax.S_TYPE.FALSE));
                        addOrder(false, lineNumber + 4, new ListElement(ListElement.ElementType.VOID));
                        addOrder(false, lineNumber + 5, new Syntax(Syntax.S_TYPE.END));
                        break;
                    case WHILE:
                    case FOR:
                        addOrder(false, lineNumber + 1, new ListElement(ListElement.ElementType.VOID));
                        addOrder(false, lineNumber + 2, new Syntax(Syntax.S_TYPE.END));
                        break;
                }
            }
        }
    }

    public void setOrder(int lineNumber, ListElement listElement) {
//        if (programList.get(lineNumber) instanceof Syntax) {
//            switch (((Syntax) programList.get(lineNumber)).getsType()) {
//                case IF:
//                case WHILE:
//                case FOR:
//                    int endBuf = programList.indexOf(searchEndingSyntax(lineNumber));
//                    for (int i = lineNumber; i <= endBuf; i++) {
//                        programList.remove(lineNumber);
//                    }
//                    break;
//            }
//        }
        programList.set(lineNumber, listElement);
    }

    public void removeOrder(int lineNumber) {
        if (programList.get(lineNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(lineNumber)).getsType()) {
                case IF:
                case WHILE:
                case FOR:
                    int endBuf = programList.indexOf(searchEndingSyntax(lineNumber));
                    for (int i = lineNumber; i <= endBuf; i++) {
                        programList.remove(lineNumber);
                    }
                    break;
                default:
                    programList.remove(lineNumber);
            }
        } else
            programList.remove(lineNumber);
        if (programList.size() == 0)
            addOrder(true, 0, new ListElement(ListElement.ElementType.VOID));
    }

    public boolean moveOrder(int beforeNumber, int afterNumber) {
        if (afterNumber >= programList.size()) return false;
        if (beforeNumber != afterNumber &&
                isCanControlOrder(beforeNumber) &&
                (isCanControlAfterOrder(afterNumber) || programList.get(afterNumber).getElementType() == ListElement.ElementType.VOID) &&
                (programList.get(beforeNumber) instanceof Command || (getSelectionScope(beforeNumber) + beforeNumber < afterNumber || beforeNumber > afterNumber))) {
            /** 移動対象のバッファリング */
            ArrayList<ListElement> movingList = new ArrayList<>();
            if (programList.get(beforeNumber) instanceof Syntax&& !isCanUseSingleSyntax(beforeNumber)) {
                for (int i = beforeNumber; i <= programList.indexOf(searchEndingSyntax(beforeNumber)); i++)
                    movingList.add(programList.get(i));
            } else {
                movingList.add(programList.get(beforeNumber));
            }
            /** 移動元のデータを削除 */
            for (int i = 0; i < movingList.size(); i++)
                programList.remove(beforeNumber);
            /** 移動先にデータを追加 */
            if (beforeNumber > afterNumber) {
                for (int i = 0; i < movingList.size(); i++) {
                    programList.add(afterNumber + i, movingList.get(i));
                }
            } else {
                for (int i = 0; i < movingList.size(); i++) {
                    programList.add(afterNumber + i - movingList.size(), movingList.get(i));
                }
            }
            return true;
        } else
            return false;
    }

    public int boot(DelegateData data) {
        ListElement booting;
        boolean isFunction = false;
        try {
            while (data.getNowLineNumber() < programList.size() && !isFunction) {
                if ((booting = programList.get(data.getNowLineNumber())).boot(data)) {
                    if (booting instanceof Command) {
                        data.setNowLineNumber(data.getNowLineNumber() + 1);
                        data.setWaitTurn(((Command) booting).getEvaluation().getValue(data));
                        return 0;
                    } else if (booting instanceof Syntax) {
                        /** 構文のタイプを取得 */
                        switch (((Syntax) booting).getsType()) {
                            case IF:
                                if (((Syntax) booting).checkRequire(data) != 0) {
                                    /** trueラベルへ行番号を変更 */
                                    data.setNowLineNumber(searchLabelNumber(programList.indexOf(booting), Syntax.S_TYPE.TRUE, false));
                                } else {
                                    /** falseラベルへ行番号を変更 */
                                    data.setNowLineNumber(searchLabelNumber(programList.indexOf(booting), Syntax.S_TYPE.FALSE, false));
                                }
                                break;
                            case WHILE:
                                if (((Syntax) booting).checkRequire(data) == 0)
                                /** endラベルへ行番号を変更 */
                                    data.setNowLineNumber(programList.indexOf(searchEndingSyntax(programList.indexOf(booting))));
                                break;
                            case FOR:
                                /** 変数の初期化（１回だけ） */
                                /** ループバッファが空ではなかったら */
                                if (data.getForBootingList().size() != 0) {
                                    /** そのfor文がバッファに既に登録されていなかったら */
                                    if (!(data.getForBootingList().get(data.getForBootingList().size() - 1).getFunctionName().equals(name) && data.getForBootingList().get(data.getForBootingList().size() - 1).getLineNumber() == programList.indexOf(booting))) {
                                        /** 初期化処理 */
                                        ((Syntax) booting).getTarget().getAlmightyVariable(data).setIntValue(((Syntax) booting).getInitValue().getValue(data));
                                        /** ループバッファにfor文の情報を登録 */
                                        data.getForBootingList().add(new ForBuffer(name, programList.indexOf(booting)));
                                    }
                                } else {
                                    /** 初期化処理 */
                                    ((Syntax) booting).getTarget().getAlmightyVariable(data).setIntValue(((Syntax) booting).getInitValue().getValue(data));
                                    /** ループバッファにfor文の情報を登録 */
                                    data.getForBootingList().add(new ForBuffer(name, programList.indexOf(booting)));
                                }

                                /** 条件式が成立していなかったらENDラベルまでジャンプ */
                                if (((Syntax) booting).checkRequire(data) == 0) {
                                    data.setNowLineNumber(programList.indexOf(searchEndingSyntax(programList.indexOf(booting))));
                                    /** forループの最新バッファを削除 */
                                    data.getForBootingList().remove(data.getForBootingList().get(data.getForBootingList().size() - 1));
                                }

                                break;
                            case END:
                                /** 対応する構文をサーチ */
                                Syntax syntax = searchBeginningSyntax(programList.indexOf(booting));
                                switch (syntax.getsType()) {
                                    case FOR:
                                        /** 増分フェイズ */
                                        syntax.getTarget().getAlmightyVariable(data).setIntValue(syntax.getInclementValue().getValue(data));
                                    case WHILE:
                                        /** 開始ラベルにジャンプ */
                                        data.setNowLineNumber(programList.indexOf(syntax));
                                        return 0;
                                    default:
                                        break;
                                }
                                break;
                            case FALSE:
                                /** trueならばendラベルへ行番号を変更 */
                                if (searchBeginningSyntax(programList.indexOf(booting)).checkRequire(data) != 0) {
                                    data.setNowLineNumber(searchLabelNumber(programList.indexOf(booting), Syntax.S_TYPE.END, false));
                                }
                                break;
                            case FUNCTION:
                                data.getBreakList().add(new BrakingData(data.getNowLineNumber(), data.getNowFunction()));
                                data.setNowFunction(((Syntax) booting).getFunctionName());
                                data.setNowLineNumber(-1);
                                isFunction = true;
                                break;
                            case BREAK:
                                data.setNowLineNumber(programList.indexOf(searchLoopEndSyntax(programList.indexOf(searchBeginningSyntax(programList.indexOf(booting))))));
                                break;
                            case CONTINUE:
                                data.setNowLineNumber(programList.indexOf(searchLoopEndSyntax(programList.indexOf(searchBeginningSyntax(programList.indexOf(booting))))) - 1);
                                break;
                            default:
                                break;
                        }
                    }
                }
//                if (booting instanceof Command) {
//                    if (((Command) booting).getType() == Command.C_TYPE.RET || ((Command) booting).getType() == Command.C_TYPE.CALC)
//                        if (((Command) booting).getTarget().isPinVariable()) {
//                            data.setNowLineNumber(data.getNowLineNumber() + 1);
//                            return 0;
//                        }
//                }
                data.setNowLineNumber(data.getNowLineNumber() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.setErrorFunctionName(data.getNowFunction());
            data.setErrorLineNumber(data.getNowLineNumber());
            return -1;
        }
        if (isFunction) {
            if (data.getBreakList().size() > 10)
                return -1;
            else
                return 0;
        }


        if (!(name.equals("MAIN") || name.equals("SETUP"))) {
            data.setNowFunction(data.getBreakList().get(data.getBreakList().size() - 1).getFunctionName());
            data.setNowLineNumber(data.getBreakList().get(data.getBreakList().size() - 1).getLineNumber() + 1);
            data.getBreakList().remove(data.getBreakList().get(data.getBreakList().size() - 1));
        }
        if (name.equals("MAIN")) {
            data.setNowLineNumber(0);
            data.setWaitTurn(0);
        }
        return 0;
    }

    public void refactorProgram(boolean isFunction, String before, String after) {
        for (ListElement listElement : programList) {
            listElement.refactorProgram(isFunction, before, after);
        }
    }

    public void refactorProgram(boolean isFunction, String name, ArrayList<ErrorStatus> errorList) {
        for (ListElement listElement : programList) {
            if (listElement.refactorProgram(isFunction, name)) {
                errorList.add(new ErrorStatus(name, programList.indexOf(listElement)));
            }
        }
    }

    /**
     * trueで上に検索、falseで下に検索
     */
    private int searchLabelNumber(int start, Syntax.S_TYPE target, boolean cursor) {
        if (cursor) {
            for (int i = start; i >= 0; i--) {
                if (programList.get(i) instanceof Syntax) {
                    if (((Syntax) programList.get(i)).getsType() == target) {
                        return i;
                    }
                }
            }
        } else {
            for (int i = start; i < programList.size(); i++) {
                if (programList.get(i) instanceof Syntax) {
                    if (((Syntax) programList.get(i)).getsType() == target) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public boolean isCanLoopOrder(int nowLineNumber) {
        try {

            switch (searchBeginningSyntax(nowLineNumber).getsType()) {

                case IF:
                    return searchBeginningSyntax(programList.indexOf(searchBeginningSyntax(nowLineNumber))).getsType() == Syntax.S_TYPE.WHILE ||
                            searchBeginningSyntax(programList.indexOf(searchBeginningSyntax(nowLineNumber))).getsType() == Syntax.S_TYPE.FOR;
                case WHILE:
                case FOR:
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Syntax searchBeginningSyntax(int nowLineNumber) {
        int endCount = 0;
        for (int i = nowLineNumber - 1; i >= 0; i--) {
            if (programList.get(i).getElementType() == ListElement.ElementType.SYNTAX) {
                if (programList.get(i) instanceof Syntax) {
                    if (!isLabel(i)) {
                        if (((Syntax) programList.get(i)).getsType() == Syntax.S_TYPE.END) {
                            endCount++;
                        } else {
                            if (endCount == 0)
                                return (Syntax) programList.get(i);
                            else
                                endCount--;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isLabel(int listNumber) {
        if (programList.get(listNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(listNumber)).getsType()) {
                case IF:
                case WHILE:
                case FOR:
                case END:
                    return false;
                case FUNCTION:
                case TRUE:
                case FALSE:
                case BREAK:
                case CONTINUE:
                default:
                    return true;
            }
        }
        return true;
    }

    private Syntax searchEndingSyntax(int nowLineNumber) {
        int endCount = 0;
        for (int i = nowLineNumber + 1; i < programList.size(); i++) {
            if (programList.get(i).getElementType() == ListElement.ElementType.SYNTAX) {
                if (programList.get(i) instanceof Syntax) {
                    if (!isLabel(i)) {
                        if (((Syntax) programList.get(i)).getsType() != Syntax.S_TYPE.END) {
                            endCount++;
                        } else {
                            if (endCount == 0)
                                return (Syntax) programList.get(i);
                            else
                                endCount--;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Syntax searchLoopEndSyntax(int nowLineNumber) {
        int endCount = 0;
        for (int i = nowLineNumber + 1; i < programList.size(); i++) {
            if (programList.get(i).getElementType() == ListElement.ElementType.SYNTAX) {
                if (programList.get(i) instanceof Syntax) {
                    if (!isLabel(i)) {
                        if (((Syntax) programList.get(i)).getsType() != Syntax.S_TYPE.END) {
                            endCount++;
                        } else {
                            if (endCount == 0)
                                if (!(searchBeginningSyntax(i).getsType() == Syntax.S_TYPE.IF))
                                    return (Syntax) programList.get(i);
                                else
                                    endCount--;
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<String> getListString() {
        int deepLevel = 0;
        ArrayList<String> buffer = new ArrayList<>();
        for (ListElement element : programList) {
            if (element instanceof Syntax)
                if (((Syntax) element).getsType() == Syntax.S_TYPE.END) {
                    if (searchBeginningSyntax(programList.indexOf(element)).getsType() == Syntax.S_TYPE.IF)
                        deepLevel -= 2;
                    else
                        deepLevel--;
                } else if (((Syntax) element).getsType() == Syntax.S_TYPE.FALSE)
                    deepLevel--;

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < deepLevel; i++)
                builder.append("    ");
            buffer.add(new String(builder.append(element.getListString())));

            if (element instanceof Syntax) {
                switch (((Syntax) element).getsType()) {
                    case IF:
                    case WHILE:
                    case FOR:
                    case TRUE:
                    case FALSE:
                        deepLevel++;
                        break;
                }
            }
        }
        return buffer;
    }

    public boolean isCanControlOrder(int lineNumber) {
        if (programList.get(lineNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(lineNumber)).getsType()) {
                case IF:
                case WHILE:
                case FOR:
                case FUNCTION:
                case BREAK:
                case CONTINUE:
                    return true;
                default:
                    return false;
            }
        } else
            return (programList.get(lineNumber) instanceof Command);
    }

    private boolean isCanControlAfterOrder(int lineNumber) {
        if (programList.get(lineNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(lineNumber)).getsType()) {
                case TRUE:
                case FALSE:
                case END:
                    return false;
                default:
                    return true;
            }
        } else
            return (programList.get(lineNumber) instanceof Command);
    }

    private int getSelectionScope(int lineNumber) {
        if (programList.get(lineNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(lineNumber)).getsType()) {
                case IF:
                case WHILE:
                case FOR:
                    System.out.println(programList.indexOf(searchEndingSyntax(lineNumber)) - lineNumber);
                    return programList.indexOf(searchEndingSyntax(lineNumber)) - lineNumber;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    private boolean isCanUseSingleSyntax(int lineNumber){
        if (programList.get(lineNumber) instanceof Syntax) {
            switch (((Syntax) programList.get(lineNumber)).getsType()) {
                case FUNCTION:
                case BREAK:
                case CONTINUE:
                    return true;
                default:
                    return false;
            }
        } else
            return false;
    }
}
