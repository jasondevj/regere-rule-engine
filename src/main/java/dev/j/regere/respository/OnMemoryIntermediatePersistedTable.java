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

package dev.j.regere.respository;

import dev.j.regere.domain.RegereRuleFlowWrapper;

import java.util.*;

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
    public RegereRuleFlowWrapper load(String regereId, String commonIdentifier, Map<String, Object> currentEvent) {
        final MemoryObject memoryObject = memoryStore.get(regereId + commonIdentifier);
        if (memoryObject != null) {
            final RegereRuleFlowWrapper wrapper = new RegereRuleFlowWrapper(loadPersistedValues(currentEvent,
                    memoryObject.getCurrentEvent()), memoryObject.getPassedPreRuleIds());
            wrapper.setFinalRuleExecuted(memoryObject.isFinalRuleExecuted());
            return wrapper;
        }
        return new RegereRuleFlowWrapper(currentEvent, new HashSet<Integer>(10));
    }

    @Override
    public void persistEvent(String regereId, String commonIdentifier, RegereRuleFlowWrapper flowWrapper, List<String> persitableKeys) {
        final Map<String, Object> map = returnPersistableValues(flowWrapper.getEvent(), persitableKeys);
        memoryStore.put(regereId + commonIdentifier, new MemoryObject(map, flowWrapper.isFinalRuleExecuted(), flowWrapper.getPassedPreRuleIds()));
    }

    private class MemoryObject {

        private final Map<String,Object> currentEvent;
        private final boolean finalRuleExecuted;
        private final Set<Integer> passedPreRuleIds;

        public MemoryObject(Map<String, Object> currentEvent, boolean finalRuleExecuted, Set<Integer> passedPreRuleIds) {
            this.currentEvent = currentEvent;
            this.finalRuleExecuted = finalRuleExecuted;
            this.passedPreRuleIds = passedPreRuleIds;
        }


        public boolean isFinalRuleExecuted() {
            return finalRuleExecuted;
        }

        public Set<Integer> getPassedPreRuleIds() {
            return passedPreRuleIds;
        }

        public Map<String, Object> getCurrentEvent() {
            return currentEvent;
        }
    }
}
