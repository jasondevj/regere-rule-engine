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

package dev.j.regere.service;

import dev.j.regere.MalformedException;
import dev.j.regere.domain.RegereRule;
import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import dev.j.regere.respository.OnMemoryIntermediatePersistedTable;
import dev.j.regere.service.impl.RegereRuleAnalyzerService;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerServiceFunctionTest {

    private RegereRuleAnalyzerService analyzerService;
    private HashMap<String, Object> currentEvent;
    private OnMemoryIntermediatePersistedTable persistedTable;

    @Before
    public void setUp() throws MalformedException {
        analyzerService = new RegereRuleAnalyzerService();
        analyzerService.setJsonParser(new DefaultJsonParser());
        createEvent();
        persistedTable = new OnMemoryIntermediatePersistedTable();
        persistedTable.init();
        analyzerService.setIntermediatePersistedTable(persistedTable);
        final HashMap<String, FinalRuleGoalAchievedListener> finalListeners = new HashMap<>();
        final HashMap<String, PreRuleGoalAchievedListener> preListeners = new HashMap<>();

        preListeners.put("listener-1", new PreRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, String identifier) {
                System.out.println("PRE");
            }
        });
        finalListeners.put("listener-2", new FinalRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, RegereRule regereRule) {
                System.out.println("FINAL");
            }
        });

        analyzerService.addRule(new ByteArrayInputStream(testRule.getBytes()));
        analyzerService.setFinalRuleGoalAchievedListeners(finalListeners);
        analyzerService.setPreRuleGoalAchievedListeners(preListeners);
        analyzerService.init();

    }

    private void createEvent() {
        currentEvent = new HashMap<>();
        final Random r = new Random();
        currentEvent.put("user_id", "1234");
        currentEvent.put("total_number_of_topup", 11l);
        currentEvent.put("current_top_up_amount", 30l);
        currentEvent.put("total_number_of_sms", (double) r.nextInt(2234326));
        currentEvent.put("KFG", (long) r.nextInt(22226));
        currentEvent.put("current_date", new Date());
        currentEvent.put("class_of_service", "ABC");
    }

    @Test
    public void testAnalyze() throws Exception {
        analyzerService.analyze(currentEvent);
    }

    private static final String testRule = "{\n" +
            "  \"regere-id\" : \"regere_id_1\",\n" +
            "  \"types\" : {\"total_number_of_topup\" : \"LONG\",\n" +
            "             \"total_number_of_sms\" : \"DOUBLE\",\n" +
            "             \"current_top_up_amount\" : \"LONG\",\n" +
            "             \"current_date\" : \"DATE\",\n" +
            "             \"class_of_service\" : \"STRING\"},\n" +
            "  \"unique-identifier\" : \"user_id\",\n" +
            "  \"persistable-values\" : [\"current_top_up_amount\", \"total_number_of_topup\"],\n" +
            "\n" +
            "  \"pre-rules\" : [\n" +
            "                    {\"pre-rule-id\" : 1, \"pre-rule\" : \"(total_number_of_topup != 10) || (current_top_up_amount == 30 || class_of_service == ABC)\", \"listener-call-value\" : \"Pre rule 1 matches\"},\n" +
            "                    {\"pre-rule-id\" : 2, \"pre-rule\" : \"((total_number_of_topup != 10) && (total_number_of_topup != 15)) || (current_top_up_amount > 0 || class_of_service != ABC)\", \"listener-call-value\" : \"Pre rule 2 matches\"},\n" +
            "                    {\"pre-rule-id\" : 3, \"pre-rule\" : \"(total_number_of_topup != 10) || !(current_top_up_amount == 30 || ((class_of_service == ABC) && !(class_of_service == ABC)))\", \"listener-call-value\" : \"Pre rule 3 matches\"}\n" +
            "               ],\n" +
            "  \"pre-rule-action-listener\" : \"listener-1\",\n" +
            "\n" +
            "  \"final-rule\" : \"(total_number_of_topup >= 11 && class_of_service == ABC)\",\n" +
            "  \"final-rule-action-listener\" : {\"listener\" : \"listener-2\", \"value\" : \"10\"}\n" +
            "}\n" +
            "\n";
}
