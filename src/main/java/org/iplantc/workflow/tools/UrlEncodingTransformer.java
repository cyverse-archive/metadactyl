package org.iplantc.workflow.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.iplantc.workflow.WorkflowException;

/**
 * A transformer that can be used to URL encode strings.
 * 
 * @author Dennis Roberts
 */
public class UrlEncodingTransformer implements Transformer<String, String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String transform(String source) {
        try {
            return URLEncoder.encode(source, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e) {
            throw new WorkflowException("ISO-8859-1 is not supported", e);
        }
    }
}
