package org.iplantc.workflow.util;

import net.sf.json.JSONObject;

/**
 * Utility methods for dealing with JSON objects using the Source Forge library.
 * 
 * @author Dennis Roberts
 */
public class SfJsonUtils {

    // Prevent instantiation.
    private SfJsonUtils() {}

    /**
     * Provides access to a string field that can have one of several names.
     * 
     * @param json the JSON object.
     * @param defaultValue the default field value.
     * @param keys the field names.
     * @return the field value if it's found or the default value if the field isn't found.
     */
    public static String optString(JSONObject json, String defaultValue, String... keys) {
        for (String key : keys) {
            if (json.containsKey(key)) {
                return json.getString(key);
            }
        }
        return defaultValue;
    }
}
