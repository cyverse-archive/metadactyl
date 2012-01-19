package org.iplantc.workflow.experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.iplantc.hibernate.util.HibernateUtil;
import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.persistence.dto.transformation.Transformation;
import org.iplantc.workflow.core.TransformationActivity;

public  class AnalysisFactory {
	Map<String, TransformationActivity> analyses =  new HashMap<String, TransformationActivity>();

	public TransformationActivity getFastXTrimmerTask(){
		TransformationActivity fastxTrimmer = new TransformationActivity();
		
		UUID uuid = UUID.randomUUID();
		
		fastxTrimmer.setId(uuid.toString().replace("-", ""));
		fastxTrimmer.setName("FastX Trimmer");
		fastxTrimmer.setDescription("some fastx description");
		
		uuid = UUID.randomUUID();
		
		TransformationStep step1 = new TransformationStep();
		step1.setGuid(uuid.toString().replace("-", ""));
		step1.setName("fastxsteps");
		
		Transformation transformation = new Transformation();
		
		transformation.setName("fastx transformation");
		transformation.setTemplate_id("t152be766a851453491e220ff7bea12de");
		
		step1.setTransformation(transformation);
		fastxTrimmer.addStep(step1);
		
		return fastxTrimmer;
	}
	
    public TransformationActivity getFastXBCodeTask(){
		TransformationActivity fastxTrimmer = new TransformationActivity();
		
		UUID uuid = UUID.randomUUID();
		
		fastxTrimmer.setId(uuid.toString().replace("-", ""));
		fastxTrimmer.setName("FastX Barcode Splitter");
		fastxTrimmer.setDescription("some fastx description");
		
		uuid = UUID.randomUUID();
		
		TransformationStep step1 = new TransformationStep();
		step1.setGuid(uuid.toString().replace("-", ""));
		step1.setName("fastxsteps");
		
		Transformation transformation = new Transformation();
		
		transformation.setName("fastx transformation");
		transformation.setTemplate_id("t152be766a851453491e220ff7bea12de");
		
		step1.setTransformation(transformation);
		fastxTrimmer.addStep(step1);
		
		return fastxTrimmer;
	}
	
	public TransformationActivity getDaceActivity(){
		TransformationActivity dace = new TransformationActivity();
		
		UUID uuid = UUID.randomUUID();
		
		dace.setId(uuid.toString().replace("-", ""));
		dace.setName("Discrete Ancestral Character Estimation");
		dace.setDescription("Dace");
		
		uuid = UUID.randomUUID();
		
		TransformationStep step1 = new TransformationStep();
				
		step1.setGuid(uuid.toString().replace("-", ""));
		step1.setName("dace_step");
		
		Transformation transformation = new Transformation();
		transformation.setName("dace transformation");
		transformation.setTemplate_id("t12bd045b75933352e31302e3231d0429321818ccf8c");
		
		step1.setTransformation(transformation);
		dace.addStep(step1);
		
		return dace;
	}

	public TransformationActivity getSimplerActivity() {
		TransformationActivity simpler = new TransformationActivity();
		
		simpler.setId("a12af368916d33352e31302e3231d01170012afe3ff7");
		simpler.setName("Simple R");
		simpler.setDescription("Some description");
		
		TransformationStep step = new TransformationStep();
		step.setGuid("s15af368916d33352e31302e3231d01170012afe3ff7");
		step.setName("unique step");
		step.setGuid("s_some_id");
		
		Transformation t = new Transformation();
		
		t.setName("some name");
		t.setTemplate_id("t12af368916d33352e31302e3231d01170012afe3c7");
		
		step.setTransformation(t);
		simpler.addStep(step);
		
		return simpler;
	}
	
	public TransformationActivity getSimplerNOinActivity() {
		TransformationActivity simpler = new TransformationActivity();
		
		simpler.setId("a12af368916d33352e31302e3231d01170012afe3ff7s");
		simpler.setName("Simple R no input");
		simpler.setDescription("Some description");
		
		TransformationStep step = new TransformationStep();
		step.setGuid("s15af368916d33352e31302e3231d01170012afe3ff7");
		step.setName("unique step");
		step.setGuid("s_some_id");
		
		Transformation t = new Transformation();
		
		t.setName("some name");
		t.setTemplate_id("t12af368916d33352e31302e3231d01170012afe3c88");
		
		step.setTransformation(t);
		simpler.addStep(step);
		
		return simpler;
	}
	
	public static void main(String[] args) throws Exception{
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionfac = configuration.buildSessionFactory();
		HibernateUtil.setSessionFactoryForTesting(sessionfac);

		AnalysisFactory fac = new AnalysisFactory();
		
		TransformationActivity activity = fac.getDaceActivity();
		
		activity.setWorkspaceId(1);
		
		saveTransformationActivity(activity);
	}

	private static void saveTransformationActivity(TransformationActivity activity) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(activity);
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
