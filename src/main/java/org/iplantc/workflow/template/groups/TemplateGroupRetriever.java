package org.iplantc.workflow.template.groups;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.hibernate.util.SessionTask;
import org.iplantc.hibernate.util.SessionTaskWrapper;
import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.Rating;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.marshaller.TemplateGroupMarshaller;
import org.iplantc.workflow.service.RatingService;
import org.iplantc.workflow.service.WorkspaceInitializer;
import org.iplantc.workflow.service.mule.WorkspaceInitializerImpl;

public class TemplateGroupRetriever extends HibernateAccessor {

    private RatingService ratingService;

    private int favoritesAnalysisGroupIndex;

    private WorkspaceInitializer workspaceInitializer = new WorkspaceInitializerImpl();

    public String retrievePublicAnalyses() {
        return new SessionTaskWrapper(getSessionFactory()).performTask(new SessionTask<String>() {
            @Override
            public String perform(Session session) {
                return marshalPublicAnalyses(new HibernateDaoFactory(session)).toString();
            }
        });
    }

    public String retrieveTemplateGroupById(final String templateGroupId) throws Exception {
        return new SessionTaskWrapper(getSessionFactory()).performTask(new SessionTask<String>() {
            @Override
            public String perform(Session session) {
                return retrieveTemplateGroupByIdInternal(new HibernateDaoFactory(session), templateGroupId).toString();
            }
        });
    }

    public JSONObject retrieveTemplateGroupByIdInternal(DaoFactory daoFactory, String templateGroupId) {
        JSONObject output = marshalTemplateGroup(daoFactory, templateGroupId);
        injectUserRatings(output);
        return output;
    }

    private void injectUserRatings(JSONObject output) {
        Map<String, Rating> ratings = ratingService.getUserRatings();
        JSONArray templates = output.getJSONArray("templates");

        for (int i = 0; i < templates.size(); i++) {
            JSONObject template = templates.getJSONObject(i);
            Rating rating = ratings.get(template.getString("id"));

            if (rating != null) {
                template.getJSONObject("rating").put("user", rating.getRaiting());
            }
        }
    }

    private TemplateGroup getFavoritesGroup(DaoFactory daoFactory) {
        Workspace workspace = workspaceInitializer.getWorkspace(daoFactory);
        TemplateGroup root = daoFactory.getTemplateGroupDao().findByHid(workspace.getRootAnalysisGroupId());
        if (root == null) {
            throw new WorkflowException("user's workspace is not initializeed");
        }
        if (root.getSub_groups().size() <= favoritesAnalysisGroupIndex) {
            throw new WorkflowException("unable to find favorites group in user's workspace");
        }
        return root.getSub_groups().get(favoritesAnalysisGroupIndex);
    }

    private JSONObject marshalTemplateGroup(DaoFactory daoFactory, String templateGroupId) {
        TemplateGroup favorites = getFavoritesGroup(daoFactory);
        TemplateGroupMarshaller marshaller = new TemplateGroupMarshaller(daoFactory, favorites);
        return marshaller.marshal(getTemplateGroup(daoFactory, templateGroupId));
    }

    private JSONObject marshalPublicAnalyses(DaoFactory daoFactory) {
        List<TemplateGroup> groups = getPublicGroups(daoFactory);
        TemplateGroupMarshaller marshaller = new TemplateGroupMarshaller(daoFactory, new NullTemplateGroup());
        JSONObject json = new JSONObject();
        json.put("groups", marshalGroups(marshaller, groups));
        return json;
    }

    private JSONArray marshalGroups(TemplateGroupMarshaller marshaller, List<TemplateGroup> groups) {
        JSONArray array = new JSONArray();
        for (TemplateGroup group : groups) {
            array.add(marshaller.marshal(group));
        }
        return array;
    }

    private List<TemplateGroup> getPublicGroups(DaoFactory daoFactory) {
        List<TemplateGroup> groups = new ArrayList<TemplateGroup>();
        addPublicRootTemplateGroups(groups, daoFactory);
        return groups;
    }

    private TemplateGroup getRootTemplateGroupForWorkspace(DaoFactory daoFactory, Workspace workspace) {
        Long groupId = workspace.getRootAnalysisGroupId();
        return groupId == null ? null : daoFactory.getTemplateGroupDao().findByHid(groupId);
    }

    private void addPublicRootTemplateGroups(List<TemplateGroup> groups, DaoFactory daoFactory) {
        for (Workspace workspace : daoFactory.getWorkspaceDao().findPublicWorkspaces()) {
            TemplateGroup group = getRootTemplateGroupForWorkspace(daoFactory, workspace);
            if (group != null && !groups.contains(group)) {
                groups.add(group);
            }
        }
    }

    private TemplateGroup getTemplateGroup(DaoFactory daoFactory, String id) {
        TemplateGroup group = daoFactory.getTemplateGroupDao().findById(id);
        if (group == null) {
            throw new WorkflowException("template with id, " + id + ", not found");
        }
        return group;
    }

    public RatingService getRatingService() {
        return ratingService;
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public int getFavoritesAnalysisGroupIndex() {
        return favoritesAnalysisGroupIndex;
    }

    public void setFavoritesAnalysisGroupIndex(int favoritesAnalysisGroupIndex) {
        this.favoritesAnalysisGroupIndex = favoritesAnalysisGroupIndex;
    }
}
