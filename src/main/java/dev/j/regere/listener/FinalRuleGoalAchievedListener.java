package dev.j.regere.listener;

import dev.j.regere.domain.RegereRule;

import java.util.Map;

public interface FinalRuleGoalAchievedListener {
    void achieved(Map<String, Object> event, RegereRule regereRule);
}
