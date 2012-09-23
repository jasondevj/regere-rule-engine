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