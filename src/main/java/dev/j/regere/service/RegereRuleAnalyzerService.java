package dev.j.regere.service;

import dev.j.regere.MalformedException;

import java.io.InputStream;
import java.util.Map;

public interface RegereRuleAnalyzerService {

    String REGERE_ID = "regere-id";

    void addRule(InputStream inputStream) throws MalformedException;

    void analyze(Map<String, Object> event);

    void removeRule(String ruleId);
}
