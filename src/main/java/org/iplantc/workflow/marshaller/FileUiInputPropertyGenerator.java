package org.iplantc.workflow.marshaller;

import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.PropertyType;

/**
 * Generates input properties for general files.
 * 
 * @author Dennis Roberts
 */
public class FileUiInputPropertyGenerator extends UiInputPropertyGenerator {

    /**
     * @param referenceGenomeHandler the object used to get the list of reference genomes.
     * @param daoFactory the factory used to generate data access objects.
     */
    public FileUiInputPropertyGenerator(ReferenceGenomeHandler referenceGenomeHandler, DaoFactory daoFactory) {
        super(referenceGenomeHandler, daoFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PropertyType generatePropertyType(DataObject input) {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(input.getMultiplicity().getTypeName());
        return propertyType;
    }
}
