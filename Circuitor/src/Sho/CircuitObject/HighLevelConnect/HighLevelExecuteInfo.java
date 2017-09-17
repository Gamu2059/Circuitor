package Sho.CircuitObject.HighLevelConnect;

import Sho.CircuitObject.Circuit.DrawCood;
import Sho.CircuitObject.Execute.Execute;

/**
 * 電圧、電流、抵抗、静電容量、電位などの枝部分の電気的情報を保持するクラス。
 * @author 翔
 * @version 1.1
 */
public class HighLevelExecuteInfo {
    /** 生電流 */
    private double realCurrent;
    /** 電流 */
    private double current;
    /** 前回の電流 */
    private double preCurrent;
    /** 電圧 */
    private double voltage;
    /** 抵抗(コンデンサは公式を用いた場合の値を保持する) */
    private double resistance;
    /** 静電容量 */
    private double capacitance;
    /** 電位 */
    private double potential;
    /** 最大電位 */
    private double maxPotential;
    /** 描画位置情報 */
    private DrawCood drawCood;

    public HighLevelExecuteInfo() {
        realCurrent = 0;
        current = 0;
        preCurrent = 0;
        voltage = 0;
        resistance = 0;
        capacitance = 0;
        potential = -1;
        maxPotential = 0;
        drawCood = new DrawCood();
    }

    public double getRealCurrent() {
        return realCurrent;
    }

    public void setRealCurrent(double realCurrent) {
        this.realCurrent = realCurrent;
    }

    public double getCurrent() {
        return current;
    }

    /**
     * isFirstがtrueだと、前回の電流との比較は行わず、とりあえず格納だけする。
     */
    public void setCurrent(double current) {
        double size = Math.abs(current);
        this.current = current;
        if (this.current != preCurrent) {
            if (size < Execute.THRESHOLD_CURRENT) {
                /* ～1nA */
                drawCood.setPointMaxCount(100);
                drawCood.setPointSize(0);
            } else if (size < 1e-7) {
                /* ～100nA */
                drawCood.setPointMaxCount(5);
                drawCood.setPointSize(1);
            } else if (size < 1e-5) {
                /* ～10uA */
                drawCood.setPointMaxCount(4);
                drawCood.setPointSize(1.5);
            } else if (size < 1e-3) {
                /* ～1mA */
                drawCood.setPointMaxCount(3);
                drawCood.setPointSize(2);
            } else if (size < 1e-1) {
                /* ～100mA */
                drawCood.setPointMaxCount(2);
                drawCood.setPointSize(2.5);
            } else if (size < 2){
                /* ～2A */
                drawCood.setPointMaxCount(1);
                drawCood.setPointSize(3);
            } else if (size < 5){
                /* ～5A */
                drawCood.setPointMaxCount(0);
                drawCood.setPointSize(3.5);
            } else {
                /* 5A～ */
                drawCood.setPointMaxCount(0);
                drawCood.setPointSize(4);
            }
            this.preCurrent = this.current;
        }
    }

    public double getPreCurrent() {
        return preCurrent;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = Math.abs(voltage);
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getCapacitance() {
        return capacitance;
    }

    public void setCapacitance(double capacitance) {
        this.capacitance = capacitance;
    }

    public double getPotential() {
        if (potential < 0) {
            return 0;
        } else {
            return potential;
        }
    }

    public void setPotential(double potential) {
        this.potential = potential;
    }

    public double getMaxPotential() {
        return maxPotential;
    }

    public void setMaxPotential(double maxPotential) {
        this.maxPotential = maxPotential;
    }

    public DrawCood getDrawCood() {
        return drawCood;
    }

    public void setDrawCood(DrawCood drawCood) {
        this.drawCood = drawCood;
    }

    public void moveDrawCood() {
        if (current >= 0) {
            drawCood.incBasePoint(true);
        } else if (current < 0){
            drawCood.incBasePoint(false);
        }
    }

    @Override
    public String toString() {
        return "HighLevelExecuteInfo :\n" +
                "   current : " + current + "\n" +
                "   preCurrent : " + preCurrent + "\n" +
                "   voltage : " + voltage + "\n" +
                "   resistance : " + resistance + "\n" +
                "   capacitance : " + capacitance + "\n" +
                "   potential : " + potential + "\n" +
                "   maxPotential : " + maxPotential + "\n";
    }
}
