package org.gegma;

/**
 * 
 * @author rezo
 */
public interface ProcessListener {

    void beforePlace(GegmaProcess process, Place place);

    void execute(GegmaProcess process, Place place) throws Throwable;

    void afterPlace(GegmaProcess process, Place place);
}
