package org.iplantc.workflow.tools;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.iplantc.hibernate.util.HibernateUtil;

public class ExecuteSqlScript {

	public static void main(String[] args) throws Exception{
		
		
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionfac = configuration.buildSessionFactory();
		HibernateUtil.setSessionFactoryForTesting(sessionfac);
		
		
		
		
		BufferedReader rd = new BufferedReader(new FileReader(args[0]));
		
		
		String sql = IOUtils.toString(rd);
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		
		Transaction tx = session.beginTransaction();
		
		session.createSQLQuery(sql).executeUpdate();
		
		tx.commit();
		
		// TODO CORE-1349 consider moving session.close() into a finally { }
		session.close();
		
	}
	
	
}
