package org.iplantc.workflow.user;

import org.apache.log4j.Logger;
import org.iplantc.authn.user.User;

/**
 * Details about an iPlant user (typically the current user).
 * 
 * @author Dennis Roberts
 */
public class UserDetails {

    private static final Logger LOG = Logger.getLogger(UserDetails.class);

    /**
     * The fully qualified username.
     */
    private String username;
    
    /**
     * The user's password if it's available.
     */
    private String password;

    /**
     * The user's e-mail address.
     */
    private String email;

    /**
     * The username without the qualifying information.
     */
    private String shortUsername;

    /**
     * @return the user's e-mail address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the user's password if it's available.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username without the qualifying information.
     */
    public String getShortUsername() {
        return shortUsername;
    }

    /**
     * @return the fully qualified username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param user the user information from the user session service.
     */
    public UserDetails(User user) {
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        shortUsername = user.getShortUsername();
        LOG.debug("New User Details Created:\n"
                + "\tusername = " + username + "\n"
                + "\tpassword = " + password + "\n"
                + "\temail = " + email + "\n"
                + "\tshortUsername = " + shortUsername);
    }

    /**
     * This constructor is used exclusively for unit testing.
     * 
     * @param username the fully qualified username.
     * @param password the password if it's available.
     * @param email the user's e-mail address.
     * @param shortUsername the username without the qualifying information.
     */
    public UserDetails(String username, String password, String email, String shortUsername) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.shortUsername = shortUsername;
    }
}
