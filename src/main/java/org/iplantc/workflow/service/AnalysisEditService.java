package org.iplantc.workflow.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iplantc.hibernate.util.SessionTask;
import org.iplantc.hibernate.util.SessionTaskWrapper;
import org.iplantc.persistence.dto.data.IntegrationDatum;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.client.ZoidbergClient;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.integration.TemplateExporter;
import org.iplantc.workflow.integration.json.NoIdRetentionStrategy;
import org.iplantc.workflow.integration.json.TitoIntegrationDatumMashaller;
import org.iplantc.workflow.integration.util.JsonUtils;
import org.iplantc.workflow.service.dto.AnalysisId;
import org.iplantc.workflow.user.UserDetails;
import org.json.JSONException;

/**
 * A service that allows analyses to be exported to Tito for editing.
 *
 * @author Dennis Roberts
 */
public class AnalysisEditService {

    /**
     * The Hibernate session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * A client used to communicate with Zoidberg.
     */
    private ZoidbergClient zoidbergClient;

    /**
     * Used to get the user's details.
     */
    private UserService userService;

    /**
     * Used to save apps.
     */
    private WorkflowImportService workflowImportService;

    /**
     * @param sessionFactory the Hibernate session factory.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @param zoidbergClient a client used to communicate with Zoidberg.
     */
    public void setZoidbergClient(ZoidbergClient zoidbergClient) {
        this.zoidbergClient = zoidbergClient;
    }

    /**
     * @param userService used to get the user's details.
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param workflowImportService used to save apps.
     */
    public void setWorkflowImportService(WorkflowImportService workflowImportService) {
        this.workflowImportService = workflowImportService;
    }

    /**
     * Prepares an analysis for editing. If the analysis already belongs to the user in TITO then this service merely
     * ensures that the analysis is not marked as deleted. If the analysis does not belong to the user in TITO, this
     * service makes a copy of the analysis for the user in TITO and returns the new analysis ID.
     *
     * @param analysisId the analysis identifier.
     * @return the (possibly new) analysis identifier.
     */
    public String prepareAnalysisForEditing(final String analysisId) {
        return new SessionTaskWrapper(sessionFactory).performTask(new SessionTask<String>() {

            @Override
            public String perform(Session session) {
                return editAnalysis(new HibernateDaoFactory(session), analysisId);
            }
        });
    }

    /**
     * Copies an analysis. This is different from preparing an analysis for editing in that a new copy of the analysis
     * is created even if the user has the ability to edit the original.
     *
     * @param analysisId the original analysis identifier.
     * @return the new analysis identifier.
     */
    public String copyAnalysis(final String analysisId) {
        return new SessionTaskWrapper(sessionFactory).performTask(new SessionTask<String>() {

            @Override
            public String perform(Session session) {
                return copyAnalysis(new HibernateDaoFactory(session), analysisId);
            }
        });
    }

    /**
     * Prepares an analysis for editing. If the analysis already belongs to the user in TITO then this service merely
     * ensures that the analysis is not marked as deleted. If the analysis does not belong to the user in TITO, this
     * service makes a copy of the analysis for the user in TITO and returns the new analysis ID.
     *
     * @param daoFactory used to obtain data access objects.
     * @param analysisId the analysis identifier.
     * @return the (possibly new) analysis identifier.
     */
    private String editAnalysis(DaoFactory daoFactory, String analysisId) {
        UserDetails userDetails = userService.getCurrentUserDetails();
        JSONObject analysis = getAnalysisFromZoidberg(analysisId, userDetails.getShortUsername());
        if (analysis != null) {
            ensureAnalysisNotDeleted(analysis);
            return new AnalysisId(analysisId).toString();
        }
        else {
            return copyAnalysis(daoFactory, analysisId, userDetails);
        }
    }

