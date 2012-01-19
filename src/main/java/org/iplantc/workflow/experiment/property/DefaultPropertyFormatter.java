package org.iplantc.workflow.experiment.property;

import java.util.List;
import java.util.Map;

import org.iplantc.workflow.model.Property;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.iplantc.persistence.dto.step.TransformationStep;

/**
 * The default property formatter to use when there is no special case for the property type.
 * 
 * @author Dennis Roberts
 */
public class DefaultPropertyFormatter extends PropertyFormatter {

    /**
     * @param config the experiment configuration.
     * @param step the transformation step.
     * @param property the property being formatted.
     * @param propertyValues a map of property names to property values.
     */
    public DefaultPropertyFormatter(JSONObject config, TransformationStep step, Property property,
            Map<String, List<String>> propertyValues) {
        super(config, step, property, propertyValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject formatProperty() {
        JSONObject json = null;
        String value = getValue();
        if (!StringUtils.isBlank(value) || !property.getOmitIfBlank()) {
            json = super.formatProperty();
            json.put("value", value);
            registerPropertyValue(value);
        }
        return json;
    }
}
