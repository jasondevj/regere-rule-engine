/*
 *   (C) Copyright 2012-2013 hSenid Software International (Pvt) Limited.
 *   All Rights Reserved.
 *
 *   These materials are unpublished, proprietary, confidential source code of
 *   hSenid Software International (Pvt) Limited and constitute a TRADE SECRET
 *   of hSenid Software International (Pvt) Limited.
 *
 *   hSenid Software International (Pvt) Limited retains all title to and intellectual
 *   property rights in these materials.
 *
 */
package dev.j.regere.service;

import dev.j.regere.MalformedException;
import dev.j.regere.condition.RegereBoolean;
import dev.j.regere.domain.RegerRule;
import dev.j.regere.domain.RegereRuleFlowWrapper;
import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerServiceImpl implements RegereRuleAnalyzerService {

    private static final Logger logger = Logger.getLogger(RegereRuleAnalyzerServiceImpl.class);
    private DefaultJsonParser jsonParser;
    private IntermediateEventLoader intermediateEventLoader;

    private Map<String, PreRuleGoalAchievedListener> preRuleGoalAchievedListeners;
    private Map<String, FinalRuleGoalAchievedListener> finalRuleGoalAchievedListeners;

    private final Map<String, RegerRule> ruleMap = new HashMap<>(10);


    public void init() {
    }

    @Override
    public void analyze(Map<String, Object> summarizedEvents) {
        Map<String, Object> objectObjectMap = Collections.emptyMap();
        analyze(objectObjectMap, summarizedEvents);
    }

    @Override
    public void analyze(Map<String, Object> currentEvent, Map<String, Object> summarizedEvents) {

        for (String ruleId : ruleMap.keySet()) {

            final RegerRule regerRule = ruleMap.get(ruleId);

            Map<String, Object> updatedEvent = intermediateEventLoader.load(ruleId, regerRule.getCommonIdentifier(), currentEvent);
            RegereRuleFlowWrapper flowWrapper = new RegereRuleFlowWrapper(updatedEvent, summarizedEvents);

            final List<RegereBoolean> preRules = regerRule.getPreRules();
            final boolean allPreRulesPassed = executePreRules(regerRule, flowWrapper, preRules);
            if (allPreRulesPassed) {
                logger.info("All pre rules were passed executing final rule");
                executeFinalRule(regerRule, flowWrapper);
            }
            intermediateEventLoader.persistEvent(ruleId, (String) currentEvent.get(regerRule.getCommonIdentifier()), updatedEvent, regerRule.getPersistableEvents());
        }
    }

    private void executeFinalRule(RegerRule regerRule, RegereRuleFlowWrapper flowWrapper) {
        final boolean pass = regerRule.getFinalRule().booleanValue(flowWrapper);
        if(pass) {
            logger.info("Rule matched [" + regerRule.getRuleId() + "]");
            finalRuleGoalAchievedListeners.get(regerRule.getFinalRuleLister());
        } else {
            logger.debug("Final Rule[" + regerRule.getRuleId() + "] did not match");
        }
    }

    private boolean executePreRules(RegerRule regerRule, RegereRuleFlowWrapper flowWrapper, List<RegereBoolean> preRules) {
        boolean returnValue = true;
        for (RegereBoolean preRule : preRules) {
            final boolean pass = preRule.booleanValue(flowWrapper);
            if (pass) {
                logger.info("Rule matched [" + regerRule.getRuleId() + "]");
                preRuleGoalAchievedListeners.get(regerRule.getPreRuleLister());
            } else {
                returnValue = false;
                logger.debug("PreRule[" + regerRule.getRuleId() + "] did not match");
            }
        }

        return returnValue;
    }

    @Override
    public void addRule(InputStream inputStream) throws MalformedException {
        Map<String, Object> map = jsonParser.toJson(inputStream);
        String newRuleId = (String) map.get(REGERE_ID);
        logger.info("Adding new rule to the system rule-id[" + newRuleId + "]");
        if (ruleMap.get(newRuleId) != null) {
            logger.warn("rule-id [" + newRuleId + "] already exists in the current rule filter, adding again");
        }
        ruleMap.put(newRuleId, new RegerRule(map, (String) map.get("final-rule")));
    }

    @Override
    public void removeRule(String ruleId) {
        final Object remove = ruleMap.remove(ruleId);
        if (remove == null) {
            logger.warn("There was no rule under ID [" + ruleId + "] to remove");
        } else {
            logger.info("Rule [" + ruleId + "] was successfully removed");
        }
    }

    public void setJsonParser(DefaultJsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void setIntermediateEventLoader(IntermediateEventLoader intermediateEventLoader) {
        this.intermediateEventLoader = intermediateEventLoader;
    }

    public void setPreRuleGoalAchievedListeners(Map<String, PreRuleGoalAchievedListener> preRuleGoalAchievedListeners) {
        this.preRuleGoalAchievedListeners = preRuleGoalAchievedListeners;
    }

    public void setFinalRuleGoalAchievedListeners(Map<String, FinalRuleGoalAchievedListener> finalRuleGoalAchievedListeners) {
        this.finalRuleGoalAchievedListeners = finalRuleGoalAchievedListeners;
    }
}
