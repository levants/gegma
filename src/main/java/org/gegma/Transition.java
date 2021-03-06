package org.gegma;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author rezo
 */
public abstract class Transition implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Collection<Place> inputs = new ArrayList<Place>();

    private Collection<Place> outputs = new ArrayList<Place>();

    private GegmaProcess process;

    public Collection<Place> getInputs() {
	return inputs;
    }

    public void addInput(Place place) {
	inputs.add(place);
    }

    public Collection<Place> getOutputs() {
	return outputs;
    }

    public void addOutput(Place place) {
	outputs.add(place);
    }

    public GegmaProcess getProcess() {
	return process;
    }

    public void setProcess(GegmaProcess process) {

	this.process = process;
	for (Place place : outputs) {
	    place.setProcess(process);
	}
    }

    private boolean checkPlace(Place place) throws IOException {

	return place.getPath().equals(this) && place.hasToken();
    }

    public boolean isAvailable() throws IOException {
	// Check all incoming places are tokens or not
	Iterator<Place> places = inputs.iterator();
	boolean executable = Boolean.TRUE;
	Place place;
	while (places.hasNext() && executable) {

	    place = places.next();
	    executable = checkPlace(place);
	}

	return executable;
    }

    private void removeToken(Long placeId) {
	process.getTokens().remove(placeId);
    }

    public void addPlace(Place place) {
	process.getTokens().put(place.getPlaceId(), place);
    }

    private void untokenize() {

	for (Place place : inputs) {
	    place.untoken();
	    removeToken(place.getPlaceId());
	}
    }

    private void tokenize() {

	for (Place place : outputs) {
	    place.setToken();
	    addPlace(place);
	}
    }

    public abstract void execute();

    public void fire() {

	execute();
	untokenize();
	tokenize();
    }
}
