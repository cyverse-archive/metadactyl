package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve the list of known data sources.
 * 
 * @author Dennis Roberts
 */
public class DataSourceRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * The default constructor.
     */
    protected DataSourceRetrievalService() {
        super("from DataSource order by id", "data_sources");
    }

    /**
     * @param sessionFactory the Hibernate session factory.
     */
    public DataSourceRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
