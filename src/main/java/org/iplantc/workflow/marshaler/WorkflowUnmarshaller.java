package org.iplantc.workflow.marshaler;

import org.iplantc.workflow.core.TransformationActivity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.step.TransformationStep;

public class WorkflowUnmarshaller extends BaseMarshaller {


	public TransformationActivity unmarshallTransformationTask(JSONObject workflow){

		TransformationActivity ttask =new TransformationActivity();


		ttask.setName(workflow.getString("name"));

		JSONArray steps = workflow.getJSONArray("steps");

		for(int i=0; i < steps.size(); i++){
			JSONObject jStep = steps.getJSONObject(i);

			TransformationStep st = unmarshallTransformationStep(jStep);

			ttask.addStep(st);

		}


		return ttask;
	}

	public TransformationStep unmarshallTransformationStep(JSONObject jstep){

		TransformationStep step = new TransformationStep();

		step.setName(jstep.getString("name"));
		step.setDescription(jstep.optString("description"));


		return step;

	}


}