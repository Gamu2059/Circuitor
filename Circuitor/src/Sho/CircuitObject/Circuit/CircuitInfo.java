package Sho.CircuitObject.Circuit;

import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;
import Sho.IntegerDimension.IntegerDimension;

import java.util.Arrays;

/**
 * 電子部品の最小構成単位が保持すべき、状態と接続情報をまとめたクラス。
 */
public class CircuitInfo implements CopyTo {
    private IntegerDimension abco;
    private IntegerDimension reco = new IntegerDimension();
    private boolean terminal[] = {false, false, false, false};
    private boolean connection[] = {false, false, false, false};
    private int terminalCorrespond[] = {-1, -1, -1, -1};
    private boolean groupedTerminal[] = {false, false, false, false};
    private int highLevelConnectIndex = 0;
    private IntegerDimension nextConnectElecom[] = new IntegerDimension[4];
    private IntegerDimension nextConnectBranch[] = new IntegerDimension[4];
    private double electricCurrent[] = {0, 0, 0, 0};
    private double electricVoltage[] = {0, 0, 0, 0};

    public CircuitInfo(int abcoY, int abcoX) {
        if (abco == null) {
            abco = new IntegerDimension(abcoY, abcoX);
        }
    }

    public IntegerDimension getAbco() {
        return abco;
    }

    public IntegerDimension getReco() {
        return reco;
    }

    public void setReco(IntegerDimension reco) {
        this.reco = reco;
    }

    public boolean[] getTerminal() {
        return terminal;
    }

    public void setTerminal(boolean[] terminal) {
        this.terminal = terminal;
    }

    public boolean[] getConnection() {
        return connection;
    }

    public void setConnection(boolean[] connection) {
        this.connection = connection;
    }

    public int[] getTerminalCorrespond() {
        return terminalCorrespond;
    }

    public void setTerminalCorrespond(int[] terminalCorrespond) {
        this.terminalCorrespond = terminalCorrespond;
    }

    public boolean[] getGroupedTerminal() {
        return groupedTerminal;
    }

    public void setGroupedTerminal(boolean[] groupedTerminal) {
        this.groupedTerminal = groupedTerminal;
    }

    public int getHighLevelConnectIndex() {
        return highLevelConnectIndex;
    }

    public void setHighLevelConnectIndex(int highLevelConnectIndex) {
        this.highLevelConnectIndex = highLevelConnectIndex;
    }

    public IntegerDimension[] getNextConnectElecom() {
        return nextConnectElecom;
    }

    public void setNextConnectElecom(IntegerDimension[] nextConnectElecom) {
        this.nextConnectElecom = nextConnectElecom;
    }

    public IntegerDimension[] getNextConnectBranch() {
        return nextConnectBranch;
    }

    public void setNextConnectBranch(IntegerDimension[] nextConnectBranch) {
        this.nextConnectBranch = nextConnectBranch;
    }

    public double[] getElectricCurrent() {
        return electricCurrent;
    }

    public void setElectricCurrent(double[] electricCurrent) {
        this.electricCurrent = electricCurrent;
    }

    public double[] getElectricVoltage() {
        return electricVoltage;
    }

    public void setElectricVoltage(double[] electricVoltage) {
        this.electricVoltage = electricVoltage;
    }

    @Override
    public String toString() {
        return "CircuitInfo :\n" +
                "   abco : " + abco.toString() + "\n" +
                "   reco : " + reco.toString() + "\n" +
                "   terminal : " + Arrays.toString(terminal) + "\n" +
                "   connection : " + Arrays.toString(connection) + "\n" +
                "   terminalCorrespond : " + Arrays.toString(terminalCorrespond)
                ;
    }

    @Override
    public void copyTo(Object o) {
        if (o instanceof CircuitInfo) {
            CircuitInfo circuitInfo = (CircuitInfo) o;
            this.getReco().copyTo(circuitInfo.getReco());
            for (int i = 0; i < 4; i++) {
                circuitInfo.terminal[i] = this.terminal[i];
                circuitInfo.connection[i] = this.connection[i];
                circuitInfo.terminalCorrespond[i] = this.terminalCorrespond[i];
                circuitInfo.groupedTerminal[i] = this.groupedTerminal[i];
            }
        }
        else {
            new CannotCopyToException("CircuitInfo");
        }
    }
}
