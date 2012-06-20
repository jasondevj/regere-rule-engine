package dev.j.regere.parser.json;

import java.io.InputStream;
import java.util.Map;

public interface JsonParser {

    public void init();

    public Map<String, Object> toJson(InputStream resourceAsStream );
}
