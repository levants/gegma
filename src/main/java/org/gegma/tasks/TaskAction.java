package org.gegma.tasks;

import java.io.Serializable;

/**
 * User logic is passed to process by implementation of this interface
 * 
 * @author levan
 * 
 */
public interface TaskAction extends Serializable {

    void execute();
}
