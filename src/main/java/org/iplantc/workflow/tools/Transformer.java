package org.iplantc.workflow.tools;

/**
 * An interface that can be used to implement a collection transformer. 
 * 
 * @author Dennis Roberts
 */
public interface Transformer<S, D> {

    /**
     * Transforms a source object to a destination object.
     * 
     * @param source the source object.
     * @return the destination object.
     */
    public D transform(S source);
}
