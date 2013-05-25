package org.gegma;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author rezo
 */
public class GegmaProcess {

    private Long processId = new Random(System.currentTimeMillis()).nextLong();

    private ProcessListener listener;

    private ConcurrentMap<Long, Place> tokens = new ConcurrentHashMap<Long, Place>();

    private Map<String, Object> parameters;

    private boolean inWork = false;

    protected static final class DefaultListener implements ProcessListener {

	@Override
	public void beforePlace(GegmaProcess process, Place place) {

	}

	@Override
	public void execute(GegmaProcess process, Place place)
		throws IOException {
	}

	@Override
	public void afterPlace(GegmaProcess process, Place place) {
	}

    }

    public Long getProcessId() {
	return processId;
    }

    public void setListener(ProcessListener listener) {
	this.listener = listener;
    }

    public void setParameters(Map<String, Object> parameters) {
	this.parameters = parameters;
    }

    public ConcurrentMap<Long, Place> getTokens() {
	return tokens;
    }

    public Map<String, Object> getParameters() {
	return parameters;
    }

    private void tokenize(Place place) {

	Long placeId = place.getPlaceId();
	place.setToken();
	tokens.put(placeId, place);
    }

    public void startProcess(Place start) throws IOException {
	start.setProcess(this);
	tokenize(start);
	doNext();
    }

    public ProcessListener getListener() {
	if (listener == null) {
	    listener = new DefaultListener();
	}

	return listener;
    }

    private Collection<Place> getValidPlaces() throws IOException {

	Collection<Place> valids = new HashSet<Place>();
	Collection<Place> places = tokens.values();
	for (Place place : places) {
	    if (place.isValid()) {
		valids.add(place);
	    }
	}

	return valids;
    }

    private void execute(Place place) throws IOException {

	ProcessListener listen = getListener();
	listen.beforePlace(this, place);
	place.next();
	try {
	    listen.execute(this, place);
	} catch (Throwable th) {
	    throw new IOException(th);
	}
	listen.afterPlace(this, place);
    }

    private void execute(Collection<Place> places) throws IOException {

	for (Place place : places) {
	    if (place.isValid()) {
		execute(place);
	    }
	}
    }

    public void doNext() throws IOException {

	inWork = Boolean.TRUE;
	Collection<Place> valids;
	while (inWork) {
	    valids = getValidPlaces();
	    inWork = !valids.isEmpty();
	    if (inWork) {
		execute(valids);
	    }
	}
    }
}
