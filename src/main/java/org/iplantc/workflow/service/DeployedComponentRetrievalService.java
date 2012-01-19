package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve a list of known deployed components.
 * 
 * @author Dennis Roberts
 */
public class DeployedComponentRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query.
     */
    protected DeployedComponentRetrievalService() {
        super("from DeployedComponent", "components");
    }

    /**
     * Initializes the superclass with the appropriate query and sets the session factory.
     * 
     * @param sessionFactory the session factory.
     */
    public DeployedComponentRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
