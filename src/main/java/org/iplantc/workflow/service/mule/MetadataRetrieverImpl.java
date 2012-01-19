package org.iplantc.workflow.service.mule;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.service.MetadataRetriever;
import org.mule.DefaultMuleMessage;
import org.mule.RequestContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;

/**
 * Used to obtain metadata JSON strings from the export services.
 * 
 * @author Dennis Roberts
 */
public class MetadataRetrieverImpl extends AbstractMuleClient implements MetadataRetriever {

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getTemplateFromAnalysis(DaoFactory daoFactory, String analysisId) {
        JSONObject result = null;
        try {
            MuleEventContext eventContext = RequestContext.getEventContext();
            MuleMessage request = new DefaultMuleMessage(new Object[]{daoFactory, analysisId});
            MuleMessage response = eventContext.sendEvent(request, "vm://getTemplateFromAnalysis");
            checkForExceptionPayload("unable to get template from analysis " + analysisId, response);
            result = (JSONObject) JSONSerializer.toJSON((String) response.getPayload(String.class));
        }
        catch (MuleException e) {
            String msg = "unable to get template from analysis " + analysisId;
            LOG.error(msg, e);
            throw new WorkflowException(msg, e);
        }
        return result;
    }
}
