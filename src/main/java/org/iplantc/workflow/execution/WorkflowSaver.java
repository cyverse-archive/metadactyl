package org.iplantc.workflow.execution;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.core.TransformationActivity;

public class WorkflowSaver extends HibernateAccessor {

    public void saveWorkflow(TransformationActivity workflow) {

        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.save(workflow);
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

    }

}
