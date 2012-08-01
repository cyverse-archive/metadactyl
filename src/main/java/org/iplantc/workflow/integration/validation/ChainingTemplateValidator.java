package org.iplantc.workflow.integration.validation;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.iplantc.workflow.model.Template;

/**
 * A template validator that chains multiple validators together.
 * 
 * @author Dennis Roberts
 */
public class ChainingTemplateValidator implements TemplateValidator {

    private static final Logger LOG = Logger.getLogger(ChainingTemplateValidator.class);

    /**
     * The list of validators to apply.
     */
    List<TemplateValidator> validators = new ArrayList<TemplateValidator>();

    /**
     * Adds a validator to the list of validators to apply.
     * 
     * @param validator the validator to add to the list.
     */
    public void addValidator(TemplateValidator validator) {
        validators.add(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Template template) {
        LOG.warn("validating using " + validators.size() + " validators");
        for (TemplateValidator validator : validators) {
            validator.validate(template);
        }
    }
}
