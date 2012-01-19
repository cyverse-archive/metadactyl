package org.iplantc.workflow.marshaller;

import static org.junit.Assert.assertEquals;

import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.model.ContractType;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.marshaller.UiWorkflowMarshaller.
 * 
 * @author Dennis Roberts
 */
public class UiWorkflowMarshallerTest {

    /**
     * The workflow marshaller to use for each of the tests.
     */
    private TemplateMarshaller instance;

    /**
     * Initializes each of the tests.
     */
    @Before
    public void initialize() {
        instance = new TemplateMarshaller();
    }

    /**
     * Verifies that we can marshall a rule type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void Type() throws Exception {
        RuleType ruleType = createRuleType("185");
        ruleType.accept(instance);
        JSONObject expected = createRuleTypeJson("185");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can't marshall a rule with no rule type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForUntypedRule() throws Exception {
        createRule("170").accept(instance);
    }

    /**
     * Verifies that we can marshall a rule with a rule type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallTypedRule() throws Exception {
        Rule rule = createTypedRule("186", "187");
        rule.accept(instance);
        JSONObject expected = createTypedRuleJson(186, 187);
        
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a rule with arguments.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallRuleWithArguments() throws Exception {
        Rule rule = createTypedRule("171", "172");
        rule.addArgument("foo");
        rule.addArgument("bar");
        rule.accept(instance);
        JSONObject expected = createRuleJson();
        expected.remove("s");
        expected.append("s","foo");
        expected.append("s", "bar");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall an empty validator.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallEmptyValidator() throws Exception {
        Validator validator = createValidator("164", true);
        validator.accept(instance);
        JSONObject expected = createValidatorJson("164", true);
        String val = instance.getMarshalledWorkflow();
        
        assertEquals(expected.toString(), val);
    }

    /**
     * Verifies that we can marshall a validator with rules.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallValidatorWithRules() throws Exception {
        Validator validator = createValidator("172", true);
        validator.addRule(createTypedRule("173", "175"));
        validator.addRule(createTypedRule("174", "176"));
        validator.accept(instance);
        JSONObject expected = createValidatorJson("172", true);
        expected.append("rules", createRuleJson());
        expected.append("rules", createRuleJson());
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a contract type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallContractType() throws Exception {
        ContractType contractType = createContractType("175");
        contractType.accept(instance);
        String expected = createContractTypeJson("175").toString();
        assertEquals(expected, instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property without a property type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallEmptyProperty() throws Exception {
        Property property = createProperty("145", false);
        property.accept(instance);
        JSONObject expected = createPropertyJson("145", false);
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property with a property type.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallPropertyWithType() throws Exception {
        Property property = createTypedProperty("146", true, "147");
        property.accept(instance);
        JSONObject expected = createTypedPropertyJson("146", true, "147");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property with a validator.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallPropertyWithValidator() throws Exception {
        Property property = createTypedProperty("165", true, "166");
        property.setValidator(createValidator("167", false));
        property.accept(instance);
        JSONObject expected = createTypedPropertyJson("165", true, "166");
        expected.put("validator", createValidatorJson("167", false));
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property with a value.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallPropertyWithValue() throws Exception {
        Property property = createTypedProperty("168", true, "169");
        property.setDefaultValue("blarg");
        property.accept(instance);
        JSONObject expected = createTypedPropertyJson("168", true, "169");
        expected.put("value", "blarg");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property group with no properties.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallPropertyGroupWithoutProperties() throws Exception {
        PropertyGroup propertyGroup = createPropertyGroup("148");
        propertyGroup.accept(instance);
        JSONObject expected = createPropertyGroupJson("148");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a property group with properties.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallPropertygroupWithProperties() throws Exception {
        PropertyGroup propertyGroup = createPropertyGroup("149");
        propertyGroup.addProperty(createTypedProperty("150", true, "151"));
        propertyGroup.addProperty(createTypedProperty("152", false, "153"));
        propertyGroup.accept(instance);
        JSONObject expected = createPropertyGroupJson("149");
        expected.append("properties", createTypedPropertyJson("150", true, "151"));
        expected.append("properties", createTypedPropertyJson("152", false, "153"));
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall an empty template.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallEmptyTemplate() throws Exception {
        Template template = createTemplate("154");
        template.accept(instance);
        JSONObject expected = createTemplateJson("154");
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Verifies that we can marshall a template with property groups.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldMarshallTemplateWithPropertyGroups() throws Exception {
        Template template = createTemplateWithPropertyGroups();
        template.accept(instance);
        JSONObject expected = createTemplateJsonWithPropertyGroups();
        
        assertEquals(expected.toString(), instance.getMarshalledWorkflow());
    }

    /**
     * Creates the expected JSON for the template generated by createTemplateWithPropertyGroups().
     * 
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createTemplateJsonWithPropertyGroups() throws Exception {
        JSONObject templateJson = createTemplateJson("155");
        JSONObject propertyGroupJson1 = createPropertyGroupJson("156");
        propertyGroupJson1.append("properties", createTypedPropertyJson("157", false, "158"));
        propertyGroupJson1.append("properties", createTypedPropertyJson("159", true, "160"));
        templateJson.append("groups", propertyGroupJson1);
        JSONObject propertyGroupJson2 = createPropertyGroupJson("161");
        propertyGroupJson2.append("properties", createTypedPropertyJson("162", true, "163"));
        templateJson.append("groups", propertyGroupJson2);
        return templateJson;
    }

    /**
     * Creates a template with property groups to use for testing.
     * 
     * @return the template.
     */
    private Template createTemplateWithPropertyGroups() {
        Template template = createTemplate("155");
        PropertyGroup propertyGroup1 = createPropertyGroup("156");
        propertyGroup1.addProperty(createTypedProperty("157", false, "158"));
        propertyGroup1.addProperty(createTypedProperty("159", true, "160"));
        template.addPropertyGroup(propertyGroup1);
        PropertyGroup propertyGroup2 = createPropertyGroup("161");
        propertyGroup2.addProperty(createTypedProperty("162", true, "163"));
        template.addPropertyGroup(propertyGroup2);
        return template;
    }

