package org.gegma;

/**
 *
 * @author rezo
 */
public enum ConditionType {

    OR(1), XOR(2);
    private int code;

    private ConditionType(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
