package org.iplantc.workflow.experiment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.iplantc.workflow.client.OsmClient;

/**
 * Used to ensure that job names are unique in the OSM.
 * 
 * @author Dennis Roberts
 */
public class OsmJobNameUniquenessEnsurer extends JobNameUniquenessEnsurer {

    /**
     * The client to use to communicate with the OSM.
     */
    private OsmClient osmClient;

    /**
     * @param osmClient the client to use to communicate with the OSM.
     */
    public OsmJobNameUniquenessEnsurer(OsmClient osmClient) {
        this.osmClient = osmClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> findMatchingNames(String username, String jobName) {
        return extractJobNames(osmClient.query(prepareQuery(username, jobName), prepareOptions()));
    }

    /**
     * Prepares the options for the OSM query. For this query, we only want the job name, so we're going to tell the
     * OSM to return only that field.
     * 
     * @return the options.
     */
    private Map<String, String> prepareOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("state.name", "1");
        return options;
    }

    /**
     * Prepares the query for matching job names for the user.
     * 
     * @param username the name of the user who is submitting the job.
     * @param jobName the requested job name.
     * @return the query to send to the OSM.
     */
    private JSONObject prepareQuery(String username, String jobName) {
        JSONObject query = new JSONObject();
        query.put("state.user", username);
        query.put("state.name", prepareJobNameMatcher(jobName));
        return query;
    }

    /**
     * Prepares the JSON object used to find jobs with names that match the requested job name.
     * 
     * @param jobName the requested job name.
     * @return the JSON object.
     */
    private JSONObject prepareJobNameMatcher(String jobName) {
        JSONObject matcher = new JSONObject();
        matcher.put("$regex", "\\A\\Q" + jobName);
        return matcher;
    }

    /**
     * Extracts the job names from the query results.
     * 
     * @param response the response from the OSM query.
     * @return the list of job names.
     */
    private List<String> extractJobNames(JSONObject response) {
        List<String> jobNames = new LinkedList<String>();
        JSONArray objects = response.getJSONArray("objects");
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = objects.getJSONObject(i);
            JSONObject state = object.getJSONObject("state");
            jobNames.add(state.getString("name"));
        }
        return jobNames;
    }
}
