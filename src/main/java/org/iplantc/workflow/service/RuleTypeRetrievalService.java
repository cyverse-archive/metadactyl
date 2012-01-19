package org.iplantc.workflow.service;

import org.hibernate.SessionFactory;

/**
 * A service that can be used to retrieve the list of known rule types.
 * 
 * @author Dennis Roberts
 */
public class RuleTypeRetrievalService extends BaseWorkflowElementRetrievalService {

    /**
     * Initializes the superclass with the appropriate query.
     */
    protected RuleTypeRetrievalService() {
        super("from RuleType where deprecated is false order by displayOrder", "rule_types");
    }

    /**
     * Initializes the superclass with the appropriate query and sets the session factory.
     * 
     * @param sessionFactory the Hibernate session factory.
     */
    public RuleTypeRetrievalService(SessionFactory sessionFactory) {
        this();
        setSessionFactory(sessionFactory);
    }
}
