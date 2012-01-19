package org.iplantc.workflow.integration;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.iplantc.persistence.dao.WorkspaceDao;
import org.iplantc.persistence.dao.user.UserDao;
import org.iplantc.persistence.dto.user.User;
import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.TemplateDao;
import org.iplantc.workflow.dao.TemplateGroupDao;
import org.iplantc.workflow.dao.TransformationActivityDao;
import org.iplantc.workflow.integration.util.JsonUtils;
import org.iplantc.workflow.template.groups.TemplateGroup;
import org.iplantc.workflow.util.ListUtils;
import org.iplantc.workflow.util.Predicate;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to delete analyses from the database.
 * 
 * @author Dennis Roberts
 */
public class AnalysisDeleter {

    /**
     * Used to retrieve and save analyses.
     */
    private TransformationActivityDao analysisDao;

    /**
     * Used to remove analyses from template groups.
     */
    private TemplateGroupDao templateGroupDao;

    /**
     * Used to delete templates.
     */
    private TemplateDao templateDao;

    /**
     * Used to find the user's workspace identifier.
     */
    private WorkspaceDao workspaceDao;

    /**
     * Used to find the user.
     */
    private UserDao userDao;

    /**
     * @param daoFactory used to obtain data access objects.
     */
    public AnalysisDeleter(DaoFactory daoFactory) {
        this.analysisDao = daoFactory.getTransformationActivityDao();
        this.templateGroupDao = daoFactory.getTemplateGroupDao();
        this.templateDao = daoFactory.getTemplateDao();
        this.workspaceDao = daoFactory.getWorkspaceDao();
        this.userDao = daoFactory.getUserDao();
    }

    /**
     * Logically deletes one or more analyses.
     * 
     * @param json the JSON describing the analysis to delete.
     */
    public void logicallyDelete(JSONObject json) {
        for (TransformationActivity analysis : findAnalysesToDelete(json)) {
            analysis.setDeleted(true);
            analysisDao.save(analysis);
        }
    }

    /**
     * Physically deletes one or more analyses.
     * 
     * @param json the JSON object describing the analysis to delete.
     */
    public void physicallyDelete(JSONObject json) {
        for (TransformationActivity analysis : findAnalysesToDelete(json)) {
            physicallyDelete(analysis);
        }
    }

    /**
     * Physically deletes the given analysis.
     * 
     * @param analysis the analysis to delete.
     */
    private void physicallyDelete(TransformationActivity analysis) {
        Set<String> templateIds = analysisDao.getTemplateIdsInAnalysis(analysis);
        removeAnalysisFromTemplateGroups(analysis);
        analysisDao.delete(analysis);
        deleteOrphanedTemplates(templateIds);
    }

    /**
     * Deletes any templates that are no longer referenced by any analyses.
     * 
     * @param templateIds the list of template identifiers that may be deleted.
     */
    private void deleteOrphanedTemplates(Set<String> templateIds) {
        for (String templateId : templateIds) {
            if (analysisDao.getAnalysesReferencingTemplateId(templateId).isEmpty()) {
                templateDao.deleteById(templateId);
            }
        }
    }

    /**
     * Removes an analysis from all template groups that contain it.
     * 
     * @param analysis the analysis to remove.
     */
    private void removeAnalysisFromTemplateGroups(TransformationActivity analysis) {
        List<TemplateGroup> groups = templateGroupDao.findTemplateGroupsContainingAnalysis(analysis);
        for (TemplateGroup group : groups) {
            group.removeTemplate(analysis);
            templateGroupDao.save(group);
        }
    }

    /**
     * Finds the analyses to delete for the given deletion request JSON.
     * 
     * @param json a JSON object representing the deletion request.
     * @return the list of analyses to delete.
     */
    private List<TransformationActivity> findAnalysesToDelete(JSONObject json) {
        List<TransformationActivity> analyses = new AnalysisDeletionRequest(json).getAnalysesToDelete();
        if (analyses.isEmpty()) {
            throw new WorkflowException("no qualifying analyses found to delete");
        }
        return analyses;
    }

    /**
     * An analysis deletion request.
     */
    private class AnalysisDeletionRequest {

        /**
         * The analysis identifier.
         */
        private String analysisId;

        /**
         * The analysis name.
         */
        private String analysisName;

        /**
         * The fully qualified username.
         */
        private String username;

        /**
         * True if public analysis deletion should be enabled.
         */
        private Boolean rootDeletionRequest;

        /**
         * The user's workspace identifier.
         */
        private long workspaceId;

        /**
         * @param json a JSON object representing the deletion request.
         */
        public AnalysisDeletionRequest(JSONObject json) {
            try {
                analysisId = json.optString("analysis_id");
                analysisName = json.optString("analysis_name");
                username = JsonUtils.nonEmptyOptString(json, "", "full_username", "email");
                rootDeletionRequest = json.optBoolean("root_deletion_request", false);
                validate();
                workspaceId = StringUtils.isEmpty(username) ? -1 : getWorkspaceId();
            }
            catch (JSONException e) {
                throw new WorkflowException(e);
            }
        }

        /**
         * Validates the deletion request.
         * 
         * @throws WorkflowException if the deletion request is invalid.
         */
        private void validate() {
            if (StringUtils.isEmpty(analysisId) && StringUtils.isEmpty(analysisName)) {
                throw new WorkflowException("no analysis identifier or name provided");
            }
            if (!rootDeletionRequest && StringUtils.isEmpty(username)) {
                throw new WorkflowException("no username provided for non-root deletion request");
            }
        }

        /**
         * Gets the list of analyses to delete.
         * 
         * @return the list of analyses.
         */
        public List<TransformationActivity> getAnalysesToDelete() {
            return ListUtils.filter(new Predicate<TransformationActivity>() {
                @Override
                public Boolean call(TransformationActivity arg) {
                    return rootDeletionRequest || visibleOnlyToUser(arg);
                }
            }, getSelectedAnalyses());
        }

        /**
         * Determines whether or not the analysis is visible only to the user making the deletion request.
         * 
         * @param analysis the analysis.
         * @return true if the analysis is only visible to the user.
         */
        public boolean visibleOnlyToUser(TransformationActivity analysis) {
            return ListUtils.all(new Predicate<TemplateGroup>() {
                @Override
                public Boolean call(TemplateGroup arg) {
                    return arg.getWorkspaceId() == workspaceId;
                }
            }, templateGroupDao.findTemplateGroupsContainingAnalysis(analysis));
        }

        /**
         * Gets the list of analyses selected by the deletion request.
         * 
         * @return the list of analyses.
         */
        private List<TransformationActivity> getSelectedAnalyses() {
            return StringUtils.isEmpty(analysisId)
                    ? analysisDao.findByName(analysisName)
                    : ListUtils.asListWithoutNulls(analysisDao.findById(analysisId));
        }

        /**
         * Gets the workspace ID for the user making the deletion request.
         * 
         * @return the workspace ID.
         */
        private long getWorkspaceId() {
            Workspace workspace = workspaceDao.findByUser(getUser());
            if (workspace == null) {
                throw new WorkflowException("no workspace found for " + username);
            }
            return workspace.getId();
        }

        /**
         * Finds the record of the user making the deletion request.
         * 
         * @return the user.
         */
        private User getUser() {
            User user = userDao.findByUsername(username);
            if (user == null) {
                throw new WorkflowException("no user found for user, " + username);
            }
            return user;
        }
    }
}
