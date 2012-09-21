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
package dev.j.regere.service;

import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import dev.j.regere.respository.IntermediatePersistedTable;
import dev.j.regere.respository.OnMemoryIntermediatePersistedTable;
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


    private RegereRuleAnalyzerServiceImpl regereRuleAnalyzerService;
    private IntermediatePersistedTable persistedEventLoader;

    @Before
    public void setUp() throws Exception {
        regereRuleAnalyzerService = new RegereRuleAnalyzerServiceImpl();
        persistedEventLoader = new OnMemoryIntermediatePersistedTable();
        persistedEventLoader.init();

        regereRuleAnalyzerService.setJsonParser(new DefaultJsonParser());
        regereRuleAnalyzerService.setIntermediatePersistedTable(persistedEventLoader);
        regereRuleAnalyzerService.addRule(DefaultJsonParser.class.getResourceAsStream("/sample.regere"));
        regereRuleAnalyzerService.setPreRuleGoalAchievedListeners(new HashMap<String, PreRuleGoalAchievedListener>());
        regereRuleAnalyzerService.setFinalRuleGoalAchievedListeners(new HashMap<String, FinalRuleGoalAchievedListener>());
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
