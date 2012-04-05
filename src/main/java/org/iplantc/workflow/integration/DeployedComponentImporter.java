package org.iplantc.workflow.integration;

import org.apache.log4j.Logger;
import org.iplantc.persistence.dto.components.DeployedComponent;
import org.iplantc.workflow.dao.DeployedComponentDao;
import org.iplantc.workflow.integration.json.TitoDeployedComponentUnmarshaller;
import org.iplantc.workflow.integration.util.HeterogeneousRegistry;
import org.iplantc.workflow.integration.util.NullHeterogeneousRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to import deployed components from JSON objects. Each JSON object used to describe a deployed component must
 * contain "name" and "type" attributes. Other attributes that may be specified are "id", "location", "description",
 * "version" and "attribution". If the "id" attribute is not provided an identifier will be generated. The rest of the
 * optional attributes are left null if not specified.
 *
 * @author Dennis Roberts
 */
public class DeployedComponentImporter implements ObjectImporter {

    /**
     * Used to log error and informational messages.
     */
    private static Logger LOG = Logger.getLogger(DeployedComponentImporter.class);

    /**
     * Used to save and retrieve deployed components.
     */
    private DeployedComponentDao componentDao;

    /**
     * A registry of named objects.
     */
    private HeterogeneousRegistry registry = new NullHeterogeneousRegistry();

    /**
     * True if existing deployed components in the database should be replaced.
     */
    private boolean replaceExisting;

    /**
     * @param registry the new registry.
     */
    public void setRegistry(HeterogeneousRegistry registry) {
        this.registry = registry == null ? new NullHeterogeneousRegistry() : registry;
    }

    /**
     * Enables the replacement of existing deployed components.
     */
    @Override
    public void enableReplacement() {
        replaceExisting = true;
    }

    /**
     * Disables the replacement of existing deployed components.
     */
    @Override
    public void disableReplacement() {
        replaceExisting = false;
    }

    /**
     * Initializes the properties of a new deployed component importer instance.
     *
     * @param componentDao the deployed component DAO.
     */
    public DeployedComponentImporter(DeployedComponentDao componentDao) {
        this.componentDao = componentDao;
    }

    /**
     * Imports a deployed component using values from the given JSON object.
     *
     * @param json the JSON object describing the deployed component.
     * @throws JSONException if the JSON object is missing a required attribute.
     */
    @Override
    public void importObject(JSONObject json) throws JSONException {
        TitoDeployedComponentUnmarshaller unmarshaller = new TitoDeployedComponentUnmarshaller();
        DeployedComponent newComponent = unmarshaller.fromJson(json);
        saveOrUpdate(newComponent, json.optString("id", null));
    }

    /**
     * Either saves a new deployed component or updates an existing one.
     *
     * @param component deployed component to save or update.
     * @param specifiedId the identifier specified in the import JSON.
     */
    private void saveOrUpdate(DeployedComponent component, String specifiedId) {
        DeployedComponent existingComponent = findExistingComponent(component, specifiedId);
        if (existingComponent == null) {
            componentDao.save(component);
            registerDeployedComponent(component);
        }
        else if (replaceExisting) {
            updateExistingComponent(component, existingComponent);
            registerDeployedComponent(existingComponent);
        }
        else {
            LOG.warn("a duplicate deployed component was found for " + component.toJson().toString()
                    + " and replacement was not enabled; no update was performed");
            registerDeployedComponent(existingComponent);
        }
    }

    /**
     * Updates an existing deployed component in the database.
     *
     * @param component the new deployed component information.
     * @param existingComponent the existing deployed component.
     */
    private void updateExistingComponent(DeployedComponent component, DeployedComponent existingComponent) {
        existingComponent.setName(component.getName());
        existingComponent.setLocation(component.getLocation());
        existingComponent.setAttribution(component.getAttribution());
        existingComponent.setDescription(component.getDescription());
        existingComponent.setType(component.getType());
        existingComponent.setVersion(component.getVersion());
        componentDao.save(existingComponent);
    }

    /**
     * Finds an existing deployed component with the same name as the given deployed component.
     *
     * @param newComponent the deployed component to search for.
     * @param specifiedId the deployed component identifier specified in the import JSON.
     * @return the deployed component or null if a matching deployed component isn't found.
     */
    private DeployedComponent findExistingComponent(DeployedComponent newComponent, String specifiedId) {
        return specifiedId == null
                ? componentDao.findByNameAndLocation(newComponent.getName(), newComponent.getLocation())
                : componentDao.findById(specifiedId);
    }

    /**
     * Adds the given deployed component to the registry if there is a registry.
     *
     * @param deployedComponent the deployed component to register.
     */
    private void registerDeployedComponent(DeployedComponent deployedComponent) {
        registry.add(DeployedComponent.class, deployedComponent.getName(), deployedComponent);
    }

    /**
     * Imports multiple deployed components from the given JSON array.
     *
     * @param array the JSON array describing the list of deployed components.
     * @throws JSONException if any of the JSON objects are missing required attributes.
     */
    @Override
    public void importObjectList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            importObject(array.getJSONObject(i));
        }
    }
}
