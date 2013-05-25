package org.gegma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gegma.exceptions.InvalidPathException;

/**
 * 
 * @author rezo
 */
public class Place {

    private static Long idGenerator = 1L;
    private Long placeId = ++idGenerator;
    private String name;
    private Collection<Transition> inputs;
    private Collection<Connection> outputs;
    private GegmaProcess process;

    private Transition path;

    private AtomicBoolean token = new AtomicBoolean();

    public Place() {
    }

    public Long getPlaceId() {
	return placeId;
    }

    public Collection<Transition> getInputs() {
	return inputs;
    }

    public void addInput(Transition transition) {
	if (inputs == null) {
	    inputs = new ArrayList<Transition>();
	}
	inputs.add(transition);

	// Adds this Place as outpu to passed Transition object
	transition.addOutput(this);
    }

    public Collection<Connection> getOutputs() {
	return outputs;
    }

    public void addOutput(Connection connection) {
	if (outputs == null) {
	    outputs = new LinkedList<Connection>();
	}
	outputs.add(connection);
	// Adds as input this Place to passed Connections output Transition
	// object
	Transition transition = connection.getOutput();
	transition.addInput(this);
    }

    public void addOutput(Transition output) {

	addOutput(new Connection(output));
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public GegmaProcess getProcess() {

	return process;
    }

    public void setProcess(GegmaProcess process) {
	this.process = process;
	if (outputs != null) {
	    Transition output;
	    for (Connection connection : outputs) {
		output = connection.getOutput();
		if (output.getProcess() == null) {
		    output.setProcess(process);
		}
	    }
	}
    }

    public boolean hasToken() {

	return token.get();
    }

    public void setToken() {
	token.getAndSet(Boolean.TRUE);
    }

    public void untoken() {
	token.getAndSet(Boolean.FALSE);
    }

    private Transition choosePath() throws IOException {

	if (outputs == null || outputs.isEmpty()) {
	    path = null;
	} else {

	    Iterator<Connection> connections = outputs.iterator();
	    boolean notPath = Boolean.TRUE;
	    Connection connection;
	    while (connections.hasNext() && notPath) {

		connection = connections.next();
		notPath = !connection.getCondition().isValid(process);
		path = connection.getOutput();
	    }

	    if (notPath) {
		throw new InvalidPathException(
			"Could not find valid output trnsition");
	    }
	}

	return path;
    }

    public Transition getPath() throws IOException {

	if (path == null) {
	    choosePath();
	}

	return path;
    }

    public boolean isValid() throws IOException {

	Transition path = getPath();
	boolean valid = (path != null && path.isAvailable());

	return valid;
    }

    public void next() throws IOException {

	Transition path = getPath();
	if (path != null) {
	    path.fire();
	}
    }
}
