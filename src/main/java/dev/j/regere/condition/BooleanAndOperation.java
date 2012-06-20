package dev.j.regere.condition;


import dev.j.regere.domain.RegereRuleFlowWrapper;

public final class BooleanAndOperation extends RegereBoolean {

    private final RegereBoolean regereBoolean1;
    private final RegereBoolean regereBoolean2;

    public BooleanAndOperation(RegereBoolean newRegereBoolean1, RegereBoolean newRegereBoolean2) {
        if (newRegereBoolean1 == null || newRegereBoolean2 == null) {
            throw new IllegalArgumentException("RegereBoolean Objects cannot be null");
        }
        this.regereBoolean1 = newRegereBoolean1;
        this.regereBoolean2 = newRegereBoolean2;

    }

    /**
     * Evaluate the short circuit AND operation
     * @param flowWrapper
     */
    public boolean booleanValue(RegereRuleFlowWrapper flowWrapper) {
        return (this.regereBoolean1.booleanValue(flowWrapper) && this.regereBoolean2.booleanValue(flowWrapper));
    }


    public String toString() {
        return "(" + this.regereBoolean1 + "&&" + this.regereBoolean2 + ")";
    }

}
