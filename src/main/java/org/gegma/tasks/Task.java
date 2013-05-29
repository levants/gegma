package org.gegma.tasks;

import org.gegma.Transition;

public class Task extends Transition {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private TaskAction action;

    private String name;

    public Task() {
	this.action = new HumanTask.DefaultAction();
    }

    public Task(TaskAction action) {
	this.action = action;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public void execute() {

	System.out.println(name);
	action.execute();
    }
}
