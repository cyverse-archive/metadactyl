package org.iplantc.workflow.marshaler;

import org.iplantc.persistence.dao.refgenomes.ReferenceGenomeDao;
import org.iplantc.persistence.dto.refgenomes.ReferenceGenome;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.data.DataObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Marshals input properties that are associated with reference genomes.
 *
 * @author Dennis Roberts
 */
public class ReferenceGenomeUiInputPropertyMarshaler extends UiInputPropertyMarshaler {

    /**
     * @param daoFactory used to obtain data access objects.
     */
    public ReferenceGenomeUiInputPropertyMarshaler(DaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * Gets the property name to use for an input data object.  For reference genome properties, this is just the
     * empty string.
     *
     * @param input the input data object.
     * @return the property name to use.
     */
    @Override
    protected String getPropertyName(DataObject input) {
        return "";
    }

    /**
     * Gets the name of the property type to use for an input property.  For reference genome properties, this is
     * always {@code TextSelection}.
     *
     * @param input the input data object.
     * @return the property type name to use.
     */
    @Override
    protected String getPropertyTypeName(DataObject input) {
        return "TextSelection";
    }

    /**
     * Generates the JSON representing the validation rules to use for an input property.  For reference genomes, this
     * consists of a single {@code MustContain} rule listing the available reference genomes.
     *
     * @param input the input data object.
     * @return a JSON array representing the list of validation rules.
     * @throws JSONException if a JSON error occurs.
     */
    @Override
    protected JSONArray marshalValidationRules(DataObject input) throws JSONException {
        JSONArray result = new JSONArray();
        result.put(marshalSelectionRule());
        return result;
    }

    /**
     * Generates the JSON representing a reference genome selection rule.
     *
     * @return the generated JSON object.
     */
    private JSONObject marshalSelectionRule() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("MustContain", marshalSelectionRuleArguments());
        return json;
    }

    /**
     * Marshals the selection rule arguments for reference genomes.
     *
     * @return the array of selection rule arguments.
     * @throws JSONException if a JSON error occurs.
     */
    private JSONArray marshalSelectionRuleArguments() throws JSONException {
        JSONArray result = new JSONArray();
        ReferenceGenomeDao dao = getDaoFactory().getReferenceGenomeDao();
        for (ReferenceGenome referenceGenome : dao.list()) {
            result.put(marshalSelectionArgument(referenceGenome));
        }
        return result;
    }

    /**
     * Marshals a selection rule argument for a single reference genome.
     *
     * @param referenceGenome the reference genome.
     * @return the JSON object representing the selection rule argument.
     * @throws JSONException if a JSONError occurs.
     */
    private JSONObject marshalSelectionArgument(ReferenceGenome referenceGenome) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", referenceGenome.getUuid());
        json.put("value", referenceGenome.getUuid());
        json.put("isDefault", "false");
        json.put("display", referenceGenome.getName());
        return json;
    }
}
