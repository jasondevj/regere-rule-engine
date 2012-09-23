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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class QueueManager implements Runnable {

    private final Logger logger = Logger.getLogger(QueueManager.class);
    private final int capacity;
    private BlockingQueue<QueueMessage> messageQueue;
    private final EventConsumer consumer;


    public QueueManager(int capacity, EventConsumer consumer) {
        this.capacity = capacity;
        this.consumer = consumer;
    }

    public void init() {
        logger.info("initiating new Queue Manager with max capacity of [" + capacity + "]");
        messageQueue = new ArrayBlockingQueue<>(capacity, true);
    }

    public boolean offer(QueueMessage queueMessage) {
        if (logger.isDebugEnabled()) {
            logger.debug("Message [" + queueMessage + "] current queue size[" + messageQueue.size() + "]");
        }
        try {
            messageQueue.put(queueMessage);
        } catch (InterruptedException e) {
            logger.error("Error inserting message to queue : ", e);
        }
        return true;
    }

    private QueueMessage take() {
        QueueMessage event;
        try {
            event = messageQueue.take();
            if (logger.isDebugEnabled()) {
                logger.debug("Taken message [" + event + "] current queue size[" + messageQueue.size() + "]");
            }
        } catch (InterruptedException e) {
            logger.error("Error taking message from queue ", e);
            throw new RuntimeException(e);
        }
        return event;
    }

    public void run() {
        while (true) {
            try {
                consumer.consume(take());
            } catch (Throwable e) {
                logger.error("Error thrown to thread pool, Exception caught and ignored : ", e);
            }
        }
    }
}