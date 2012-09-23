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

package dev.j.regere.domain;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum DataType {

    LONG {
        @Override
        public boolean decode(Object val1, Object val2, String pattern) {
            Long value1 = (Long) val1;
            Long value2 = (Long) val2;
            switch (pattern) {
                case ">":
                    return value1 > value2;
                case ">=":
                    return value1 >= value2;
                case "<":
                    return value1 < value2;
                case "<=":
                    return value1 <= value2;
                case "==":
                    return value1.equals(value2);
                case "!=":
                    return !value1.equals(value2);
                default:
                    throw new RuntimeException("Unrecognised pattern [" + pattern + "]");
            }
        }

        @Override
        public Object decode(String value) {
            return Long.parseLong(value);
        }
    }, DOUBLE {
        @Override
        public boolean decode(Object val1, Object val2, String pattern) {
            Double value1 = (Double) val1;
            Double value2 = (Double) val2;
            switch (pattern) {
                case ">":
                    return value1 > value2;
                case ">=":
                    return value1 >= value2;
                case "<":
                    return value1 < value2;
                case "<=":
                    return value1 <= value2;
                case "==":
                    return value1.equals(value2);
                case "!=":
                    return !value1.equals(value2);
                default:
                    throw new RuntimeException("Unrecognised pattern [" + pattern + "]");
            }
        }

        @Override
        public Object decode(String value) {
            return Double.parseDouble(value);
        }
    }, DATE {
        @Override
        public boolean decode(Object val1, Object val2, String pattern) {
            Date value1 = (Date) val1;
            Date value2 = (Date) val2;
            switch (pattern) {
                case ">":
                    return value1.compareTo(value2) > 0;
                case ">=":
                    return value1.compareTo(value2) >= 0;
                case "<":
                    return value1.compareTo(value2) < 0;
                case "<=":
                    return value1.compareTo(value2) <= 0;
                case "==":
                    return value1.compareTo(value2) == 0;
                case "!=":
                    return value1.compareTo(value2) != 0;
                default:
                    throw new RuntimeException("Unrecognised pattern [" + pattern + "]");
            }

        }

        @Override
        public Object decode(String value) {
            try {
                return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(value);
            } catch (ParseException e) {
                logger.error("Error converting string [" + value + "] to date");
                throw new RuntimeException("Error converting string [" + value + "] to date : ", e);
            }
        }
    }, STRING {
        @Override
        public boolean decode(Object val1, Object val2, String pattern) {
            String value1 = (String) val1;
            String value2 = (String) val2;
            switch (pattern) {
                case ">":
                    return value1.compareTo(value2) > 0;
                case ">=":
                    return value1.compareTo(value2) >= 0;
                case "<":
                    return value1.compareTo(value2) < 0;
                case "<=":
                    return value1.compareTo(value2) <= 0;
                case "==":
                    return value1.compareTo(value2) == 0;
                case "!=":
                    return value1.compareTo(value2) != 0;
                default:
                    throw new RuntimeException("Unrecognised pattern [" + pattern + "]");
            }
        }

        @Override
        public Object decode(String value) {
            return value;
        }
    };

    public abstract boolean decode(Object value1, Object value2, String pattern);

    public abstract Object decode(String value);

    private static final Logger logger = Logger.getLogger(DataType.class);
}

