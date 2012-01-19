package org.iplantc.workflow.marshaller;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.model.ContractType;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;
import org.iplantc.workflow.model.WorkflowElement;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Converts a JSON object or a JSON string to a hierarchy of workflow elements. This class has to have intimate
 * knowledge of the workflow model in order to do its job. So far, no attempt has been made to find a general
 * way to unmarshall workflows in various formats because unmarshalling a workflow is something that we shouldn't
 * have to do very often, and we should only have to unmarshall workflows in one format. If this changes at some
 * point in the future, then this class will have to be generalized in some way.
 * 
 * @author Dennis Roberts
 */
public class TemplateUnmarshaller {

    /**
     * Unmarshalls a rule type from a JSON string.
     * 
     * @param jsonString the JSON string representing the rule type.
     * @return the rule type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public RuleType unmarshallRuleType(String jsonString) {
        try {
            return unmarshallRuleType(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the rule type", e);
        }
    }

    /**
     * Unmarshalls a rule type from a JSON object.
     * 
     * @param json the JSON object representing the rule type.
     * @return the rule type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public RuleType unmarshallRuleType(JSONObject json) {
        try {
            RuleType ruleType = new RuleType();
            populateCommonAttributes(ruleType, json);
            return ruleType;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the rule type", e);
        }
    }

    /**
     * Unmarshalls a rule from a JSON string.
     * 
     * @param jsonString the JSON string representing the rule.
     * @return the rule.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Rule unmarshallRule(String jsonString) {
        try {
            return unmarshallRule(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the rule", e);
        }
    }

    /**
     * Unmarshalls a rule from a JSON object.
     * 
     * @param json the JSON object representing the rule.
     * @return the rule.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Rule unmarshallRule(JSONObject json) {
        try {
            Rule rule = new Rule();
            populateCommonAttributes(rule, json);
            if (json.has("ruleType")) {
                rule.setRuleType(unmarshallRuleType(json.getJSONObject("ruleType")));
            }
            if (json.has("arguments")) {
                rule.setArguments(unmarshallStringList(json.getJSONArray("arguments")));
            }
            return rule;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the rule", e);
        }
    }

    /**
     * Converts a JSON array representing a list of strings to a list of strings.
     * 
     * @param jsonArray the JSON array to convert.
     * @return the list of strings represented by the JSON array.
     */
    private List<String> unmarshallStringList(JSONArray jsonArray) {
        List<String> result = new LinkedList<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.add(jsonArray.getString(i));
        }
        return result;
    }

    /**
     * Unmarshalls a validator from a JSON string.
     * 
     * @param jsonString the string representing the validator.
     * @return the validator.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Validator unmarshallValidator(String jsonString) {
        try {
            return unmarshallValidator(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the validator", e);
        }
    }

    /**
     * Unmarshalls a validator from a JSON object.
     * 
     * @param json the JSON object representing the validator.
     * @return the validator.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Validator unmarshallValidator(JSONObject json) {
        try {
            Validator validator = new Validator();
            populateCommonAttributes(validator, json);
            if (json.has("rules")) {
                validator.setRules(unmarshallRuleList(json.getJSONArray("rules")));
            }
            return validator;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the validator", e);
        }
    }

    /**
     * Converts a JSON array to a list of rules.
     * 
     * @param jsonArray the JSON array representing the list of rules.
     * @return the list of rules.
     */
    private List<Rule> unmarshallRuleList(JSONArray jsonArray) {
        List<Rule> result = new LinkedList<Rule>();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.add(unmarshallRule(jsonArray.getJSONObject(i)));
        }
        return result;
    }

    /**
     * Unmarshalls a property type from a JSON string.
     * 
     * @param jsonString the JSON string representing the property type.
     * @return the property type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public PropertyType unmarshallPropertyType(String jsonString) {
        try {
            return unmarshallPropertyType(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property type", e);
        }
    }

    /**
     * Unmarshalls a property type from a JSON object.
     * 
     * @param json the JSON object representing the property type.
     * @return the property type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public PropertyType unmarshallPropertyType(JSONObject json) {
        try {
            PropertyType propertyType = new PropertyType();
            populateCommonAttributes(propertyType, json);
            return propertyType;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property type", e);
        }
    }

    /**
     * Unmarshalls a property from a JSON string.
     * 
     * @param jsonString the JSON string representing the property.
     * @return the property.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Property unmarshallProperty(String jsonString) {
        try {
            return unmarshallProperty(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property", e);
        }
    }

    /**
     * Unmarshalls a property from a JSON object.
     * 
     * @param json the JSON object representing the property.
     * @return the property.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Property unmarshallProperty(JSONObject json) {
        try {
            Property property = new Property();
            populateCommonAttributes(property, json);
            if (json.has("propertyType")) {
                property.setPropertyType(unmarshallPropertyType(json.getJSONObject("propertyType")));
            }
            if (json.has("validator")) {
                property.setValidator(unmarshallValidator(json.getJSONObject("validator")));
            }
            if (json.has("isVisible")) {
                property.setIsVisible(json.getBoolean("isVisible"));
            }
            if (json.has("defaultValue")) {
                property.setDefaultValue(json.getString("defaultValue"));
            }
            return property;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property", e);
        }
    }

    /**
     * Unmarshalls a contract type from a JSON string.
     * 
     * @param jsonString the JSON string representing the contract type.
     * @return the contract type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public ContractType unmarshallContractType(String jsonString) {
        try {
            return unmarshallContractType(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the contract type", e);
        }
    }

    /**
     * Unmarshalls a contract type from a JSON object.
     * 
     * @param json the JSON object representing the contract type.
     * @return the contract type.
     * @throws WorkflowException if the JSON is invalid.
     */
    public ContractType unmarshallContractType(JSONObject json) {
        try {
            ContractType contractType = new ContractType();
            populateCommonAttributes(contractType, json);
            return contractType;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the contract type", e);
        }
    }

    /**
     * Converts a JSON object representing a list of properties to a list of properties.
     * 
     * @param jsonArray the JSON array representing the list of properties.
     * @return the list of properties.
     */
    private List<Property> unmarshallPropertyList(JSONArray jsonArray) {
        List<Property> result = new LinkedList<Property>();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.add(unmarshallProperty(jsonArray.getJSONObject(i)));
        }
        return result;
    }

    /**
     * Unmarshalls a property group from a JSON string.
     * 
     * @param jsonString the JSON string representing the property group.
     * @return the property group.
     * @throws WorkflowException if the JSON is invalid.
     */
    public PropertyGroup unmarshallPropertyGroup(String jsonString) {
        try {
            return unmarshallPropertyGroup(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property group", e);
        }
    }

    /**
     * Unmarshalls a property group from a JSON object.
     * 
     * @param json the JSON object representing the property group.
     * @return the property group.
     * @throws WorkflowException if the JSON is invalid.
     */
    public PropertyGroup unmarshallPropertyGroup(JSONObject json) {
        try {
            PropertyGroup propertyGroup = new PropertyGroup();
            populateCommonAttributes(propertyGroup, json);
            if (json.has("properties")) {
                propertyGroup.setProperties(unmarshallPropertyList(json.getJSONArray("properties")));
            }
            propertyGroup.setGroupType(json.optString("groupType", null));
            return propertyGroup;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the property group", e);
        }
    }

    /**
     * Unmarshalls a template from a JSON string.
     * 
     * @param jsonString the string representing the template.
     * @return the template.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Template unmarshallTemplate(String jsonString) {
        try {
            return unmarshallTemplate(JSONObject.fromObject(jsonString));
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the template", e);
        }
    }

    /**
     * Unmarshalls a template from a JSON object.
     * 
     * @param json the JSON object representing the template.
     * @return the template.
     * @throws WorkflowException if the JSON is invalid.
     */
    public Template unmarshallTemplate(JSONObject json) {
        try {
            Template template = new Template();
            populateCommonAttributes(template, json);
            if (json.has("propertyGroups")) {
                template.setPropertyGroups(unmarshallPropertyGroupList(json.getJSONArray("propertyGroups")));
            }
            return template;
        }
        catch (JSONException e) {
            throw new WorkflowException("unable to unmarshall the template", e);
        }
    }

    /**
     * Converts a JSON array representing a list of templates to a list of templates.
     * 
     * @param jsonArray the JSON array to convert.
     * @return the list of templates.
     */
    private List<Template> unmarshallTemplateList(JSONArray jsonArray) {
        List<Template> result = new LinkedList<Template>();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.add(unmarshallTemplate(jsonArray.getJSONObject(i)));
        }
        return result;
    }

    /**
     * Converts a JSON array representing a list of property groups to a list of property groups.
     * 
     * @param jsonArray the JSON array to convert.
     * @return the list of property groups.
     */
    private List<PropertyGroup> unmarshallPropertyGroupList(JSONArray jsonArray) {
        List<PropertyGroup> result = new LinkedList<PropertyGroup>();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.add(unmarshallPropertyGroup(jsonArray.getJSONObject(i)));
        }
        return result;
    }

    /**
     * Extracts the values of the attributes that are common to all workflow elements from the given JSON object and
     * stores them in the given workflow element. The following attributes are extracted:
     * <p>
     * <table border="1">
     * <thead>
     * <tr>
     * <th>Attribute</th>
     * <th>Description</th>
     * <th>Required</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>id</td>
     * <td>The workflow element identifier.</td>
     * <td>No</td>
     * </tr>
     * <tr>
     * <td>name</td>
     * <td>A unique name assigned to the workflow element.</td>
     * <td>Yes</td>
     * </tr>
     * <tr>
     * <td>label</td>
     * <td>A label used to identify the workflow element.</td>
     * <td>No</td>
     * </tr>
     * <tr>
     * <td>description</td>
     * <td>A brief description of the workflow element.</td>
     * <td>No</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * 
     * @param workflowElement the workflow element to populate.
     * @param json the JSON object to extract the attributes from.
     */
    private void populateCommonAttributes(WorkflowElement workflowElement, JSONObject json) {
        workflowElement.setId(json.getString("id"));
        workflowElement.setName(json.getString("name"));
        workflowElement.setLabel(json.getString("label"));
        workflowElement.setDescription(json.getString("description"));
    }
}
