package org.iplantc.workflow.model;

import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.marshaller.BaseTemplateMarshaller;

/**
 * Represents a sub-category for property validation rules.
 * 
 * @author Dennis Roberts
 */
public class RuleSubtype extends WorkflowElement {

    /**
     * Creates a new empty rule sub-type.
     */
    public RuleSubtype() {
        super();
    }

    /**
     * @param id the rule sub-type identifier.
     * @param name the rule sub-type name.
     * @param label the rule sub-type label.
     * @param description the rule sub-type description.
     */
    public RuleSubtype(String id, String name, String label, String description) {
        super(id, name, label, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(BaseTemplateMarshaller marshaller) throws WorkflowException {
        // Rule sub-types are not currently marshalled.
    }
}
