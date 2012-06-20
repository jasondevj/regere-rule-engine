package dev.j.regere.service;

import java.util.List;
import java.util.Map;

public interface IntermediateEventLoader {

    Map<String, Object> load(String ruleId, String commonIdentifier, Map<String, Object> currentEvent);

    void init();

    void persistEvent(String regereId, String commonIdentifier, Map<String, Object> currentEvent,
                      List<String> persitableKeys);
}
