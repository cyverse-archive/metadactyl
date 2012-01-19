package org.iplantc.workflow.service;

import org.apache.commons.lang.Validate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.integration.preview.WorkflowPreviewer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A service used to convert workflows in the import format to workflows in the format required by the UI.
 * 
 * @author Dennis Roberts
 */
public class WorkflowPreviewService {

    /**
     * The database session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * Used to resolve reference genomes.
     */
    private ReferenceGenomeHandler referenceGenomeHandler;

    /**
     * @param sessionFactory the database session factory.
     * @param referenceGenomeHandler used to resolve reference genomes.
     * @throws IllegalArgumentException if one of the arguments is null.
     */
    public WorkflowPreviewService(SessionFactory sessionFactory, ReferenceGenomeHandler referenceGenomeHandler) {
        Validate.notNull(sessionFactory, "missing required argument: sessionFactory");
        Validate.notNull(referenceGenomeHandler, "missing required argument: referenceGenomeHandler");
        this.sessionFactory = sessionFactory;
        this.referenceGenomeHandler = referenceGenomeHandler;
    }

    /**
     * Converts the given JSON string from the format consumed by the workflow import service to the format
     * required by the Discovery Environment UI.
     * 
     * @param jsonString the original JSON string.
     * @return the converted JSON string.
     * @throws JSONException if the JSONString is invalid or doesn't meet the requirements.
     */
    public String previewWorkflow(String jsonString) throws JSONException {
        String result = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            WorkflowPreviewer previewer = createPreviewer(session);
            result = previewer.preview(new JSONObject(jsonString)).toString();
            tx.commit();
        }
        catch (JSONException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }
        return result;
    }

    /**
     * Converts the given JSON string from the format consumed by the workflow import service to the format required
     * by the Discovery Environment UI.
     * 
     * @param jsonString the original JSON string.
     * @return the converted JSON string.
     * @throws JSONException if the JSONString is invalid or doesn't meet the requirements.
     */
    public String previewTemplate(String jsonString) throws JSONException {
        String result = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            WorkflowPreviewer previewer = createPreviewer(session);
            result = previewer.previewTemplate(new JSONObject(jsonString)).toString();
            tx.commit();
        }
        catch (JSONException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }
        return result;
    }

    /**
     * Creates the object used to generate the preview JSON.
     * 
     * @param session the database session.
     * @return the previewer.
     */
    private WorkflowPreviewer createPreviewer(Session session) {
        DaoFactory daoFactory = new HibernateDaoFactory(session);
        WorkflowPreviewer previewer = new WorkflowPreviewer(daoFactory, referenceGenomeHandler);
        return previewer;
    }
}
