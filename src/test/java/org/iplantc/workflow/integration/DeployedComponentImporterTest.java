package org.iplantc.workflow.integration;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.iplantc.workflow.util.JsonTestDataImporter.getTestJSONObject;

import org.iplantc.persistence.dto.components.DeployedComponent;
import org.iplantc.workflow.dao.mock.MockDeployedComponentDao;
import org.iplantc.workflow.integration.util.HeterogeneousRegistryImpl;
import org.iplantc.workflow.util.UnitTestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.create.DeployedComponentImporter.
 * 
 * @author Dennis Roberts
 */
public class DeployedComponentImporterTest {

    /**
     * Used to verify that the component we expect is the component we get.
     */
    private MockDeployedComponentDao mockComponentDao;

    /**
     * The deployed component importer instance being tested.
     */
    private DeployedComponentImporter deployedComponentImporter;

    /**
     * Initializes each unit test.
     */
    @Before
    public void initialize() {
        mockComponentDao = new MockDeployedComponentDao();
        deployedComponentImporter = new DeployedComponentImporter(mockComponentDao);
    }

    /**
     * Verifies that we can import a fully specified component.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void testFullySpecifiedComponent() throws JSONException {
        JSONObject json = generateJson("someid", "foo", "bar", "baz", "blarg", "glarb", "quux");
        deployedComponentImporter.importObject(json);
        assertEquals(1, mockComponentDao.getSavedObjects().size());
        DeployedComponent component = mockComponentDao.getSavedObjects().get(0);
        assertEquals("someid", component.getId());
        assertEquals("foo", component.getName());
        assertEquals("bar", component.getLocation());
        assertEquals("baz", component.getType());
        assertEquals("blarg", component.getDescription());
        assertEquals("glarb", component.getVersion());
        assertEquals("quux", component.getAttribution());
    }

    /**
     * Verifies that we can import a minimally specified component.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void testMinimallySpecifiedComponent() throws JSONException {
        JSONObject json = generateJson(null, "name", "location", "type", null, null, null);
        deployedComponentImporter.importObject(json);
        assertEquals(1, mockComponentDao.getSavedObjects().size());
        DeployedComponent component = mockComponentDao.getSavedObjects().get(0);
        assertTrue(component.getId().matches("c[0-9a-f]{32}"));
        assertEquals("name", component.getName());
        assertEquals("location", component.getLocation());
        assertEquals("type", component.getType());
        assertNull(component.getDescription());
        assertNull(component.getVersion());
        assertNull(component.getAttribution());
    }

    /**
     * Verifies that a missing name generates an exception.
     * 
     * @throws JSONException if the JSON we try to submit is invalid.
     */
    @Test(expected = JSONException.class)
    public void missingNameShouldCauseException() throws JSONException {
        JSONObject json = generateJson(null, null, "location", "type", null, null, null);
        deployedComponentImporter.importObject(json);
    }

    /**
     * Verifies that a missing location generates an exception.
     * 
     * @throws JSONException if the JSON we try to submit is invalid.
     */
    @Test(expected = JSONException.class)
    public void missingLocationShouldCauseException() throws JSONException {
        JSONObject json = generateJson(null, "name", null, "type", null, null, null);
        deployedComponentImporter.importObject(json);
    }

    /**
     * Verifies that a missing type generates an exception.
     * 
     * @throws JSONException if the JSON we try to submit is invalid.
     */
    @Test(expected = JSONException.class)
    public void missingTypeShouldCauseException() throws JSONException {
        JSONObject json = generateJson(null, "name", "location", null, null, null, null);
        deployedComponentImporter.importObject(json);
    }

