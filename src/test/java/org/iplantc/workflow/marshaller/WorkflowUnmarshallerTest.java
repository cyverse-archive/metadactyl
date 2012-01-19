package org.iplantc.workflow.marshaller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.model.ContractType;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.marshaller.WorkflowUnmarshaller.
 * 
 * @author Dennis Roberts
 */
public class WorkflowUnmarshallerTest {

    /**
     * The unmarshaller to use in each of the tests.
     */
    private TemplateUnmarshaller unmarshaller;

    /**
     * Initializes each test.
     */
    @Before
    public void initialize() {
        unmarshaller = new TemplateUnmarshaller();
    }

    /**
     * Verifies that we can unmarshall a complete rule type (that is, a rule type that has all of its attributes
     * populated) from a JSON object.
     */
    @Test
    public void shouldUnmarshallCompleteRuleTypeFromJson() {
        RuleType expected = new RuleType("1", "a", "b", "c");
        RuleType actual = unmarshaller.unmarshallRuleType(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a complete rule type (that is, a rule type that has all of its attributes
     * populated) from a string.
     */
    @Test
    public void shouldUnmarshallCompleteRuleTypeFromString() {
        RuleType expected = new RuleType("1", "a", "b", "c");
        RuleType actual = unmarshaller.unmarshallRuleType(createCommonJson("1", "a", "b", "c").toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a rule type with only a name.
     */
    @Test
    public void shouldUnmarshallEmptyRuleType() {
        RuleType expected = new RuleType("0", "name", "", "");
        RuleType actual = unmarshaller.unmarshallRuleType(createCommonJson("0", "name", "", ""));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception for a rule type with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousRuleType() {
        unmarshaller.unmarshallRuleType(createCommonJson("1", null, "label", "description"));
    }

    /**
     * Verifies that we get an exception for a bogus rule type JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusRuleTypeJson() {
        unmarshaller.unmarshallRuleType("bogus json");
    }

    /**
     * Verifies that we can unmarshall a rule with no rule type from a string.
     */
    @Test
    public void shouldUnmarshallUntypedRule() {
        Rule expected = new Rule("1", "a", "b", "c");
        Rule actual = unmarshaller.unmarshallRule(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a rule with only a name.
     */
    @Test
    public void shouldUnmarshallEmptyRule() {
        Rule expected = new Rule("0", "name", "", "");
        Rule actual = unmarshaller.unmarshallRule(createCommonJson("0", "name", "", ""));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception for a rule with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousRule() {
        unmarshaller.unmarshallRule(createCommonJson("1", null, "label", "description"));
    }

    /**
     * Verifies that we get an exception for a bogus rule JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusRuleJson() {
        unmarshaller.unmarshallRule("bogus json");
    }

    /**
     * Verifies that we can unmarshall a rule with no rule type from a string.
     */
    @Test
    public void shouldUnmarshallUntypedRuleFromString() {
        Rule expected = new Rule("1", "a", "b", "c");
        Rule actual = unmarshaller.unmarshallRule(createCommonJson("1", "a", "b", "c").toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a rule with a rule type.
     */
    @Test
    public void shouldUnmarshallTypedRule() {
        Rule expected = new Rule("1", "a", "b", "c");
        expected.setRuleType(new RuleType("2", "b", "c", "d"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("ruleType", createCommonJson("2", "b", "c", "d"));
        Rule actual = unmarshaller.unmarshallRule(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if the JSON associated with the rule type does not represent an object.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectArrayRuleType() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.accumulate("ruleType", createCommonJson("2", "b", "c", "d"));
        json.accumulate("ruleType", createCommonJson("3", "d", "e", "f"));
        unmarshaller.unmarshallRule(json);
    }

    /**
     * Verifies that we can unmarshall a rule with arguments.
     */
    @Test
    public void shouldUnmarshallRuleWithArgs() {
        Rule expected = new Rule("1", "a", "b", "c");
        expected.addArgument("foo");
        expected.addArgument("bar");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray args = new JSONArray();
        args.addAll(Arrays.asList("foo", "bar"));
        json.put("arguments", args);
        Rule actual = unmarshaller.unmarshallRule(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if the JSON associated with the arguments does not represent an array.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectScalarArgList() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("arguments", "foo");
        unmarshaller.unmarshallRule(json);
    }

    /**
     * Verifies that we can unmarshall a validator with no rules.
     */
    @Test
    public void shouldUnmarshallValidatorWithNoRules() {
        Validator expected = new Validator("1", "a", "b", "c");
        Validator actual = unmarshaller.unmarshallValidator(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a validator from a string.
     */
    @Test
    public void shouldUnmarshallValidatorFromString() {
        Validator expected = new Validator("1", "a", "b", "c");
        Validator actual = unmarshaller.unmarshallValidator(createCommonJson("1", "a", "b", "c").toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall an empty validator.
     */
    @Test
    public void shouldUnmarshallEmptyValidator() {
        Validator expected = new Validator("0", "name", "", "");
        Validator actual = unmarshaller.unmarshallValidator(createCommonJson("0", "name", "", ""));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a validator with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousValidator() {
        unmarshaller.unmarshallValidator(createCommonJson("1", null, "b", "c"));
    }

    /**
     * Verifies that we get an exception for a bogus validator JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusValidatorJson() {
        unmarshaller.unmarshallValidator("bogus json");
    }

    /**
     * Verifies that we can unmarshall a validator with rules.
     */
    @Test
    public void shouldUnmarshallValidatorWithRules() {
        Validator expected = new Validator("1", "a", "b", "c");
        expected.addRule(new Rule("2", "b", "c", "d"));
        expected.addRule(new Rule("3", "c", "d", "e"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray rulesJson = new JSONArray();
        rulesJson.add(createCommonJson("2", "b", "c", "d"));
        rulesJson.add(createCommonJson("3", "c", "d", "e"));
        json.put("rules", rulesJson);
        Validator actual = unmarshaller.unmarshallValidator(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a validator with a rule list that does not represent
     * an array.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectScalarRuleList() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("rules", new Rule("2", "b", "c", "d"));
        unmarshaller.unmarshallValidator(json);
    }

    /**
     * Verifies that we can unmarshall a property type.
     */
    @Test
    public void shouldUnmarshallPropertyType() {
        PropertyType expected = new PropertyType("1", "a", "b", "c");
        PropertyType actual = unmarshaller.unmarshallPropertyType(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a property type from a string.
     */
    @Test
    public void shouldUnmarshallPropertyTypeFromString() {
        PropertyType expected = new PropertyType("1", "a", "b", "c");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        PropertyType actual = unmarshaller.unmarshallPropertyType(json.toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property type with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousPropertyType() {
        unmarshaller.unmarshallPropertyType(createCommonJson("1", null, "b", "c"));
    }

    /**
     * Verifies that we get an exception for a bogus property type JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusPropertyTypeJson() {
        unmarshaller.unmarshallPropertyType("bogus json");
    }

    /**
     * Verifies that we can unmarshall a property with only the common attributes populated.
     */
    @Test
    public void shouldUnmarshallProperty() {
        Property expected = new Property("1", "a", "b", "c");
        Property actual = unmarshaller.unmarshallProperty(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a property with only the common attributes populated from a string.
     */
    @Test
    public void shouldUnmarshallPropertyFromString() {
        Property expected = new Property("1", "a", "b", "c");
        Property actual = unmarshaller.unmarshallProperty(createCommonJson("1", "a", "b", "c").toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousProperty() {
        unmarshaller.unmarshallProperty(createCommonJson("1", null, "b", "c"));
    }

    /**
     * Verifies that we get an exception for a bogus property JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusPropertyJson() {
        unmarshaller.unmarshallProperty("bogus json");
    }

    /**
     * Verifies that we can unmarshall a property with a property type.
     */
    @Test
    public void shouldUnmarshallTypedProperty() {
        Property expected = new Property("1", "a", "b", "c");
        expected.setPropertyType(new PropertyType("2", "b", "c", "d"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("propertyType", createCommonJson("2", "b", "c", "d"));
        Property actual = unmarshaller.unmarshallProperty(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property from JSON in which the propertyType
     * attribute appears to be an array.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectArrayPropertyType() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray propertyTypeJson = new JSONArray();
        propertyTypeJson.add(createCommonJson("2", "b", "c", "d"));
        propertyTypeJson.add(createCommonJson("3", "c", "d", "e"));
        json.put("propertyType", propertyTypeJson);
        unmarshaller.unmarshallProperty(json);
    }

    /**
     * Verifies that we can unmarshall a property with a validator.
     */
    @Test
    public void shouldUnmarshallPropertyWithValidator() {
        Property expected = new Property("1", "a", "b", "c");
        expected.setValidator(new Validator("2", "b", "c", "d"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("validator", createCommonJson("2", "b", "c", "d"));
        Property actual = unmarshaller.unmarshallProperty(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property from JSON in which the validator attribute
     * appears to be an array.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectArrayValidator() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray validatorJson = new JSONArray();
        validatorJson.add(createCommonJson("2", "b", "c", "d"));
        validatorJson.add(createCommonJson("3", "c", "d", "e"));
        json.put("validator", validatorJson);
        unmarshaller.unmarshallProperty(json);
    }

    /**
     * Verifies that we can unmarshall a property that has its isVisible property specified.
     */
    @Test
    public void shouldUnmarshallPropertyWithSpecifiedPropertyType() {
        Property expected = new Property("1", "a", "b", "c");
        expected.setIsVisible(true);
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("isVisible", true);
        Property actual = unmarshaller.unmarshallProperty(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property from JSON in which the isVisible flag
     * doesn't represent a Boolean value.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectArrayVisibilityFlag() {
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("isVisible", 1);
        unmarshaller.unmarshallProperty(json);
    }

    /**
     * Verifies that we can unmarshall a property with a default value.
     */
    @Test
    public void shouldUnmarshallPropertyWithDefaultValue() {
        Property expected = new Property("1", "a", "b", "c");
        expected.setDefaultValue("new default value");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("defaultValue", "new default value");
        Property actual = unmarshaller.unmarshallProperty(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a contract type.
     */
    @Test
    public void shouldUnmarshallContractType() {
        ContractType expected = new ContractType("1", "a", "b", "c");
        ContractType actual = unmarshaller.unmarshallContractType(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a contract type from a string.
     */
    @Test
    public void shouldUnmarshallContractTypeFromString() {
        ContractType expected = new ContractType("1", "a", "b", "c");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        ContractType actual = unmarshaller.unmarshallContractType(json.toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception when we try to unmarshall a contract type without a name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousContractType() {
        JSONObject json = createCommonJson("1", null, "b", "c");
        unmarshaller.unmarshallContractType(json);
    }

    /**
     * Verifies that we get an exception for a bogus contract type JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusContractTypeJson() {
        unmarshaller.unmarshallContractType("bogus json");
    }

    /**
     * Verifies that we can unmarshall a property group with only the common properties populated.
     */
    @Test
    public void shouldUnmarshallPropertyGroup() {
        PropertyGroup expected = new PropertyGroup("1", "a", "b", "c");
        PropertyGroup actual = unmarshaller.unmarshallPropertyGroup(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a property group from a string.
     */
    @Test
    public void shouldUnmarshallPropertyGroupFromString() {
        PropertyGroup expected = new PropertyGroup("1", "a", "b", "c");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        PropertyGroup actual = unmarshaller.unmarshallPropertyGroup(json.toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property group with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousPropertyGroup() {
        unmarshaller.unmarshallPropertyGroup(createCommonJson("1", null, "b", "c"));
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a property group from a bogus JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusPropertyGroupJson() {
        unmarshaller.unmarshallPropertyGroup("bogus json");
    }

    /**
     * Verifies that we can unmarshall a property group with properties.
     */
    @Test
    public void shouldUnmarshallPropertyGroupWithProperties() {
        PropertyGroup expected = new PropertyGroup("1", "a", "b", "c");
        expected.addProperty(new Property("2", "b", "c", "d"));
        expected.addProperty(new Property("3", "c", "d", "e"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray propertiesJson = new JSONArray();
        propertiesJson.add(createCommonJson("2", "b", "c", "d"));
        propertiesJson.add(createCommonJson("3", "c", "d", "e"));
        json.put("properties", propertiesJson);
        PropertyGroup actual = unmarshaller.unmarshallPropertyGroup(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a property group with a group type.
     */
    @Test
    public void shouldUnmarshallTypedPropertyGroup() {
        PropertyGroup expected = new PropertyGroup("1", "a", "b", "c");
        expected.setGroupType("someArbitraryGroupType");
        JSONObject json = createCommonJson("1", "a", "b", "c");
        json.put("groupType", "someArbitraryGroupType");
        PropertyGroup actual = unmarshaller.unmarshallPropertyGroup(json);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a template with only common properties populated.
     */
    @Test
    public void shouldUnmarshallTemplate() {
        Template expected = new Template("1", "a", "b", "c");
        Template actual = unmarshaller.unmarshallTemplate(createCommonJson("1", "a", "b", "c"));
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we can unmarshall a template from a string.
     */
    @Test
    public void shouldUnmarshallTemplateFromString() {
        Template expected = new Template("1", "a", "b", "c");
        Template actual = unmarshaller.unmarshallTemplate(createCommonJson("1", "a", "b", "c").toString());
        assertEquals(expected, actual);
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a template with no name.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectAnonymousTemplate() {
        unmarshaller.unmarshallTemplate(createCommonJson("1", null, "b", "c"));
    }

    /**
     * Verifies that we get an exception if we try to unmarshall a template from a bogus JSON string.
     */
    @Test(expected = WorkflowException.class)
    public void shouldRejectBogusTemplateJsonString() {
        unmarshaller.unmarshallTemplate("bogus json");
    }

    /**
     * Verifies that we can unmarshall a template with property groups.
     */
    @Test
    public void shouldUnmarshallTemplateWithPropertyGroups() {
        Template expected = new Template("1", "a", "b", "c");
        expected.addPropertyGroup(new PropertyGroup("2", "b", "c", "d"));
        expected.addPropertyGroup(new PropertyGroup("3", "c", "d", "e"));
        JSONObject json = createCommonJson("1", "a", "b", "c");
        JSONArray propertyGroupsJson = new JSONArray();
        propertyGroupsJson.add(createCommonJson("2", "b", "c", "d"));
        propertyGroupsJson.add(createCommonJson("3", "c", "d", "e"));
        json.put("propertyGroups", propertyGroupsJson);
        Template actual = unmarshaller.unmarshallTemplate(json);
        assertEquals(expected, actual);
    }

    /**
     * Creates a JSON object representing any type of workflow element.
     * 
     * @param id the rule type identifier.
     * @param name name of the rule type.
     * @param label the label to display in the UI.
     * @param description the description to display in tool-tip text.
     * @return the JSON object.
     */
    private JSONObject createCommonJson(String id, String name, String label, String description) {
        JSONObject json = new JSONObject();
        putIfNotNull(json, "id", id);
        putIfNotNull(json, "name", name);
        putIfNotNull(json, "label", label);
        putIfNotNull(json, "description", description);
        return json;
    }

    /**
     * Adds a value to a JSON object only if that value is not null.
     * 
     * @param json the JSON object to put the value in.
     * @param key the key used to identify the value.
     * @param value the value to put in the JSON object.
     */
    private void putIfNotNull(JSONObject json, String key, Object value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
