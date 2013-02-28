package org.iplantc.workflow.tools;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.iplantc.hibernate.util.HibernateUtil;
import org.iplantc.workflow.marshaler.NotificationSetUnmarshaller;
import org.iplantc.workflow.template.notifications.NotificationSet;
import org.json.JSONObject;

public class SaveNotificationSets {

	public static void main(String[] args) throws Exception {


		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionfac = configuration.buildSessionFactory();
		HibernateUtil.setSessionFactoryForTesting(sessionfac);

		BufferedReader rd = new BufferedReader(new FileReader(args[0]));



		String notification = IOUtils.toString(rd);

		JSONObject json_fastx = new JSONObject(notification);

		NotificationSet set1 = null;

		NotificationSetUnmarshaller unmarshaller = new NotificationSetUnmarshaller();

		set1 = unmarshaller.unmarshallNotificationSet(json_fastx);



		//save it
        saveNotificationSet(set1);


	}

    private static void saveNotificationSet(NotificationSet notificationSet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
	        session.save(notificationSet);
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
