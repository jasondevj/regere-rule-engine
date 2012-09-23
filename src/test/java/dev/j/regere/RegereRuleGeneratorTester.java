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

package dev.j.regere;

import dev.j.regere.condition.RegereBoolean;
import dev.j.regere.service.RegereRuleGenerator;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class RegereRuleGeneratorTester {

    /**
     * testBooleanValueException
     */
    public void testBooleanValueException() {
        booleanValueException("A");
        booleanValueException(")");
        booleanValueException("false)");
        booleanValueException("(");
        booleanValueException("(true)(");
        booleanValueException("|");
        booleanValueException("|true");
        booleanValueException("|false");
        booleanValueException("true|");
        booleanValueException("false|");
        booleanValueException("!");
        booleanValueException("true!");
        booleanValueException("false!");
        booleanValueException("&");
        booleanValueException("&true");
        booleanValueException("&false");
        booleanValueException("true&");
        booleanValueException("false&");
        booleanValueException("truetrue");
        booleanValueException("falsefalse");
        booleanValueException("truefalse");
        booleanValueException("truetrue");
        booleanValueException("truea");
    }

    /**
     * testBooleanValueResult
     */
    @Test
    public void testBooleanValueResult() throws MalformedException {
        booleanValueResult("!!A<B", false, "(!(!A<B))");
        booleanValueResult("(!(!true))", true, "(!(!true))");
        booleanValueResult("!(!true)", true, "(!(!true))");
        booleanValueResult("(true||true)&&true", true, "((true||true)&&true)");
        booleanValueResult("((true||true)&&true)", true, "((true||true)&&true)");
        booleanValueResult("(true||false)&&true", true, "((true||false)&&true)");
        booleanValueResult("((true||false)&&true)", true, "((true||false)&&true)");
        booleanValueResult("(true||true)&&false", false, "((true||true)&&false)");
        booleanValueResult("((true||true)&&false)", false, "((true||true)&&false)");
        booleanValueResult("(false||false)&&true", false, "((false||false)&&true)");
        booleanValueResult("((false||false)&&true)", false, "((false||false)&&true)");
        booleanValueResult("(true||true)&&!true", false, "((true||true)&&(!true))");
        booleanValueResult("((true||true)&&!true)", false, "((true||true)&&(!true))");
        booleanValueResult("(true||true)&&!false", true, "((true||true)&&(!false))");
        booleanValueResult("((true||true)&&!false)", true, "((true||true)&&(!false))");
        booleanValueResult("(true||true)&&(true||true)", true, "((true||true)&&(true||true))");
        booleanValueResult("((true||true)&&(true||true))", true, "((true||true)&&(true||true))");
        booleanValueResult("(true||true)&&(false||false)", false, "((true||true)&&(false||false))");
        booleanValueResult("((true||true)&&(false||false))", false, "((true||true)&&(false||false))");
        booleanValueResult("(false||false)&&(true||true)", false, "((false||false)&&(true||true))");
        booleanValueResult("((false||false)&&(true||true))", false, "((false||false)&&(true||true))");
        booleanValueResult("(true||true)&&!(true||true)", false, "((true||true)&&(!(true||true)))");
        booleanValueResult("(true||true)&&!(false||false)", true, "((true||true)&&(!(false||false)))");
        booleanValueResult("(false&&false)||!(false&&false)", true, "((false&&false)||(!(false&&false)))");
        booleanValueResult("(!(false&&false))||(!(false&&false))", true, "((!(false&&false))||(!(false&&false)))");
        booleanValueResult("((!(false&&false))||(!(false&&false)))", true, "((!(false&&false))||(!(false&&false)))");
        booleanValueResult("(!(true&&false))||(true&&false)", true, "((!(true&&false))||(true&&false))");
        booleanValueResult("((!(true&&false))||(true&&false))", true, "((!(true&&false))||(true&&false))");
        booleanValueResult("(false||false)&&(false||false)", false, "((false||false)&&(false||false))");
        booleanValueResult("((false||false)&&(false||false))", false, "((false||false)&&(false||false))");
        booleanValueResult("(false&&false)||(false&&false)", false, "((false&&false)||(false&&false))");
        booleanValueResult("((false&&false)||(false&&false))", false, "((false&&false)||(false&&false))");
        booleanValueResult("(false&&false)||(true&&true)", true, "((false&&false)||(true&&true))");
        booleanValueResult("((false&&false)||(true&&true))", true, "((false&&false)||(true&&true))");
        booleanValueResult("(true&&true)||(false&&false)", true, "((true&&true)||(false&&false))");
        booleanValueResult("((true&&true)||(false&&false))", true, "((true&&true)||(false&&false))");
        booleanValueResult("(true&&true)||(true&&true)", true, "((true&&true)||(true&&true))");
        booleanValueResult("(true&&true)||!(true&&true)", true, "((true&&true)||(!(true&&true)))");
        booleanValueResult("((true&&true)||(true&&true))", true, "((true&&true)||(true&&true))");
        booleanValueResult("!((true&&true)||(true&&true))", false, "(!((true&&true)||(true&&true)))");
        booleanValueResult("(!((true&&true)||(true&&true)))", false, "(!((true&&true)||(true&&true)))");
        booleanValueResult("(!(true&&true))||(true&&true)", true, "((!(true&&true))||(true&&true))");
        booleanValueResult("((!(true&&true))||(true&&true))", true, "((!(true&&true))||(true&&true))");
        // -----------------------------------------------------------
        booleanValueResult("!false&&true", true, "((!false)&&true)");
        booleanValueResult("!false||false", true, "((!false)||false)");
        booleanValueResult("!true&&true", false, "((!true)&&true)");
        booleanValueResult("!true||false", false, "((!true)||false)");
        booleanValueResult("!(true||false)&&true", false, "((!(true||false))&&true)");
        booleanValueResult("!(false||false)&&true", true, "((!(false||false))&&true)");
        booleanValueResult("!(false||false)&&!false", true, "((!(false||false))&&(!false))");
        booleanValueResult("!(true||true)&&(true||true)", false, "((!(true||true))&&(true||true))");
        booleanValueResult("!(false||false)&&(true||true)", true, "((!(false||false))&&(true||true))");
        booleanValueResult("!(false||false)&&!(false||false)", true, "((!(false||false))&&(!(false||false)))");
        booleanValueResult("!(false&&false)||(false&&false)", true, "((!(false&&false))||(false&&false))");
        booleanValueResult("!(true&&true)||(false&&false)", false, "((!(true&&true))||(false&&false))");
        // -----------------------------------------------------------
        booleanValueResult("!true&&false||true", true, "(((!true)&&false)||true)");
        booleanValueResult("!(false&&false)||!(false&&false)", true, "((!(false&&false))||(!(false&&false)))");
        booleanValueResult("!(true&&true)||(true&&true)", true, "((!(true&&true))||(true&&true))");
    }

    /**
     * @param booleanExpresion
     */
    private void booleanValueException(final String booleanExpresion) {
        booleanValueExceptionLR(booleanExpresion);
    }

    /**
     * @param booleanExpresion
     */
    private void booleanValueExceptionLR(final String booleanExpresion) {
        try {
            System.out.println("");
            System.out.println("readLeftToRight");
            System.out.println("booleanExpresion = " + booleanExpresion);
            RegereBoolean generatedBoolean = RegereRuleGenerator.generate(booleanExpresion, new HashMap<String, String>());
            boolean result = generatedBoolean.booleanValue(null);
            System.err.println("expected         = Exception");
            System.err.println("result           = " + result);
            fail();
        } catch (Exception e) {
            System.out.println("expected         = Exception");
            System.out.println("result           = " + e.toString());
        }
    }

    /**
     * @param booleanExpresion
     * @param boolExpected
     * @param strExpected
     */
    private void booleanValueResult(final String booleanExpresion, final boolean boolExpected, final String strExpected) throws MalformedException {
        booleanValueResultLR(booleanExpresion, boolExpected, strExpected);
    }


    /**
     * @param booleanExpresion
     * @param boolExpected
     * @param strExpected
     */
    private void booleanValueResultLR(String booleanExpresion,boolean boolExpected, final String strExpected) throws MalformedException {
        try {
            System.out.println("");
            System.out.println("readLeftToRight");
            System.out.println("booleanExpresion = " + booleanExpresion);
//            BooleanExpression boolExpr = BooleanExpression.readLeftToRight(booleanExpresion);
            RegereBoolean generatedBoolean = RegereRuleGenerator.generate(booleanExpresion, new HashMap<String, String>());
            boolean result = generatedBoolean.booleanValue(null);
            System.out.println("boolExpected     = " + boolExpected);
            System.out.println("result           = " + result);
            assertEquals(boolExpected, result);
            System.out.println("strExpected      = " + strExpected);
            System.out.println("result           = " + generatedBoolean);
            assertEquals(strExpected, generatedBoolean.toString());
        } catch (MalformedException e) {
            throw e;
        }
    }
}
