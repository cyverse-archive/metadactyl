package org.iplantc.workflow.service.mule;

import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.service.WorkspaceInitializer;
import org.mule.DefaultMuleMessage;
import org.mule.RequestContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;

/**
 * Used to initialize a user's workspace.
 *
 * @author Dennis Roberts
 */
public class WorkspaceInitializerImpl extends AbstractMuleClient implements WorkspaceInitializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeWorkspace(DaoFactory daoFactory, String username) {
        try {
            MuleEventContext eventContext = RequestContext.getEventContext();
            MuleMessage message = new DefaultMuleMessage(new Object[]{daoFactory, username});
            MuleMessage response = eventContext.sendEvent(message, "vm://createWorkspace");
            checkForExceptionPayload("unable to initialize workspace for " + username, response);
        }
        catch (MuleException e) {
            String msg = "unable to initialize workspace for " + username;
            LOG.error(msg, e);
            throw new WorkflowException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Workspace getWorkspace(DaoFactory daoFactory) {
        try {
            MuleEventContext eventContext = RequestContext.getEventContext();
            MuleMessage request = new DefaultMuleMessage(new Object[]{daoFactory});
            MuleMessage response = eventContext.sendEvent(request, "vm://getOrCreateWorkspace");
            checkForExceptionPayload("unable to get the current user's workspace", response);
            return (Workspace) response.getPayload(Workspace.class);
        }
        catch (MuleException e) {
            String msg = "unable to get the current user's workspace";
            LOG.error(msg, e);
            throw new WorkflowException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Workspace getWorkspace(DaoFactory daoFactory, String username) {
        try {
            MuleEventContext eventContext = RequestContext.getEventContext();
            MuleMessage request = new DefaultMuleMessage(new Object[]{daoFactory, username});
            MuleMessage response = eventContext.sendEvent(request, "vm://getOrCreateWorkspaceForUsername");
            checkForExceptionPayload("unable to get the workspace for " + username, response);
            return (Workspace) response.getPayload(Workspace.class);
        }
        catch (MuleException e) {
            String msg = "unable to get the current user's workspace";
            LOG.error(msg, e);
            throw new WorkflowException(msg, e);
        }
    }
}
