package org.iplantc.workflow.marshaller;

import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.template.groups.TemplateGroup;

/**
 * A template group marshaler that does not include analyses.
 *
 * @author Dennis Roberts
 */
public class SimpleTemplateGroupMarshaller {

    /**
     * Used to obtain data access objects.
     */
    private DaoFactory daoFactory;

    /**
     * @param daoFactory used to obtain data access objects.
     */
    public SimpleTemplateGroupMarshaller(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Marshals a template group.
     *
     * @param templateGroup the template group to marshal.
     * @return the marshaled template group.
     */
    public JSONObject marshalTemplateGroup(TemplateGroup templateGroup) {
        JSONObject json = new JSONObject();
        json.put("name", templateGroup.getName());
        json.put("id", templateGroup.getId());
        json.put("description", templateGroup.getDescription());
        json.put("groups", marshalSubgroups(templateGroup.getSub_groups()));
        json.put("template_count", templateGroup.countActiveTemplates());
        json.put("is_public", isInPublicWorkspace(templateGroup));
        return json;
    }

    /**
     * Marshals a list of template groups.
     *
     * @param templateGroups the list of template groups.
     * @return the marshaled list of template groups.
     */
    private JSONArray marshalSubgroups(List<TemplateGroup> templateGroups) {
        JSONArray array = null;
        if (!templateGroups.isEmpty()) {
            array = new JSONArray();
            for (TemplateGroup templateGroup : templateGroups) {
                array.add(marshalTemplateGroup(templateGroup));
            }
        }
        return array;
    }

    /**
     * Determines if the template group is in a public workspace.
     * 
     * @param templateGroup the template group.
     * @return true if the template group is in a public workspace.
     */
    private boolean isInPublicWorkspace(TemplateGroup templateGroup) {
        Workspace workspace = daoFactory.getWorkspaceDao().findById(templateGroup.getWorkspaceId());
        return workspace == null ? false : workspace.getIsPublic();
    }
}
