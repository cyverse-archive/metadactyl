package org.iplantc.workflow.validation;

/**
 * This class provides an initial validation for the experiments submited
 * by a user by calling the save experiment service.
 * 
 * 
 * @author Juan Antonio Raygoza Garay
 * 
 */


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.RuleType;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;

public class ExperimentValidator {

	private String error;
	
	
	public ExperimentValidator() {
		error="\n";
	}
	
	
	public boolean isValid(Template template, JSONObject userExperiment) throws Exception{
		
		boolean valid = true;
		
		
		try{
		JSONObject config = userExperiment.getJSONObject("config");
		
		List<PropertyGroup> property_groups = template.getPropertyGroups();
		
		for(int i=0; i < property_groups.size(); i++ ){
			
			List<Property> props = property_groups.get(i).getProperties();
			
			for(int j=0; j < props.size(); j++){
				
				Property prop = props.get(j);
				JSONArray values = config.getJSONArray(prop.getName());

				valid = isValid(props.get(j),values);
				
			}
			
		}
		}catch(Exception ex){
			throw ex;
		}
		
		return valid;
	}
	
	@SuppressWarnings("rawtypes")
    public boolean isValid(Property property, List values) throws Exception
	{
		
		if(property.getValidator()==null) return true;
		
		Validator validator = property.getValidator();
		
		
		if(values.size()==0 && validator.isRequired()){
			error+= "Property "+ property.getName() +" is required and wasn't found in the experiment definition.\n";
			return false;
		}
		
		
		List<Rule> rules = validator.getRules();
		
		for(int i=0; i < rules.size(); i++){
			Rule rule = rules.get(i);
			
			RuleType rule_type = rule.getRuleType();
			
			List<String> arguments = rule.getArguments();
			
			if(rule_type.equals("IntAbove")){
				int my_int=0;
				
				if(values.size()!=1){
					error+="The number of arguments for Property "+property.getName()+" must be exactly one.\n";
					return false;
				}
				
				try{
					my_int = Integer.parseInt(values.get(0).toString().trim());
				}catch(NumberFormatException nex){
					error+="The specified value for Property "+property.getName()+"  must be an integer.\n";
					return false;
				}
				
				
				
				int int_arg = Integer.parseInt(arguments.get(0).trim());
				
				if(int_arg <= my_int){
					error+= "The value supplied for the Property "+property.getName()+"  must be grater than "+arguments.get(0);
					return false;
				}
				
				continue;
				
			}
			
			if(rule_type.equals("MustContain")){
				
				if(values.size()!=1){
					error+="The number of arguments for Property "+property.getName()+" must be exactly one.\n";
					return false;
				}
				
				Set<String>  value_set = new HashSet<String>(arguments);
				
				if(!value_set.contains(values.get(0).toString())){
					error+= "The value supplied for Property "+property.getName()+" must be one of ("+arguments.get(0);
					
					for(int k=1; k < arguments.size();k++ ){
						error+=","+arguments.get(k);
					}
					
					error+="). Note that values are case sensitive.";
					return false;
				}
				
				
				continue;
			}
			
			if(rule_type.equals("DoubleRange")){
				
				if(values.size()!=2){
					error+="The number of arguments for Property "+property.getName()+" must be two.\n";
					return false;
				}
				
				double low=0.0;
				double high =0.0;
				
				try{
					 low = Double.parseDouble(values.get(0).toString());
				}catch(NumberFormatException nex){
					error+= "The first argument for Property" + property.getName()+" must be a valid real number.\n";
					return false;
				}
				
				try{
					 high = Double.parseDouble(values.get(1).toString());
				}catch(NumberFormatException nex){
					error+= "The second argument for Property" + property.getName()+" must be a valid real number.\n";
					return false;
				}
				
				
				if(high < low){
					error+= "Please check the order of the arguments supplied for Property "+property.getName()+".\n";
				}
				
				
				continue;
				
				
			}
			
			if(rule_type.equals("NonEmptyArray")){
				
				if(values.size()==0){
					error+="The number of arguments for Property "+property.getName()+" must be grater than zero.\n";
					return false;
				}
				
				continue;
				
				
			}
			
			
		}
		
		
		
		return true;
	}
	
	public String getErrorString(){
		return error;
	}
	
	
	
	
	
	
}
