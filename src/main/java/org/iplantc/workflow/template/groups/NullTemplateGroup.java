package org.iplantc.workflow.template.groups;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONArray;
import org.iplantc.workflow.core.TransformationActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used when a template group is required but none is available.
 * 
 * @author Dennis Roberts
 */
public class NullTemplateGroup extends TemplateGroup {

    /**
     * @{inheritDoc}
     */
    @Override
    public void addGroup(TemplateGroup group) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addTemplate(TransformationActivity template) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean containsActiveAnalyses() {
        return false;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean containsAnalysis(TransformationActivity analysis) {
        return false;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean containsGroup(String name) {
        return false;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int countActiveTemplates() {
        return 0;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean directlyContainsAnalysisWithId(String analysisId) {
        return false;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public long getHid() {
        return 0;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getId() {
        return "";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public List<TemplateGroup> getSub_groups() {
        return new ArrayList<TemplateGroup>();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public TemplateGroup getSubgroup(String name) {
        return null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Set<TransformationActivity> getTemplates() {
        return new HashSet<TransformationActivity>();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public long getWorkspaceId() {
        return 0;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void removeTemplate(TransformationActivity template) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setDescription(String description) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setHid(long hid) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setId(String id) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setName(String name) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setSub_groups(List<TemplateGroup> sub_groups) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setTemplates(Set<TransformationActivity> templates) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setWorkspaceId(long workspaceId) {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("hid", 0);
        json.put("id", 0);
        json.put("name", "");
        json.put("description", "");
        json.put("analyses", new JSONArray());
        return json;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String toString() {
        try {
            return toJson().toString(4);
        }
        catch (JSONException e) {
            return "";
        }
    }
}
