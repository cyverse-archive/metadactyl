package org.iplantc.workflow.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to import objects from JSON objects or arrays.
 * 
 * @author Dennis Roberts
 */
public interface ObjectImporter {

    /**
     * Enables replacement of existing objects.
     */
    public void enableReplacement();

    /**
     * Disables replacement of existing objects.
     */
    public void disableReplacement();

    /**
     * Imports a single object using information in a JSON object.
     * 
     * @param json the JSON object.
     * @throws JSONException if the JSON object does not meet the expectations of the importer.
     */
    public void importObject(JSONObject json) throws JSONException;

    /**
     * Imports a list of objects using information in a JSON array.
     * 
     * @param array the JSON array
     * @throws JSONException if the JSON array does not meet the expectations of the importer.
     */
    public void importObjectList(JSONArray array) throws JSONException;
}
