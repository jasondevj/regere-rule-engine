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
package dev.j.regere.respository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.j.regere.util.CommonUtil.loadPersistedValues;
import static dev.j.regere.util.CommonUtil.returnPersistableValues;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class OnMemoryIntermediatePersistedTable implements IntermediatePersistedTable {

    private HashMap<String, MemoryObject> memoryStore;


    @Override
    public void init() {
        memoryStore = new HashMap<>(100000);
    }

    @Override
    public Map<String, Object> load(String regereId, String commonIdentifier, Map<String, Object> currentEvent) {
        final MemoryObject memoryObject = memoryStore.get(regereId + commonIdentifier);
        if (memoryObject != null) {
            return loadPersistedValues(currentEvent, memoryObject.getCurrentEvent());
        }
        return currentEvent;
    }

    @Override
    public void persistEvent(String regereId, String commonIdentifier, Map<String, Object> currentEvent, List<String> persitableKeys) {
        final Map<String, Object> map = returnPersistableValues(currentEvent, persitableKeys);
        memoryStore.put(regereId + commonIdentifier, new MemoryObject(map));
    }

    private class MemoryObject {

        private final Map<String,Object> currentEvent;

        public MemoryObject(Map<String, Object> currentEvent) {
            this.currentEvent = currentEvent;
        }

        public Map<String, Object> getCurrentEvent() {
            return currentEvent;
        }
    }
}
