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
package dev.j.regere.service.impl;

import dev.j.regere.MalformedException;
import dev.j.regere.condition.RegereBoolean;
import dev.j.regere.domain.RegereRule;
import dev.j.regere.domain.RegereRuleFlowWrapper;
import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import dev.j.regere.queue.EventConsumer;
import dev.j.regere.queue.QueueInitiator;
import dev.j.regere.queue.QueueMessage;
import dev.j.regere.respository.IntermediatePersistedTable;
import dev.j.regere.service.RegereRuleAnalyzer;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerService implements RegereRuleAnalyzer, EventConsumer {

    private static final Logger logger = Logger.getLogger(RegereRuleAnalyzerService.class);
    private DefaultJsonParser jsonParser;
    private IntermediatePersistedTable intermediatePersistedTable;

    private Map<String, PreRuleGoalAchievedListener> preRuleGoalAchievedListeners;
    private Map<String, FinalRuleGoalAchievedListener> finalRuleGoalAchievedListeners;
    private final Map<String, RegereRule> ruleMap = new HashMap<>(10);
    private QueueInitiator queueInitiator;


    public void init() {
        queueInitiator = QueueInitiator.getInstance();
    }


    @Override
    public void analyze(Map<String, Object> event) {
        for (String ruleId : ruleMap.keySet()) {
            final RegereRule regereRule = ruleMap.get(ruleId);
            final String identifier = (String) event.get(regereRule.getIdentifier());
            queueInitiator.manager(identifier, this).offer(new QueueMessage(identifier, regereRule, event));
        }
    }

    @Override
    public void consume(QueueMessage message) {
        final String identifier = message.getIdentifier();
        final RegereRule regereRule = message.getRegereRule();
        final Map<String, Object> event = message.getEvent();
        RegereRuleFlowWrapper flowWrapper = intermediatePersistedTable.load(regereRule.getRuleId(), identifier, event);

        final List<RegereBoolean> preRules = regereRule.getPreRules();
        if (!flowWrapper.isFinalRuleExecuted()) {
            executePreRules(regereRule, flowWrapper, preRules);
            if (flowWrapper.isAllPreRulesPassed()) {
                logger.info("All pre rules were passed executing final rule");
                executeFinalRule(regereRule, flowWrapper);
            }
        }
        intermediatePersistedTable.persistEvent(regereRule.getRuleId(), identifier, flowWrapper, regereRule.getPersistableEvents());
    }

    private void executeFinalRule(RegereRule regereRule, RegereRuleFlowWrapper flowWrapper) {
        final boolean pass = regereRule.getFinalRule().booleanValue(flowWrapper);
        if (pass) {
            logger.info("Rule matched [" + regereRule.getRuleId() + "]");
            final FinalRuleGoalAchievedListener listener = finalRuleGoalAchievedListeners.get(regereRule.getFinalRuleListener());
            if (listener != null) {
                flowWrapper.setFinalRuleExecuted(true);
                listener.achieved(flowWrapper.getEvent(), regereRule);
            }
        } else {
            logger.debug("Final Rule[" + regereRule.getRuleId() + "] did not match");
        }
    }

    private void executePreRules(RegereRule regereRule, RegereRuleFlowWrapper flowWrapper, List<RegereBoolean> preRules) {
        final Set<Integer> passedPreRuleIds = flowWrapper.getPassedPreRuleIds();
        for (RegereBoolean preRule : preRules) {
            if (!passedPreRuleIds.contains(preRule.getRuleId())) {
                final boolean pass = preRule.booleanValue(flowWrapper);
                if (pass) {
                    logger.info("Rule matched [" + regereRule.getRuleId() + "]");
                    final PreRuleGoalAchievedListener listener = preRuleGoalAchievedListeners.get(regereRule.getPreRuleListener());
                    if (listener != null) {
                        flowWrapper.addPassedPreRuleId(preRule.getRuleId());
                        listener.achieved(flowWrapper.getEvent(), preRule.getListenersCallerIdentifier());
                    }
                } else {
                    flowWrapper.setAllPreRulesPassed(false);
                    logger.debug("PreRule[" + regereRule.getRuleId() + "] did not match");
                }
            }
        }
    }

    @Override
    public void addRule(InputStream inputStream) throws MalformedException {
        Map<String, Object> map = jsonParser.toJson(inputStream);
        String newRuleId = (String) map.get(REGERE_ID);
        logger.info("Adding new rule to the system rule-id[" + newRuleId + "]");
        if (ruleMap.get(newRuleId) != null) {
            logger.warn("rule-id [" + newRuleId + "] already exists in the current rule filter, adding again");
        }
        ruleMap.put(newRuleId, new RegereRule(map, (String) map.get("final-rule")));
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

    public void setIntermediatePersistedTable(IntermediatePersistedTable intermediatePersistedTable) {
        this.intermediatePersistedTable = intermediatePersistedTable;
    }

    public void setPreRuleGoalAchievedListeners(Map<String, PreRuleGoalAchievedListener> preRuleGoalAchievedListeners) {
        this.preRuleGoalAchievedListeners = preRuleGoalAchievedListeners;
    }

    public void setFinalRuleGoalAchievedListeners(Map<String, FinalRuleGoalAchievedListener> finalRuleGoalAchievedListeners) {
        this.finalRuleGoalAchievedListeners = finalRuleGoalAchievedListeners;
    }

}
