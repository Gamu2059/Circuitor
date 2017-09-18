package Sho.CircuitObject.Circuit;

import static Sho.CircuitObject.Circuit.CircuitOperateBehavior.Behavior.NO_ACTION;

/**
 * 回路エディタの操作を保持する。
 */
public class CircuitOperateBehavior {
    public enum Behavior{
        /** 切替直後、もしくは何も操作し始めていない状態 */
        NO_ACTION,
        /** 範囲指定している状態 */
        RANGING,
        /** 範囲指定し終わった状態 */
        RANGED,
        /** 対象を移動させている状態 */
        MOVE,
    }
    private Behavior behavior;
    private Behavior preBehavior;

    public CircuitOperateBehavior() {
        setBehavior(NO_ACTION);
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        if (this.behavior != behavior) {
            this.preBehavior = this.behavior;
            this.behavior = behavior;
        }
    }

    public Behavior getPreBehavior() {
        return preBehavior;
    }
}