    /**
     * Prepares a new copy of an analysis for editing. This is different from editAnalysis in that a new copy of the
     * analysis is made even if the user already has the ability to edit the original.
     *
     * @param daoFactory used to obtain data access objects.
     * @param analysisId the original analysis identifier.
     * @return the new analysis identifier.
     */
    private String copyAnalysis(DaoFactory daoFactory, String analysisId) {
        return copyAnalysis(daoFactory, analysisId, userService.getCurrentUserDetails());
    }

    /**
     * Retrieves the analysis from Zoidberg.
     *
     * @param analysisId the analysis identifier.
     * @param username the username.
     * @return the analysis.
     */
    private JSONObject getAnalysisFromZoidberg(String analysisId, String username) {
        JSONObject result = zoidbergClient.getAnalysesWithId(analysisId);
        JSONArray analyses = result.getJSONArray("objects");
        for (int i = 0; i < analyses.size(); i++) {
            JSONObject analysis = analyses.getJSONObject(i);
            if (analysis.optString("user").equals(username)) {
                return analysis;
            }
        }
        return null;
    }

    /**
     * Ensures that the analysis isn't marked as deleted.
     *
     * @param analysis the analysis.
     */
    private void ensureAnalysisNotDeleted(JSONObject analysis) {
        if (analysis.optBoolean("deleted", false)) {
            analysis.remove("deleted");
            zoidbergClient.updateAnalysis(analysis);
        }
    }

    /**
     * Retrieves the analysis using the metadata retriever.
     *
     * @param daoFactory used to obtain data access objects.
     * @param analysisId the analysis identifier.
     * @return the analysis.
     */
    private JSONObject exportAnalysis(DaoFactory daoFactory, String analysisId) {
        return JsonUtils.toNetSfJsonObject(new TemplateExporter(daoFactory, new NoIdRetentionStrategy()).exportTemplate(
                analysisId));
    }

    /**
     * Saves an app using the metadata import service.
     *
     * @param analysis the analysis identifier.
     */
    private void importAnalysis(String jsonString) {
        try {
            workflowImportService.importTemplate(jsonString);
        }
        catch (JSONException e) {
            throw new WorkflowException(e);
        }
    }

    /**
     * Prepares a new copy of an analysis for editing. This is different from editAnalysis in that a new copy of the
     * analysis is made even if the user already has the ability to edit the original.
     *
     * @param daoFactory used to obtain data access objects.
     * @param analysisId the original analysis identifier.
     * @param userDetails information about the current user.
     * @return the new analysis identifier.
     */
    private String copyAnalysis(DaoFactory daoFactory, String analysisId, UserDetails userDetails) {
        JSONObject analysis = exportAnalysis(daoFactory, analysisId);
        analysis = convertAnalysisToCopy(analysis, userDetails);

        analysisId = zoidbergClient.saveAnalysis(analysis);

        analysis.put("tito", analysisId);
        analysis.put("id", analysisId);

        importAnalysis(analysis.toString());

        return new AnalysisId(analysisId).toString();
    }

    /**
     * Converts the app to a copy for the given user info.
     *
     * @param analysis the app to convert.
     * @param userDetails information about the current user.
     * @return the app as a copy.
     */
    private JSONObject convertAnalysisToCopy(JSONObject analysis, UserDetails userDetails) {
        TitoIntegrationDatumMashaller marshaller = new TitoIntegrationDatumMashaller();
        String analysisName = "Copy of " + analysis.getString("name");
        String username = userDetails.getShortUsername();
        String email = userDetails.getEmail();
        String fullUsername = userDetails.getUsername();

        analysis.put("name", analysisName);
        analysis.put("full_username", fullUsername);
        analysis.put("implementation", marshaller.toJson(createIntegrationDatum(email, username)).toString());
        analysis.put("user", username);

        return analysis;
    }

    /**
     * Creates an integration datum for the current user.
     *
     * @param email the user's e-mail address.
     * @param username the username.
     * @return the integration datum.
     */
    private IntegrationDatum createIntegrationDatum(String email, String username) {
        IntegrationDatum integrationDatum = new IntegrationDatum();
        integrationDatum.setIntegratorEmail(email);
        integrationDatum.setIntegratorName(username);
        return integrationDatum;
    }
}
