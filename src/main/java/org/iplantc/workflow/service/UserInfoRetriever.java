package org.iplantc.workflow.service;

import org.iplantc.workflow.user.UserDetails;

/**
 * Used to obtain information about the current user.
 * 
 * @author Dennis Roberts
 */
public interface UserInfoRetriever {

    /**
     * Obtains details about the current user.
     * 
     * @return the user details.
     */
    public UserDetails getCurrentUserDetails();
}
