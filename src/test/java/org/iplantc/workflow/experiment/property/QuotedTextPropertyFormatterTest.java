package org.iplantc.workflow.experiment.property;

import static org.junit.Assert.assertNull;

import net.sf.json.JSONObject;

import org.iplantc.workflow.model.Property;
import org.junit.Test;

/**
 * Unit tests for QuotedText properties.
 * 
 * @author Dennis Roberts
 */
public class QuotedTextPropertyFormatterTest extends BasePropertyFormatterTester {

    /**
     * Verifies that we can format a property with a default value.
     */
    @Test
    public void testPropertyWithDefaultValue() {
        JSONObject config = createConfig();
        Property property = createProperty("property value", 1, "QuotedText");
        QuotedTextPropertyFormatter formatter = new QuotedTextPropertyFormatter(config, createStep(), property,
            createPropertyValueMap());
        JSONObject formattedProperty = formatter.formatProperty();
        assertFormattedPropertyValid(formattedProperty, "id", "name", 1, " \"property value\"");
    }

    /**
     * Verifies that we can format a property with a specified value.
     */
    @Test
    public void testPropertyWithSpecifiedValue() {
        JSONObject config = createConfig("specified value");
        Property property = createProperty("property value", 1, "QuotedText");
        QuotedTextPropertyFormatter formatter = new QuotedTextPropertyFormatter(config, createStep(), property,
            createPropertyValueMap());
        JSONObject formattedProperty = formatter.formatProperty();
        assertFormattedPropertyValid(formattedProperty, "id", "name", 1, " \"specified value\"");
    }

    /**
     * Verifies that a property with a blank value is formatted if its omit-if-blank setting is disabled.
     */
    @Test
    public void testBlankPropertyWithOmitIfBlankSettingDisabled() {
        JSONObject config = createConfig();
        Property property = createProperty("", 1, "QuotedText", false);
        QuotedTextPropertyFormatter formatter = new QuotedTextPropertyFormatter(config, createStep(), property,
            createPropertyValueMap());
        JSONObject formattedProperty = formatter.formatProperty();
        assertFormattedPropertyValid(formattedProperty, "id", "name", 1, " \"\"");
    }

    /**
     * Verifies that a property with a blank value is not formatted if its omit-if-blank setting is enabled.
     */
    @Test
    public void testBlankPropertyWithOmitIfBlankSettingEnabled() {
        JSONObject config = createConfig();
        Property property = createProperty("", 1, "QuotedText", true);
        QuotedTextPropertyFormatter formatter = new QuotedTextPropertyFormatter(config, createStep(), property,
            createPropertyValueMap());
        assertNull(formatter.formatProperty());
    }
}
