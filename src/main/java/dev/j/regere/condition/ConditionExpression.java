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
    private final String key;
    private final String value;


    public ConditionExpression(String expression, Map<String, String> type) {
        this.expression = expression;
        Matcher m = conditionExpressionRegex.matcher(expression);
        m.find();
        pattern = m.group();
        final String[] split = expression.split(pattern);
        key = split[0];
        value = split[1];
        dataType = DataType.valueOf(type.get(key));
        logger.info("loading expression[" + expression + "] key[" + key + "] value[" + value +
                "] pattern[" + pattern + "] dataType[" + dataType + "] ");
    }


    /**
	 * Check the condition mapping
     * @param flowWrapper
     */
	public boolean booleanValue(RegereRuleFlowWrapper flowWrapper) {
        return dataType.decode(flowWrapper.getEvent().get(key), dataType.decode(value), pattern);
	}

	public String toString() {
		return expression;
	}
}
