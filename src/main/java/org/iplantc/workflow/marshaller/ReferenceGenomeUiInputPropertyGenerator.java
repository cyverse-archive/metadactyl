package org.iplantc.workflow.marshaller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;

/**
 * Generates input properties for reference genomes, sequences and annotations.
 * 
 * @author Dennis Roberts
 */
public class ReferenceGenomeUiInputPropertyGenerator extends UiInputPropertyGenerator {

    /**
     * @param referenceGenomeHandler the object used to get the list of reference genomes.
     * @param daoFactory the factory used to generate data access objects.
     */
    public ReferenceGenomeUiInputPropertyGenerator(ReferenceGenomeHandler referenceGenomeHandler, DaoFactory daoFactory)
    {
        super(referenceGenomeHandler, daoFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PropertyType generatePropertyType(DataObject input) {
        PropertyType propertyType = new PropertyType();
        propertyType.setName("ValueSelection");
        return propertyType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Rule> generateValidationRules(DataObject input) {
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(createReferenceGenomeValidationRule());
        return rules;
    }

    /**
     * Creates the validation rule for reference genomes.
     * 
     * @return the validation rule.
     */
    private Rule createReferenceGenomeValidationRule() {
        Rule rule = new Rule();
        rule.setName("referenceGenomeValidationRule");
        rule.setId(UUID.randomUUID().toString());
        rule.setLabel("Reference Genome Validation Rule");
        rule.setDescription("generated rule to validate reference genomes");
        rule.setRuleType(getRuleType("MustContain"));
        rule.setArguments(buildValidationRuleArgumentList());
        return rule;
    }

    /**
     * Builds the argument list for the reference genome property.
     * 
     * @return the argument list.
     */
    private List<String> buildValidationRuleArgumentList() {
        List<String> result = new ArrayList<String>();
        for (String name : getReferenceGenomeHandler().getRefrenceGenomeNames()) {
            result.add(buildOneValidationRuleArgument(name));
        }
        return result;
    }

    /**
     * Builds a single validation rule argument for the reference genome.
     * 
     * @param name the name of the reference genome.
     * @return the validation rule argument, which is a string representation of a JSON object.
     */
    private String buildOneValidationRuleArgument(String name) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("value", name);
        json.put("isDefault", "false");
        json.put("display", name);
        return json.toString();
    }

    /**
     * Gets the rule type with the given name.
     * 
     * @param name the name of the rule type.
     * @return the rule type.
     */
    private RuleType getRuleType(String name) {
        RuleType ruleType = getDaoFactory().getRuleTypeDao().findUniqueInstanceByName(name);
        if (ruleType == null) {
            throw new RuntimeException("missing required rule type: " + name);
        }
        return ruleType;
    }
}
