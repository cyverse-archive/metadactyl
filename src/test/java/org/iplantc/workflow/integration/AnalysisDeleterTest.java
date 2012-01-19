package org.iplantc.workflow.integration;

import org.iplantc.persistence.dto.user.User;
import org.iplantc.persistence.dto.workspace.Workspace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.mock.MockDaoFactory;
import org.iplantc.workflow.dao.mock.MockTemplateGroupDao;
import org.iplantc.workflow.dao.mock.MockTransformationActivityDao;
import org.iplantc.workflow.dao.mock.MockUserDao;
import org.iplantc.workflow.dao.mock.MockWorkspaceDao;
import org.iplantc.workflow.template.groups.TemplateGroup;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.crud.AnalysisDeleter.
 * 
 * @author Dennis Roberts
 */
public class AnalysisDeleterTest {

    /**
     * Used to obtain data access objects.
     */
    private MockDaoFactory daoFactory;

    /**
     * A mock analysis DAO used for testing.
     */
    private MockTransformationActivityDao analysisDao;

    /**
     * A mock template group data access object used for testing.
     */
    private MockTemplateGroupDao analysisGroupDao;

    /**
     * A mock user data access object used for testing.
     */
    private MockUserDao userDao;

    /**
     * A mock workspace data access object used for testing.
     */
    private MockWorkspaceDao workspaceDao;

    /**
     * The analysis deleter instance being tested.
     */
    private AnalysisDeleter deleter;

    /**
     * Sets up each of the unit tests.
     */
    @Before
    public void setUp() {
        initializeDaoFactory();
        initializeAnalysisDao();
        initializeUserDao();
        initializeWorkspaceDao();
        initializeAnalysisGroupDao();
        initializeAnalysisDeleter();
    }

    /**
     * Initializes the data access object factory.
     */
    private void initializeDaoFactory() {
        daoFactory = new MockDaoFactory();
    }

    /**
     * Initializes the analysis data access object.
     */
    private void initializeAnalysisDao() {
        analysisDao = daoFactory.getMockTransformationActivityDao();
        analysisDao.save(createAnalysis("foo"));
    }

    /**
     * Initializes the user data access object.
     */
    private void initializeUserDao() {
        userDao = daoFactory.getMockUserDao();
        userDao.save(createUser("somebody@iplantcollaborative.org"));
        userDao.save(createUser("nobody@iplantcollaborative.org"));
    }

    /**
     * Initializes the workspace data access object.
     */
    private void initializeWorkspaceDao() {
        workspaceDao = daoFactory.getMockWorkspaceDao();
        workspaceDao.save(createWorkspace("somebody@iplantcollaborative.org"));
        workspaceDao.save(createWorkspace("nobody@iplantcollaborative.org"));
    }

    /**
     * Initializes the analysis group data access object.
     */
    private void initializeAnalysisGroupDao() {
        analysisGroupDao = daoFactory.getMockTemplateGroupDao();
        analysisGroupDao.save(createAnalysisGroup());
    }

    /**
     * Initialize the analysis deleter.
     */
    private void initializeAnalysisDeleter() {
        deleter = new AnalysisDeleter(daoFactory);
    }

    /**
     * Creates a template group containing the test analysis.
     * 
     * @return the template group.
     */
    private TemplateGroup createAnalysisGroup() {
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setWorkspaceId(workspaceDao.getSavedObjects().get(0).getId());
        templateGroup.addTemplate(analysisDao.getSavedObjects().get(0));
        return templateGroup;
    }

    /**
     * Creates a workspace to use for testing.
     * 
     * @param email the e-mail address of the user that owns the workspace.
     * @return the workspace.
     */
    private Workspace createWorkspace(String email) {
        Workspace workspace = new Workspace();
        workspace.setIsPublic(false);
        workspace.setUser(userDao.findByUsername(email));
        return workspace;
    }

