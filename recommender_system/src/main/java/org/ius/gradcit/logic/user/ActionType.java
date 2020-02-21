package org.ius.gradcit.logic.user;

public enum ActionType {
    PUBLICATION("publication", 1.2f),
    LIKE("like",0.7f),
    COMMENT("comment", 0.7f),
    VIEW("view", 0.5f);

    private final String action;
    private final float coef;

    ActionType(String action, float coef) {
        this.action = action;
        this.coef = coef;
    }

    public String getAction() {
        return action;
    }

    public float getCoef() {
        return coef;
    }
}
