package org.iplantc.workflow.marshaller;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyType;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.Validator;

/**
 * Converts data objects to input properties for the discovery environment UI.
 * 
 * @author Dennis Roberts
 */
public class UiInputPropertyGenerator {

    /**
     * Used to get the list of reference genomes for property types that require them.
     */
    private ReferenceGenomeHandler referenceGenomeHandler;

    /**
     * The factory used to generate data access objects.
     */
    private DaoFactory daoFactory;

    /**
     * @return the object used to get the list of reference genomes for property types that require them.
     */
    protected ReferenceGenomeHandler getReferenceGenomeHandler() {
        return referenceGenomeHandler;
    }

    /**
     * @return the factory used to generate data access objects.
     */
    protected DaoFactory getDaoFactory() {
        return daoFactory;
    }

    /**
     * @param referenceGenomeHandler the object used to get the list of reference genomes.
     * @param daoFactory the factory used to generate data access objects.
     */
    public UiInputPropertyGenerator(ReferenceGenomeHandler referenceGenomeHandler, DaoFactory daoFactory) {
        this.referenceGenomeHandler = referenceGenomeHandler;
        this.daoFactory = daoFactory;
    }

    /**
     * Converts a data object to an input property.
     * 
     * @param input the data object to convert.
     * @return the input property.
     */
    public Property generateProperty(DataObject input) {
        Property property = new Property();
        property.setName(input.getSwitchString());
        property.setVisible(true);
        property.setLabel(!StringUtils.isEmpty(input.getLabel()) ? input.getLabel() : input.getName());
        property.setId(input.getId());
        property.setDescription(input.getDescription());
        property.setValidator(generateValidator(input));
        property.setPropertyType(generatePropertyType(input));
        return property;
    }

    /**
     * Generates the property type for the given data object.
     * 
     * @param input the data object.
     * @return the property type.
     */
    protected PropertyType generatePropertyType(DataObject input) {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(input.getInfoTypeName());
        return propertyType;
    }

    /**
     * Generates the validator for the given data object.
     * 
     * @param input the data object.
     * @return the validator.
     */
    protected Validator generateValidator(DataObject input) {
        Validator validator = new Validator();
        validator.setName("");
        validator.setRequired(input.isRequired());
        validator.setRules(generateValidationRules(input));
        return validator;
    }

    /**
     * Creates validation rules for the given data object.
     * 
     * @param input the data object.
     * @return the list of validation rules.
     */
    protected List<Rule> generateValidationRules(DataObject input) {
        return new LinkedList<Rule>();
    }
}