    /**
     * Creates a user with the given e-mail address.
     * 
     * @param email the e-mail address.
     * @return the user.
     */
    private User createUser(String email) {
        User user = new User();
        user.setUsername(email);
        return user;
    }

    /**
     * Creates a new analysis.
     * 
     * @param id the identifier of the new analysis.
     * @return the analysis.
     */
    private TransformationActivity createAnalysis(String id) {
        return createAnalysis(id, id + "Name");
    }

    /**
     * Creates a new analysis.
     * 
     * @param id the identifier of the new analysis.
     * @param name the name of the new analysis.
     * @return the new analysis.
     */
    private TransformationActivity createAnalysis(String id, String name) {
        TransformationActivity analysis = new TransformationActivity();
        analysis.setId(id);
        analysis.setName(name);
        analysis.setDescription(id + "Description");
        analysis.setDeleted(false);
        return analysis;
    }

    /**
     * Verifies that we can delete an analysis by ID.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldLogicallyDeleteAnalysisById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that we can delete an analysis by ID using an old-style deletion request.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldLogicallyDeleteAnalysisByIdUsingOldStyleRequest() throws Exception {
        JSONObject json = createOldStyleDeletionRequest("foo", null, "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that only the specified analysis is deleted.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldNotDeleteUnidentifiedAnalyses() throws Exception {
        analysisDao.save(createAnalysis("bar"));
        JSONObject json = createDeletionRequest("foo", null, "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(2, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
        assertFalse(analysisDao.getSavedObjects().get(1).isDeleted());
    }

    /**
     * Verifies that we can delete an analysis by name.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldLogicallyDeleteAnalysisByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that we can delete an analysis by name using an old-style deletion request.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldLogicallyDeleteAnalysisByNameUsingOldStyleRequest() throws Exception {
        JSONObject json = createOldStyleDeletionRequest(null, "fooName", "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that we can delete multiple analyses with the same name.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldDeleteMultipleAnalysesByName() throws Exception {
        analysisDao.save(createAnalysis("bar", "fooName"));
        JSONObject json = createDeletionRequest(null, "fooName", "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
        assertEquals(2, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
        assertTrue(analysisDao.getSavedObjects().get(1).isDeleted());
    }

    /**
     * Verifies that we get an exception if we send in a deletion request for an unknown analysis ID.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForUnknownAnalysisId() throws Exception {
        JSONObject json = createDeletionRequest("unknownid", null, "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we send in a deletion request for an unknown analysis name.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForUnknownAnalysisName() throws Exception {
        JSONObject json = createDeletionRequest(null, "unknownname", "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we send in a deletion request that doesn't specify an analysis ID or name.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForDeletionRequestWithoutIdOrName() throws Exception {
        JSONObject json = createDeletionRequest(null, null, "somebody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we try to delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldNotLogicallyDeleteAnalysisVisibleToAnotherUserById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we try to delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldNotLogicallyDeleteAnalysisVisibleToAnotherUserByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", false);
        deleter.logicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we try to delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldNotPhysicallyDeleteAnalysisVisibleToAnotherUserById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", false);
        deleter.physicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if we try to delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldNotPhysicallyDeleteAnalysisVisibleToAnotherUserByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", false);
        deleter.physicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if no e-mail address is provided for a non-root deletion request.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForMissingEmailAddress() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, null, false);
        deleter.physicallyDelete(json);
    }

    /**
     * Verifies that we get an exception if a user without a workspace submits a non-root deletion request.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = WorkflowException.class)
    public void shouldGetExceptionForMissingWorkspace() throws Exception {
        workspaceDao.deleteByUser(userDao.findByUsername("somebody@iplantcollaborative.org"));
        JSONObject json = createDeletionRequest("foo", null, "somebody@iplantcollaborative.org", false);
        deleter.physicallyDelete(json);
    }

    /**
     * Verifies that a root deletion request can logically delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallyDeleteAnalysesVisibleToAnotherUserById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", true);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that a root deletion request can logically delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallyDeleteAnalysesVisibleToAnotherUserByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", true);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that a root deletion request can physically delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanPhysicallyDeleteAnalysesVisibleToAnotherUserById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", true);
        deleter.physicallyDelete(json);
        assertEquals(0, analysisDao.getSavedObjects().size());
    }

    /**
     * Verifies that a root deletion request can physically delete an analysis that is visible to another user.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallPhysicallyDeleteAnalysesVisibleToAnotherUserByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", true);
        deleter.physicallyDelete(json);
        assertEquals(0, analysisDao.getSavedObjects().size());
    }

    /**
     * Verifies that root deletion requests can logically delete analyses without providing an e-mail address.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallyDeleteAnalysesWithoutEmailById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", true);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that root deletion requests can logically delete analyses without providing an e-mail address.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallyDeleteAnalysesWithoutEmailByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", true);
        deleter.logicallyDelete(json);
        assertEquals(1, analysisDao.getSavedObjects().size());
        assertTrue(analysisDao.getSavedObjects().get(0).isDeleted());
    }

    /**
     * Verifies that root deletion requests can physically delete analyses without providing an e-mail address.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanPhysicallyDeleteAnalysesWithoutEmailById() throws Exception {
        JSONObject json = createDeletionRequest("foo", null, "nobody@iplantcollaborative.org", true);
        deleter.physicallyDelete(json);
        assertEquals(0, analysisDao.getSavedObjects().size());
    }

    /**
    /**
     * Verifies that root deletion requests can physically delete analyses without providing an e-mail address.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void rootDeletionRequestsCanLogicallPhysicallyDeleteAnalysesWithoutEmailByName() throws Exception {
        JSONObject json = createDeletionRequest(null, "fooName", "nobody@iplantcollaborative.org", true);
        deleter.physicallyDelete(json);
        assertEquals(0, analysisDao.getSavedObjects().size());
    }

    /**
     * Creates an old-style deletion request, which still needs to be supported.
     * 
     * @param id the analysis ID.
     * @param name the analysis name.
     * @param email the e-mail address of the user requesting the deletion.
     * @param rootRequest true if this is a "root" deletion request.
     * @return the deletion request.
     * @throws JSONException if we try to build an invalid JSON object.
     */
    private JSONObject createOldStyleDeletionRequest(String id, String name, String email, Boolean rootRequest) throws
            JSONException {
        JSONObject json = createPartialDeletionRequest(id, name, rootRequest);
        if (email != null) {
            json.put("email", email);
        }
        return json;
    }

