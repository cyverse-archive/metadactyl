package org.iplantc.workflow.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iplantc.authn.service.UserSessionService;
import org.iplantc.hibernate.util.SessionTask;
import org.iplantc.hibernate.util.SessionTaskWrapper;
import org.iplantc.persistence.dto.data.IntegrationDatum;
import org.iplantc.workflow.client.ZoidbergClient;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.core.TransformationActivityReference;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.TemplateGroupDao;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.template.groups.TemplateGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kris Healy <healyk@iplantcollaborative.org>
 */
public class TemplateGroupService {
    public static final String BETA_TEMPLATE_GROUP_ID = "g5401bd146c144470aedd57b47ea1b979";
    public static final String ANALYSIS_ID_KEY = "analysis_id";
    
    private SessionFactory sessionFactory;
    private ZoidbergClient zoidbergClient;
    private UserSessionService userSessionService;
    
    public TemplateGroupService() {
        
    }
    
    /**
     * Helper function used to easily get a template group by id.  Throws a runtime
     * exception if the id isn't found.
     */
    private TemplateGroup getTemplateGroup(DaoFactory daoFactory, String templateGroupId) {
        TemplateGroup group = daoFactory.getTemplateGroupDao().findById(templateGroupId);
                    
        if(group == null) {
            throw new RuntimeException("No group found with id " + templateGroupId);
        }
        
        return group;
    }
    
    /**
     * Helper function to get a transformation activity by id.  Throws a runtime
     * exception if the id isn't found.
     */
    private TransformationActivity getTransformationActivity(DaoFactory daoFactory, String analysisId) {
        TransformationActivity analysis = daoFactory.getTransformationActivityDao().findById(analysisId);

        if(analysis == null) {
            throw new RuntimeException("No analysis found with id " + analysisId);
        }
        
        return analysis;
    }
    
    public String makeAnalysisPublic(String jsonInput) throws Exception {
        final JSONObject input = new JSONObject(jsonInput);
        
        return new SessionTaskWrapper(sessionFactory).performTask(new SessionTask<String>() {            
            @Override
            public String perform(Session session) {
                try {
                    String analysisId;
                    DaoFactory daoFactory = new HibernateDaoFactory(session);

                    analysisId = input.getString(ANALYSIS_ID_KEY);

                    TemplateGroupDao templateGroupDao = daoFactory.getTemplateGroupDao();

                    TemplateGroup group = templateGroupDao.findById(BETA_TEMPLATE_GROUP_ID);
                    TransformationActivity transformationActivity = getTransformationActivity(daoFactory, analysisId);
                    
                    fillIntegrationDatum(transformationActivity);
                    fillReferences(transformationActivity);
                    fillSuggestedGroups(daoFactory, transformationActivity);
                    transformationActivity.setDescription(input.getString("desc"));
                    transformationActivity.setWikiurl(input.getString("wiki_url"));
                    
                    transformationActivity.setIntegrationDate(new Date());

                    // Remove Analysis from it's current groups
                    List<TemplateGroup> currentGroups = templateGroupDao.findTemplateGroupsContainingAnalysis(transformationActivity);
                    for (TemplateGroup templateGroup : currentGroups) {
                        templateGroup.removeTemplate(transformationActivity);
                    }

                    // Add the analysis to the beta group
                    group.addTemplate(transformationActivity);
                    templateGroupDao.save(group);
                    
                    String result = zoidbergClient.makePublic(userSessionService.getUser().getUsername(), transformationActivity.getId());

                    return "{}";
                } catch(JSONException jsonException) {
                    throw new RuntimeException(jsonException);
                }
            }

            private void fillIntegrationDatum(TransformationActivity transformationActivity) throws JSONException {
                // Fill in the transformation activity information
                IntegrationDatum integrationDatum = new IntegrationDatum();
                integrationDatum.setIntegratorEmail(input.getString("email"));
                integrationDatum.setIntegratorName(input.getString("integrator"));
                transformationActivity.setIntegrationDatum(integrationDatum);
            }
            
            private void fillReferences(TransformationActivity transformationActivity) throws JSONException {
                JSONArray references = input.getJSONArray("references");
                for (int i = 0; i < references.length(); i++) {
                    TransformationActivityReference ref = new TransformationActivityReference();
                    ref.setReferenceText(references.getString(i));
                    transformationActivity.getReferences().add(ref);
                }
            }
            
            private void fillSuggestedGroups(DaoFactory daoFactory, TransformationActivity transformationActivity) throws JSONException {
                TemplateGroupDao templateGroupDao = daoFactory.getTemplateGroupDao();
                JSONArray groups = input.getJSONArray("groups");
                
                for (int i = 0; i < groups.length(); i++) {
                    transformationActivity.getSuggestedGroups().add(templateGroupDao.findById(groups.getString(i)));
                }
            }
        });
    }
    
    /**
     * Service endpoint to add an Analysis to a Template Group.
     */
    public String addAnalysisToTemplateGroup(String jsonInput) throws Exception {
        final JSONObject input = new JSONObject(jsonInput);
        
        return new SessionTaskWrapper(sessionFactory).performTask(new SessionTask<String>() {
            @Override
            public String perform(Session session) {
                List<String> templateGroups = null;
                String analysisId;
                DaoFactory daoFactory = new HibernateDaoFactory(session);
                
                try {
                    analysisId = input.getString(ANALYSIS_ID_KEY);
                    templateGroups = extractTemplateGroupsFromJson();
                } catch(JSONException jsonException) {
                    throw new RuntimeException(jsonException);
                }
                
                if(templateGroups == null || templateGroups.isEmpty()) {
                    throw new RuntimeException("No groups provided in input.");
                } else if(analysisId == null) {
                    throw new RuntimeException("No analysis_id provided in input.");
                } else {
                    for (String groupId : templateGroups) {
                        TemplateGroup group = getTemplateGroup(daoFactory, groupId);
                        TransformationActivity analysis = getTransformationActivity(daoFactory, analysisId);
                        
                        group.addTemplate(analysis);
                        daoFactory.getTemplateGroupDao().save(group);
                    }
                    
                    return "{}";
                }
            }

            private List<String> extractTemplateGroupsFromJson() throws JSONException {
                List<String> templateGroups = new LinkedList<String>();
                JSONArray templateGroupsArray = input.getJSONArray("groups");
                
                for (int i = 0; i < templateGroupsArray.length(); i++) {
                    templateGroups.add(templateGroupsArray.getString(i));
                }
                
                return templateGroups;
            }
        });
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ZoidbergClient getZoidbergClient() {
        return zoidbergClient;
    }

    public void setZoidbergClient(ZoidbergClient zoidbergClient) {
        this.zoidbergClient = zoidbergClient;
    }

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }
}
