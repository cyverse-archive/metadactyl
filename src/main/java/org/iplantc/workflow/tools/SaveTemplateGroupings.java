package org.iplantc.workflow.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.iplantc.hibernate.util.HibernateUtil;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.template.groups.TemplateGroup;


public class SaveTemplateGroupings {

	public static void main(String[] args) throws Exception {

		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionfac = configuration.buildSessionFactory();
		HibernateUtil.setSessionFactoryForTesting(sessionfac);

		BufferedReader rd = new BufferedReader(new FileReader(args[0]));

		
		String jsons = IOUtils.toString(rd);
		

		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsons);

		TemplateGroup group = new TemplateGroup();

		group.setWorkspaceId(1);

		parseTemplateGroup(group, json);


		// TODO CORE-1349 consider doing tx.commit(), tx.rollback(), and session.close() in a try...catch...finally
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		
		session.save(group);

		tx.commit();

		session.close();


	}


	public static void parseTemplateGroup(TemplateGroup group, JSONObject json) throws Exception{

		group.setName(json.optString("name"));
		group.setId(json.optString("id"));
		group.setDescription(json.optString("description"));

		if(json.has("templates")){
			JSONArray array = json.getJSONArray("templates");


			for(int i=0; i < array.size(); i++){

				JSONObject jsons = array.getJSONObject(i);
				String template_id = jsons.getString("id");
				// TODO CORE-1349 consider moving session.close() into a finally { }
				Session session = HibernateUtil.getSessionFactory().openSession();

				List<TransformationActivity> templates = session.createQuery("from TransformationActivity where id='"+template_id+"'").list();

				if(templates.size()==0) throw new Exception("Incorrect analysis id for "+jsons.getString("name") +"  "+template_id);

				group.addTemplate(templates.get(0));
				session.close();

			}
		}


		if(json.has("groups")){
			JSONArray groups = json.getJSONArray("groups");




			for(int i=0; i < groups.size(); i++){
				TemplateGroup a_group = new TemplateGroup();
				parseTemplateGroup(a_group, groups.getJSONObject(i));
				group.addGroup(a_group);
			}

		}
	}


}
