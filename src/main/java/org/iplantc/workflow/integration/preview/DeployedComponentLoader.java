package org.iplantc.workflow.integration.preview;

import org.iplantc.persistence.dto.components.DeployedComponent;
import org.iplantc.workflow.integration.json.TitoDeployedComponentUnmarshaller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Loads deployed components that are described by JSON objects into memory.
 * 
 * @author Dennis Roberts
 */
public class DeployedComponentLoader extends ObjectLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadObject(JSONObject json) throws JSONException {
        TitoDeployedComponentUnmarshaller unmarshaller = new TitoDeployedComponentUnmarshaller();
        if (json != null) {
            loadDeployedComponent(unmarshaller, json);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadObjectList(JSONArray array) throws JSONException {
        TitoDeployedComponentUnmarshaller unmarshaller = new TitoDeployedComponentUnmarshaller();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                loadDeployedComponent(unmarshaller, json);
            }
        }
    }

    /**
     * Loads a deployed component from a JSON object using the given unmarshaller.
     * 
     * @param unmarshaller used to convert the JSON object to a deployed component.
     * @param json the JSON object.
     */
    private void loadDeployedComponent(TitoDeployedComponentUnmarshaller unmarshaller, JSONObject json)
            throws JSONException {
        DeployedComponent deployedComponent = unmarshaller.fromJson(json);
        registerByName(DeployedComponent.class, deployedComponent.getName(), deployedComponent);
        registerById(DeployedComponent.class, deployedComponent.getId(), deployedComponent);
    }
}
