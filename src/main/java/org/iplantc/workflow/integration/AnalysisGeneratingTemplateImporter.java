package org.iplantc.workflow.integration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.iplantc.persistence.dto.data.IntegrationDatum;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.TransformationActivityDao;
import org.iplantc.workflow.integration.json.TitoIntegrationDatumUnmarshaller;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.service.WorkspaceInitializer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A template importer that automatically generates an analysis for every template that it imports
 * 
 * @author Dennis Roberts
 */
public class AnalysisGeneratingTemplateImporter extends TemplateImporter {

    /**
     * Used to log error and informational messages.
     */
    private static final Logger LOG = Logger.getLogger(AnalysisGeneratingTemplateImporter.class);
    /**
     * Used to import new template groups.
     */
    private TemplateGroupImporter templateGroupImporter;
    /**
     * Used to initialize the user's workspace.
     */
    private WorkspaceInitializer workspaceInitializer;

    /**
     * @param daoFactory the factory used to generate data access objects.
     * @param templateGroupImporter used to add analyses to template groups.
     */
    public AnalysisGeneratingTemplateImporter(DaoFactory daoFactory, TemplateGroupImporter templateGroupImporter,
            WorkspaceInitializer workspaceInitializer) {
        super(daoFactory);
        this.templateGroupImporter = templateGroupImporter;
        this.workspaceInitializer = workspaceInitializer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNewTemplate(Template template, JSONObject json) {
        LOG.warn("saving a new template: " + template.getName());
        getDaoFactory().getTemplateDao().save(template);
        generateAnalysis(template, json);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceExistingTemplate(Template template, Template existingTemplate, JSONObject json) {
        LOG.warn("replacing an existing template: " + template.getName());
        template.setId(existingTemplate.getId());
        getDaoFactory().getTemplateDao().delete(existingTemplate);
        getDaoFactory().getTemplateDao().save(template);
        updateOrGenerateAnalysis(template, existingTemplate, json);
    }

    /**
     * Updates an existing analysis or generates a new one. If the existing template already has a default analysis then
     * the existing analysis is updated to point to the new . If the existing template does note
     * 
     * @param template the new template.
     * @param existingTemplate the existing template that is being replaced.
     * @param json the JSON object representing the template.
     */
    private void updateOrGenerateAnalysis(Template template, Template existingTemplate, JSONObject json) {
        TransformationActivityDao analysisDao = getDaoFactory().getTransformationActivityDao();
        TransformationActivity analysis = analysisDao.findById(existingTemplate.getId());
        if (analysis == null) {
            generateAnalysis(template, json);
        }
        else {
            analysis.setIntegrationDatum(unmarshalIntegrationDatum(json));
            analysis.setName(template.getName());
            analysis.setDescription(template.getDescription());
            analysis.setDeleted(false);
            analysisDao.save(analysis);
        }
    }

    /**
     * Unmarshals the integration datum from information in the template JSON.
     *
     * @param json the JSON object representing the template.
     * @return the integration JSON.
     */
    private IntegrationDatum unmarshalIntegrationDatum(JSONObject json) {
        try {
            return new TitoIntegrationDatumUnmarshaller().fromJson(json);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * Generates and saves a single-step analysis for the given template.
     * 
     * @param template the template.
     * @param json the JSON object representing the template.
     */
    private void generateAnalysis(Template template, JSONObject json) {
        TransformationActivity analysis = new AnalysisGenerator().generateAnalysis(template);
        analysis.setIntegrationDatum(unmarshalIntegrationDatum(json));
        String username = getUsername(json, analysis);
        initializeWorkspace(username);
        getDaoFactory().getTransformationActivityDao().save(analysis);
        templateGroupImporter.addAnalysisToWorkspace(username, analysis);
        getRegistry().add(TransformationActivity.class, analysis.getName(), analysis);
    }

    /**
     * Gets the username for the analysis.  If the fully qualified username was provided in the JSON then it will be
     * used.  Otherwise, the e-mail address of the analysis integrator will be used.
     * 
     * @param json the JSON object representing the template being imported.
     * @param analysis the analysis that was generated for the template being imported.
     * @return the fully qualified username.
     */
    private String getUsername(JSONObject json, TransformationActivity analysis) {
        String username = json.optString("full_username");
        if (StringUtils.isEmpty(username)) {
            username = analysis.getIntegrationDatum().getIntegratorEmail();
        }
        if (StringUtils.isEmpty(username)) {
            throw new WorkflowException("username not provided for analysis: " + analysis.getName());
        }
        return username;
    }

    /**
     * Initializes the user's workspace.
     *
     * @param username the fully qualified username.
     */
    private void initializeWorkspace(String username) {
        workspaceInitializer.initializeWorkspace(getDaoFactory(), username);
    }
}
