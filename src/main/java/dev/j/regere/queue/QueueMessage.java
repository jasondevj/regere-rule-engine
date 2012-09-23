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

package dev.j.regere.queue;

import dev.j.regere.domain.RegereRule;

import java.util.Map;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class QueueMessage {

    private final String identifier;
    private final RegereRule regereRule;
    private final Map<String,Object> event;

    public QueueMessage(String identifier, RegereRule regereRule, Map<String, Object> event) {
        this.identifier = identifier;
        this.regereRule = regereRule;
        this.event = event;
    }

    public String getIdentifier() {
        return identifier;
    }

    public RegereRule getRegereRule() {
        return regereRule;
    }

    public Map<String, Object> getEvent() {
        return event;
    }
}
