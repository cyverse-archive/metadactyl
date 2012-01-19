package org.iplantc.workflow.integration.json;

import java.util.Set;
import org.iplantc.persistence.dto.components.DeployedComponent;
import org.iplantc.persistence.dto.data.DeployedComponentDataFile;
import org.iplantc.workflow.integration.util.ImportUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to convert JSON documents describing deployed components to Deployed Components.
 * 
 * @author Dennis Roberts
 */
public class TitoDeployedComponentUnmarshaller extends AbstractTitoDataFileUnmarshaller<DeployedComponentDataFile>
        implements TitoUnmarshaller<DeployedComponent> {

    private TitoIntegrationDatumMashaller integrationDatumUnmarshaller;

    public TitoDeployedComponentUnmarshaller() {
        this.integrationDatumUnmarshaller = new TitoIntegrationDatumMashaller();
    }

    /**
     * Creates a new deployed component from the given JSON object.
     * 
     * @param json the JSON object describing the deployed component.
     * @return the deployed component.
     * @throws JSONException if the JSON object is missing a required attribute.
     */
    @Override
    public DeployedComponent fromJson(JSONObject json) throws JSONException {
        DeployedComponent deployedComponent = new DeployedComponent();
        deployedComponent.setId(ImportUtils.getId(json, "id", "c"));
        deployedComponent.setName(json.getString("name"));
        deployedComponent.setType(json.getString("type"));
        deployedComponent.setLocation(json.getString("location"));
        deployedComponent.setDescription(json.optString("description", null));
        deployedComponent.setVersion(json.optString("version", null));
        deployedComponent.setAttribution(json.optString("attribution", null));

        deployedComponent.setIntegrationDatum(integrationDatumUnmarshaller.fromJson(json));
        deployedComponent.setDeployedComponentDataFiles(unmarshallDataFiles(json));

        return deployedComponent;
    }

    @Override
    protected void unmarshallDataFileList(JSONArray jsonFiles, boolean input, Set<DeployedComponentDataFile> files)
            throws JSONException {
        for (int i = 0; i < jsonFiles.length(); i++) {
            DeployedComponentDataFile dataFile = new DeployedComponentDataFile();

            dataFile.setInputFile(input);
            dataFile.setFilename(jsonFiles.getString(i));
            files.add(dataFile);
        }
    }
}
