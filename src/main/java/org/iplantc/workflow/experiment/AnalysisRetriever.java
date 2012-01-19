package org.iplantc.workflow.experiment;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;

/**
 * Used to retrieve analyses from the database.
 */
public class AnalysisRetriever extends HibernateAccessor {

    /**
     * Gets a single transformation activity with the given identifier.
     * 
     * @param id the transformation activity identifier.
     * @return the transformation activity.
     * @throws WorkflowException if too many or too few matches are found.
     */
    public TransformationActivity getTransformationActivity(String id) throws WorkflowException {
        List<TransformationActivity> activities = getActivities(id);
        if (activities.isEmpty()) {
            throw new WorkflowException("no matching transformation activities found for ID: " + id);
        }
        else if (activities.size() > 1) {
            throw new WorkflowException("multiple matching transformation activities found for id: " + id);
        }
        return activities.get(0);
    }

    /**
     * Gets a list of transformation activities with the given identifier.
     * 
     * @param id the transformation activity identifier.
     * @return the list of transformation activities.
     */
    @SuppressWarnings("unchecked")
    private List<TransformationActivity> getActivities(String id) {
        List<TransformationActivity> result = null;
        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            result = session.createQuery("from TransformationActivity ta where id='" + id + "'").list();
            tx.commit();
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

}
