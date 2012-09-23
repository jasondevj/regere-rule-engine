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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.InputStream;
import java.util.Map;

public class DefaultJsonParser implements JsonParser {

    private static final Logger logger = Logger.getLogger(DefaultJsonParser.class);
    public void init() {
        //do nothing
    }

    public Map<String, Object> toJson(InputStream inputStream) {
        logger.info("loading json object for stream [" + inputStream + "]");
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> objectMap = mapper.readValue(inputStream,
                    new TypeReference<Map<String, Object>>() { });
            logger.info("JSON object loaded [" + objectMap + "]");
            return objectMap;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON stream : ", e);
        }
    }
}
