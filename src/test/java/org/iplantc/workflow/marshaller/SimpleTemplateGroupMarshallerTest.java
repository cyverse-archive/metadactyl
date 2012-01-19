package org.iplantc.workflow.marshaller;

import net.sf.json.JSONArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.workspace.Workspace;
import org.iplantc.workflow.dao.mock.MockDaoFactory;
import org.iplantc.workflow.template.groups.TemplateGroup;
import org.iplantc.workflow.util.UnitTestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.marshaller.SimpleTemplateGroupMarshaller.
 *
 * @author Dennis Roberts
 */
public class SimpleTemplateGroupMarshallerTest {

    /**
     * A mock data access object for testing.
     */
    private MockDaoFactory daoFactory;

    /**
     * The instance being tested.
     */
    private SimpleTemplateGroupMarshaller marshaller;

    /**
     * The public workspace identifier.
     */
    private long publicWorkspaceId;

    /**
     * The private workspace identifier.
     */
    private long privateWorkspaceId;

    /**
     * Initializes each unit test.
     */
    @Before
    public void setUp() {
        initializeDaoFactory();
        marshaller = new SimpleTemplateGroupMarshaller(daoFactory);
    }

    /**
     * Initializes the data access object factory.
     */
    private void initializeDaoFactory() {
        daoFactory = new MockDaoFactory();
        publicWorkspaceId = addWorkspace(true);
        privateWorkspaceId = addWorkspace(false);
    }

    /**
     * Adds a workspace to the mock workspace DAO.
     * 
     * @param isPublic true if the workspace is public.
     * @return the workspace.
     */
    private long addWorkspace(boolean isPublic) {
        Workspace workspace = new Workspace();
        workspace.setIsPublic(isPublic);
        daoFactory.getWorkspaceDao().save(workspace);
        return workspace.getId();
    }

    /**
     * Verifies that the marshaler successfully marshals the root template group.
     */
    @Test
    public void shouldMarshalRootTemplateGroup() {
        JSONObject json = marshaller.marshalTemplateGroup(createTemplateGroup(privateWorkspaceId));
        assertEquals("root", json.getString("name"));
        assertEquals("rootid", json.getString("id"));
        assertEquals("rootdescription", json.getString("description"));
        assertFalse(json.getBoolean("is_public"));
        assertEquals(4, json.getInt("template_count"));
    }

    /**
     * Verifies that the marshaler marshals subgroups.
     */
    @Test
    public void shouldMarshalSubgroups() {
        JSONObject json = marshaller.marshalTemplateGroup(createTemplateGroup(privateWorkspaceId));
        JSONArray subgroups = json.getJSONArray("groups");
        assertEquals(2, subgroups.size());

        JSONObject subgroup1 = subgroups.getJSONObject(0);
        assertEquals("subgroup1", subgroup1.getString("name"));
        assertEquals("subgroup1id", subgroup1.getString("id"));
        assertEquals("subgroup1description", subgroup1.getString("description"));
        assertFalse(subgroup1.getBoolean("is_public"));
        assertEquals(2, subgroup1.getInt("template_count"));

        JSONObject subgroup2 = subgroups.getJSONObject(1);
        assertEquals("subgroup2", subgroup2.getString("name"));
        assertEquals("subgroup2id", subgroup2.getString("id"));
        assertEquals("subgroup2description", subgroup2.getString("description"));
        assertFalse(subgroup1.getBoolean("is_public"));
        assertEquals(1, subgroup2.getInt("template_count"));
    }

    /**
     * Verifies that public template groups are listed as public.
     */
    @Test
    public void shouldMarshalPublicRootTemplateGroup() {
        JSONObject json = marshaller.marshalTemplateGroup(createTemplateGroup(publicWorkspaceId));
        assertTrue(json.getBoolean("is_public"));
        JSONArray subgroups = json.getJSONArray("groups");
        assertEquals(2, subgroups.size());

        JSONObject subgroup1 = subgroups.getJSONObject(0);
        assertTrue(subgroup1.getBoolean("is_public"));

        JSONObject subgroup2 = subgroups.getJSONObject(1);
        assertTrue(subgroup1.getBoolean("is_public"));
    }

    /**
     * Creates a template group for testing.
     *
     * @param workspaceId the workspace identifier.
     * @return the template group.
     */
    private TemplateGroup createTemplateGroup(long workspaceId) {
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setName("root");
        templateGroup.setId("rootid");
        templateGroup.setDescription("rootdescription");
        templateGroup.setWorkspaceId(workspaceId);
        templateGroup.addGroup(createSubgroup("subgroup1", workspaceId, "bar", "baz"));
        templateGroup.addGroup(createSubgroup("subgroup2", workspaceId, "quux"));
        templateGroup.addTemplate(UnitTestUtils.createAnalysis("foo"));
        return templateGroup;
    }

    /**
     * Creates a template subgroup with the given name.
     * 
     * @param name the subgroup name.
     * @param workspaceId the workspace identifier.
     * @param analysisNames the names of the analyses in the subgroup.
     * @return the subgroup.
     */
    private TemplateGroup createSubgroup(String name, long workspaceId, String... analysisNames) {
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setName(name);
        templateGroup.setId(name + "id");
        templateGroup.setDescription(name + "description");
        templateGroup.setWorkspaceId(workspaceId);
        for (String analysisName : analysisNames) {
            templateGroup.addTemplate(UnitTestUtils.createAnalysis(analysisName));
        }
        return templateGroup;
    }
}
