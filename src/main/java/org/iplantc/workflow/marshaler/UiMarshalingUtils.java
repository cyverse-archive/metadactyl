package org.iplantc.workflow.marshaler;

import java.util.LinkedList;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.Validator;

/**
 * Utility methods for marshaling the JSON that is sent to the UI.
 *
 * @author Dennis Roberts
 */
public class UiMarshalingUtils {

    /**
     * Prevent instantiation.
     */
    private UiMarshalingUtils() {
    }

    /**
     * Generates the validator for a data object.
     *
     * @param dataObject the data object.
     * @return the validator.
     */
    public static Validator generateValidator(DataObject dataObject) {
        Validator validator = new Validator();
        validator.setName("");
        validator.setRequired(dataObject.isRequired());
        validator.setRules(new LinkedList<Rule>());
        return validator;
    }
}
