package org.iplantc.workflow.mock;

import java.util.HashMap;

import org.iplantc.workflow.TemplateFetcher;
import org.iplantc.workflow.model.Template;

/**
 * A mock template fetcher for use during testing.
 * 
 * @author Dennis Roberts
 */
public class MockTemplateFetcher implements TemplateFetcher {

    /**
     * The templates that this fetcher knows about, indexed by name.
     */
    HashMap<String, Template> templates = new HashMap<String, Template>();

    /**
     * Adds a template to the list of templates that this template fetcher knows about.
     * 
     * @param template the template to add.
     */
    public void addTemplate(Template template) {
        templates.put(template.getName(), template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Template fetchTemplateByName(String name) {
        return templates.get(name);
    }
}
