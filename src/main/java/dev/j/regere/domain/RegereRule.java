/*
 * Copyright 2012 The regere-rule-engine Project
 *
 * The regere-rule-engine Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package dev.j.regere.domain;

import dev.j.regere.MalformedException;
import dev.j.regere.condition.RegereBoolean;
import dev.j.regere.service.RegereRuleGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegereRule {

    public static final String REGERE_ID = "regere-id";
    public static final String PRE_RULES = "pre-rules";
    public static final String PRE_RULE = "pre-rule";
    public static final String UNIQUE_IDENTIFIER = "unique-identifier";
    public static final String PRE_RULE_ACTION_LISTENER = "pre-rule-action-listener";
    public static final String FINAL_RULE_ACTION_LISTENER = "final-rule-action-listener";
    public static final String LISTENER = "listener";
    public static final String PERSISTABLE_VALUES = "persistable-values";
    public static final String LISTENER_CALL_VALUE = "listener-call-value";
    public static final String PRE_RULE_ID = "pre-rule-id";
    private final String ruleId;
    private final String identifier;
    private final List<RegereBoolean> preRules;
    private final List<String> persistableEvents;

    private final RegereBoolean finalRule;
    private final String preRuleListener;
    private String finalRuleListener;

    public RegereRule(Map<String, Object> map, String finalRuleStr) throws MalformedException {
        this.ruleId = (String) map.get(REGERE_ID);

        preRules = new ArrayList<>(10);
        for (Map rule : ((List<Map>) map.get(PRE_RULES))) {
            final RegereBoolean regereBoolean = RegereRuleGenerator.generate((String) rule.get(PRE_RULE), (Map<String, String>) map.get("types"));
            regereBoolean.setListenersCallerIdentifier((String) rule.get(LISTENER_CALL_VALUE));
            regereBoolean.setRuleId((Integer) rule.get(PRE_RULE_ID));

            preRules.add(regereBoolean);
        }
        identifier = (String) map.get(UNIQUE_IDENTIFIER);
        persistableEvents = (List<String>) map.get(PERSISTABLE_VALUES);

        finalRule = RegereRuleGenerator.generate(finalRuleStr, (Map<String, String>) map.get("types"));
        preRuleListener = (String) map.get(PRE_RULE_ACTION_LISTENER);
        finalRuleListener = ((Map<String, String>) map.get(FINAL_RULE_ACTION_LISTENER)).get(LISTENER);
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

    public String getPreRuleListener() {
        return preRuleListener;
    }

    public String getFinalRuleListener() {
        return finalRuleListener;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getPersistableEvents() {
        return persistableEvents;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RegerRule");
        sb.append("{ruleId='").append(ruleId).append('\'');
        sb.append(", identifier='").append(identifier).append('\'');
        sb.append(", preRules=").append(preRules);
        sb.append(", persistableEvents=").append(persistableEvents);
        sb.append(", finalRule=").append(finalRule);
        sb.append(", preRuleListener='").append(preRuleListener).append('\'');
        sb.append(", finalRuleListener='").append(finalRuleListener).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
