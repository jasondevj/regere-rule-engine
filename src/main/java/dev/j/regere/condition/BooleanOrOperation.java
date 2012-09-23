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

public final class BooleanOrOperation extends RegereBoolean {

	private RegereBoolean regereBoolean1;
	private RegereBoolean regereBoolean2;

	public BooleanOrOperation(final RegereBoolean newRegereBoolean1, final RegereBoolean newRegereBoolean2) {
		if (newRegereBoolean1 == null || newRegereBoolean2 == null) {
			throw new IllegalArgumentException("RegereBoolean Objects cannot be null");
		}
		this.regereBoolean1 = newRegereBoolean1;
		this.regereBoolean2 = newRegereBoolean2;

	}

    /**
     * Evaluate the short circuit OR operation
     * @param flowWrapper
     */
	public boolean booleanValue(RegereRuleFlowWrapper flowWrapper) {
		return (this.regereBoolean1.booleanValue(flowWrapper) || this.regereBoolean2.booleanValue(flowWrapper));
	}

	public String toString() {
		return "(" + this.regereBoolean1 + "||" + this.regereBoolean2 + ")";
	}

}
