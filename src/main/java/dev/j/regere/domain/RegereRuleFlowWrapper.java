package dev.j.regere.domain;

import java.util.Map;

public class RegereRuleFlowWrapper {

    //todo need to think if the intermediate event will be useful for the flow
//    private Map<String, Object> intermediateEvents;
    private Map<String, Object> event;

    public RegereRuleFlowWrapper(Map<String, Object> event) {
//        this.intermediateEvents = intermediateEvents;
        this.event = event;
    }

//    public Map<String, Object> getIntermediateEvents() {
//        return intermediateEvents;
//    }

    public Map<String, Object> getEvent() {
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RegereRuleFlowWrapper");
        sb.append("{event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}
