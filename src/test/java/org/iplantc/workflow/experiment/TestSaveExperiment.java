package org.iplantc.workflow.experiment;

/**
 * Runs the tests for saving a user-defined experiment
 * 
 * @author Juan Antonio Raygoza Garay -- iPlant Colalborative
 */

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;

public class TestSaveExperiment extends TestCase{

	public void testSaveAnExperiment() throws Exception{
		
		Template template = new Template();
		
		PropertyGroup property_group = new PropertyGroup();
		
		Property property = new Property();
		
		property.setName("skipTrimmer");
		property.setDefaultValue("true");
		Validator val1 = new Validator();
		val1.setRequired(false);
		property.setValidator(val1);
		property_group.addProperty(property);
		
		Property property2 = new Property();
		property2.setName("readLength");
		property2.setDefaultValue("36");
		Validator val2 = new Validator();
		val2.setRequired(true);
		property2.setValidator(val2);
		property_group.addProperty(property2);
		
		template.addPropertyGroup(property_group);
		
		
		/** Json obtained from user **/
		
		JSONObject user_data = new JSONObject();
		
		user_data.put("name", "test_data");
		user_data.put("description", "none");
		user_data.put("template_id", 5);
		
		JSONObject user_config = new JSONObject();
		
		user_config.put("skipTrimmer","true");
		user_config.put("readLength", "72");
		
		user_data.put("config",user_config);
		
		
		/** compute the transformation (delta) object by SaveTransform **/
		
		SaveExperiment ex =  new SaveExperiment();
		
		JSONObject delta_transformation = ex.saveExperiment(template, user_data.toString());
		
		/** test for validity **/
		
		JSONObject delta_config = delta_transformation.getJSONObject("config");
		assertEquals(1, delta_config.size());
		assertEquals(72, delta_config.getInt("readLength"));
		
		
		
		
	}
	
	
}
