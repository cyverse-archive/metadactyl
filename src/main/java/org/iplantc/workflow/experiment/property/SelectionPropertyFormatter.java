package org.iplantc.workflow.experiment.property;

import static org.iplantc.workflow.experiment.ParamUtils.setParamNameAndValue;

import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.workflow.model.Property;

/**
 * The property formatter to use for Selection arguments.
 * 
 * @author Dennis Roberts
 */
public class SelectionPropertyFormatter extends PropertyFormatter {

    /**
     * @param config the experiment configuration.
     * @param step the transformation step.
     * @param property the property being formatted.
     * @param propertyValues a map of property names to property values.
     */
    public SelectionPropertyFormatter(JSONObject config, TransformationStep step, Property property,
        Map<String, List<String>> propertyValues)
    {
        super(config, step, property, propertyValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSON formatProperty() {
        JSONObject result;
        String value = getValue();
        if (StringUtils.isBlank(value)) {
            result = null;
        }
        else if (StringUtils.isNumeric(value)) {
            result = formatOldStyleProperty(Integer.parseInt(value));
        }
        else {
            result = formatNewStyleProperty((JSONObject)JSONSerializer.toJSON(value));
        }
        return result;
    }

    /**
     * Formats a new-style property, in which the name and value are specified by a JSON object.
     * 
     * @param value the property value.
     * @return the formatted property.
     */
    protected JSONObject formatNewStyleProperty(JSONObject propertyJson) {
        return formatProperty(propertyJson.getString("name"), propertyJson.optString("value"));
    }

    /**
     * Formats an old-style property, in which the name is specified by an element in the property name, which is a
     * comma-delimited string, and a numeric value is used to indicate which element is selected.
     * 
     * @param index
     * @return
     */
    private JSONObject formatOldStyleProperty(int index) {
        JSONObject result = null;
        String[] values = property.getName().split(",");
        if (index >= 0 && values.length > index) {
            String[] components = values[index].split("\\s+|=", 2);
            result = formatProperty(components[0], components.length > 1 ? components[1] : "");
        }
        return result;
    }

    /**
     * Formats a property with the given name and value.
     * 
     * @param name the name (that is, the command-line option) used to identify the property.
     * @param value the property value.
     * @return the formatted property.
     */
    private JSONObject formatProperty(String name, String value) {
        JSONObject json = null;
        if (!StringUtils.isBlank(name) || !StringUtils.isBlank(value)) {
            json = new JSONObject();
            json.put("order", property.getOrder());
            setParamNameAndValue(json, name, value);
            json.put("id", property.getId());
            registerPropertyValue("");
        }
        return json;
    }
}
