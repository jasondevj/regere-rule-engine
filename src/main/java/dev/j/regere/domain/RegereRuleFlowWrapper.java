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

import java.util.Map;
import java.util.Set;

public class RegereRuleFlowWrapper {

    private Map<String, Object> event;
    private boolean allPreRulesPassed = true;
    private boolean finalRuleExecuted = false;
    private Set<Integer> passedPreRuleIds;

    public RegereRuleFlowWrapper(Map<String, Object> event, Set<Integer> passedPreRuleIds) {
        this.event = event;
        this.passedPreRuleIds = passedPreRuleIds;
    }

    public Map<String, Object> getEvent() {
        return event;
    }

    public boolean isAllPreRulesPassed() {
        return allPreRulesPassed;
    }

    public void setAllPreRulesPassed(boolean allPreRulesPassed) {
        this.allPreRulesPassed = allPreRulesPassed;
    }

    public Set<Integer> getPassedPreRuleIds() {
        return passedPreRuleIds;
    }

    public void addPassedPreRuleId(int id) {
        passedPreRuleIds.add(id);
    }

    public boolean isFinalRuleExecuted() {
        return finalRuleExecuted;
    }

    public void setFinalRuleExecuted(boolean finalRuleExecuted) {
        this.finalRuleExecuted = finalRuleExecuted;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RegereRuleFlowWrapper");
        sb.append("{event=").append(event);
        sb.append(", allPreRulesPassed=").append(allPreRulesPassed);
        sb.append(", finalRuleExecuted=").append(finalRuleExecuted);
        sb.append(", passedPreRuleIds=").append(passedPreRuleIds);
        sb.append('}');
        return sb.toString();
    }
}
