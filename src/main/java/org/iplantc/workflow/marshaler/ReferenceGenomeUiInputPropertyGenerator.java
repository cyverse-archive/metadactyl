package org.iplantc.workflow.marshaler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.refgenomes.ReferenceGenome;

import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.util.Lambda;
import org.iplantc.workflow.util.ListUtils;

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
    public ReferenceGenomeUiInputPropertyGenerator(DaoFactory daoFactory)
    {
        super(daoFactory);
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
        return ListUtils.map(new Lambda<ReferenceGenome, String>() {
            @Override
            public String call(ReferenceGenome arg) {
                JSONObject json = new JSONObject();
                json.put("name", arg.getUuid());
                json.put("value", arg.getUuid());
                json.put("isDefault", "false");
                json.put("display", arg.getName());
                return json.toString();
            }
        }, getDaoFactory().getReferenceGenomeDao().list());
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
