package org.iplantc.workflow.experiment;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * Static utility methods for formatting job parameters.
 *
 * @author Dennis Roberts
 */
public class ParamUtils {

    /**
     * Prevent instantiation.
     */
    private ParamUtils() {
    }

    /**
     * Sets the name and value of a command-line parameter. If the name ends with an equals sign then the name field is
     * left empty and the value field will contain the name and value concatenated together. Otherwise, the name field
     * will contain the name and the value field will contain the value.
     *
     * @param param the parameter that is being formatted.
     * @param name the name of the parameter (that is, the command-line option flag).
     * @param value the value of the parameter.
     */
    public static void setParamNameAndValue(JSONObject param, String name, String value) {
        if (name.endsWith("=")) {
            param.put("name", "");
            param.put("value", name + StringUtils.defaultString(value));
        }
        else {
            param.put("name", name);
            param.put("value", value);
        }
    }
}
