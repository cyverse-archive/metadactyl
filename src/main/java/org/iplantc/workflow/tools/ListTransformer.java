package org.iplantc.workflow.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to transform all of the elements in a list.
 * 
 * @author Dennis Roberts
 */
public class ListTransformer<S, D> {

    /**
     * The transformer used to transform all of the list elements.
     */
    private Transformer<S, D> transformer;

    /**
     * @param transformer the transformer used to transform list elements.
     */
    public ListTransformer(Transformer<S, D> transformer) {
        this.transformer = transformer;
    }

    /**
     * Uses the transformer to convert all elements in the given list.
     * 
     * @param source the source list.
     * @return the destination list.
     */
    public List<D> transform(List<S> source) {
        List<D> dest = new ArrayList<D>();
        for (S sourceElement : source) {
            dest.add(transformer.transform(sourceElement));
        }
        return dest;
    }
}
