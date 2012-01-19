package org.iplantc.workflow.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
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
import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.data.InputOutputMap;
import org.iplantc.workflow.experiment.AnalysisUnmarshaller;
import org.iplantc.workflow.model.Template;
import org.iplantc.persistence.dto.transformation.Transformation;

public class SaveAnalysis {
	public static void main(String[] args)  throws Exception{
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionfac = configuration.buildSessionFactory();
		HibernateUtil.setSessionFactoryForTesting(sessionfac);
		
		
		
		BufferedReader rd = new BufferedReader(new FileReader(args[0]));
		
		
		String json = IOUtils.toString(rd);
		
		JSONObject analysis = (JSONObject)JSONSerializer.toJSON(json);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 
		
		AnalysisUnmarshaller unmarshaller = new AnalysisUnmarshaller();
		
		TransformationActivity activity = unmarshaller.unmarshallTransformationActivity(analysis);
		
		
		List<TransformationStep> steps = activity.getSteps();
		
		for(int i=0; i < steps.size(); i++){
			
			TransformationStep step = steps.get(i);
			
			Transformation transformation = step.getTransformation();
			
			String template_id = transformation.getTemplate_id();
			
			Template template = (Template) session.createQuery("from Template t where id='"+template_id+"'").uniqueResult();
			
			if(template==null) throw new Exception("The  template for step "+step.getName()+" doesn't exist!!");
			
		}
		
		
		if(analysis.has("mappings")){
			JSONArray mappings = analysis.getJSONArray("mappings");
			
			for(int i=0; i < mappings.size();i++){
				JSONObject jmap = mappings.getJSONObject(i);
				
				InputOutputMap map = new InputOutputMap();
				
				TransformationStep source = activity.getStepByName(jmap.getString("source_step"));
				
				if(source==null) throw new Exception("Source step doesn't exist, please check spelling");
				
				TransformationStep target = activity.getStepByName(jmap.getString("target_step"));
				
				if(target==null) throw new Exception("Target step doesn't exist, please check spelling");
				
				map.setSource(source);
				map.setTarget(target);
				
				Iterator<String> keys = jmap.getJSONObject("map").keys();
				
				String sTemplate = source.getTransformation().getTemplate_id();
				String tTemplate = target.getTransformation().getTemplate_id();
				
				Template source_template = (Template) session.createQuery("from Template t where id='"+sTemplate+"'").uniqueResult();
				Template target_template = (Template) session.createQuery("from Template t where id='"+tTemplate+"'").uniqueResult();
				
				if(source_template==null) throw new Exception("The source template doesn't exist!!");
				if(target_template==null) throw new Exception("The target template doesn't exist!!");
				
				while(keys.hasNext()){
					String key= keys.next();
					String value = jmap.getJSONObject("map").getString(key);
					
					//if(!source_template.hasOutputObject(key)) throw new Exception(" The specified property '"+key+"' is not an output of "+source_template.getName());
					//if(!target_template.hasInputObject(value)) throw new Exception(" The specified property '"+value+"' is not an input of "+target_template.getName());
					
					
				}
				
				
				//Map<String,String> map =
				
				//while(keys.hasNext()) {
					//System.out.println(keys.next());
				//}
				
				map.setInput_output_relation(jmap.getJSONObject("map"));
				
				activity.addMapping(map);
				
			}
			
		}
		
		
		// TODO CORE-1349 consider doing tx.commit(), tx.rollback(), and session.close() in a try...catch...finally
		Transaction tx = session.beginTransaction();
		
		session.save(activity);
		
		tx.commit();
		
		session.close();
		
		
		
		
		
	}
	
	
}
