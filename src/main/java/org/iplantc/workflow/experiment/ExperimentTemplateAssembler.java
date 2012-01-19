package org.iplantc.workflow.experiment;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Template;
import org.iplantc.persistence.dto.transformation.Transformation;

public class ExperimentTemplateAssembler {
	public Template AssembleExperimentTemplate(Template template, Transformation transformation) {
        Map<String, String> propertyValues = transformation.getPropertyValues();
        List<PropertyGroup> groups = new LinkedList<PropertyGroup>();

        for(int i=0; i < groups.size(); i++){
            PropertyGroup group = groups.get(i);
            List<Property> properties = group.getProperties();

            for(int j=0; j < properties.size();j++){

                Property property = properties.get(j);
                
                if(propertyValues.containsKey(property.getName())){
                    property.setDefaultValue(propertyValues.get(property.getName()));
                }
            }
        }

        return template;
    }
}
