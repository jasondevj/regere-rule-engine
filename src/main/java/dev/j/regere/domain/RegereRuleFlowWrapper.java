package dev.j.regere.domain;

import java.util.Map;
import java.util.Set;

public class RegereRuleFlowWrapper {

    private Map<String, Object> event;
    private boolean allPreRulesPassed = true;
    private boolean finalRuleExecuted = false;
    private Set<Integer> passedPreRuleIds;

    public RegereRuleFlowWrapper(Map<String, Object> event, Set<Integer> passedPreRuleIds) {
        this.event = event;
        this.passedPreRuleIds = passedPreRuleIds;
    }

    public Map<String, Object> getEvent() {
        return event;
    }

    public boolean isAllPreRulesPassed() {
        return allPreRulesPassed;
    }

    public void setAllPreRulesPassed(boolean allPreRulesPassed) {
        this.allPreRulesPassed = allPreRulesPassed;
    }

    public Set<Integer> getPassedPreRuleIds() {
        return passedPreRuleIds;
    }

    public void addPassedPreRuleId(int id) {
        passedPreRuleIds.add(id);
    }

    public boolean isFinalRuleExecuted() {
        return finalRuleExecuted;
    }

    public void setFinalRuleExecuted(boolean finalRuleExecuted) {
        this.finalRuleExecuted = finalRuleExecuted;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RegereRuleFlowWrapper");
        sb.append("{event=").append(event);
        sb.append(", allPreRulesPassed=").append(allPreRulesPassed);
        sb.append(", finalRuleExecuted=").append(finalRuleExecuted);
        sb.append(", passedPreRuleIds=").append(passedPreRuleIds);
        sb.append('}');
        return sb.toString();
    }
}
