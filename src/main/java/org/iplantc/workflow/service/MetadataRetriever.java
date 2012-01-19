package org.iplantc.workflow.service;

import net.sf.json.JSONObject;
import org.iplantc.workflow.dao.DaoFactory;

/**
 * Used to get metadata JSON from various export services.
 * 
 * @author Dennis Roberts
 */
public interface MetadataRetriever {
    
    /**
     * Retrieves a template from an analysis using the workflow export service.
     * 
     * @param daoFactory used to obtain data access objects.
     * @param analysisId the analysis identifier.
     * @return the template JSON.
     */
    public JSONObject getTemplateFromAnalysis(DaoFactory daoFactory, String analysisId);
}
