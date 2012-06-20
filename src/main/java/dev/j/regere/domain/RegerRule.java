package dev.j.regere.domain;

import dev.j.regere.MalformedException;
import dev.j.regere.condition.RegereBoolean;
import dev.j.regere.service.RegereRuleGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegerRule {

    public static final String REGERE_ID = "regere-id";
    public static final String PRE_RULES = "pre-rules";
    public static final String PRE_RULE = "pre-rule";
    public static final String COMMON_UNIQUE_IDENTIFIER = "common-unique-identifier";
    public static final String PRE_RULE_ACTION_LISTENER = "pre-rule-action-listener";
    public static final String FINA_RULE_ACTION_LISTENER = "fina-rule-action-listener";
    public static final String LISTENER = "listener";
    public static final String PERSISTABLE_VALUES = "persistable-values";
    private final String ruleId;
    private final String commonIdentifier;
    private final List<RegereBoolean> preRules;
    private final List<String> persistableEvents;

    private final RegereBoolean finalRule;
    private final String preRuleLister;
    private String finalRuleLister;

    public RegerRule(Map<String, Object> map, String finalRuleStr) throws MalformedException {
        this.ruleId = (String) map.get(REGERE_ID);

        preRules = new ArrayList<>(10);
        for (Map map1 : ((List<Map>) map.get(PRE_RULES))) {
            preRules.add(RegereRuleGenerator.generate((String) map1.get(PRE_RULE), (Map<String, String>) map.get("types")));
        }
        commonIdentifier = (String) map.get(COMMON_UNIQUE_IDENTIFIER);
        persistableEvents = (List<String>) map.get(PERSISTABLE_VALUES);

        finalRule = RegereRuleGenerator.generate(finalRuleStr, (Map<String, String>) map.get("types"));
        preRuleLister = (String) map.get(PRE_RULE_ACTION_LISTENER);
        finalRuleLister = ((Map<String, String>) map.get(FINA_RULE_ACTION_LISTENER)).get(LISTENER);
    }

    public String getRuleId() {
        return ruleId;
    }

    public List<RegereBoolean> getPreRules() {
        return preRules;
    }

    public RegereBoolean getFinalRule() {
        return finalRule;
    }

    public String getPreRuleLister() {
        return preRuleLister;
    }

    public String getFinalRuleLister() {
        return finalRuleLister;
    }

    public String getCommonIdentifier() {
        return commonIdentifier;
    }

    public List<String> getPersistableEvents() {
        return persistableEvents;
    }
}
