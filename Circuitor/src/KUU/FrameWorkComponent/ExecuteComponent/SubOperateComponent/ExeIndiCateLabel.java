package KUU.FrameWorkComponent.ExecuteComponent.SubOperateComponent;

import Master.BorderMaster.BorderMaster;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * 電圧計や電流計の実際値を表示するための機能を備えたクラス。
 */
public class ExeIndiCateLabel extends JLabel {
    /**
     * 表示する値の単位。
     */
    private String unit;

    public ExeIndiCateLabel(String unit) {
        super();
        setOpaque(true);
        setBorder(BorderMaster.getRegularBorder());
        setHorizontalAlignment(RIGHT);
        this.unit = unit + "]";
    }

    public String getUnit() {
        return unit;
    }

    /**
     * 実行の最初に呼び出される。
     * 値は何も表示しない。
     */
    public void initFormattedValue() {
        setText("----- [ " + unit);
    }

    /**
     * 値を最大３桁までの整数とdigit桁までの少数で構成するように調整して文字列で返す。
     * ただし、Infinityの場合は"±Infinity"、Not a Numberの場合は"Not a Number"とする。
     */
    public static StringBuffer getFormattedValue(double value, int digit) {
        if (value == Double.NaN) {
            return new StringBuffer("Not a Number [ ");
        } else if (value == Double.POSITIVE_INFINITY) {
            return new StringBuffer("Infinity [ ");
        } else if (value == Double.NEGATIVE_INFINITY) {
            return new StringBuffer("-Infinity [ ");
        } else {
            if (Math.abs(value) == 0) {
                return new StringBuffer("0 [ ");
            } else {
                return getInternalFormattedValue(value, digit);
            }
        }
    }

    /**
     * 値を最大３桁までの整数とdigit桁までの少数で構成するように調整して文字列で返す本体処理。
     */
    private static StringBuffer getInternalFormattedValue(double value, int digit) {
        int decimalPoint = 0;
        /* 値を整数部分１桁で表す時の指数表記を求める */
        if (Math.abs(value) > 1) {
            while (Math.abs(value / Math.pow(10, ++decimalPoint + 1)) >= 1) ;
            decimalPoint -= decimalPoint % 3;
        } else if (Math.abs(value) < 1) {
            while (Math.abs(value / Math.pow(10, --decimalPoint)) < 1) ;
            decimalPoint = decimalPoint - decimalPoint % 3 - (decimalPoint % 3 == 0 ? 0 : 3);
        }
        value /= Math.pow(10, decimalPoint);
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP);
        StringBuffer stringBuffer = new StringBuffer(bigDecimal.toString());
        /* 単位の接辞を追加する */
        if (decimalPoint < -24) {
            stringBuffer.append(" [?");
        } else if (decimalPoint >= -24 && decimalPoint < -21) {
            stringBuffer.append(" [y");
        } else if (decimalPoint >= -21 && decimalPoint < -18) {
            stringBuffer.append(" [z");
        } else if (decimalPoint >= -18 && decimalPoint < -15) {
            stringBuffer.append(" [a");
        } else if (decimalPoint >= -15 && decimalPoint < -12) {
            stringBuffer.append(" [f");
        } else if (decimalPoint >= -12 && decimalPoint < -9) {
            stringBuffer.append(" [p");
        } else if (decimalPoint >= -9 && decimalPoint < -6) {
            stringBuffer.append(" [n");
        } else if (decimalPoint >= -6 && decimalPoint < -3) {
            stringBuffer.append(" [u");
        } else if (decimalPoint >= -3 && decimalPoint < 0) {
            stringBuffer.append(" [m");
        } else if (decimalPoint >= 0 && decimalPoint < 3) {
            stringBuffer.append(" [ ");
        } else if (decimalPoint >= 3 && decimalPoint < 6) {
            stringBuffer.append(" [k");
        } else if (decimalPoint >= 6 && decimalPoint < 9) {
            stringBuffer.append(" [M");
        } else if (decimalPoint >= 9 && decimalPoint < 12) {
            stringBuffer.append(" [G");
        } else if (decimalPoint >= 12 && decimalPoint < 15) {
            stringBuffer.append(" [T");
        } else if (decimalPoint >= 15 && decimalPoint < 18) {
            stringBuffer.append(" [T");
        } else if (decimalPoint >= 18 && decimalPoint < 21) {
            stringBuffer.append(" [T");
        } else if (decimalPoint >= 21 && decimalPoint < 24) {
            stringBuffer.append(" [T");
        } else if (decimalPoint >= 24 && decimalPoint < 27) {
            stringBuffer.append(" [T");
        } else if (decimalPoint >= 27) {
            stringBuffer.append(" [!");
        }
        return stringBuffer;
    }

    /**
     * 値を調整してラベルに表示するようにする。
     */
    public void setFormattedValue(double value) {
        setText(getFormattedValue(value,3).append(unit).toString());
    }
}