    /**
     * Creates the expected JSON for a template that was created by createTemplate();
     * 
     * @param id the template identifier.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createTemplateJson(String id) throws Exception {
        JSONObject template = createWorkflowElementJson(id, "j", "k", "l");
        template.put("type","m");
        return template;
    }

    /**
     * Creates a template to use for testing.
     * 
     * @param id the template identifier.
     * @return the new template.
     */
    private Template createTemplate(String id) {
        Template t = new Template(id, "j", "k", "l");
        t.setType("m");
        return t;
    }

    /**
     * Creates the expected JSON for a property group created by createPropertyGroup().
     * 
     * @param id the property group identifier.
     * @return the property group.
     * @throws Exception if an error occurs.
     */
    private JSONObject createPropertyGroupJson(String id) throws Exception {
        JSONObject pgroup =  createWorkflowElementJson(id, "g", "h", "i");
        pgroup.put("type", "m");
        return pgroup;
    }

    /**
     * Creates a property group to use for testing.
     * 
     * @param id the property group identifier.
     * @return the new property group.
     */
    private PropertyGroup createPropertyGroup(String id) {
        PropertyGroup pg = new PropertyGroup(id, "g", "h", "i");
        pg.setGroupType("m");
        return pg;
    }

    /**
     * Creates the expected JSON for a property that was created by createTypedProperty().
     * 
     * @param id the identifier assigned to the property.
     * @param isVisible true if the property should be visible in the UI.
     * @param propertyTypeId the identifier assigned to the property type.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createTypedPropertyJson(String id, boolean isVisible, String propertyTypeId) throws Exception {
        JSONObject json = createPropertyJson(id, isVisible);
        json.put("type", "a");
        return json;
    }

    /**
     * Creates a property with a property type.
     * 
     * @param id the identifier to assign to the property.
     * @param isVisible true if the property should be visible in the UI.
     * @param propertyTypeId the identifier to assign to the property type.
     * @return the property.
     */
    private Property createTypedProperty(String id, boolean isVisible, String propertyTypeId) {
        Property property = createProperty(id, isVisible);
        property.setPropertyType(createPropertyType(propertyTypeId));
        return property;
    }

    /**
     * Creates the expected JSON object for a property created by createProperty().
     * 
     * @param id the identifier assigned to the property
     * @param isVisible true if the JSON object should be visible in the UI.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createPropertyJson(String id, boolean isVisible) throws Exception {
        JSONObject json = createWorkflowElementJson(id, "d", "e", "f");
        json.put("isVisible", isVisible);
        json.put("description", "f");
        return json;
    }

    /**
     * Creates a property to use for testing.
     * 
     * @param id the identifier to assign to the property.
     * @param isVisible true if the property should be visible in the UI.
     * @return the new property.
     */
    private Property createProperty(String id, boolean isVisible) {
        Property property = new Property(id, "d", "e", "f");
        property.setIsVisible(isVisible);
        return property;
    }

