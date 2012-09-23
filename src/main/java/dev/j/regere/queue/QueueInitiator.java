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
    private int numberOfThreads = 3;

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
