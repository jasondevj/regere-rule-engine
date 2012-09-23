package dev.j.regere.service;

import dev.j.regere.MalformedException;
import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import dev.j.regere.respository.IntermediatePersistedTable;

import java.io.InputStream;
import java.util.Map;

public interface RegereRuleAnalyzer {

    String REGERE_ID = "regere-id";

    void addRule(InputStream inputStream) throws MalformedException;

    void analyze(Map<String, Object> event);

    void removeRule(String ruleId);

    void init();

    void setJsonParser(DefaultJsonParser jsonParser);

    void setIntermediatePersistedTable(IntermediatePersistedTable intermediatePersistedTable);

    void setPreRuleGoalAchievedListeners(Map<String, PreRuleGoalAchievedListener> preRuleGoalAchievedListeners);

    void setFinalRuleGoalAchievedListeners(Map<String, FinalRuleGoalAchievedListener> finalRuleGoalAchievedListeners);
}
