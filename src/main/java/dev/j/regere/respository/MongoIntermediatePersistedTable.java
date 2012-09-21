package dev.j.regere.respository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static dev.j.regere.util.CommonUtil.loadPersistedValues;
import static dev.j.regere.util.CommonUtil.returnPersistableValues;

public class MongoIntermediatePersistedTable implements IntermediatePersistedTable {

    private static final Logger logger = Logger.getLogger(MongoIntermediatePersistedTable.class);
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
            throw new RuntimeException("Error loading mognodb : ", e);
        }
    }

    @Override
    public Map<String, Object> load(String regereId, String commonIdentifier, Map<String, Object> currentEvent) {

        final BasicDBObject dbList = new BasicDBObject(ID, regereId + STRING_COLAN + commonIdentifier);
        if (logger.isDebugEnabled()) {
            logger.debug("loading intermediate event for key [" + dbList.get(ID) + "]");
        }
        final DBObject dbObject = dbCollection.findOne(dbList);
        if (dbObject != null) {
            logger.info("New intermediate object found loading the object from db");
            currentEvent = loadPersistedValues(currentEvent, dbObject.toMap());
        }
        return currentEvent;
    }

    @Override
    public void persistEvent(String regereId, String commonIdentifier, Map<String, Object> currentEvent,
                             List<String> persitableKeys) {
        final Map<String, Object> persistMap = returnPersistableValues(currentEvent, persitableKeys);
        persistMap.put(ID, regereId + STRING_COLAN + commonIdentifier);
        if (logger.isDebugEnabled()) {
            logger.debug("saving intermediate event key [" + persistMap.get(ID) + "]");
        }
        if (persistMap.size() > 1) {
            dbCollection.save(new BasicDBObject(persistMap));
        } else {
            logger.info("no data needs to be save saving is ignored for key [" + persistMap.get(ID) + "]");
        }
    }
}
