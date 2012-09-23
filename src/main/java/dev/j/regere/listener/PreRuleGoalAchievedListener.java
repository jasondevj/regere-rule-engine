package dev.j.regere.listener;

import java.util.Map;

public interface PreRuleGoalAchievedListener {
    void achieved(Map<String, Object> event, String identifier);
}
