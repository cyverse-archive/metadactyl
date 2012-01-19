package org.iplantc.workflow.marshaller;

import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;

import org.json.JSONObject;

public class WorkflowMarshaller extends BaseMarshaller {
	public void visit(TransformationActivity workflow) throws WorkflowException {
        try{
            JSONObject json = createJsonObject();

            json.put("id", workflow.getId());
            json.put("name", workflow.getName());
            json.put("description", workflow.getDescription());


            jsonStack.push(json);
		} catch(Exception ex) {
			throw new WorkflowException(ex);
		}
	}
	
	public void leave(TransformationActivity workflow) throws WorkflowException {
		jsonStack.pop();
	}

	public void visit(TransformationStep step) throws WorkflowException{
        try {
			JSONObject json = new JSONObject();
			
			json.put("id", step.getId());
			json.put("name", step.getName());
			json.put("description", step.getDescription());
			
			appendToParentProperty("steps", json);
			
			jsonStack.push(json);
			
		} catch(Exception e) {
			throw new WorkflowException(e);
		}
	}
	
	public void leave(TransformationStep step) throws WorkflowException {
		jsonStack.pop();
	}
}
