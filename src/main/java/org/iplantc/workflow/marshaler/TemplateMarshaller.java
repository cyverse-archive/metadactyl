package org.iplantc.workflow.marshaler;

import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.ContractType;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;
import org.iplantc.workflow.model.WorkflowElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Marshalls a workflow for the user interface.
 *
 * @author Dennis Roberts
 */
public class TemplateMarshaller implements BaseTemplateMarshaller {

    /**
     * The number of spaces to indent nested JSON objects when logging.
     */
    private static final int JSON_INDENT = 4;

    /**
     * Used to log error and informational messages.
     */
    private static final Logger LOG = Logger.getLogger(TemplateMarshaller.class);

    /**
     * The top-level JSON object that we're building. This object will contain all of components that are marshalled.
     */
    private JSONObject cumulativeJson = null;

    /**
     * A stack of JSON objects used to preserve the hierarchical representation of the data in the JSON.
     */
    private Stack<JSONObject> jsonStack = new Stack<JSONObject>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarshalledWorkflow() throws WorkflowException {
        if (cumulativeJson == null) {
            throw new WorkflowException("nothing has been marshalled yet");
        }
        return cumulativeJson.toString();
    }

    /**
     * {@inheritDoc}
     */
    public JSONObject getCumulativeJson() throws WorkflowException {
        if (cumulativeJson == null) {
            throw new WorkflowException("nothing has been marshalled yet");
        }
        return cumulativeJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Template template) throws WorkflowException {
        try {
            JSONObject json = createJsonObject();

            appendCommonProperties(json, template);
            json.put("type", template.getTemplateType());
            appendToParentProperty("children", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(Template template) throws WorkflowException {
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(PropertyGroup propertyGroup) throws WorkflowException {
        try {
            // if(!propertyGroup.isVisible()) return;
            JSONObject json = createJsonObject();
            appendCommonProperties(json, propertyGroup);
            json.put("type", propertyGroup.getGroupType());
            appendToParentProperty("groups", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(PropertyGroup propertyGroup) throws WorkflowException {
        // if(!propertyGroup.isVisible()) return;
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Property property) throws WorkflowException {
        try {
            // if(!property.getIsVisible()) return;

            JSONObject json = createJsonObject();
            appendCommonProperties(json, property);
            json.put("isVisible", property.getIsVisible());
            if (property.getDefaultValue() != null) {
                json.put("value", property.getDefaultValue());
            }
            if (property.getPropertyType() != null) {
                json.put("type", property.getPropertyType().getName());
            }
            json.put("description", StringUtils.defaultString(property.getDescription()));
            appendToParentProperty("properties", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(Property property) throws WorkflowException {
        // if(!property.getIsVisible()) return;
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(PropertyType propertyType) throws WorkflowException {
        try {
            jsonStack.peek().put("type", propertyType.getName());
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(PropertyType propertyType) throws WorkflowException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(ContractType contractType) throws WorkflowException {
        try {
            JSONObject json = createJsonObject();
            appendCommonProperties(json, contractType);
            setParentProperty("contractType", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(ContractType contractType) throws WorkflowException {
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Validator validator) throws WorkflowException {
        try {
            JSONObject json = createJsonObject();
            appendCommonProperties(json, validator);
            json.put("required", validator.isRequired());
            setParentProperty("validator", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(Validator validator) throws WorkflowException {
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Rule rule) throws WorkflowException {
        validateRuleType(rule.getRuleType());
        try {
            JSONObject json = createJsonObject();

            String ruleType = rule.getRuleType().getName();
            json.put(ruleType, buildArgsArray(rule.getArguments()));

            if (ruleType.equalsIgnoreCase("MustContain")) {
                convertMustContainRules(json);
            }

            appendToParentProperty("rules", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * Converts MustContain rules in the given jsonMustContain object from Strings to JSONObjects. If old
     * MustContain rules are given, where each rule is just a display string and the values are stored as
     * CSV fields in the property name, then the property name is cleared and the rules are reformatted
     * appropriately.
     *
     * @param jsonMustContain A JSONObject with a "MustContain" array of Strings.
     * @throws JSONException
     */
    private void convertMustContainRules(JSONObject jsonMustContain) throws JSONException {
        // Find the property JSON object so we can get the name list.
        JSONObject validator = jsonStack.pop();
        JSONObject property = jsonStack.peek();
        jsonStack.push(validator);

        if (LOG.isDebugEnabled()) {
            LOG.debug("validator: " + validator.toString(JSON_INDENT));
            LOG.debug("property: " + property.toString(JSON_INDENT));
        }

        JSONArray ruleList = jsonMustContain.getJSONArray("MustContain");

        // Check for old MustContain rule comma separated value fields in the property's name.
        String[] nameList = null;
        String propertyName = property.getString("name");
        if (propertyName != null && !propertyName.isEmpty()) {
            nameList = propertyName.split(",");

            // clobber the property name
            property.put("name", "");
        }

        for (int i = 0, ruleCount = ruleList.length(); i < ruleCount; i++) {
            // The rule may be a String or a Number.
            Object rule = ruleList.get(i);

            JSONObject jsonRule = null;

            try {
                // First check if the rule is already in the new JSON format
                jsonRule = new JSONObject(rule.toString());
                if (!jsonRule.has("name")) {
                    jsonRule.put("name", "");
                }
            }
            catch (JSONException oldRule) {
                // This rule is not a JSON object, so it must be an old MustContain rule.
                String arg;

                if (nameList != null && nameList.length == ruleCount) {
                    // nameList must be a list of values and the rules are their display values.
                    arg = nameList[i];
                }
                else {
                    // The MustContain rules are both the display and value strings.
                    arg = rule.toString();
                }

                // Extract the command-line option and value from the argument.
                String[] components = arg.trim().split("\\s+|=", 2);
                String option = components[0];
                String value = components.length > 1 ? components[1] : "";

                // Build the rule.
                jsonRule = new JSONObject();
                jsonRule.put("display", rule);
                jsonRule.put("name", option);
                jsonRule.put("value", value);
                jsonRule.put("isDefault", false);
            }

            // Replace the rule string with JSON.
            ruleList.put(i, jsonRule);
        }
    }

    private void validateRuleType(RuleType ruleType) {
        if (ruleType == null || StringUtils.isEmpty(ruleType.getName())) {
            throw new WorkflowException("rule with no type encountered");
        }
    }

    private JSONArray buildArgsArray(List<String> arguments) {
        JSONArray result = new JSONArray();
        for (String arg : arguments) {
            if (isInteger(arg)) {
                result.put(new Integer(arg.trim()));
            }
            else if (isDouble(arg)) {
                result.put(new Double(arg.trim()));
            }
            else {
                result.put(arg);
            }
        }
        return result;
    }

    private boolean isDouble(String arg) {
        return arg.trim().matches("[-]?\\p{Digit}+[\\.]\\p{Digit}+");
    }

    private boolean isInteger(String arg) {
        return arg.trim().matches("[-]?\\p{Digit}+");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(Rule rule) throws WorkflowException {
        jsonStack.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(RuleType ruleType) throws WorkflowException {
        try {
            JSONObject json = createJsonObject();
            appendCommonProperties(json, ruleType);
            setParentProperty("ruleType", json);
            jsonStack.push(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(RuleType ruleType) throws WorkflowException {
        jsonStack.pop();
    }

    /**
     * Adds the JSON object that is currently being created an array property in the parent object if the parent
     * exists.
     *
     * @param propertyName the name of the property to use.
     * @param json the JSON object to to append to the parent property.
     * @throws JSONException if we can't append to the property.
     */
    private void appendToParentProperty(String propertyName, JSONObject json) throws JSONException {
        if (!jsonStack.isEmpty()) {
            jsonStack.peek().append(propertyName, json);
        }
    }

    /**
     * Adds the JSON object that is currently being created to the parent object if the parent exists.
     *
     * @param propertyName the name of the property to use.
     * @param json the JSON object to add to the parent.
     * @throws JSONException if the property can't be set.
     */
    private void setParentProperty(String propertyName, JSONObject json) throws JSONException {
        if (!jsonStack.isEmpty()) {
            jsonStack.peek().put(propertyName, json);
        }
    }

    /**
     * Adds the properties that are common to all workflow elements to the given JSON object.
     *
     * @param json the JSON object to add the properties to.
     * @param workflowElement the workflow elements to get the property values from.
     * @throws JSONException if one of the properties can't be set.
     */
    private void appendCommonProperties(JSONObject json, WorkflowElement workflowElement) throws JSONException {

        json.put("id", workflowElement.getId());
        json.put("name", workflowElement.getName());
        json.put("label", workflowElement.getLabel());
    }

    public void visitInputs(List<DataObject> input) throws JSONException {
    }

    public void leaveInputs(List<DataObject> input) throws JSONException {
    }

    /**
     * Creates a JSON object to use in marshalling a workflow. The first JSON object that is created is treated
     * as the overall JSON object that we're creating.
     *
     * @return the JSON object
     */
    private JSONObject createJsonObject() {
        JSONObject json = new JSONObject();
        if (cumulativeJson == null) {
            cumulativeJson = json;
        }
        return json;
    }
}
