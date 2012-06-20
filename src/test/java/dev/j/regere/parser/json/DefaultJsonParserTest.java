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
package dev.j.regere.parser.json;

import org.junit.Test;

import java.util.Map;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class DefaultJsonParserTest {

    @Test
    public void testToJson() throws Exception {
        Map<String,Object> objectMap = new DefaultJsonParser().toJson(DefaultJsonParser.class.getResourceAsStream("/sample.regere"));
        for (Map.Entry<String, Object> objectEntry : objectMap.entrySet()) {
            System.out.println(objectEntry);
        }
    }
}
