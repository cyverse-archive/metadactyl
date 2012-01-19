package org.iplantc.workflow.experiment;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.iplantc.workflow.tools.UrlEncodingListTransformer;

/**
 * Used to assemble iRODS URLs.
 * 
 * @author Dennis Roberts
 */
public class IrodsUrlAssembler implements UrlAssembler {

    /**
     * The iRODS protocol.
     */
    private static final String PROTOCOL = "irods";

    /**
     * The name of the iRODS proxy user.
     */
    private String user;

    /**
     * The password for the iRODS proxy account.
     */
    private String password;

    /**
     * The iRODS zone.
     */
    private String zone;

    /**
     * The iRODS host.
     */
    private String host;

    /**
     * The iRODS port.
     */
    private String port;

    /**
     * @param user the name of the iRODS proxy user.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the name of the iRODS proxy user.
     */
    public String getUser() {
        return user;
    }

    /**
     * @param password the password for the iRODS proxy user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password for the iRODS proxy user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param zone the iRODS zone.
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * @return the iRODS zone.
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param host the iRODS host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the iRODS host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @param port the iRODS port.
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the iRODS port.
     */
    public String getPort() {
        return port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assembleUrl(String path) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PROTOCOL).append("://");
        buffer.append(user).append(":").append(password).append("@").append(host).append(":").append(port);
        buffer.append(urlEncodePath(path));
        return buffer.toString();
    }

    /**
     * URL encodes a path without encoding the slashes in the path.
     * 
     * @param path the path to encode.
     * @return the encoded path.
     */
    private String urlEncodePath(String path) {
        return StringUtils.join(new UrlEncodingListTransformer().transform(Arrays.asList(path.split("/"))), "/");
    }
}
