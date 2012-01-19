package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve the list of known property types.
 * 
 * @author Dennis Roberts
 */
public class PropertyTypeRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query.
     */
    protected PropertyTypeRetrievalService() {
        super("from PropertyType where deprecated is false order by displayOrder", "property_types");
    }

    /**
     * Initializes the superclass with the appropriate query and sets the session factory.
     * 
     * @param sessionFactory the session factory.
     */
    public PropertyTypeRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
