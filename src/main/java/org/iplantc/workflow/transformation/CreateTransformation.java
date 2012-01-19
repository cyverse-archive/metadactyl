package org.iplantc.workflow.transformation;

import java.util.Iterator;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.persistence.dto.transformation.Transformation;

public class CreateTransformation extends HibernateAccessor {
    public void saveTransform(JSONObject delta) throws Exception {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transformation transform = buildTransformation(delta, session);
            session.save(transform);
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

    @SuppressWarnings("unchecked")
    private Transformation buildTransformation(JSONObject delta, Session session) throws Exception {
        JSONObject modified_properties = delta.getJSONObject("config");
        Transformation transform = new Transformation();
        transform.setName(delta.getString("name"));
        transform.setTemplate_id(delta.getString("template_id"));
        Iterator<String> keys = modified_properties.keys();
        
        while (keys.hasNext()) {
            String name = (String) keys.next();
            
            transform.addPropertyValue(name, modified_properties.getString(name));
        }
        return transform;
    }
}
