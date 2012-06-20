package dev.j.regere.condition;

import dev.j.regere.domain.DataType;
import dev.j.regere.domain.RegereRuleFlowWrapper;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Conditional mapping checking class
 */
public final class ConditionExpression extends RegereBoolean {

    private static final Logger logger = Logger.getLogger(ConditionExpression.class);
    private static final Pattern conditionExpressionRegex = Pattern.compile("(>=|>|<|<=|==|!=)");

	private final String expression;
	private final String pattern;
    private final DataType dataType;
    private final String key1;
    private final String key2;
    private final String mapKey1;
    private final String mapKey2;


    public ConditionExpression(String expression, Map<String, String> type) {
        this.expression = expression;
        Matcher m = conditionExpressionRegex.matcher(expression);
        m.find();
        pattern = m.group();
        final String[] split = expression.split(pattern);
        key1 = split[0];
        key2 = split[1];
        mapKey1 = split[0].split("_S|_C")[0];
        mapKey2 = split[1].split("_S|_C")[0];
        dataType = DataType.valueOf(type.get(mapKey1));
        logger.info("loading expression[" + expression + "] key1[" + key1 + "] key2[" + key2 +
                "] pattern[" + pattern + "] mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
    }


    /**
	 * Check the condition mapping
     * @param flowWrapper
     */
	public boolean booleanValue(RegereRuleFlowWrapper flowWrapper) {

        boolean decodedBoolean;
        if (key1.endsWith("_C") && key2.endsWith("_C")) {
            logger.debug("_C, _C matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getCurrentEvent().get(mapKey1), flowWrapper.getCurrentEvent().get(mapKey2), pattern);
        } else if (key1.endsWith("_C") && key2.endsWith("_S")) {
            logger.debug("_C, _S matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getCurrentEvent().get(mapKey1), flowWrapper.getSummarizedEvents().get(mapKey2), pattern);
        } else if (key1.endsWith("_S") && key2.endsWith("_S")) {
            logger.debug("_S, _S matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getSummarizedEvents().get(mapKey1), flowWrapper.getSummarizedEvents().get(mapKey2), pattern);
        } else if (key1.endsWith("_S") && key2.endsWith("_C")) {
            logger.debug("_S, _C matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getSummarizedEvents().get(mapKey1), flowWrapper.getCurrentEvent().get(mapKey2), pattern);
        } else if (key1.endsWith("_C")) {
            logger.debug("key1 _C, numeric matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getCurrentEvent().get(mapKey1), dataType.decode(key2), pattern);
        } else if (key1.endsWith("_S")) {
            logger.debug("key1 _S, numeric matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(flowWrapper.getSummarizedEvents().get(mapKey1), dataType.decode(key2), pattern);
        } else if (key2.endsWith("_C")) {
            logger.debug("key2 _C, numeric matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(dataType.decode(key1), flowWrapper.getCurrentEvent().get(mapKey2), pattern);
        } else if (key2.endsWith("_S")) {
            logger.debug("key2 _S, numeric matched loading mapKey1[" + mapKey1 + "] mapKey2[" + mapKey2 + "] ");
            decodedBoolean = dataType.decode(dataType.decode(key1), flowWrapper.getSummarizedEvents().get(mapKey2), pattern);
        } else {
            throw new RuntimeException("Nothing matched this is a major error key1[" + key1 + "] key2[" + key2 + "], this scenario should not happen");
        }
        return decodedBoolean;
	}

	public String toString() {
		return expression;
	}
}
