package org.iplantc.workflow.tools;

/**
 * A list transformer that can be used to URL encode all elements in a list.
 * 
 * @author Dennis Roberts
 */
public class UrlEncodingListTransformer extends ListTransformer<String, String> {

    /**
     * The default constructor.
     */
    public UrlEncodingListTransformer() {
        super(new UrlEncodingTransformer());
    }
}
