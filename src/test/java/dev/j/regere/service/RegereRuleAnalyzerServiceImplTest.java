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

import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import dev.j.regere.respository.IntermediatePersistedTable;
import dev.j.regere.respository.OnMemoryIntermediatePersistedTable;
import dev.j.regere.service.impl.RegereRuleAnalyzerService;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerServiceImplTest {


    private RegereRuleAnalyzerService regereRuleAnalyzerService;
    private IntermediatePersistedTable persistedEventLoader;

    @Before
    public void setUp() throws Exception {
        regereRuleAnalyzerService = new RegereRuleAnalyzerService();
        persistedEventLoader = new OnMemoryIntermediatePersistedTable();
        persistedEventLoader.init();

        regereRuleAnalyzerService.setJsonParser(new DefaultJsonParser());
        regereRuleAnalyzerService.setIntermediatePersistedTable(persistedEventLoader);
        regereRuleAnalyzerService.addRule(DefaultJsonParser.class.getResourceAsStream("/sample.regere"));
        regereRuleAnalyzerService.setPreRuleGoalAchievedListeners(new HashMap<String, PreRuleGoalAchievedListener>());
        regereRuleAnalyzerService.setFinalRuleGoalAchievedListeners(new HashMap<String, FinalRuleGoalAchievedListener>());
        regereRuleAnalyzerService.init();
    }

    @Test
    public void testAnalyzeFullPass() throws Exception {
        final HashMap<String, Object> currentEvent = new HashMap<>();
        currentEvent.put("user_id", "User 1");
        currentEvent.put("total_number_of_topup", (long) 11);
        currentEvent.put("current_top_up_amount", (long) 30);
        currentEvent.put("total_number_of_sms", (double) 5);
        currentEvent.put("KFG", (long) 1);
        currentEvent.put("current_date", new Date());
        currentEvent.put("class_of_service", "ABC");
//        final HashMap<String, Object> summarizedEvents = new HashMap<>();
//        summarizedEvents.put("total_number_of_topup", new Long(11));
//        summarizedEvents.put("current_top_up_amount", new Long(30));
//        summarizedEvents.put("total_number_of_sms", new Double(5));
//        summarizedEvents.put("KFG", new Long(1));
//        summarizedEvents.put("current_date", new Date());
//        summarizedEvents.put("class_of_service", "ABC");

        regereRuleAnalyzerService.analyze(currentEvent);
    }

    @Test
    public void testAnalyzePreOnlyPass() throws Exception {
        final HashMap<String, Object> currentEvent = new HashMap<>();
        currentEvent.put("user_id", "User 2");
        currentEvent.put("total_number_of_topup", (long) 11);
        currentEvent.put("current_top_up_amount", (long) 30);
        currentEvent.put("total_number_of_sms", (double) 5);
        currentEvent.put("KFG", (long) 1);
        currentEvent.put("current_date", new Date());
        currentEvent.put("class_of_service", "ABCD");
//        final HashMap<String, Object> summarizedEvents = new HashMap<>();
//        summarizedEvents.put("total_number_of_topup", new Long(11));
//        summarizedEvents.put("current_top_up_amount", new Long(30));
//        summarizedEvents.put("total_number_of_sms", new Double(5));
//        summarizedEvents.put("KFG", new Long(1));
//        summarizedEvents.put("current_date", new Date());
//        summarizedEvents.put("class_of_service", "ABC");

        regereRuleAnalyzerService.analyze(currentEvent);
    }


    @Test
    public void testAddRule() throws Exception {

    }

    @Test
    public void testRemoveRule() throws Exception {

    }
}
