package org.iplantc.workflow.experiment.property;

import java.util.List;
import java.util.Map;

import org.iplantc.workflow.model.Property;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.iplantc.persistence.dto.step.TransformationStep;

/**
 * The property formatter to use for QuotedText arguments.
 * 
 * @author Dennis Roberts
 */
public class QuotedTextPropertyFormatter extends PropertyFormatter {

    /**
     * @param config the experiment configuration.
     * @param step the transformation step.
     * @param property the property being formatted.
     * @param propertyValues a map of property names to property values.
     */
    public QuotedTextPropertyFormatter(JSONObject config, TransformationStep step, Property property,
            Map<String, List<String>> propertyValues) {
        super(config, step, property, propertyValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject formatProperty() {
        JSONObject json = null;
        String rawValue = getValue();
        if (!property.getOmitIfBlank() || !StringUtils.isBlank(rawValue)) {
            json = super.formatProperty();
            String value = " \"" + rawValue + "\"";
            registerPropertyValue(value);
            json.put("value", value);
        }
        return json;
    }
}
