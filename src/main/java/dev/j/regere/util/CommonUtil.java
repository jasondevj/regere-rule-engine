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

package dev.j.regere.util;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class CommonUtil {

    private static final Logger logger = Logger.getLogger(CommonUtil.class);

    public static Map<String, Object> returnPersistableValues(Map<String, Object> allValues, List<String> persitableKeys) {
        HashMap<String, Object> persistableValues = new HashMap<>();
        for (String persitableKey : persitableKeys) {
            final Object obj = allValues.get(persitableKey);
            if (obj != null) {
                persistableValues.put(persitableKey, obj);
            }
        }
        return persistableValues;
    }

    public static Map<String, Object> loadPersistedValues(Map<String, Object> currentEvent, Map<String, Object> persistedMap) {
        HashMap<String, Object> returnEvent = new HashMap<>(currentEvent);
        for (Map.Entry<String, Object> entry : persistedMap.entrySet()) {
            if (entry.getValue() instanceof Long) {
                final Long currentValue = (Long) currentEvent.get(entry.getKey());
                returnEvent.put(entry.getKey(), (currentValue + ((Long) entry.getValue())));
            } else if (entry.getValue() instanceof Double) {
                final Double currentValue = (Double) currentEvent.get(entry.getKey());
                returnEvent.put(entry.getKey(), (currentValue + ((Double) entry.getValue())));
            } else {
                logger.warn("Types did not match any available datatypes, this should not happen type [" + entry.getValue().getClass() + "]");
            }
        }
        return returnEvent;
    }

}