    /**
     * Creates a deletion request for the analysis with the given identifier, name or both.
     * 
     * @param id the analysis identifier.
     * @param name the analysis name.
     * @param username the fully qualified username of the person making the deletion request.
     * @param rootRequest true if this is a "root" deletion request.
     * @return the deletion request.
     * @throws JSONException if we try to build an invalid JSON object.
     */
    private JSONObject createDeletionRequest(String id, String name, String username, Boolean rootRequest) throws
            JSONException {
        JSONObject json = createPartialDeletionRequest(id, name, rootRequest);
        if (username != null) {
            json.put("full_username", username);
        }
        return json;
    }

    /**
     * Creates a partial deletion request containing the fields that are common to both the old-style and new-style
     * deletion requests.
     * 
     * @param id the analysis ID.
     * @param name the analysis name.
     * @param rootRequest true if this is a "root" deletion request.
     * @return the partial deletion request.
     * @throws JSONException if we try to build an invalid JSON object.
     */
    private JSONObject createPartialDeletionRequest(String id, String name, Boolean rootRequest) throws JSONException {
        JSONObject json = new JSONObject();
        if (id != null) {
            json.put("analysis_id", id);
        }
        if (name != null) {
            json.put("analysis_name", name);
        }
        if (rootRequest != null) {
            json.put("root_deletion_request", rootRequest);
        }
        return json;
    }
}
