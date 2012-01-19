package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve the list of known information types.
 * 
 * @author Dennis Roberts
 */
public class InfoTypeRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query and list name.
     */
    protected InfoTypeRetrievalService() {
        super("from InfoType where deprecated is false order by displayOrder", "info_types");
    }

    /**
     * Initializes the superclass with the appropriate query and list name and sets the session factory.
     * 
     * @param sessionFactory
     */
    public InfoTypeRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
