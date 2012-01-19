package org.iplantc.workflow.dao;

import org.iplantc.persistence.dto.components.DeployedComponent;

/**
 * Used to access persistent deployed components.
 *
 * @author Dennis Roberts
 */
public interface DeployedComponentDao extends GenericObjectDao<DeployedComponent> {

    /**
     * Finds a deployed component by its name and location.
     * 
     * @param name the name of the deployed component.
     * @param location the location of the deployed component
     * @return the deployed component or null if a match wasn't found.
     */
    public DeployedComponent findByNameAndLocation(String name, String location);

    /**
     * Finds a deployed component by its name.
     * 
     * @param name the name of the deployed component.
     * @return the deployed component or null if a match wasn't found.
     */
    public DeployedComponent findUniqueInstanceByName(String name);
}
