package dev.j.regere.respository;

import java.util.List;
import java.util.Map;

public interface IntermediatePersistedTable {

    void init();

    Map<String, Object> load(String regereId, String commonIdentifier, Map<String, Object> currentEvent);

    void persistEvent(String regereId, String commonIdentifier, Map<String, Object> currentEvent,
                      List<String> persitableKeys);
}
