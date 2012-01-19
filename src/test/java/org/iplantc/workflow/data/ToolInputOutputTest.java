package org.iplantc.workflow.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.iplantc.workflow.model.WorkflowElementTest;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.data.ToolInputOutput.
 * 
 * @author Dennis Roberts
 */
public class ToolInputOutputTest extends WorkflowElementTest<ToolInputOutput> {

    /**
     * The identifier to use in all unit tests.
     */
    private static final String ID = "someId";

    /**
     * The name to use in all unit tests.
     */
    private static final String NAME = "someName";

    /**
     * The label to use in all unit tests.
     */
    private static final String LABEL = "someLabel";

    /**
     * The description to use in all unit tests.
     */
    private static final String DESCRIPTION = "someDescription";

    /**
     * The default multiplicity setting to use in all unit tests;
     */
    private static final String MULTIPLICITY = "single";

    /**
     * The default argument list position to use in all unit tests.
     */
    private static final int ARGUMENT_LIST_POSITION = 1;

    /**
     * The default command-line option to use in all unit tests.
     */
    private static final String COMMAND_LINE_OPTION = "--foo";

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToolInputOutput createInstance() {
        ToolInputOutput io = new ToolInputOutput(ID, NAME, LABEL, DESCRIPTION);
        io.setMultiplicity(MULTIPLICITY);
        io.setArgumentListPosition(ARGUMENT_LIST_POSITION);
        io.setCommandLineOption(COMMAND_LINE_OPTION);
        io.setSupportedDataObjects(Arrays.asList(createDataObject("foo")));
        return io;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToolInputOutput createInstance(String id, String name, String label, String description) {
        ToolInputOutput io = new ToolInputOutput(id, name, label, description);
        io.setMultiplicity(MULTIPLICITY);
        io.setArgumentListPosition(ARGUMENT_LIST_POSITION);
        io.setCommandLineOption(COMMAND_LINE_OPTION);
        io.setSupportedDataObjects(Arrays.asList(createDataObject("foo")));
        return io;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getElementId() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getElementName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getElementLabel() {
        return LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getElementDescription() {
        return DESCRIPTION;
    }

    /**
     * Verifies that equals can detect different multiplicity settings.
     */
    @Test
    public void equalsShouldDetectDifferentMultiplicitySettings() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        io2.setMultiplicity("many");
        assertFalse(io1.equals(io2));
        assertFalse(io2.equals(io1));
    }

    /**
     * Verifies that the hash code includes the multiplicity setting.
     */
    @Test
    public void hashCodeShouldIncludeMultiplicitySetting() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        assertTrue(io1.hashCode() == io2.hashCode());
        io2.setMultiplicity("many");
        assertFalse(io1.hashCode() == io2.hashCode());
    }

    /**
     * Verifies that equals can detect different argument list positions.
     */
    @Test
    public void equalsShouldDetectDifferentArgumentListPositions() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        io2.setArgumentListPosition(27);
        assertFalse(io1.equals(io2));
        assertFalse(io2.equals(io1));
    }

    /**
     * Verifies that the argument list position is included in the hash code calculation.
     */
    @Test
    public void hashCodeShouldIncludeArgumentListPosition() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        assertTrue(io1.hashCode() == io2.hashCode());
        io2.setArgumentListPosition(52);
        assertFalse(io1.hashCode() == io2.hashCode());
    }

    /**
     * Verifies that equals can detect different command-line arguments.
     */
    @Test
    public void equalsShouldDetectDifferentCommandLineOptions() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        io2.setCommandLineOption("--bar");
        assertFalse(io1.equals(io2));
        assertFalse(io2.equals(io1));
    }

    /**
     * Verifies that the command-line option is included in the hash code calculation.
     */
    @Test
    public void hashCodeShouldIncludeCommandLineOption() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        assertTrue(io1.hashCode() == io2.hashCode());
        io2.setCommandLineOption("--bar");
        assertFalse(io1.hashCode() == io2.hashCode());
    }

    /**
     * Verifies that equals can detect different data object lists.
     */
    @Test
    public void equalsShouldDetectDifferentDataObjectLists() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        io2.addSupportedDataObject(createDataObject("bar"));
        assertFalse(io1.equals(io2));
        assertFalse(io2.equals(io1));
    }

    /**
     * Verifies that the list of supported data objects is included in the hash code calculation.
     */
    @Test
    public void hashCodeShouldIncludeDataObjectLists() {
        ToolInputOutput io1 = createInstance();
        ToolInputOutput io2 = createInstance();
        assertTrue(io1.hashCode() == io2.hashCode());
        io2.addSupportedDataObject(createDataObject("bar"));
        assertFalse(io1.hashCode() == io2.hashCode());
    }

    /**
     * Creates a data object with the given name.
     * 
     * @param name the name of the data object.
     * @return the data object.
     */
    private DataObject createDataObject(String name) {
        DataObject dataObject = new DataObject();
        dataObject.setName(name);
        return dataObject;
    }
}
