package org.iplantc.workflow.template.notifications;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.marshaler.NotificationMarshaller;
import org.iplantc.workflow.marshaler.TemplateMarshaller;
import org.iplantc.workflow.model.Template;

public class NotificationAppender extends HibernateAccessor {

	public String appendNotificationToTemplate(Template template) throws Exception{


		TemplateMarshaller marshaller = new TemplateMarshaller();

		template.accept(marshaller);

		JSONObject jtemplate = (JSONObject) JSONSerializer.toJSON(marshaller.getMarshalledWorkflow());

		NotificationSetRetriever retriever = new NotificationSetRetriever();

		retriever.setSessionFactory(getSessionFactory());

		NotificationSet set = retriever.retrieveNotificationSetByTemplateId(template.getId());

		if(set!=null){
			NotificationMarshaller marshaller2 = new NotificationMarshaller();

			set.accept(marshaller2);

			JSONObject jnotification = (JSONObject) JSONSerializer.toJSON(marshaller2.getMarshalledWorkflow());



			jtemplate.put("wizardNotifications", jnotification.getJSONArray("wizardNotifications"));

		}

		return jtemplate.toString();

	}

}
