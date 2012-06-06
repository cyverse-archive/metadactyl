package org.iplantc.workflow;

import org.hibernate.Session;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateTransformationActivityDao;
import org.iplantc.workflow.marshaller.UiAnalysisMarshaller;
import org.iplantc.workflow.model.Template;

/**
 * Fetches a template using hibernate.
 * 
 * @author: Juan Antonio Raygoza Garay
 * 
 */

public class HibernateTemplateFetcher extends HibernateAccessor implements TemplateFetcher {

    public HibernateTemplateFetcher() {
        super();
    }

    /**
     * Fetches a template by analysis id.
     */
    public Template fetchTemplateByName(String id) throws Exception {
        Session session = getSessionFactory().openSession();
        try {
            DaoFactory daoFactory = new HibernateDaoFactory(session);
            UiAnalysisMarshaller marshaller = new UiAnalysisMarshaller(daoFactory);
            TransformationActivity analysis = loadAnalysis(id, session);
            Template template = marshaller.templateFromAnalysis(analysis);
            return template;
        }
        finally {
            session.close();
        }

    }

    private TransformationActivity loadAnalysis(String id, Session session) throws Exception {
        TransformationActivity analysis = new HibernateTransformationActivityDao(session).findById(id);
        if (analysis == null) {
            throw new Exception("no analysis with ID, " + id + ", found");
        }
        return analysis;
    }
}
