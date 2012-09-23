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
