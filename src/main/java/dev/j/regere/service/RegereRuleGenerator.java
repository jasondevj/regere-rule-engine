package dev.j.regere.service;

import dev.j.regere.MalformedException;
import dev.j.regere.condition.*;
import dev.j.regere.util.ExpressionValidationUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegereRuleGenerator {

    private static final Pattern conditionExpressionRegex = Pattern.compile("([\\w]+(>|<|<=|>=|==|!=)\\w+)");

    public static final String OPEN_BRACKETS = "(";
    public static final String CLOSE_BRACKETS = ")";
    public static final char PIPE_CHAR = '|';
    public static final char AMPERSAND_CHAR = '&';
    public static final char EXCLAMATION_CHAR = '!';
    public static final char SPACE_CHAR = ' ';
    public static final char FULL_STOP_CHAR = '.';
    public static final char CLOSE_BRACKETS_CHAR = ')';
    public static final char OPEN_BRACKETS_CHAR = '(';

    private String booleanExpression;
    private Map<String, String> dataTypeMap;

    public static RegereBoolean generate(final String expression, Map<String, String> dataTypeMap) throws MalformedException {
        RegereRuleGenerator rg = new RegereRuleGenerator();
        rg.booleanExpression = ExpressionValidationUtil.validAndFormat(expression);
        rg.dataTypeMap = dataTypeMap;
        return rg.toIBoolean(rg.booleanExpression, rg.booleanExpression.length());
    }

    /**
     * Transform the supplied expression to {@link RegereBoolean}.
     */
    private RegereBoolean toIBoolean(final String formattedBooleanExpression, final int index) throws MalformedException {
        char lastChar = getLastChar(formattedBooleanExpression);
//        if (Character.toString(lastChar).matches("\\s")) {
//            lastChar = SPACE_CHAR;
//        }
        String substring = getSubstringWithoutLastChar(formattedBooleanExpression);
//        System.out.println(index + " : " + lastChar + " [" + substring + "]");
        switch (lastChar) {
//            case SPACE_CHAR:
//                return toIBoolean(substring, index - 1);
            case CLOSE_BRACKETS_CHAR:
                String openToEnd = getFromOpenBracketsToEnd(substring, index - 1);
                String beginToOpen = getFromBeginToOpenBrackets(substring, index - 1);
                RegereBoolean boolOpenToEnd = toIBoolean(openToEnd, index - 1);
                return toIBoolean(boolOpenToEnd, beginToOpen, index - 1);
            //T, F are just kept for debugging proposes
//            case 'T':
//                return toIBoolean(new ConditionExpression(String.valueOf("True"), dataTypeMap), substring, index - 1);
//            case 'F':
//                return toIBoolean(new ConditionExpression(String.valueOf("False"), dataTypeMap), substring, index - 1);
            default: {
                Matcher m = conditionExpressionRegex.matcher(substring + lastChar);
                String expression = "";

                //this is done to always find the last group
                while (m.find()) {
                    expression = m.group();
                }
                substring = substring.substring(0, substring.length() - expression.length() + 1);
                return toIBoolean(new ConditionExpression(expression, dataTypeMap), substring, index - 1);
            }
        }
    }

    /**
     * Transform the supplied expression to {@link RegereBoolean}.
     */
    private RegereBoolean toIBoolean(final RegereBoolean lastRegereBoolean, final String formattedBooleanExpression,
                                     final int index) throws MalformedException {

        char lastChar = getLastChar(formattedBooleanExpression);

        if (Character.toString(lastChar).matches("\\s")) {
            lastChar = SPACE_CHAR;
        }
        String substring = getSubstringWithoutLastChar(formattedBooleanExpression);
        switch (lastChar) {
//            case SPACE_CHAR:
//                return toIBoolean(lastRegereBoolean, substring, index - 1);
            case FULL_STOP_CHAR:
                return lastRegereBoolean;
            case OPEN_BRACKETS_CHAR:
                return toIBoolean(lastRegereBoolean, substring, index - 1);
            case PIPE_CHAR:
                RegereBoolean boolFirstOr = toIBoolean(substring, index - 2);
                return new BooleanOrOperation(boolFirstOr, lastRegereBoolean);
            case AMPERSAND_CHAR:
                RegereBoolean boolFirstAnd = toIBoolean(substring, index - 2);
                return new BooleanAndOperation(boolFirstAnd, lastRegereBoolean);
            case EXCLAMATION_CHAR:
                RegereBoolean boolNot = new BooleanNotOperation(lastRegereBoolean);
                return toIBoolean(boolNot, substring, index - 1);
            default: {
                throw new MalformedException("Expected parameters [' '], [||], [&&], [|] but the given expression ["
                        + booleanExpression + "] does not match it");
            }
        }
    }

    /**
     * Returns the last character.
     */
    private char getLastChar(final String expression) {
        if (expression.length() == 0) {
            return FULL_STOP_CHAR;
        }
        return expression.charAt(expression.length() - 1);
    }

    /**
     * Returns the supplied expression without the last character
     */
    private String getSubstringWithoutLastChar(final String expression) {
        if (expression == null || expression.length() == 0) {
            return "";
        }
        return expression.substring(0, expression.length() - 1);
    }

    /**
     * Returns the substring from the begin of the expression open brackets
     */
    private String getFromBeginToOpenBrackets(final String expression, final int index) throws MalformedException {
        if (expression == null || expression.length() == 0) {
            return "";
        }
        int toIndex = getIndexOfOpenBrackets(expression, index);
        return expression.substring(0, toIndex);
    }

    /**
     * Returns the substring from open brackets to the end
     */
    private String getFromOpenBracketsToEnd(final String expression, final int index) throws MalformedException {
        if (expression == null || expression.length() == 0) {
            return "";
        }
        int fromIndex = getIndexOfOpenBrackets(expression, index) + 1;
        return expression.substring(fromIndex, expression.length());
    }

    /**
     * Returns the index of the open brackets.
     */
    private int getIndexOfOpenBrackets(final String expression, final int index) throws MalformedException {

        int lastIndexOfOpenBrackets = getLastIndexOf(expression, OPEN_BRACKETS, expression.length());
        int lastIndexOfCloseBrackets = getLastIndexOf(expression, CLOSE_BRACKETS, expression.length());

        while (lastIndexOfCloseBrackets != -1 && lastIndexOfOpenBrackets < lastIndexOfCloseBrackets) {
            lastIndexOfOpenBrackets = getLastIndexOf(expression, OPEN_BRACKETS, lastIndexOfOpenBrackets);
            lastIndexOfCloseBrackets = getLastIndexOf(expression, CLOSE_BRACKETS, lastIndexOfCloseBrackets);
        }
        if (lastIndexOfOpenBrackets == -1) {
            int parenthesisIndex = index - (expression.length() - lastIndexOfCloseBrackets);
            throw new MalformedException("Have a close bracket close without an open index[" + parenthesisIndex +
                    "] expression [" + booleanExpression + "]");
        }
        return lastIndexOfOpenBrackets;
    }

    /**
     * Return the last index of the supplied searched string. The search begins
     * from end to start
     */
    private int getLastIndexOf(final String expresion, final String searchedStr, final int toIndex) {
        if (toIndex < 0) {
            return -1;
        } else if (toIndex >= expresion.length()) {
            return expresion.lastIndexOf(searchedStr);
        } else {
            return expresion.substring(0, toIndex).lastIndexOf(searchedStr);
        }
    }

    public String toString() {
        return "RegereExpression [" + booleanExpression + "]";
    }
}
