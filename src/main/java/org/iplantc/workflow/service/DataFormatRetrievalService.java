package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve the list of known data formats.
 * 
 * @author Dennis Roberts
 */
public class DataFormatRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query and list name.
     */
    protected DataFormatRetrievalService() {
        super("from DataFormat order by displayOrder", "formats");
    }

    /**
     * Initializes the superclass with the appropriate query and list name and sets the session factory.
     * 
     * @param sessionFactory the session factory.
     */
    public DataFormatRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
