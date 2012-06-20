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
