package dev.j.regere.condition;

import dev.j.regere.domain.RegereRuleFlowWrapper;

public abstract class RegereBoolean {

    private int ruleId;
    private String listenersCallerIdentifier;

	public abstract boolean booleanValue(RegereRuleFlowWrapper flowWrapper);

    public String getListenersCallerIdentifier() {
        return listenersCallerIdentifier;
    }

    public void setListenersCallerIdentifier(String listenersCallerIdentifier) {
        this.listenersCallerIdentifier = listenersCallerIdentifier;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }
}
