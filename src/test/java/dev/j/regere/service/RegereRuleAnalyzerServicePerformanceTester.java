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

import dev.j.regere.domain.RegereRule;
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
import java.util.Map;
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

    private RegereRuleAnalyzerService regereRuleAnalyzerService;
    private IntermediatePersistedTable persistedEventLoader;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Before
    public void setUp() throws Exception {
        regereRuleAnalyzerService = new RegereRuleAnalyzerService();
        persistedEventLoader = new OnMemoryIntermediatePersistedTable();
        persistedEventLoader.init();
        regereRuleAnalyzerService.setJsonParser(new DefaultJsonParser());
        regereRuleAnalyzerService.setIntermediatePersistedTable(persistedEventLoader);
        regereRuleAnalyzerService.addRule(DefaultJsonParser.class.getResourceAsStream("/sample.regere"));
        initiateListeners();
        regereRuleAnalyzerService.init();
    }

    private void initiateListeners() {
        final HashMap<String, PreRuleGoalAchievedListener> preListeners = new HashMap<>();
        final HashMap<String, Map> preMap = new HashMap<>(1000000);
        preListeners.put("listener-1", new PreRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, String identifier) {
                if (preMap.containsKey(event.get("user_id") + identifier)) {
                    System.out.println("Duplicate FINAL : [" + event + "]");
                    System.out.println("Current Event [" + preMap.get(event.get("user_id")) + "]");
                } else {
                    preMap.put((String) event.get("user_id") + identifier, event);
                }

            }
        });

        final HashMap<String, FinalRuleGoalAchievedListener> finalListeners = new HashMap<>();
        final HashMap<String, Map> finalMap = new HashMap<>(1000000);
        finalListeners.put("listener-2", new FinalRuleGoalAchievedListener() {
            @Override
            public void achieved(Map<String, Object> event, RegereRule regereRule) {
                if (finalMap.containsKey(event.get("user_id"))) {
                    System.out.println("Duplicate FINAL : [" + event + "]");
                    System.out.println("Current Event [" + finalMap.get(event.get("user_id")) + "]");
                } else {
                    finalMap.put((String) event.get("user_id"), event);
                }
            }
        });


        regereRuleAnalyzerService.setPreRuleGoalAchievedListeners(preListeners);
        regereRuleAnalyzerService.setFinalRuleGoalAchievedListeners(finalListeners);
    }

    @Test
    public void testAnalyzePerformance() throws Exception {
        final long[] total = {0};
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final int currentTps = counter.getAndSet(0);
                total[0] += currentTps;
                System.out.println("Current tranastion per sec [" + currentTps + "] total [" + total[0] + "]");
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 50; i++) {
            createThreadProducer(new Random(), 1000000);
        }
        Thread.sleep(1000000);
    }

    private void createThreadProducer(final Random r, final int numberOfCycles) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < numberOfCycles; i++) {
                    try {
                        final HashMap<String, Object> currentEvent = new HashMap<>();
                        currentEvent.put("user_id", Long.toString(r.nextInt(1000)));
                        currentEvent.put("total_number_of_topup", (long) r.nextInt(2234236));
                        currentEvent.put("current_top_up_amount", (long) r.nextInt(233436));
                        currentEvent.put("total_number_of_sms", (double) r.nextInt(2234326));
                        currentEvent.put("KFG", (long) r.nextInt(22226));
                        currentEvent.put("current_date", new Date());
                        currentEvent.put("class_of_service", "AB" + (char)i);
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
