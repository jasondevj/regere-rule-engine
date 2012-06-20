package dev.j.regere.domain;

import java.util.Map;

public class RegereRuleFlowWrapper {

    //todo need to think if the intermediate event will be useful for the flow
//    private Map<String, Object> intermediateEvents;
    private Map<String, Object> currentEvent;
    private Map<String, Object> summarizedEvents;

    public RegereRuleFlowWrapper(Map<String, Object> currentEvent, Map<String, Object> summarizedEvents) {
//        this.intermediateEvents = intermediateEvents;
        this.currentEvent = currentEvent;
        this.summarizedEvents = summarizedEvents;
    }

//    public Map<String, Object> getIntermediateEvents() {
//        return intermediateEvents;
//    }

    public Map<String, Object> getCurrentEvent() {
        return currentEvent;
    }

    public Map<String, Object> getSummarizedEvents() {
        return summarizedEvents;
    }
}
