package org.iplantc.workflow.service.mule;

import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.service.UserInfoRetriever;
import org.iplantc.workflow.user.UserDetails;
import org.mule.DefaultMuleMessage;
import org.mule.RequestContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;

/**
 * Used to obtain information about the current user.
 * 
 * @author Dennis Roberts
 */
public class UserInfoRetrieverImpl extends AbstractMuleClient implements UserInfoRetriever {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails getCurrentUserDetails() {
        try {
            MuleEventContext eventContext = RequestContext.getEventContext();
            MuleMessage request = new DefaultMuleMessage("");
            MuleMessage response = eventContext.sendEvent(request, "vm://getCurrentUserDetails");
            checkForExceptionPayload("unable to get the current user's details", response);
            return (UserDetails) response.getPayload(UserDetails.class);
        }
        catch (MuleException e) {
            String msg = "unable to get the current user's details";
            LOG.error(msg, e);
            throw new WorkflowException(msg, e);
        }
    }
}
