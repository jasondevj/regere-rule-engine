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
