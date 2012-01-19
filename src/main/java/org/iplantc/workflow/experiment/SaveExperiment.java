package org.iplantc.workflow.experiment;

import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Template;

public class SaveExperiment {

    public JSONObject saveExperiment(Template template, String json_string) throws Exception {

        JSONObject template_data = (JSONObject) JSONSerializer.toJSON(json_string);

        JSONObject delta = new JSONObject();

        delta.put("name", template_data.getString("name"));
        delta.put("description", template_data.getString("description"));
        delta.put("template_id", template_data.getString("template_id"));

        JSONObject delta_config = new JSONObject();

        JSONObject config = template_data.getJSONObject("config");

        List<DataObject> inputs = template.getInputs();

        for (int i = 0; i < inputs.size(); i++) {
            DataObject input = inputs.get(i);

            if (config.has(input.getName())) {

                delta_config.put(input.getName(), config.get(input.getName()));
                config.remove(input.getName());

            }

        }

        List<DataObject> outputs = template.getOutputs();

        for (int i = 0; i < outputs.size(); i++) {
            DataObject output = outputs.get(i);

            if (config.containsKey(output.getName())) {
                delta_config.put(output.getName(), config.get(output.getName()));
            }

        }

        List<PropertyGroup> groups = template.getPropertyGroups();

        for (int i = 0; i < groups.size(); i++) {

            List<Property> props = groups.get(i).getProperties();

            for (int j = 0; j < props.size(); j++) {

                Property property = props.get(j);

                if (property.getValidator() != null && property.getValidator().isRequired()
                    && !config.has(property.getName()))
                {
                    throw new Exception("The property " + property.getName()
                        + " is required, but it wasn't specified in the experiment information");
                }

                if (!property.getName().trim().equals("")
                    && !config.getString(property.getName()).equals(property.getDefaultValue()))
                {

                    delta_config.put(property.getName(), config.getString(property.getName()));
                }
                config.remove(property.getName());
            }

        }

        delta.put("config", delta_config);

        return delta;
    }

}
