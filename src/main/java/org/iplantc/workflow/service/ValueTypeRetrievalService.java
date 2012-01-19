package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve a list of known value types.
 * 
 * @author Dennis Roberts
 */
public class ValueTypeRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query and list name.
     */
    protected ValueTypeRetrievalService() {
        super("from ValueType", "value_types");
    }

    /**
     * Initializes the superclass with the appropriate query and list name and sets the session factory.
     */
    public ValueTypeRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
