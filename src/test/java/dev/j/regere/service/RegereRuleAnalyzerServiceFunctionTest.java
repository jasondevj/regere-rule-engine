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

import dev.j.regere.MalformedException;
import dev.j.regere.domain.RegerRule;
import dev.j.regere.listener.FinalRuleGoalAchievedListener;
import dev.j.regere.listener.PreRuleGoalAchievedListener;
import dev.j.regere.parser.json.DefaultJsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.mockito.Mockito.*;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerServiceFunctionTest {

    private RegereRuleAnalyzerServiceImpl analyzerService;
    private HashMap<String, Object> currentEvent;

    @Before
    public void setUp() throws MalformedException {
        analyzerService = new RegereRuleAnalyzerServiceImpl();
        analyzerService.setJsonParser(new DefaultJsonParser());
        final IntermediatePersistedTable persistedTable = mock(IntermediatePersistedTable.class);
        createEvent();
        when(persistedTable.load("regere_id_1", "user_id", currentEvent)).thenReturn(currentEvent);
        analyzerService.setIntermediatePersistedTable(persistedTable);
        final HashMap<String, FinalRuleGoalAchievedListener> finalListeners = new HashMap<>();
        final HashMap<String, PreRuleGoalAchievedListener> preListeners = new HashMap<>();

        preListeners.put("listener-1", new PreRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, RegerRule regerRule) {
                System.out.println("PRE");
            }
        });
        finalListeners.put("listener-2", new FinalRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, RegerRule regerRule) {
                System.out.println("FINAL");
            }
        });

        analyzerService.addRule(new ByteArrayInputStream(testRule.getBytes()));
        analyzerService.setFinalRuleGoalAchievedListeners(finalListeners);
        analyzerService.setPreRuleGoalAchievedListeners(preListeners);

    }

    private void createEvent() {
        currentEvent = new HashMap<>();
        final Random r = new Random();
        currentEvent.put("user_id", "1234");
        currentEvent.put("total_number_of_topup", 11l);
        currentEvent.put("current_top_up_amount", 30l);
        currentEvent.put("total_number_of_sms", new Double(r.nextInt(2234326)));
        currentEvent.put("KFG", new Long(r.nextInt(22226)));
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
            "  \"common-unique-identifier\" : \"user_id\",\n" +
            "  \"persistable-values\" : [\"current_top_up_amount\", \"total_number_of_topup\"],\n" +
            "\n" +
            "  \"pre-rules\" : [\n" +
            "                    {\"pre-rule\" : \"(total_number_of_topup != 10) || (current_top_up_amount == 30 || class_of_service == ABC)\", \"listener-call-value\" : \"Pre rule 1 matches\"},\n" +
            "                    {\"pre-rule\" : \"((total_number_of_topup != 10) && (total_number_of_topup != 15)) || (current_top_up_amount > 0 || class_of_service != ABC)\", \"listener-call-value\" : \"Pre rule 2 matches\"},\n" +
            "                    {\"pre-rule\" : \"(total_number_of_topup != 10) || !(current_top_up_amount == 30 || ((class_of_service == ABC) && !(class_of_service == ABC)))\", \"listener-call-value\" : \"Pre rule 3 matches\"}\n" +
            "               ],\n" +
            "  \"pre-rule-action-listener\" : \"listener-1\",\n" +
            "\n" +
            "  \"final-rule\" : \"(total_number_of_topup >= 11 && class_of_service == ABC)\",\n" +
            "  \"fina-rule-action-listener\" : {\"listener\" : \"listener-2\", \"value\" : \"10\"}\n" +
            "}\n" +
            "\n";
}
