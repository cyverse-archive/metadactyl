package org.iplantc.workflow.client;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * A client for communicating with Zoidberg.
 * 
 * @author Dennis Roberts
 */
public class ZoidbergClient extends AbstractHttpClient {

    /**
     * Saves an analysis.
     * 
     * @param analysis the analysis to save.
     * @return the new analysis ID.
     */
    public String saveAnalysis(JSONObject analysis) {
        return putWithStringResponse(createRequestUrl("in-progress", null), analysis);
    }

    /**
     * Updates an analysis.
     * 
     * @param analysis the analysis to save.
     * @return the analysis identifier.
     */
    public String updateAnalysis(JSONObject analysis) {
        return postWithStringResponse(createRequestUrl("in-progress", null), analysis);
    }

    /**
     * Logically marks an analysis as deleted.
     *
     * @param username The username of the App owner.
     * @param analysisId The tito ID of the App.
     * @return the analysis ID of the deleted App (the same as analysisId).
     */
    public String deleteAnalysis(String username, String analysisId) {
        JSONObject analysis = new JSONObject();
        analysis.put("action", "delete");
        analysis.put("user", username);
        analysis.put("tito", analysisId);

        return postWithStringResponse(createRequestUrl("in-progress", null), analysis);
    }

    /**
     * Gets an analysis.
     * 
     * @param analysisId the analysis identifier.
     * @return the analysis.
     */
    public JSONObject getAnalysesWithId(String analysisId) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("tito", analysisId);
        return getWithJsonResponse(createRequestUrl("in-progress", options));
    }
    
    /**
     * Makes an analysis public
     * 
     * @param analysisId
     * @return 
     */
    public String makePublic(String userId, String analysisId) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("tito", analysisId);
        options.put("user", userId);
        
        return postWithStringResponse(createRequestUrl("make-public", options), new JSONObject());
    }
}
