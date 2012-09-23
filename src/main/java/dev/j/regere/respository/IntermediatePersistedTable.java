package dev.j.regere.respository;

import dev.j.regere.domain.RegereRuleFlowWrapper;

import java.util.List;
import java.util.Map;

public interface IntermediatePersistedTable {

    void init();

    RegereRuleFlowWrapper load(String regereId, String commonIdentifier, Map<String, Object> currentEvent);

    void persistEvent(String regereId, String commonIdentifier, RegereRuleFlowWrapper flowWrapper,
                      List<String> persitableKeys);
}
