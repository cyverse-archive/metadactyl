package org.iplantc.workflow.integration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
     * Indicates what should be done if an existing object matches the one being imported.
     */
    private UpdateMode updateMode = UpdateMode.DEFAULT;

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
     * Enables the replacement of existing objects.
     */
    @Override
    public void enableReplacement() {
        updateMode = UpdateMode.REPLACE;
    }

    /**
     * Disables the replacement of existing objects.
     */
    @Override
    public void disableReplacement() {
        updateMode = UpdateMode.THROW;
    }

    /**
     * Instructs the importer to ignore attempts to replace existing objects.
     */
    @Override
    public void ignoreReplacement() {
        updateMode = UpdateMode.IGNORE;
    }

    /**
     * @return the current update mode.
     */
    public UpdateMode getUpdateMode() {
        return updateMode;
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
