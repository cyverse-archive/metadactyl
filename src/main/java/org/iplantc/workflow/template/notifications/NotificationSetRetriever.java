package org.iplantc.workflow.template.notifications;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.hibernate.util.HibernateAccessor;


public class NotificationSetRetriever extends HibernateAccessor {

    @SuppressWarnings("unchecked")
    public NotificationSet retrieveNotificationSetByTemplateId(String template_id) throws Exception{
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    Query query = session.createQuery("from NotificationSet where template_id = ?");
		    query.setString(0, template_id);
	        List<NotificationSet> notification_sets = query.list();
	        if(notification_sets.isEmpty()) {
                return null;
            }
	        NotificationSet result = notification_sets.get(0);
	        tx.commit();
	        return result;
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
