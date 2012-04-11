package org.iplantc.workflow.experiment.property;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.step.TransformationStep;

import org.iplantc.workflow.model.Property;

import static org.iplantc.workflow.experiment.ParamUtils.setParamNameAndValue;

/**
 * The property formatter to use for Flag arguments.
 * 
 * @author Dennis Roberts
 */
public class FlagPropertyFormatter extends PropertyFormatter {

    /**
     * @param config the experiment configuration.
     * @param step the transformation step.
     * @param property the property being formatted.
     * @param propertyValues a map of property names to property values.
     */
    public FlagPropertyFormatter(JSONObject config, TransformationStep step, Property property,
        Map<String, List<String>> propertyValues)
    {
        super(config, step, property, propertyValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject formatProperty() {
        JSONObject json = null;
        String name = determinePropertyName();
        if (name != null) {
            registerPropertyValue("");
            json = new JSONObject();
            json.put("order", property.getOrder());
            json.put("id", property.getId());
            setParamNameAndValue(json, name, "");
        }
        return json;
    }

    /**
     * Determines the name to use for the formatted property.
     * 
     * @return the name to use.
     */
    private String determinePropertyName() {
        String name;
        String[] possibleNames = property.getName().split(",");
        boolean enabled = Boolean.parseBoolean(getValue());
        if (possibleNames.length == 1) {
            name = enabled ? possibleNames[0] : null;
        }
        else {
            name = enabled ? possibleNames[0] : possibleNames[1];
        }
        return name;
    }
}
