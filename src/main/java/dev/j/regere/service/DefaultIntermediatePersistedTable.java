package dev.j.regere.service;

import com.mongodb.*;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

public class DefaultIntermediatePersistedTable implements IntermediatePersistedTable {

    private static final Logger logger = Logger.getLogger(DefaultIntermediatePersistedTable.class);
    public static final String STRING_COLAN = ":";
    public static final String ID = "_id";
    private DBCollection dbCollection;

    @Override
    public void init() {
        try {
            logger.info("loading mongodb with default details");
            dbCollection = new Mongo().getDB("regere_db").getCollection("regere_intermediate_table");
            logger.info("mongodb successfully loaded");
        } catch (UnknownHostException e) {
            new RuntimeException("Error loading mognodb : ", e);
        }
    }

    @Override
    public Map<String, Object> load(String regereId, String commonIdentifier, Map<String, Object> currentEvent) {
        final BasicDBObject dbList = new BasicDBObject();
        final String key = regereId + STRING_COLAN + commonIdentifier;
        dbList.put(ID, key);
        if (logger.isDebugEnabled()) {
            logger.debug("loading intermediate event for key [" + key + "]");
        }
        final DBObject dbObject = dbCollection.findOne(dbList);
        if (dbObject != null) {
            logger.info("New intermediate object found loading the object from db");
            final Map<String, Object> map = dbObject.toMap();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Long) {
                    final Long currentValue = (Long) currentEvent.get(entry.getKey());
                    currentEvent.put(entry.getKey(), (currentValue + ((Long) entry.getValue())));
                } else if (entry.getValue() instanceof Double) {
                    final Long currentValue = (Long) currentEvent.get(entry.getKey());
                    currentEvent.put(entry.getKey(), (currentValue + ((Double) entry.getValue())));
                } else {
                    logger.warn("Types did not match any available datatypes, this should not happen type [" + entry.getValue().getClass() + "]");
                }
            }
            return currentEvent;
        } else {
            return currentEvent;
        }
    }

    @Override
    public void persistEvent(String regereId, String commonIdentifier, Map<String, Object> currentEvent,
                             List<String> persitableKeys) {
        final BasicDBObject dbObject = new BasicDBObject(ID, regereId + STRING_COLAN + commonIdentifier);
        if (logger.isDebugEnabled()) {
            logger.debug("saving intermediate event key [" + dbObject.get(ID) + "]");
        }
        for (String persitableKey : persitableKeys) {
            final Object obj = currentEvent.get(persitableKey);
            if (obj != null) {
                dbObject.put(persitableKey, obj);
            }
        }
        if (dbObject.size() > 1) {
            dbCollection.save(dbObject);
        } else {
            logger.info("no data needs to be save saving is ignored for key [" + dbObject.get(ID) + "]");
        }
    }
}
