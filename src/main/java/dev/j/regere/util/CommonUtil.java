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
