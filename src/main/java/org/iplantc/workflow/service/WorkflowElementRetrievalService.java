package org.iplantc.workflow.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A general service used to obtain lists of workflow elements.
 * 
 * @author Dennis Roberts
 */
public class WorkflowElementRetrievalService {

    /**
     * The Hibernate session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * Used to map component types to sub-services.
     */
    private Map<String, BaseWorkflowElementRetrievalService> subServiceMap;

    /**
     * Sets the session factory.  The sub-service map is also reinitialized after the session factory is set because
     * all sub-services need to have the session factory.
     * 
     * @param sessionFactory the session factory.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        initialize();
    }

    /**
     * Initializes the sub-service map.
     */
    private void initialize() {
        subServiceMap = new HashMap<String, BaseWorkflowElementRetrievalService>();
        subServiceMap.put("components", new DeployedComponentRetrievalService(sessionFactory));
        subServiceMap.put("formats", new DataFormatRetrievalService(sessionFactory));
        subServiceMap.put("info-types", new InfoTypeRetrievalService(sessionFactory));
        subServiceMap.put("property-types", new PropertyTypeRetrievalService(sessionFactory));
        subServiceMap.put("rule-types", new RuleTypeRetrievalService(sessionFactory));
        subServiceMap.put("value-types", new ValueTypeRetrievalService(sessionFactory));
    }

    /**
     * Gets either the list of all workflow elements or the list of elements of a specific type.
     * 
     * @param elementType the type of element to search for.
     * @return the list of elements of the given type.
     * @throws RuntimeException if the element type isn't recognized.
     */
    public String getElements(String elementType) throws RuntimeException {
        return elementType.equals("all") ? getAllElements() : getElementsOfType(elementType);
    }

    /**
     * Gets all of the lists of workflow element types that this service supports.
     * 
     * @return the lists of workflow element types as a JSON string.
     */
    @SuppressWarnings("unchecked")
    private String getAllElements() {
        JSONObject cumulativeResult = new JSONObject();
        try {
            for (String key : subServiceMap.keySet()) {
                JSONObject serviceResult = subServiceMap.get(key).retrieve();
                Iterator<String> subkeys = serviceResult.keys();
                while (subkeys.hasNext()) {
                    String subkey = subkeys.next();
                    cumulativeResult.put(subkey, serviceResult.get(subkey));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return cumulativeResult.toString();
    }

    /**
     * Gets the list of elements of a specific type.
     * 
     * @param elementType the type of element to search for.
     * @return the list of elements of the given type.
     * @throws RuntimeExcerption if the element type isn't recognized.
     */
    private String getElementsOfType(String elementType) throws RuntimeException {
        BaseWorkflowElementRetrievalService service = subServiceMap.get(elementType);
        if (service == null) {
            throw new RuntimeException("unrecognized workflow component type");
        }
        return service.retrieve().toString();
    }
}
