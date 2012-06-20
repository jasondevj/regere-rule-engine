package dev.j.regere.parser.json;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.InputStream;
import java.util.Map;

public class DefaultJsonParser implements JsonParser {

    private static final Logger logger = Logger.getLogger(DefaultJsonParser.class);
    public void init() {
        //do nothing
    }

    public Map<String, Object> toJson(InputStream inputStream) {
        logger.info("loading json object for stream [" + inputStream + "]");
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> objectMap = mapper.readValue(inputStream,
                    new TypeReference<Map<String, Object>>() { });
            logger.info("JSON object loaded [" + objectMap + "]");
            return objectMap;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON stream : ", e);
        }
    }
}
