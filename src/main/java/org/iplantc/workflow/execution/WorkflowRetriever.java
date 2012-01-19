package org.iplantc.workflow.execution;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.core.TransformationActivity;

public class WorkflowRetriever extends HibernateAccessor{

	@SuppressWarnings("rawtypes")
    public TransformationActivity retrieveWorkflowById(String id) throws Exception{
		
		Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TransformationActivity workflow=null;
            
            List workflows = session.createQuery("from TransformationActivity tt where id='"+id+"'").list();
            
            if(workflows.size()<1) throw new Exception(" The requested workflow with id "+id+" does not exist.");
            
            workflow = (TransformationActivity) workflows.get(0);
            
            
            tx.commit();
            return workflow;
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
