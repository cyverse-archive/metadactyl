package org.iplantc.workflow;

import org.iplantc.workflow.model.Template;

/**
 * Used to fetch a workflow template.
 * 
 * @author Dennis Roberts
 */
public interface TemplateFetcher {

    /**
     * Fetches the template with the given name.
     * 
     * @param name the name of the template to fetch.
     * @return the template.
     */
    public Template fetchTemplateByName(String name) throws Exception;
}
