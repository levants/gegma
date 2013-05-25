package org.gegma;

/**
 * 
 * @author rezo
 */
public class Connection {

    private Condition condition;

    private Transition output;

    /**
     * Default condition
     * 
     * @author levan
     * 
     */
    protected static final class DefaultCondition implements Condition {

	@Override
	public boolean isValid(GegmaProcess process) {

	    return Boolean.TRUE;
	}
    }

    public Connection() {
    }

    public Connection(Transition output) {
	this.output = output;
	this.condition = new DefaultCondition();
    }

    public Connection(Transition output, Condition condition) {
	this.output = output;
	this.condition = condition;
    }

    public Condition getCondition() {
	return condition;
    }

    public Transition getOutput() {
	return output;
    }

}