    /**
     * Verifies that we can import multiple components at once.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void testMultipleComponents() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(generateJson("someid", "foo", "bar", "baz", "blarg", "glarb", "quux"));
        array.put(generateJson(null, "name", "location", "type", null, null, null));
        deployedComponentImporter.importObjectList(array);
        assertEquals(2, mockComponentDao.getSavedObjects().size());
        DeployedComponent component1 = mockComponentDao.getSavedObjects().get(0);
        DeployedComponent component2 = mockComponentDao.getSavedObjects().get(1);
        assertEquals("someid", component1.getId());
        assertEquals("foo", component1.getName());
        assertEquals("bar", component1.getLocation());
        assertEquals("baz", component1.getType());
        assertEquals("blarg", component1.getDescription());
        assertEquals("glarb", component1.getVersion());
        assertEquals("quux", component1.getAttribution());
        assertTrue(component2.getId().matches("c[0-9a-f]{32}"));
        assertEquals("name", component2.getName());
        assertEquals("location", component2.getLocation());
        assertEquals("type", component2.getType());
        assertNull(component2.getDescription());
        assertNull(component2.getVersion());
        assertNull(component2.getAttribution());
    }

    /**
     * Verifies that the importer will register deployed components if a registry is specified.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void shouldRegisterDeployedComponents() throws JSONException {
        HeterogeneousRegistryImpl registry = UnitTestUtils.createRegistry();
        deployedComponentImporter.setRegistry(registry);
        deployedComponentImporter.importObject(generateJson(null, "roo", "rar", "raz", null, null, null));
        assertEquals(3, registry.size(DeployedComponent.class));
        assertNotNull(registry.get(DeployedComponent.class, "roo"));
    }

    /**
     * Verifies that the importer will register deployed components when a list of deployed components is being created.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void shouldRegisterMultipleDeployedComponents() throws JSONException {
        HeterogeneousRegistryImpl registry = UnitTestUtils.createRegistry();
        deployedComponentImporter.setRegistry(registry);
        JSONArray array = new JSONArray();
        array.put(generateJson(null, "roo", "rar", "raz", null, null, null));
        array.put(generateJson(null, "glarb", "blrfl", "quux", null, null, null));
        deployedComponentImporter.importObjectList(array);
        assertEquals(4, registry.size(DeployedComponent.class));
        assertNotNull(registry.get(DeployedComponent.class, "roo"));
        assertNotNull(registry.get(DeployedComponent.class, "glarb"));
    }

    /**
     * Verifies that the importer silently ignore attempts to replace an existing deployed component if replacement is
     * disabled.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void shouldNotReplaceExistingDeployedComponentIfReplacementDisabled() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(generateJson("foo", "zaz", "/usr/bin", "rexecutable", null, null, null));
        array.put(generateJson("bar", "zaz", "/usr/bin", "executable", null, null, null));
        deployedComponentImporter.importObjectList(array);
        assertEquals(1, mockComponentDao.getSavedObjects().size());
        assertEquals("rexecutable", mockComponentDao.getSavedObjects().get(0).getType());
    }

    /**
     * Verifies that the importer still registers a duplicate deployed component in the registry, even if no update is
     * performed.
     * 
     * @throws JSONException if a JSON error occurs.
     */
    @Test
    public void shouldRegisterExistingDeployedComponentIfReplacementDisabled() throws JSONException {
        deployedComponentImporter.importObject(generateJson("foo", "zaz", "/usr/bin", "rexecutable", null, null, null));
        deployedComponentImporter.disableReplacement();
        HeterogeneousRegistryImpl registry = UnitTestUtils.createRegistry();
        deployedComponentImporter.setRegistry(registry);
        deployedComponentImporter.importObject(generateJson("bar", "zaz", "/usr/bin", "executable", null, null, null));
        assertEquals(1, mockComponentDao.getSavedObjects().size());
        assertEquals("rexecutable", mockComponentDao.getSavedObjects().get(0).getType());
        assertNotNull(registry.get(DeployedComponent.class, "zaz"));
    }

    /**
     * Verifies that the importer will replace an existing deployed component if it's configured to do so.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void shouldReplaceExistingDeployedComponentIfReplacementEnabled() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(generateJson("foo", "bar", "/usr/bin", "rexecutable", null, null, null));
        array.put(generateJson("bar", "bar", "/usr/bin", "executable", null, null, null));
        deployedComponentImporter.enableReplacement();
        deployedComponentImporter.importObjectList(array);
        assertEquals(1, mockComponentDao.getSavedObjects().size());
        assertEquals("executable", mockComponentDao.getSavedObjects().get(0).getType());
    }

    /**
     * Verifies that the importer will add an existing deployed component to the registry if it's updated.
     * 
     * @throws JSONException if we try to use an invalid attribute name.
     */
    @Test
    public void shouldAddUpdatedExistingDeployedComponentToRegistry() throws JSONException {
        deployedComponentImporter.enableReplacement();
        deployedComponentImporter.importObject(generateJson("foo", "bar", "/usr/bin", "rexecutable", null, null, null));
        HeterogeneousRegistryImpl registry = UnitTestUtils.createRegistry();
        deployedComponentImporter.setRegistry(registry);
        deployedComponentImporter.importObject(generateJson("baz", "bar", "/usr/bin", "executable", null, null, null));
        assertNotNull(registry.get(DeployedComponent.class, "bar"));
        assertEquals("foo", registry.get(DeployedComponent.class, "bar").getId());
        assertEquals("executable", registry.get(DeployedComponent.class, "bar").getType());
    }

    /**
     * Generates the JSON object to pass to the import service.
     * 
     * @param id the component identifier.
     * @param name the component name.
     * @param location the component location.
     * @param type the component type.
     * @param description the component description.
     * @param version the component version.
     * @param attribution the component attribution.
     * @return the JSON object.
     * @throws JSONException if we try to use an invalid attribute name.
     */
    private JSONObject generateJson(String id, String name, String location, String type, String description,
            String version, String attribution) throws JSONException {
        JSONObject json = new JSONObject();
        putIfNotNull(json, "id", id);
        json.put("name", name);
        putIfNotNull(json, "location", location);
        json.put("type", type);
        putIfNotNull(json, "description", description);
        putIfNotNull(json, "version", version);
        putIfNotNull(json, "attribution", attribution);

        try {
            json.put("implementation", getTestJSONObject("implementation_fragment"));
        }
        catch (IOException ioException) {
            throw new RuntimeException("Unable to load json", ioException);
        }

        return json;
    }

    /**
     * Adds a value to a JSON object if the value is not null.
     * 
     * @param json the JSON object.
     * @param name the name of the value.
     * @param value the actual value.
     * @throws JSONException if the name is invalid.
     */
    private void putIfNotNull(JSONObject json, String name, String value) throws JSONException {
        if (value != null) {
            json.put(name, value);
        }
    }
}
