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

import dev.j.regere.domain.RegereRuleFlowWrapper;

public final class BooleanNotOperation extends RegereBoolean {

	private final RegereBoolean regereBoolean;

	public BooleanNotOperation(final RegereBoolean newRegereBoolean) {
		if (newRegereBoolean == null) {
			throw new IllegalArgumentException("RegereBoolean cannot be null");
		}
		this.regereBoolean = newRegereBoolean;
	}

    /**
     * Evaluate the NOT operation
     * @param flowWrapper
     */
	public boolean booleanValue(RegereRuleFlowWrapper flowWrapper) {
		return !regereBoolean.booleanValue(flowWrapper);
	}

	public String toString() {
		return "(!" + this.regereBoolean + ")";
	}
}
