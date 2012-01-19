package org.iplantc.workflow.user;

/**
 * Encapsulated user information.
 * 
 * @author Dennis Roberts
 */
public class UserInfo {

    /**
     * The workspace ID.
     */
    private String workspaceId;

    /**
     * @return the workspace ID.
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId the workspace ID.
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * @param workspaceId the workspace ID as a long integer.
     */
    public void setWorkspaceId(long workspaceId) {
        this.workspaceId = String.valueOf(workspaceId);
    }
}
