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
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleAnalyzerServicePerformanceTester {

    private RegereRuleAnalyzerServiceImpl regereRuleAnalyzerService;
    private IntermediatePersistedTable persistedEventLoader;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger counter = new AtomicInteger(0);

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
    public void testAnalyzePerformance() throws Exception {

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Current tranastion per sec [" + counter.getAndSet(0) + "]");
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 20; i++) {
            createThreadProducer(new Random());
        }
        Thread.sleep(1000000);
    }

    private void createThreadProducer(final Random r) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    try {
                        final HashMap<String, Object> currentEvent = new HashMap<>();
                        currentEvent.put("user_id", Long.toString(r.nextInt(10000)));
                        currentEvent.put("total_number_of_topup", (long) r.nextInt(2234236));
                        currentEvent.put("current_top_up_amount", (long) r.nextInt(233436));
                        currentEvent.put("total_number_of_sms", (double) r.nextInt(2234326));
                        currentEvent.put("KFG", (long) r.nextInt(22226));
                        currentEvent.put("current_date", new Date());
                        currentEvent.put("class_of_service", "ABC");
                        regereRuleAnalyzerService.analyze(currentEvent);
                        counter.incrementAndGet();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
