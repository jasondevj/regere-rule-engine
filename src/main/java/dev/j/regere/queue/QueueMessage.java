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
