package org.iplantc.workflow.service.mule;

import org.apache.log4j.Logger;
import org.iplantc.workflow.WorkflowException;
import org.mule.api.ExceptionPayload;
import org.mule.api.MuleMessage;

/**
 * An abstract Mule client class implementation.
 * 
 * @author Dennis Roberts
 */
public abstract class AbstractMuleClient {

    /**
     * Used to log error messages.
     */
    protected final Logger LOG;

    /**
     * The default constructor.
     */
    public AbstractMuleClient() {
        LOG = Logger.getLogger(getClass());
    }

    /**
     * Checks for an exception payload in a response.
     * 
     * @param msg the detail message to log if an exception is detected.
     * @param response the response.
     */
    protected void checkForExceptionPayload(String msg, MuleMessage response) {
        ExceptionPayload exceptionPayload = response.getExceptionPayload();
        if (exceptionPayload != null) {
            LOG.error(msg, exceptionPayload.getException());
            throw new WorkflowException(msg, exceptionPayload.getException());
        }
    }
}
