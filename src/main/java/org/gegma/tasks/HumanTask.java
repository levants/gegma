package org.gegma.tasks;

import org.gegma.Transition;

/**
 * 
 * @author rezo
 */
public class HumanTask extends Transition {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String ownerUser;

    private String ownerGroup;

    private String description;

    private String name;

    private TaskAction action;

    protected static final class DefaultAction implements TaskAction {

	@Override
	public void execute() {
	}

    }

    public HumanTask() {
	this.action = new DefaultAction();
    }

    public HumanTask(TaskAction action) {
	this.action = action;
    }

    public String getOwnerUser() {
	return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
	this.ownerUser = ownerUser;
    }

    public String getOwnerGroup() {
	return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
	this.ownerGroup = ownerGroup;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public void execute() {

	System.out.println(name);
	action.execute();
    }
}
