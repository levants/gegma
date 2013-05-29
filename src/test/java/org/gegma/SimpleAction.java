package org.gegma;

import java.util.Map;

import org.gegma.tasks.TaskAction;

public class SimpleAction implements TaskAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private GegmaProcess process;

    private Map<String, Object> parameters;

    private String key;

    private Object value;

    public SimpleAction(GegmaProcess process, String key, Object value) {

	this.process = process;
	this.key = key;
	this.value = value;
    }

    @Override
    public void execute() {

	System.out.format("%s %s\n", key, value);
	parameters = process.getParameters();
	parameters.put(key, value);
    }

}
