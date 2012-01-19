package org.iplantc.workflow.marshaller;

import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.template.groups.TemplateGroup;

public class TemplateGroupMarshaller {

    private DaoFactory daoFactory;
    
    private TemplateGroup favorites;

    public TemplateGroupMarshaller(DaoFactory daoFactory, TemplateGroup favorites) {
        this.daoFactory = daoFactory;
        this.favorites = favorites;
    }

    public JSONObject marshal(TemplateGroup group) {
        JSONObject json = null;
        json = new JSONObject();
        json.put("name", group.getName());
        json.put("description", group.getDescription());
        json.put("id", group.getId());
        json.put("templates", marshalTemplates(group.getAllAnalyses()));
        json.put("template_count", group.countActiveTemplates());
        json.put("is_public", isPublic(group));
        return json;
    }

    private Object marshalTemplates(List<TransformationActivity> templates) {
        JSONArray array = new JSONArray();
        for (TransformationActivity template : templates) {
            JSONObject json = marshalTemplate(template);
            if (json != null) {
                array.add(json);
            }
        }
        return array;
    }

    private JSONObject marshalRating(TransformationActivity template) {
        JSONObject result = new JSONObject();

        result.put("average", template.getAverageRating());
        return result;
    }

    private JSONObject marshalTemplate(TransformationActivity template) {
        JSONObject json = null;
        if (!template.isDeleted()) {
            json = new JSONObject();
            json.put("id", template.getId());
            json.put("name", template.getName());
            json.put("description", template.getDescription());
            
            String name = "";
            String email = "";
            
            if(template.getIntegrationDatum() != null) {
                name = template.getIntegrationDatum().getIntegratorName();
                email = template.getIntegrationDatum().getIntegratorEmail();
            }
            
            json.put("integrator_email", email);
            json.put("integrator_name", name);
            
            if(template.getIntegrationDate() != null) {
                json.put("integration_date", template.getIntegrationDate().getTime());
            }
            
            json.put("rating", marshalRating(template));
            json.put("is_public", isPublic(template));
            json.put("is_favorite", favorites.containsAnalysis(template));
            json.put("wiki_url", template.getWikiurl());
        }
        return json;
    }

    private boolean isPublic(TemplateGroup group) {
        boolean result = false;
        Workspace workspace = daoFactory.getWorkspaceDao().findById(group.getWorkspaceId());
        if (workspace != null) {
            result = workspace.getIsPublic();
        }
        return result;
    }

    private boolean isPublic(TransformationActivity template) {
        boolean result = false;
        for (TemplateGroup group : daoFactory.getTemplateGroupDao().findTemplateGroupsContainingAnalysis(template)) {
            if (isPublic(group)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
