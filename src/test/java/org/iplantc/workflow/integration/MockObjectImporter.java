package org.iplantc.workflow.integration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.iplantc.workflow.integration.ObjectImporter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A mock object importer used for testing.
 * 
 * @author Dennis Roberts
 */
public class MockObjectImporter implements ObjectImporter {

    /**
     * The objects that have been imported.
     */
    private List<JSONObject> importedObjects = new LinkedList<JSONObject>();

    /**
     * The arrays that have been imported.
     */
    private List<JSONArray> importedArrays = new LinkedList<JSONArray>();

    /**
     * True if existing objects with the same name should be replaced.
     */
    private boolean replaceExisting;

    /**
     * @return the list of imported objects.
     */
    public List<JSONObject> getImportedObjects() {
        return Collections.unmodifiableList(importedObjects);
    }

    /**
     * @return the list of imported arrays.
     */
    public List<JSONArray> getImportedArrays() {
        return Collections.unmodifiableList(importedArrays);
    }

    /**
     * @return the current replacement flag value.
     */
    public boolean replacementsEnabled() {
        return replaceExisting;
    }

    /**
     * Enables the replacement of existing objects.
     */
    @Override
    public void enableReplacement() {
        replaceExisting = true;
    }

    /**
     * Disables the replacement of existing objects.
     */
    @Override
    public void disableReplacement() {
        replaceExisting = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importObject(JSONObject json) throws JSONException {
        importedObjects.add(json);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importObjectList(JSONArray array) throws JSONException {
        importedArrays.add(array);
    }
}
