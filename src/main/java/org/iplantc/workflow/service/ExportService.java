package org.iplantc.workflow.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iplantc.hibernate.util.SessionTask;
import org.iplantc.hibernate.util.SessionTaskWrapper;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.TransformationActivityDao;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Template;
import org.json.JSONObject;

/**
 *
 * @author Kris Healy <healyk@iplantcollaborative.org>
 */
public class ExportService {
    private SessionFactory sessionFactory;

    public ExportService() {
    }
    
    public String canExportAnalysis(String jsonString) throws Exception {
        final JSONObject input = new JSONObject(jsonString);
        
        return new SessionTaskWrapper(sessionFactory).performTask(new SessionTask<String>() {
            @Override
            public String perform(Session session) {
                try {
                    DaoFactory daoFactory = new HibernateDaoFactory(session);
                    TransformationActivityDao transformationActivityDao = daoFactory.getTransformationActivityDao();
                
                    TransformationActivity analysis = transformationActivityDao.findById(input.getString("analysis_id"));
                    List<Template> templates = daoFactory.getTemplateDao().findTemplatesInAnalysis(analysis);
                    
                    JSONObject result = new JSONObject();
                    result.put("can-export", true);
                
                    if (templates.isEmpty()) {
                        result.put("can-export", false);
                        result.put("cause", "Application contains no steps and cannot be copied or modified.");
                    }
                    
                    for (Template template : templates) {
                        for (PropertyGroup propertyGroup : template.getPropertyGroups()) {
                            for (Property property : propertyGroup.getProperties()) {
                                if(property.getPropertyType().getValueType() == null) {
                                    result.put("can-export", false);
                                    result.put("cause", "Application contains Properties that cannot be copied into Tito.");
                                }
                            }
                        }
                    }
                    
                    return result.toString();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    } 
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