    /**
     * Creates a property type to use for testing.
     * 
     * @param id the identifier used for the property type.
     * @return the property type.
     */
    private PropertyType createPropertyType(String id) {
        return new PropertyType(id, "a", "b", "c");
    }

    /**
     * Creates the expected JSON for a contract generated by createContract().
     * 
     * @param id the contract identifier.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createContractJson(String id) throws Exception {
        return createWorkflowElementJson(id, "t", "u", "v");
    }

    /**
     * Creates the expected JSON for a contract generated by createTypedContract();
     * 
     * @param id the contract identifier.
     * @param contractTypeId the contract type identifier.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createTypedContractJson(String id, String contractTypeId) throws Exception {
        JSONObject json = createContractJson(id);
        json.put("contractType", createContractTypeJson(contractTypeId));
        return json;
    }

    /**
     * Creates the expected JSON for a contract type generated by creteContractType();
     * 
     * @param id the contract type identifier.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createContractTypeJson(String id) throws Exception {
        return createWorkflowElementJson(id, "q", "r", "s");
    }

    /**
     * Creates a contract type to use for testing.
     * 
     * @param id the contract type identifier.
     * @return the new contract type.
     */
    private ContractType createContractType(String id) {
        return new ContractType(id, "q", "r", "s");
    }

    /**
     * Creates the expected JSON for the validator created by createValidator().
     * 
     * @param id the identifier used for the validator.
     * @param required true if the property being validated is required.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createValidatorJson(String id, boolean required) throws Exception {
        JSONObject json = createWorkflowElementJson( id, "m", "n", "o");
        
        json.put("required", required);
        
        return json;
    }

    /**
     * Creates a validator to use for testing.
     * 
     * @param id the identifier to use for the validator.
     * @param required true if the property being validated is required.
     * @return the validator.
     */
    private Validator createValidator(String id, boolean required) {
        Validator validator = new Validator(id, "m", "n", "o");
        validator.setRequired(required);
        return validator;
    }

    /**
     * Creates the expected JSON for a rule created by createRule();
     * 
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createRuleJson() throws Exception {
    	JSONObject rule = new JSONObject();
    	rule.put("s", new JSONArray());
        return rule;
    }

    /**
     * Creates a rule for testing.
     * 
     * @param id the rule identifier.
     * @return the new rule.
     */
    private Rule createRule(String id) {
        return new Rule("id", "p", "q", "r");
    }

    /**
     * Creates the expected JSON for a rule type generated by createRuleType().
     * 
     * @param id the rule type identifier.
     * @return the new rule type.
     * @throws Exception if an error occurs.
     */
    private JSONObject createRuleTypeJson(String id) throws Exception {
        return createWorkflowElementJson(id, "s", "t", "u");
    }

    /**
     * Creates a rule type for testing.
     * 
     * @param id the rule type identifier.
     * @return the new rule type.
     */
    private RuleType createRuleType(String id) {
        return new RuleType(id, "s", "t", "u");
    }

    /**
     * Creates the expected JSON for a rule generated by createTypedRule().
     * 
     * @param id the rule identifier.
     * @param ruleTypeId the rule type identifier.
     * @return the new rule.
     * @throws Exception if an error occurs.
     */
    private JSONObject createTypedRuleJson(long id, long ruleTypeId) throws Exception {
        JSONObject json = createRuleJson();
        return json;
    }

    /**
     * Creates a typed rule for testing.
     * 
     * @param id the rule identifier.
     * @param ruleTypeId the rule type identifier.
     * @return the new rule.
     */
    private Rule createTypedRule(String id, String ruleTypeId) {
        Rule rule = createRule(id);
        rule.setRuleType(createRuleType(ruleTypeId));
        return rule;
    }

    /**
     * Creates JSON for a generic workflow element.
     * 
     * @param id the workflow element identifier.
     * @param name the name of the workflow element.
     * @param label the label to display in the UI when the element is displayed.
     * @param description a brief description of the workflow element.
     * @return the JSON object.
     * @throws Exception if an error occurs.
     */
    private JSONObject createWorkflowElementJson(String id, String name, String label, String description)
        throws Exception
    {
        JSONObject json = new JSONObject();
       
        json.put("id", id);
        json.put("name", name);
        json.put("label", label);
        return json;
    }
}
