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

package dev.j.regere.queue;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class QueueInitiator {

    private static final Logger logger = Logger.getLogger(QueueInitiator.class);
    private static QueueInitiator instance;
    private Map<Integer, QueueManager> queue;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup cachedThreadPool = new ThreadGroup("CachedThreadPool");
    private int numberOfThreads;

    private QueueInitiator() {
        logger.info("initiating queues...");
        numberOfThreads = Runtime.getRuntime().availableProcessors() * 2 + 2;
        logger.info("number of threads set as total (process * 2 + 2)  [" + numberOfThreads + "]");
        queue = new HashMap<>(numberOfThreads);
    }

    public QueueManager manager(String identifier, EventConsumer consumer) {
        final int hashCode = Math.abs(identifier.hashCode() % numberOfThreads);
        QueueManager queueManager = queue.get(hashCode);
        if (queueManager == null) {
            queueManager = new QueueManager(numberOfThreads, consumer);
            queueManager.init();
            createNewThread(queueManager).start();
            queue.put(hashCode, queueManager);
        }
        return queueManager;
    }

    public Thread createNewThread(Runnable r) {
        return new Thread(cachedThreadPool, r, "thread-mediator-" + threadNumber.getAndIncrement(), 0);
    }

    public static QueueInitiator getInstance() {
        if (instance == null) {
            instance = new QueueInitiator();
        }
        return instance;
    }
}
